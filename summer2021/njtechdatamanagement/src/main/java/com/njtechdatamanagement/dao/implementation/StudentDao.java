package com.njtechdatamanagement.dao.implementation;

import com.njtechdatamanagement.dao.DataDao;
import com.njtechdatamanagement.domain.*;
import com.njtechdatamanagement.mapper.CourseRowMapper;
import com.njtechdatamanagement.mapper.RegistrationRowMapper;
import com.njtechdatamanagement.mapper.SectionRowMapper;
import com.njtechdatamanagement.mapper.StudentRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/24/2021
 */

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class StudentDao implements DataDao<Student> {
    public static final int ONE = 1;
    private final JdbcTemplate template;

    public RegistrationPayload registerStudent(Long studentId, Long courseNumber, Long sectionNumber, String time) {
        RegistrationPayload registrationPayload;
        Student student;
        Course course = null;
        Section section = null;
        try {
            student = get(studentId);
        } catch (Exception exception) {
            throw new RuntimeException("Student not found. Please verify the student ID.");
        }
        String totalClassQuery = "SELECT COUNT(student_id) FROM Registrations WHERE student_id = ?";
        Integer currentStudentCount = template.queryForObject(totalClassQuery, Integer.class, student.getId());
        if(currentStudentCount == 4) {
            throw new RuntimeException("Student is already taking 4 classes for the semester.");
        }
        if (courseNumber != null && StringUtils.isNotBlank(time)) {
            String courseQuery = "SELECT * FROM Courses WHERE course_number = ?";
            try {
                course = template.queryForObject(courseQuery, new CourseRowMapper(), courseNumber);
            } catch (Exception exception) {
                throw new RuntimeException("Course not found. Please verify the course code.");
            }
            String alreadyRegisteredQuery = "SELECT student_id FROM Registrations WHERE student_id = ? AND course_number = ?";
            Integer alreadyRegisteredStudentId = template.queryForObject(alreadyRegisteredQuery, Integer.class, student.getId(), course.getId());
            if (alreadyRegisteredStudentId != 0) {
                System.out.println(alreadyRegisteredStudentId);
                throw new RuntimeException("Student is already registered for this course.");
            }
            String sectionInRoomQuery = "SELECT section_number FROM SectionInRooms WHERE course_number = ? AND time = ?";
            Long sectionId = template.queryForObject(sectionInRoomQuery, Long.class, sectionNumber, time);
            String insertQuery = "INSERT INTO Registrations (student_id, section_number, course_number) VALUES (?, ?, ?)";
            template.update(insertQuery, student.getId(), sectionId, courseNumber);
            registrationPayload = new RegistrationPayload(student.getId(), section.getId(), course.getId(), time);
        } else {
            String sectionQuery = "SELECT * FROM Sections WHERE section_number = ?";
            try {
                section = template.queryForObject(sectionQuery, new SectionRowMapper(), sectionNumber);
            } catch (Exception exception) {
                throw new RuntimeException("Section not found. Please verify the section number.");
            }
            String alreadyRegisteredQuery = "SELECT course_number FROM Registrations WHERE student_id = ? AND section_number = ?";
            Integer alreadyRegisteredCourseId = null;
            try {
                alreadyRegisteredCourseId = template.queryForObject(alreadyRegisteredQuery, Integer.class, student.getId(), section.getId());
            } catch (Exception ignored) {

            }
            System.out.println(alreadyRegisteredCourseId);
            if(alreadyRegisteredCourseId != null) { throw new RuntimeException("Student is already registered for this course."); }
            String currentEnrolledQuery = "SELECT COUNT(section_number) FROM registrations WHERE section_number = ?";
            Integer currentEnrolled = template.queryForObject(currentEnrolledQuery, Integer.class, section.getId());
            if (currentEnrolled + ONE > section.getMaxEnroll()) {
                System.out.println(section.getMaxEnroll());
                throw new RuntimeException("Unable to register student. There is no more room in this section.");
            } else {
                String insertQuery = "INSERT INTO Registrations (student_id, section_number, course_number) VALUES (?, ?, ?)";
                template.update(insertQuery, student.getId(), section.getId(), section.getCourseId());
                registrationPayload = new RegistrationPayload(student.getId(), section.getId(), section.getCourseId(), time);
            }
        }
        return registrationPayload;
    }

    public Collection<Registration> registration() {
        String query = "SELECT c.course_number AS 'course_code', sec.section_number AS 'section_code', c.course_name, secro.weekday AS 'week_day', secro.time AS 'time', bu.building_name, bu.building_location, st.staff_name AS 'instructor', s.student_id, s.student_first_name, s.student_last_name, s.student_major, s.student_year FROM students s JOIN registrations reg ON s.student_id = reg.student_id JOIN sections sec ON reg.section_number = sec.section_number JOIN courses c ON reg.course_number = c.course_number JOIN departments dep ON c.department_code = dep.department_code JOIN staff st ON sec.staff_ssn = st.staff_ssn JOIN sectioninrooms secro ON c.course_number = secro.course_number JOIN buildings bu ON secro.building_id = bu.building_id ORDER BY s.student_last_name ASC";
        return template.query(query, new RegistrationRowMapper());
    }

    @Override
    public Student create(Student student) throws DataAccessException {
        String query = "INSERT INTO Students (student_ssn, student_first_name, student_last_name, student_address, student_high_school, student_year, student_major) VALUES (?, ?, ?, ?, ?, ?, ?)";
        template.update(query, student.getSsn(), student.getFirstName(), student.getLastName(), student.getAddress(), student.getYear(), student.getHighSchool(), student.getMajor());
        return student;
    }

    @Override
    public Collection<Student> list(int limit) throws DataAccessException {
        String query = "SELECT * FROM Students ORDER BY student_first_name LIMIT ?";
        return template.query(query, new StudentRowMapper(), limit);
    }

    @Override
    public Student get(Long id) throws DataAccessException {
        String query = "SELECT * FROM Students WHERE student_id = ?";
        return template.queryForObject(query, new StudentRowMapper(), id);
    }

    @Override
    public Student update(Student student) throws DataAccessException {
        String query = "UPDATE Students SET student_ssn = ?, student_first_name = ?, student_last_name = ?, student_address = ?, student_year = ?, student_high_school = ?, student_major = ? WHERE student_id = ?";
        template.update(query, student.getSsn(), student.getFirstName(), student.getLastName(), student.getAddress(), student.getYear(), student.getHighSchool(), student.getMajor(), student.getId());
        return student;
    }

    @Override
    public boolean delete(Long id) throws DataAccessException {
        String query = "DELETE FROM Students WHERE student_id = ?";
        return template.update(query, id) > 0;
    }
}
