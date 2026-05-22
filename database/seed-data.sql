-- =========================================
-- SEED DATA FOR SCHOOL MANAGEMENT DATABASE
-- =========================================

SET FOREIGN_KEY_CHECKS = 0;

-- =========================================
-- ROOMS
-- =========================================
INSERT INTO Rooms (room_name, room_type)
VALUES
  ('A101', 'CLASSROOM'),
  ('A102', 'CLASSROOM'),
  ('B201', 'CLASSROOM'),
  ('LAB01', 'LAB'),
  ('LAB02', 'LAB');

-- =========================================
-- CLASSES
-- =========================================
INSERT INTO Classes (class_name, section, room_id)
VALUES
  ('Grade 10', 'A', 1),
  ('Grade 10', 'B', 2),
  ('Grade 11', 'A', 3),
  ('Grade 11', 'B', 4),
  ('Grade 12', 'A', 5);

-- =========================================
-- TEACHERS
-- =========================================
INSERT INTO Teachers (teacher_name, phone, email, salary)
VALUES
  ('Nguyen Van An', '0901234567', 'an.teacher@hcmiu.edu.com.vn', 1800.00),
  ('Tran Thi Mai', '0912345678', 'mai.teacher@hcmiu.edu.com.vn', 1750.00),
  ('Le Hoang Nam', '0923456789', 'nam.teacher@hcmiu.edu.com.vn', 1900.00),
  ('Pham Gia Bao', '0934567890', 'bao.teacher@hcmiu.edu.com.vn', 1850.00),
  ('Vo Minh Khang', '0945678901', 'khang.teacher@hcmiu.edu.com.vn', 2000.00);

-- =========================================
-- SUBJECTS
-- =========================================
INSERT INTO Subjects (subject_name)
VALUES
  ('Mathematics'),
  ('Physics'),
  ('Chemistry'),
  ('English'),
  ('Computer Science');

-- =========================================
-- STUDENTS
-- =========================================
INSERT INTO Students (roll_no, student_name, class_id)
VALUES
  ('STU001', 'Nguyen Minh Phuc', 1),
  ('STU002', 'Tran Bao Chau', 2),
  ('STU003', 'Le Quoc Huy', 3),
  ('STU004', 'Pham Gia Linh', 4),
  ('STU005', 'Vo Thanh Dat', 5);

-- =========================================
-- TEACHING ASSIGNMENTS
-- =========================================
INSERT INTO TeachingAssignments (class_id, subject_id, teacher_id)
VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 3, 3),
  (4, 4, 4),
  (5, 5, 5);

-- =========================================
-- STUDENT FEES
-- =========================================
INSERT INTO StudentFees (student_id, amount, due_date, payment_date, status)
VALUES
  (1, 500.00, '2026-06-01', '2026-05-20', 'PAID'),
  (2, 500.00, '2026-06-01', NULL, 'UNPAID'),
  (3, 550.00, '2026-06-05', '2026-05-22', 'PAID'),
  (4, 600.00, '2026-06-10', NULL, 'UNPAID'),
  (5, 650.00, '2026-06-15', '2026-05-21', 'PAID');

-- =========================================
-- CLASS SCHEDULES
-- =========================================
INSERT INTO ClassSchedules (
  class_id,
  subject_id,
  room_id,
  day_of_week,
  start_time,
  end_time
)
VALUES
  (1, 1, 1, 'MONDAY', '08:00:00', '09:30:00'),
  (2, 2, 2, 'TUESDAY', '09:45:00', '11:15:00'),
  (3, 3, 3, 'WEDNESDAY', '13:00:00', '14:30:00'),
  (4, 4, 4, 'THURSDAY', '08:00:00', '09:30:00'),
  (5, 5, 5, 'FRIDAY', '10:00:00', '11:30:00');

-- =========================================
-- USERS
-- Password example:
-- admin123 / teacher123 / student123
-- Replace with real BCrypt hashes in production
-- =========================================
INSERT INTO Users (
  username,
  password_hash,
  email,
  role_id,
  is_active
)
VALUES
  ('admin', '$2a$10$MIrjr5q6lJ9/VufBJFDScO.rL718v2An8D9pNaeiv1Y41dry6ecme', 'admin@hcmiu.edu.com.vn', 1),
  ('teacheran', '$2a$10$xFdJZ7V8jgOv4O3rOoapLun6CocFZKj7BAcriZjTFqWT9aikRQCpa', 'an.teacher@hcmiu.edu.com.vn', 2),
  ('studentphuc', '$2a$10$c/p4GC/cZt2B99/OPChKXe4cS892BxS/FE/ghudmuodHwdn1oe45W', 'phuc.student@hcmiu.edu.com.vn', 3);