# API Reference

This application is a server-rendered MVC web app. All endpoints are Jakarta Servlet URL patterns that return either an HTML page (JSP forward) or an HTTP redirect. There is no REST/JSON API layer — the browser communicates via standard HTML forms and GET requests.

---

## Authentication

Sessions are managed server-side via `HttpSession`. Authentication state is checked on every request by `AuthFilter` (`/*`).

**Session attributes set on login:**

| Attribute | Type | Description |
|---|---|---|
| `user` | `User` | Full user object |
| `role` | `String` | Role name: `ADMIN`, `TEACHER`, or `STUDENT` |
| `fullName` | `String` | Display username |
| `csrfToken` | `String` | UUID token, validated on logout |

**Remember Me** stores a UUID token hash in the `RememberTokens` table (30-day TTL) and a corresponding `remember_token` cookie (HttpOnly, 30 days). `AuthFilter` validates the cookie on every unauthenticated request and auto-restores the session if the token is still valid.

---

## Public Endpoints

These paths are accessible without an active session.

### `GET /login`
Renders the login form.

**Response:** `200 OK` — `login.jsp`

---

### `POST /login`
Authenticates a user with username/password.

**Form Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `username` | Yes | Plain-text username |
| `password` | Yes | Plain-text password (BCrypt-verified server-side) |
| `remember` | No | Value `"on"` to enable Remember Me cookie |

**Success Response:** `302 Redirect`
- Admin → `/payroll`
- Teacher → `/financial-statistics`
- Student → `/payment`

**Failure Response:** `200 OK` — `login.jsp` with `error` attribute set

---

### `GET /register`
Renders the user registration form.

**Response:** `200 OK` — `registration-form.jsp`

---

### `POST /register`
Creates a new user account and links it to an existing teacher or student record by code.

**Form Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `username` | Yes | Must be unique |
| `password` | Yes | Plain text; BCrypt-hashed before storage |
| `email` | Yes | Must be unique |
| `role` | Yes | Integer role ID: `2` = Teacher, `3` = Student |
| `teacherCode` | Conditional | Required when `role = 2` |
| `studentCode` | Conditional | Required when `role = 3` |

**Validation errors** are returned as request attributes (e.g. `errorUsername`, `errorPassword`, `errorEmail`, `errorStudentCode`) and the form is re-rendered.

**Success Response:** `302 Redirect` — same role-based redirect as `/login`

---

### `GET /logout`
Displays a logged-out confirmation page (no session invalidation).

**Response:** `302 Redirect` → `/login?message=You+have+been+logged+out`

---

### `POST /logout`
Validates the CSRF token, invalidates the session, and deletes the Remember Me cookie and database token.

**Form Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `csrfToken` | Yes | Must match `session.getAttribute("csrfToken")` |

**Success Response:** `302 Redirect` → `/login?message=You+have+been+logged+out`

**Failure Response:** `403 Forbidden` — CSRF token mismatch

---

## Protected Endpoints

All protected endpoints require an active session. Unauthenticated requests are redirected to `/login` by `AuthFilter`.

---

### Student Fee Payment Portal

**Access:** `STUDENT` only — enforced by `StudentFilter`

#### `GET /payment`
Lists all fee records for the currently logged-in student, ordered by due date descending.

**Query Parameters:**

| Parameter | Description |
|---|---|
| *(none)* | Defaults to `action=list` |

**Response:** `200 OK` — `fee-payment-portal.jsp`

**Request attributes set:**

| Attribute | Type | Description |
|---|---|---|
| `student` | `Student` | Resolved student record for the session user |
| `fees` | `List<StudentFee>` | All fee records for this student |
| `totalDebt` | `BigDecimal` | Sum of all `UNPAID` fees |
| `flashSuccess` | `String` | One-time success message (from session) |
| `flashError` | `String` | One-time error message (from session) |

**Fee statuses:**

| Status | Description |
|---|---|
| `PAID` | Paid, with `payment_date` recorded |
| `UNPAID` | Outstanding |
| `OVERDUE` | Unpaid and past `due_date` (resolved at read time by `FeeStatus.resolve()`) |

---

#### `POST /payment?action=pay`
Marks a single fee as paid for the currently logged-in student.

**Form Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `action` | Yes | Must be `"pay"` |
| `feeId` | Yes | Integer ID of the fee record to pay |

The `student_id` guard in the SQL (`WHERE fee_id = ? AND student_id = ?`) ensures a student cannot pay another student's fee.

**Success Response:** `302 Redirect` → `/payment` with `flashSuccess` in session

