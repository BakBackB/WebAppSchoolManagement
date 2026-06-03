# Setup Guide

This guide walks through setting up the project for local development from scratch.

---

## Prerequisites

Make sure the following are installed before you begin:

| Tool | Minimum Version | Notes |
|---|---|---|
| JDK | 17 | Any distribution (Temurin, Oracle, etc.) |
| Apache Maven | 3.8 | `mvn -v` to verify |
| Apache Tomcat | 10.1 | Must be Tomcat 10+ for Jakarta EE 10 / Servlet 6.0 |
| MySQL Server | 8.0 | MySQL 8+ for `GENERATED ALWAYS AS` and event scheduler support |
| Node.js | 18+ | Only needed to build Tailwind CSS |
| Git | Any | — |

---

## 1. Clone the Repository

```bash
git clone https://github.com/BakBackB/WebAppSchoolManagement.git
cd WebAppSchoolManagement
```

---

## 2. Set Up the Database

### 2a. Create the schema

```bash
mysql -u root -p < database/schema.sql
```

This creates the `school_management` database, all tables, indexes, foreign keys, and the hourly event that auto-transitions overdue payroll records.

### 2b. Load seed data

```bash
mysql -u root -p school_management < database/seed-data.sql
```

The seed data includes sample teachers, students, classes, subjects, and user accounts for testing each role.

### 2c. Enable the MySQL Event Scheduler

The payroll status event (`update_passed_paydate_event`) requires the event scheduler to be running. Check the current state:

```sql
SHOW VARIABLES LIKE 'event_scheduler';
```

If it returns `OFF`, enable it for the current session:

```sql
SET GLOBAL event_scheduler = ON;
```

To make it persistent across restarts, add this line to your `my.cnf` / `my.ini`:

```ini
[mysqld]
event_scheduler=ON
```

---

## 3. Configure the Database Connection

The DAOs currently use hardcoded `DriverManager` credentials. Before running the app, update the connection constants in each DAO file:

```
backend/src/main/java/com/school_management/dao/UserDAO.java
backend/src/main/java/com/school_management/dao/StudentDAO.java
backend/src/main/java/com/school_management/dao/TeacherDAO.java
backend/src/main/java/com/school_management/dao/StudentFeeDAO.java
backend/src/main/java/com/school_management/dao/TeacherPayrollDAO.java
```

In each file, find and update:

```java
private static final String DB_URL  = "jdbc:mysql://localhost:3306/school_management?useSSL=false";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password_here";
```

> **Tip — use a JNDI DataSource instead:**
> For a cleaner setup that avoids credentials in source code, configure a Tomcat JNDI DataSource in `backend/src/main/webapp/META-INF/context.xml` and look up `jdbc/schoolDB` via `InitialContext` in the DAOs. This also gives you connection pooling for free.

Example `context.xml` with a DataSource:

```xml
<Context path="/school_management">
  <Resource name="jdbc/schoolDB"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="com.mysql.cj.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/school_management?useSSL=false"
            username="your_db_user"
            password="your_db_password"
            maxTotal="20"
            maxIdle="5"
            maxWaitMillis="10000"/>
</Context>
```

---

## 4. Build the Tailwind CSS

The frontend build compiles Tailwind utility classes from the JSP files into a single CSS output file.

```bash
cd frontend
npm install
npx tailwindcss -i ./src/input.css \
  -o ../backend/src/main/webapp/css/output.css \
  --minify
```

For active development with live rebuilds on JSP changes:

```bash
npx tailwindcss -i ./src/input.css \
  -o ../backend/src/main/webapp/css/output.css \
  --watch
```

> The output path `backend/src/main/webapp/css/output.css` is listed in `.gitignore` — do not commit it.

---

## 5. Build the Backend

```bash
cd backend
mvn clean package
```

This produces `backend/target/school_management.war`.

---

## 6. Deploy to Tomcat

### Option A — Copy WAR manually

```bash
cp backend/target/school_management.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh    # Linux/macOS
$CATALINA_HOME/bin/startup.bat   # Windows
```

Tomcat auto-deploys the WAR on startup.

### Option B — NetBeans IDE

The project includes `backend/nb-configuration.xml` which configures NetBeans to deploy to a local Tomcat instance. Open the `backend/` folder as a Maven project in NetBeans, set up a Tomcat server under Tools → Servers, and use Run Project (F6) to build and deploy in one step.

### Option C — Maven Tomcat plugin

Add the Tomcat Maven plugin to `pom.xml` and use:

```bash
mvn tomcat10:run
```

---

## 7. Access the Application

Once Tomcat is running, open:

```
http://localhost:8080/school_management/login
```

### Default Seed Accounts

Check `database/seed-data.sql` for the exact usernames. General account types available after seeding:

| Role | Landing Page After Login |
|---|---|
| Admin | `/payroll` |
| Teacher | `/financial-statistics` |
| Student | `/payment` |

---

## 8. Generating Password Hashes

If you need to create additional users manually in the database, use the included utility to generate a BCrypt hash:

```bash
cd backend
mvn compile
mvn exec:java -Dexec.mainClass="com.school_management.util.PasswordHashGenerator" \
  -Dexec.args="your_plain_text_password"
```

Insert the output hash directly into the `Users.password_hash` column.

---

## Common Issues

**`ClassNotFoundException: com.mysql.cj.jdbc.Driver`**
The MySQL Connector JAR is a Maven dependency and should be bundled in the WAR. If you see this error, run `mvn clean package` again and confirm `mysql-connector-j-8.2.0.jar` appears in `WEB-INF/lib/` inside the WAR.

**`HTTP 404` on all pages after deploy**
Confirm the context path. The WAR is named `school_management.war` so Tomcat serves it at `/school_management`. If you rename the WAR or deploy it as `ROOT.war`, adjust your URLs accordingly.

**`Access denied for user 'root'@'localhost'`**
Double-check the `DB_USER` and `DB_PASSWORD` constants in each DAO file. Also verify the MySQL user has `SELECT`, `INSERT`, `UPDATE`, `DELETE`, and `EVENT` privileges on `school_management`.

**Tailwind classes not applying**
The `output.css` file may be missing or stale. Re-run the Tailwind build step (Step 4) and redeploy. Make sure `output.css` is present in `backend/src/main/webapp/css/` before running `mvn package`.

**`IllegalArgumentException` on `PayrollStatus.valueOf`**
The `status` column value in the database does not match one of `PENDING`, `DISBURSED`, `ON_HOLD`. Check for stale rows with unexpected casing or extra whitespace using `SELECT DISTINCT status FROM TeacherPayroll`.