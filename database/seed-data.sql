USE school_management;

SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- ROOMS
-- =========================
INSERT INTO Rooms (room_name, room_type) VALUES
('A101', 'CLASSROOM'),
('A102', 'CLASSROOM'),
('B201', 'CLASSROOM'),
('LAB01', 'LAB'),
('LAB02', 'LAB');
SELECT * FROM Users;
-- =========================
-- CLASSES
-- =========================
INSERT INTO Classes (class_name, section, room_id) VALUES
('SE1801', 'A', 1),
('SE1802', 'A', 2),
('AI1801', 'B', 3),
('GD1801', 'A', 1),
('IB1801', 'B', 2);

-- =========================
-- TEACHERS
-- =========================
SELECT * FROM Teachers;
INSERT INTO Teachers (teacher_code, teacher_name, phone, email, salary) VALUES
('TIT001', 'Nguyen Van Minh', '0901234567', 'minh.nguyen@school.edu.vn', 1800.00),
('TIT002', 'Tran Thi Lan', '0912345678', 'lan.tran@school.edu.vn', 2000.00),
('TSE001', 'Le Hoang Phuc', '0923456789', 'phuc.le@school.edu.vn', 2200.00),
('TAI001', 'Pham Quoc Bao', '0934567890', 'bao.pham@school.edu.vn', 2100.00),
('TBA001', 'Vo Thi Huong', '0945678901', 'huong.vo@school.edu.vn', 1950.00);

-- =========================
-- SUBJECTS
-- =========================
INSERT INTO Subjects (subject_name) VALUES
('Database Systems'),
('Java Web Development'),
('Data Structures'),
('Operating Systems'),
('Software Engineering');

-- =========================
-- STUDENTS
-- =========================
INSERT INTO Students (student_code, student_name, major, email, class_id) VALUES
('SE001', 'Tran Gia Bao', 'Software Engineering', 'bao.tran@student.edu.com', 1),
('SE002', 'Nguyen Minh Quan', 'Software Engineering', 'quan.nguyen@student.edu.com', 1),
('SE003', 'Le Anh Khoa', 'Software Engineering', 'khoa.le@student.edu.com', 2),
('AI001', 'Pham Duc Huy', 'Artificial Intelligence', 'huy.pham@student.edu.com', 3),
('GD001', 'Vo Thanh Dat', 'Graphic Design', 'dat.vo@student.edu.com', 4),
('IB001', 'Nguyen Hoang Nam', 'International Business', 'nam.nguyen@student.edu.com', 5),
('SE004', 'Tran Bao Chau', 'Software Engineering', 'chau.tran@student.edu.com', 2),
('AI002', 'Le Minh Thu', 'Artificial Intelligence', 'thu.le@student.edu.com', 3),
('GD002', 'Pham Ngoc Han', 'Graphic Design', 'han.pham@student.edu.com', 4),
('IB002', 'Vo Gia Huy', 'International Business', 'huy.vo@student.edu.com', 5);

-- =========================
-- TEACHING ASSIGNMENTS
-- =========================
INSERT INTO TeachingAssignments (class_id, subject_id, teacher_id) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 3),
(3, 4, 4),
(4, 5, 5);

-- =========================
-- CLASS SCHEDULES
-- =========================
INSERT INTO ClassSchedules (
    class_id,
    subject_id,
    room_id,
    day_of_week,
    start_time,
    end_time
) VALUES
(1, 1, 1, 'MONDAY', '07:00:00', '09:00:00'),
(1, 2, 4, 'TUESDAY', '09:00:00', '11:00:00'),
(2, 3, 2, 'WEDNESDAY', '13:00:00', '15:00:00'),
(3, 4, 3, 'THURSDAY', '07:00:00', '09:00:00'),
(4, 5, 1, 'FRIDAY', '15:00:00', '17:00:00');

-- =========================
-- STUDENT FEES
-- =========================
INSERT INTO StudentFees (
    student_id,
    amount,
    due_date,
    payment_date,
    status
) VALUES
(1, 500.00, '2026-06-01', '2026-05-25', 'PAID'),
(2, 500.00, '2026-06-01', NULL, 'UNPAID'),
(3, 550.00, '2026-06-01', '2026-05-28', 'PAID'),
(4, 600.00, '2026-06-01', NULL, 'UNPAID'),
(5, 450.00, '2026-06-01', '2026-05-20', 'PAID');

-- =========================
-- TEACHER PAYROLL
-- =========================
INSERT INTO TeacherPayroll (
    teacher_id,
    pay_period,
    base_salary,
    allowances,
    deductions,
    payment_date,
    status
) VALUES
(1, '2026-05', 1800.00, 150.00, 50.00, '2026-05-10', 'DISBURSED'),
(2, '2026-05', 2000.00, 200.00, 100.00, '2026-05-10', 'DISBURSED'),
(3, '2026-05', 2200.00, 250.00, 120.00, '2026-05-10', 'PENDING'),
(4, '2026-05', 2100.00, 180.00, 80.00, '2026-05-10', 'ON_HOLD'),
(5, '2026-05', 1950.00, 170.00, 70.00, '2026-05-10', 'DISBURSED');
SELECT * FROM TeacherPayroll;
-- =========================
-- USERS
-- password = 123456
-- =========================
INSERT INTO Users (
    username,
    password_hash,
    email,
    role_id,
    is_active
) VALUES
(
    'admin',
    '$2a$10$u8VMeafBqkuzXh43WLkLH.eaoB54nNilZOsdnGeAURpicUeTyQcpy',
    'admin@school.edu.vn',
    1,
    TRUE
),
(
    'teacher_minh',
    '$2a$10$50KAf2.Xoc1e.A.lyqBmTub79lR/LwvKXrlvr8shx40cvik3G9aLS',
    'minh.teacher@school.edu.vn',
    2,
    TRUE
),
(
    'student_bao',
    '$2a$10$6FbKeRuPGayM9jr.T1z2k.wbmWJtQ5cNjPsf7dDhL6LsxMbICZ6NS',
    'bao.student@school.edu.vn',
    3,
    TRUE
);
SELECT * FROM TeacherPayroll;
SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = 'student_bao' AND is_active = TRUE;
-- =========================
-- REMEMBER TOKENS
-- =========================
INSERT INTO RememberTokens (
    user_id,
    token_hash,
    expires_at
) VALUES
(1, 'tokenhash_admin_001', '2026-12-31 23:59:59'),
(2, 'tokenhash_teacher_001', '2026-12-31 23:59:59'),
(3, 'tokenhash_teacher_002', '2026-12-31 23:59:59'),
(4, 'tokenhash_student_001', '2026-12-31 23:59:59'),
(5, 'tokenhash_student_002', '2026-12-31 23:59:59');

-- =========================
-- AUDIT LOGS
-- =========================
INSERT INTO AuditLogs (
    user_id,
    action_type,
    description,
    ip_address
) VALUES
(1, 'LOGIN', 'Admin logged into system', '192.168.1.10'),
(2, 'VIEW_SCHEDULE', 'Teacher viewed class schedule', '192.168.1.11'),
(3, 'UPDATE_GRADE', 'Teacher updated student grade', '192.168.1.12'),
(4, 'PAY_FEE', 'Student paid tuition fee', '192.168.1.13'),
(5, 'LOGOUT', 'Student logged out', '192.168.1.14');

SET FOREIGN_KEY_CHECKS = 1;
SELECT * FROM Roles;