**Failure Response:** `302 Redirect` → `/payment` with `flashError` in session

---

### Teacher Payroll Management

**Access:** `ADMIN` only — enforced by `AdminFilter`

#### `GET /payroll`
Lists all payroll records for a given pay period with status summary counts.

**Query Parameters:**

| Parameter | Description |
|---|---|
| `period` | Pay period in `YYYY-MM` format. Defaults to current month. |
| `action` | Optional: `generate`, `edit`, `confirmDelete` |

**Response:** `200 OK` — `salary-payroll-dashboard.jsp`

**Request attributes set:**

| Attribute | Type | Description |
|---|---|---|
| `payrolls` | `List<TeacherPayroll>` | Records for the selected period |
| `selectedPeriod` | `String` | The active period filter |
| `currentTime` | `String` | Month + year label (e.g. `"JUNE 2026"`) |
| `currentPeriod` | `String` | Current period in `YYYY-MM` (e.g. `"2026-06"`) |
| `totalPayroll` | `Integer` | Sum of all `net_amount` across all records |
| `disbursedCount` | `Integer` | Count of `DISBURSED` records |
| `pendingCount` | `Integer` | Count of `PENDING` records |
| `onHoldCount` | `Integer` | Count of `ON_HOLD` records |

---

#### `GET /payroll?action=generate`
Generates payroll records for the current month for all teachers. Each record is seeded with the teacher's `salary` as `base_salary`. Allowances and deductions default to `0.00`.

**Behaviour:** If payroll for the current period already exists, redirects back with a `flashError` — duplicate generation is blocked.

**Response:** `302 Redirect` → `/payroll?period=YYYY-MM`

---

#### `GET /payroll?action=edit&payrollId={id}`
Loads a single payroll record into the edit form.

**Query Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `payrollId` | Yes | Integer ID of the payroll record |

**Response:** `200 OK` — `payroll-form.jsp` with `payroll` attribute set

---

#### `GET /payroll?action=confirmDelete&payrollId={id}`
Shows a confirmation page before deleting a payroll record.

**Response:** `200 OK` — `confirm-delete-payroll.jsp` with `payroll` and `period` attributes

---

#### `POST /payroll?action=update`
Updates base salary, allowances, deductions, and status for a single payroll record. `net_amount` is recomputed by the database (`GENERATED ALWAYS AS`).

**Form Parameters:**

| Parameter | Required | Validation |
|---|---|---|
| `payrollId` | Yes | Integer |
| `period` | Yes | `YYYY-MM` string |
| `baseSalary` | Yes | Non-negative decimal |
| `allowances` | Yes | Non-negative decimal |
| `deductions` | Yes | Non-negative decimal |
| `status` | Yes | One of `PENDING`, `DISBURSED`, `ON_HOLD` |

Validation errors are set as request attributes (`errorBaseSalary`, `errorAllowances`, `errorDeductions`, `errorStatus`) and the form is re-rendered.

**Success Response:** `302 Redirect` → `/payroll?period=YYYY-MM`

---

#### `POST /payroll?action=delete`
Deletes a single payroll record permanently.

**Form Parameters:**

| Parameter | Required | Description |
|---|---|---|
| `payrollId` | Yes | Integer ID of the record to delete |
| `period` | No | Used to redirect back to the correct period view |

**Response:** `302 Redirect` → `/payroll?period=YYYY-MM`

---

### Financial Statistics

**Access:** `ADMIN` and `TEACHER` — enforced by `TeacherFilter`

#### `GET /financial-statistics`
Renders the financial statistics dashboard.

**Response:** `200 OK` — `financial-statistic.jsp`

---

## Payroll Status Lifecycle

```
PENDING  ──(payment date passes without disbursement)──►  ON_HOLD
PENDING  ──(admin marks as disbursed)────────────────────► DISBURSED
ON_HOLD  ──(admin manually updates)──────────────────────► PENDING | DISBURSED
```

A MySQL scheduled event (`update_passed_paydate_event`) runs hourly and automatically transitions any `PENDING` record whose `payment_date` is in the past to `ON_HOLD`.

---

## Error Handling

| Scenario | Behaviour |
|---|---|
| Unknown route | `404` → `error404.jsp` (configured in `web.xml`) |
| Unauthenticated request | `302` → `/login` |
| Insufficient role | `302` → role's own landing page with `error` query param |
| Invalid CSRF token on logout | `403 Forbidden` |
| `SQLException` in a controller | Re-thrown as `ServletException("Database error")` → Tomcat 500 page |