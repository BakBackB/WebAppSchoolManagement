CREATE DATABASE school_management;

USE school_management;

CREATE TABLE
  Teachers (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    salary DECIMAL(10, 2) NOT NULL
  );

CREATE TABLE
  Classes (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(50) NOT NULL,
    section VARCHAR(10) NOT NULL,
    room_id INT
  );

CREATE TABLE
  Rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_name VARCHAR(50) NOT NULL,
    room_type ENUM ('CLASSROOM', 'LAB') NOT NULL
  );

CREATE TABLE
  Students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_code VARCHAR(20) UNIQUE NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    major VARCHAR(50) NOT NULL,
    class_id INT NOT NULL
  );

CREATE TABLE
  Subjects (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) UNIQUE NOT NULL
  );

CREATE TABLE
  TeachingAssignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT NOT NULL
  );

CREATE TABLE
  ClassSchedules (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    subject_id INT NOT NULL,
    room_id INT NOT NULL,
    day_of_week ENUM (
      'MONDAY',
      'TUESDAY',
      'WEDNESDAY',
      'THURSDAY',
      'FRIDAY',
      'SATURDAY'
    ) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
  );

CREATE TABLE
  StudentFees (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status ENUM ('PAID', 'UNPAID') DEFAULT 'UNPAID'
  );

CREATE TABLE
  TeacherPayroll (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_id INT NOT NULL,
    pay_period VARCHAR(7) NOT NULL,
    base_salary DECIMAL(10, 2) NOT NULL,
    allowances DECIMAL(10, 2) DEFAULT 0.00,
    deductions DECIMAL(10, 2) DEFAULT 0.00,
    net_amount DECIMAL(10, 2) GENERATED ALWAYS AS (base_salary + allowances - deductions) STORED,
    payment_date DATE NULL,
    status ENUM ('DISBURSED', 'PENDING', 'ON HOLD') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );

CREATE TABLE
  Roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
  );

CREATE TABLE
  Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role_id INT NOT NULL,
    is_active BOOLEAN NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

CREATE TABLE
  RememberTokens (
    token_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    token_hash VARCHAR(64) UNIQUE NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

CREATE TABLE
  AuditLogs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    action_type VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

CREATE UNIQUE INDEX Classes_index_0 ON Classes (class_name, section);

CREATE UNIQUE INDEX Rooms_index_1 ON Rooms (room_name);

CREATE UNIQUE INDEX TeachingAssignments_index_2 ON TeachingAssignments (class_id, subject_id);

CREATE UNIQUE INDEX unique_room_schedule ON ClassSchedules (room_id, day_of_week, start_time, end_time);

CREATE UNIQUE INDEX idx_teacher_period ON TeacherPayroll (teacher_id, pay_period);

CREATE INDEX idx_payroll_status ON TeacherPayroll (status);

CREATE INDEX idx_expires ON RememberTokens (expires_at);

ALTER TABLE Students ADD FOREIGN KEY (class_id) REFERENCES Classes (class_id) ON DELETE CASCADE;

ALTER TABLE TeachingAssignments ADD FOREIGN KEY (class_id) REFERENCES Classes (class_id);

ALTER TABLE TeachingAssignments ADD FOREIGN KEY (subject_id) REFERENCES Subjects (subject_id);

ALTER TABLE TeachingAssignments ADD FOREIGN KEY (teacher_id) REFERENCES Teachers (teacher_id);

ALTER TABLE ClassSchedules ADD FOREIGN KEY (class_id) REFERENCES Classes (class_id);

ALTER TABLE ClassSchedules ADD FOREIGN KEY (subject_id) REFERENCES Subjects (subject_id);

ALTER TABLE ClassSchedules ADD FOREIGN KEY (room_id) REFERENCES Rooms (room_id);

ALTER TABLE StudentFees ADD FOREIGN KEY (student_id) REFERENCES Students (student_id) ON DELETE CASCADE;

ALTER TABLE TeacherPayroll ADD FOREIGN KEY (teacher_id) REFERENCES Teachers (teacher_id) ON DELETE CASCADE;

ALTER TABLE Users ADD FOREIGN KEY (role_id) REFERENCES Roles (role_id);

ALTER TABLE RememberTokens ADD FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE;

ALTER TABLE AuditLogs ADD FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE SET NULL;

-- Disable foreign key checks for INSERT
SET
  FOREIGN_KEY_CHECKS = 0;

INSERT INTO
  Roles (role_name, description)
VALUES
  ('ADMIN', 'Full system access and configuration'),
  (
    'TEACHER',
    'Access to grading, schedules, and assigned classes'
  ),
  (
    'STUDENT',
    'Access to view attendance, grades, and fees'
  );

-- Re-enable foreign key checks
SET
  FOREIGN_KEY_CHECKS = 1;
  
