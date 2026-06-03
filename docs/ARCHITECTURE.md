# Architecture

## Overview

WebAppSchoolManagement is a server-rendered MVC web application built on the Jakarta EE Servlet stack. The browser communicates with the server via HTML form submissions and GET requests. The server processes requests, queries MySQL through a plain JDBC DAO layer, and returns rendered JSP pages. There is no REST/JSON API and no client-side framework.

```
Browser
  │  HTML form POST / GET
  ▼
Tomcat (Jakarta Servlet Container)
  │
  ├── Filter Chain
  │     AuthFilter     (/*) — authentication gate, Remember Me resolution
  │     AdminFilter    (/payroll) — admin-only guard
  │     TeacherFilter  (/financial-statistics) — admin + teacher guard
  │     StudentFilter  (/payment) — student-only guard
  │
  ├── Servlet Controllers
  │     LoginController          /login
  │     LogoutController         /logout
  │     RegisterController       /register
  │     PaymentController        /payment
  │     PayrollController        /payroll
  │     FinancialStatisticController  /financial-statistics
  │
  ├── DAO Layer (plain JDBC)
  │     UserDAO, StudentDAO, TeacherDAO
  │     StudentFeeDAO, TeacherPayrollDAO
  │
  └── JSP Views (WEB-INF/views/)
        login.jsp, registration-form.jsp
        fee-payment-portal.jsp
        salary-payroll-dashboard.jsp, payroll-form.jsp, confirm-delete-payroll.jsp
        financial-statistic.jsp, error404.jsp
  │
  ▼
MySQL 8
  Tables: Users, Roles, Students, Teachers, Classes, Rooms, Subjects,
          TeachingAssignments, ClassSchedules, StudentFees,
          TeacherPayroll, RememberTokens, AuditLogs
```

---

## Layer Breakdown

### Filter Chain

Filters run before every request and enforce cross-cutting concerns. They are registered via `@WebFilter` annotations and execute in the order Tomcat resolves them.

| Filter | URL Pattern | Responsibility |
|---|---|---|
| `AuthFilter` | `/*` | Unauthenticated requests → `/login`. Resolves Remember Me cookie and auto-restores session if the token is still valid. Static assets and public paths bypass this check. |
| `AdminFilter` | `/payroll` | Allows only `ADMIN`. Redirects teachers to `/financial-statistics`, students to `/payment`. |
| `TeacherFilter` | `/financial-statistics` | Allows `ADMIN` and `TEACHER`. Redirects students to `/payment`. |
| `StudentFilter` | `/payment` | Allows `STUDENT`. Redirects teachers to `/financial-statistics`. |

The public paths exempt from `AuthFilter` are `/login`, `/logout`, `/register`, and any path ending in a static asset extension (`.css`, `.js`, `.png`, etc.).

---

### Controllers (Servlets)

Each controller maps to a single URL pattern and handles both `GET` (render / navigate) and `POST` (mutate / submit) verbs. Business logic is kept thin in controllers — they validate input, delegate to DAOs, set request/session attributes, and forward or redirect.

**Action dispatch pattern** — controllers that manage multiple sub-operations read a `?action=` query parameter and switch on it:

```java
// Example from PayrollController
switch (action) {
    case "generate":   generatePayroll(request, response); break;
    case "edit":       showEditForm(request, response);    break;
    case "confirmDelete": confirmDelete(request, response); break;
    default:           listPayroll(request, response);
}
```

**Flash messages** are stored as session attributes, read once by the next request, and immediately removed from the session. This implements the Post/Redirect/Get (PRG) pattern to prevent duplicate form submissions on browser refresh.

---

### DAO Layer

Each DAO class encapsulates all SQL for one aggregate root. DAOs open and close their own JDBC connections per method using try-with-resources. There is no connection pooling configured at the application level (each DAO hard-codes a `DriverManager.getConnection` call — see [SETUP.md](SETUP.md) for configuring a Tomcat JNDI DataSource as an improvement).

| DAO | Table(s) | Key Operations |
|---|---|---|
| `UserDAO` | `Users`, `Roles`, `RememberTokens` | authenticate, insertUser, storeToken, validateToken, deleteToken |
| `StudentDAO` | `Students`, `Classes` | getAll, getById, getByCode, getByUserId, insert, update, delete |
| `TeacherDAO` | `Teachers` | getAll, getById, getByCode, getByUserId, insert, update, delete |
| `StudentFeeDAO` | `StudentFees`, `Students` | getFeesByStudentId, payFee, sumUnpaidByStudent, countUnpaidByStudent |
| `TeacherPayrollDAO` | `TeacherPayroll`, `Teachers` | getByPeriod, getById, insertPayroll, updatePayroll, deletePayroll, periodAlreadyGenerated, count* |

