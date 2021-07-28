USE njtechdata;

DROP TABLE IF EXISTS Students;

CREATE TABLE Students
(
    student_id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    student_ssn         VARCHAR(36)     NOT NULL,
    student_first_name  VARCHAR(255) DEFAULT NULL,
    student_last_name   VARCHAR(255) DEFAULT NULL,
    student_address     VARCHAR(255) DEFAULT NULL,
    student_high_school VARCHAR(255) DEFAULT NULL,
    student_year        VARCHAR(10)  DEFAULT NULL,
    student_major       VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (student_id)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Departments;

CREATE TABLE Departments
(
    department_code        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    department_name        VARCHAR(255)   DEFAULT NULL,
    department_budget      DECIMAL(10, 2) DEFAULT 0.00,
    department_building_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (department_code)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Courses;

CREATE TABLE Courses
(
    course_number          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    course_name            VARCHAR(255) DEFAULT NULL,
    course_credit          SMALLINT(10) DEFAULT 0,
    student_ta_hr_required SMALLINT(10) DEFAULT 0,
    department_code        BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (course_number),
    FOREIGN KEY (department_code) REFERENCES Departments (department_code)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Staff;

CREATE TABLE Staff
(
    staff_ssn         BIGINT UNSIGNED NOT NULL,
    staff_name        VARCHAR(255)   DEFAULT NULL,
    staff_salary      DECIMAL(10, 2) DEFAULT 0.00,
    staff_rank        VARCHAR(10)    DEFAULT NULL,
    staff_course_load TINYINT        DEFAULT 0,
    PRIMARY KEY (staff_ssn)
);

DROP TABLE IF EXISTS Sections;

CREATE TABLE Sections
(
    section_number     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    course_number      BIGINT UNSIGNED NOT NULL,
    section_year       SMALLINT(10),
    section_semester   VARCHAR(10) DEFAULT NULL,
    section_max_enroll TINYINT     DEFAULT 0,
    staff_ssn          BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (section_number, course_number),
    FOREIGN KEY (staff_ssn) REFERENCES Staff (staff_ssn)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Registrations;

CREATE TABLE Registrations
(
    student_id     BIGINT UNSIGNED NOT NULL,
    section_number BIGINT UNSIGNED NOT NULL,
    course_number  BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (student_id) REFERENCES Students (student_id),
    FOREIGN KEY (section_number) REFERENCES Sections (section_number),
    FOREIGN KEY (course_number) REFERENCES Courses (course_number)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Buildings;

CREATE TABLE Buildings
(
    building_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    building_name     VARCHAR(255) DEFAULT NULL,
    building_location VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (building_id)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Rooms;

CREATE TABLE Rooms
(
    building_id       BIGINT UNSIGNED NOT NULL,
    room_number       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    room_capacity     SMALLINT(10) DEFAULT 1,
    room_audio_visual BOOLEAN      DEFAULT FALSE,
    PRIMARY KEY (room_number, building_id),
    FOREIGN KEY (building_id) REFERENCES Buildings (building_id)
) AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS SectionInRooms;

CREATE TABLE SectionInRooms
(
    building_id             BIGINT UNSIGNED NOT NULL,
    room_number             BIGINT UNSIGNED NOT NULL,
    course_number           BIGINT UNSIGNED NOT NULL,
    section_in_room_weekday VARCHAR(30) DEFAULT NULL,
    section_in_room_time    VARCHAR(30) DEFAULT NULL,
    FOREIGN KEY (building_id) REFERENCES Buildings (building_id),
    FOREIGN KEY (room_number) REFERENCES Rooms (room_number),
    FOREIGN KEY (course_number) REFERENCES Courses (course_number)
);