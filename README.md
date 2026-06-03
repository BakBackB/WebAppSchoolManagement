# WebAppSchoolManagement

A web-based school management system built with Java Servlets, JSP, and MySQL. The application handles student fee payments, teacher payroll management, user registration, and role-based access control for admins, teachers, and students.

---

## Features

- **Role-Based Access Control** — Three distinct roles (Admin, Teacher, Student), each with their own dashboard and restricted access via servlet filters
- **Student Fee Payment Portal** — Students can view their outstanding fees, check due dates, and submit payments
- **Teacher Payroll Management** — Admins can generate, edit, update, and delete payroll records with support for base salary, allowances, and deductions; net pay is computed automatically
- **Financial Statistics Dashboard** — Teachers can view school financial data (fee collections and payroll overview)
- **Secure Authentication** — Session-based login with BCrypt password hashing, CSRF token protection on logout, and persistent "Remember Me" via database-backed tokens
- **User Registration** — Self-registration for teachers and students by linking to existing teacher/student codes
- **Automatic Payroll Status Updates** — A MySQL scheduled event checks hourly and marks overdue pending payrolls as `ON_HOLD`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Web Framework | Jakarta Servlet API 6.0, JSP 3.1, JSTL 3.0 |
| Build Tool | Apache Maven |
| Server | Apache Tomcat 10+ |
| Database | MySQL 8+ |
| Password Hashing | jBCrypt 0.4 |
| Frontend Styling | Tailwind CSS 3.4 |
| JDBC Driver | MySQL Connector/J 8.2.0 |

---

## Project Structure

```
WebAppSchoolManagement/
├── backend/
│   └── src/main/
│       ├── java/com/school_management/
│       │   ├── controller/        # Servlet controllers (Login, Logout, Register, Payment, Payroll, FinancialStatistic)
│       │   ├── dao/               # Data Access Objects (Student, Teacher, User, StudentFee, TeacherPayroll)
│       │   ├── dto/               # Enums (DayOfWeekEnum, FeeStatus, PayrollStatus, RoomType)
│       │   ├── filter/            # Servlet filters for RBAC (AuthFilter, AdminFilter, TeacherFilter, StudentFilter)
│       │   ├── model/             # JPA-style POJOs (User, Student, Teacher, StudentFee, TeacherPayroll, etc.)
│       │   └── util/              # Utilities (PasswordHashGenerator)
│       └── webapp/
│           ├── META-INF/context.xml   # DataSource / DB connection config
│           └── WEB-INF/
│               ├── views/             # JSP pages (login, registration, payment portal, payroll dashboard, etc.)
│               └── web.xml
├── database/
│   ├── schema.sql                 # Full DDL — tables, indexes, FK constraints, scheduled event
│   ├── seed-data.sql              # Sample data for development
│   ├── ER-diagram.png             # Entity-Relationship diagram
│   └── school.mwb                 # MySQL Workbench model file
├── docs/
│   ├── API.md
│   ├── CONTRIBUTING.md
│   └── DEPLOYMENT.md
├── frontend/
│   ├── src/input.css              # Tailwind CSS entry point
│   ├── tailwind.config.js         # Tailwind configured to scan JSP files
│   └── package.json
└── README.md
```

---

## Database Schema

The schema includes the following tables:

- `Users` — authentication credentials, role assignment, account status
- `Roles` — `ADMIN`, `TEACHER`, `STUDENT`
- `Teachers` — teacher profiles linked to `Users`
- `Students` — student profiles linked to `Users` and `Classes`
- `Classes` / `Rooms` / `Subjects` — academic structure
- `TeachingAssignments` — maps teachers to classes and subjects
- `ClassSchedules` — timetable with room, day, and time slots
- `StudentFees` — per-student fee records with `PAID` / `UNPAID` status
- `TeacherPayroll` — payroll records with auto-computed `net_amount`
- `RememberTokens` — persistent login tokens
- `AuditLogs` — structured action logging (prepared for activation)

---

## Getting Started

### Prerequisites

- Java 17+
- Apache Maven 3.8+
- Apache Tomcat 10+
- MySQL 8+
- Node.js (for Tailwind CSS build)

### 1. Clone the Repository

```bash
git clone https://github.com/BakBackB/WebAppSchoolManagement.git
cd WebAppSchoolManagement
```

### 2. Set Up the Database

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p school_management < database/seed-data.sql
```

### 3. Configure the DataSource

Edit `backend/src/main/webapp/META-INF/context.xml` and update the database connection details:

```xml
<Resource name="jdbc/schoolDB"
          url="jdbc:mysql://localhost:3306/school_management"
          username="YOUR_DB_USER"
          password="YOUR_DB_PASSWORD" ... />
```

### 4. Build the Tailwind CSS

```bash
cd frontend
npm install
npx tailwindcss -i ./src/input.css -o ../backend/src/main/webapp/css/output.css --watch
```

### 5. Build and Deploy the Backend

```bash
cd backend
mvn clean package
```

Copy the generated `target/school_management.war` to your Tomcat `webapps/` directory and start Tomcat.

### 6. Access the Application

Open your browser and navigate to:

```
http://localhost:8080/school_management/login
```

---

## User Roles & Access

| Role | Landing Page | Access |
|---|---|---|
| **Admin** | `/payroll` | Full access — payroll management, financial statistics |
| **Teacher** | `/financial-statistics` | Read-only financial overview |
| **Student** | `/payment` | View and pay their own fees only |

Role access is enforced by servlet filters:
- `AuthFilter` — redirects unauthenticated users to `/login`
- `AdminFilter` — guards `/payroll`
- `TeacherFilter` — guards `/financial-statistics`
- `StudentFilter` — restricts `/payment` to students

---

## Security Highlights

- **BCrypt password hashing** for all stored passwords
- **Session fixation protection** — old sessions are invalidated on login
- **CSRF token validation** on logout
- **"Remember Me"** tokens are stored hashed in the database with a 30-day expiry; tokens are cleared on logout
- **Role-based filter chain** prevents privilege escalation between roles
- **Generic error messages** on login failure to prevent username enumeration

---

## License

This project is licensed under the **Mozilla Public License 2.0**. See the [LICENSE](LICENSE) file for details.