---

### Model Layer

Plain Java objects (no JPA/ORM). Each class maps to a database table. Relationships are resolved eagerly in the DAO layer via JOIN queries and sub-object construction.

```
User ──── Role
 │
 ├── Student ──── SchoolClass ──── Room
 │        └────── StudentFee
 │
 └── Teacher
          └────── TeacherPayroll
                  TeachingAssignment ──── Subject, SchoolClass, Teacher
                  ClassSchedule ──── Subject, Room, SchoolClass
```

---

### DTOs / Enums

| Enum | Values | Used In |
|---|---|---|
| `PayrollStatus` | `PENDING`, `DISBURSED`, `ON_HOLD` | `TeacherPayroll`, payroll filter/update |
| `FeeStatus` | `PAID`, `UNPAID`, `OVERDUE` | `StudentFee` — `OVERDUE` is a virtual status resolved at read time by `FeeStatus.resolve()` (compares `due_date` against today); it is never stored in the database |
| `RoomType` | `CLASSROOM`, `LAB` | `Room` |
| `DayOfWeekEnum` | `MONDAY`–`SATURDAY` | `ClassSchedule` |

---

### Views (JSP)

Views live under `WEB-INF/views/` and are never accessible directly — they can only be reached via a servlet forward. JSTL Core (`c:`) and EL expressions are used for rendering. Tailwind CSS utility classes are compiled from `frontend/src/input.css` into `backend/src/main/webapp/css/output.css`.

| View | Served By | Role |
|---|---|---|
| `login.jsp` | `LoginController` | Login form |
| `registration-form.jsp` | `RegisterController` | New user registration |
| `fee-payment-portal.jsp` | `PaymentController` | Student fee list + pay action |
| `salary-payroll-dashboard.jsp` | `PayrollController` | Admin payroll list with status summary |
| `payroll-form.jsp` | `PayrollController` | Edit a single payroll record |
| `confirm-delete-payroll.jsp` | `PayrollController` | Delete confirmation screen |
| `financial-statistic.jsp` | `FinancialStatisticController` | Financial overview for teachers/admins |
| `error404.jsp` | Tomcat (via `web.xml`) | Custom 404 page |

---

## Security Architecture

### Authentication Flow

```
Request arrives
     │
     ▼
AuthFilter checks session
     │
     ├── Session valid ──────────────────────────────► Continue to controller
     │
     └── No session
              │
              ▼
         Check remember_token cookie
              │
              ├── Cookie present → validate against RememberTokens table
              │        │
              │        ├── Valid → restore session → redirect to role landing page
              │        │
              │        └── Expired/not found → redirect to /login
              │
              └── No cookie → redirect to /login
```

### Session Fixation Protection

On successful login, any pre-existing anonymous session is invalidated (`oldSession.invalidate()`) before a new session is created. This prevents an attacker from planting a known session ID and reusing it after the victim logs in.

### CSRF Protection

A UUID CSRF token is stored in the session on login. The logout form embeds this token as a hidden field. `LogoutController.doPost` validates that the submitted token matches the session token before invalidating the session. A mismatch returns `403 Forbidden`.

### Password Storage

Passwords are hashed with BCrypt (`jBCrypt 0.4`). The salt is embedded in the hash, so no separate salt column is needed. Verification uses `BCrypt.checkpw(plaintext, storedHash)`.

### Defence-in-Depth on Payments

`StudentFeeDAO.payFee` issues the SQL `WHERE fee_id = ? AND student_id = ?`, so even if the filter is bypassed, a student cannot mark another student's fee as paid.

---

## Database Architecture

The schema is in `database/schema.sql`. Key design decisions:

- `TeacherPayroll.net_amount` is a `GENERATED ALWAYS AS (base_salary + allowances - deductions) STORED` column — the database always computes it, eliminating any risk of application-layer arithmetic drift.
- A MySQL scheduled event (`update_passed_paydate_event`) runs hourly and sets any `PENDING` payroll whose `payment_date` has passed to `ON_HOLD`, decoupling time-based status transitions from the application.
- `RememberTokens.expires_at` is indexed (`idx_expires`) to make token validation and cleanup fast.
- `AuditLogs` table and the `AuditLogger`/`LoginAttemptTracker` scaffolding exist in the codebase (commented out) and are ready to be activated.

---

## Monorepo Structure

```
/
├── backend/    Maven WAR project — all Java, JSP, and web config
├── frontend/   Tailwind CSS build — outputs to backend/webapp/css/output.css
└── database/   SQL schema, seed data, ER diagram, MySQL Workbench file
```

The frontend build is a compile step only. There is no Node.js server; Tailwind generates a static CSS file that is served by Tomcat alongside the JSP pages.