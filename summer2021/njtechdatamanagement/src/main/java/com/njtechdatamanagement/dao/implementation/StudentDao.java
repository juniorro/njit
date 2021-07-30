package com.njtechdatamanagement.dao.implementation;

import com.njtechdatamanagement.dao.DataDao;
import com.njtechdatamanagement.domain.Registration;
import com.njtechdatamanagement.domain.RegistrationPayload;
import com.njtechdatamanagement.domain.Section;
import com.njtechdatamanagement.domain.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JdbcTemplate template;

    // TODO Need to implement full registration logic
    public RegistrationPayload registerStudent(Long studentId, Long courseNumber, Long sectionNumber) {
        RegistrationPayload registrationPayload;
        Student student = get(studentId);
        if (sectionNumber > 0) {
            String sectionQuery = "SELECT * FROM Sections WHERE section_number = ?";
            Section section = template.queryForObject(sectionQuery, sectionRowMapper, sectionNumber);
            if (section == null) {
                throw new RuntimeException("Section does not exist");
            } else if (section.getMaxEnroll() > 25) {
                throw new RuntimeException("Section is at full capacity");
            } else {
                String insertQuery = "INSERT INTO Registrations (student_id, section_number, course_number) VALUES (?, ?, ?)";
                template.update(insertQuery, student.getId(), section.getId(), section.getCourseId());
                registrationPayload = new RegistrationPayload(student.getId(), section.getId(), section.getCourseId());
            }
        } else {
            throw new RuntimeException("A section number is required to enroll in a course");
        }
        return registrationPayload;
    }

    public Collection<Registration> registration() {
        String query = "SELECT c.course_number AS 'course_code', sec.section_number AS 'section_code', c.course_name, secro.section_in_room_weekday AS 'week_day', secro.section_in_room_time AS 'time', bu.building_name, bu.building_location, st.staff_name AS 'instructor', s.student_id, s.student_first_name, s.student_last_name, s.student_major, s.student_year FROM students s JOIN registrations reg ON s.student_id = reg.student_id JOIN sections sec ON reg.section_number = sec.section_number JOIN courses c ON reg.course_number = c.course_number JOIN departments dep ON c.department_code = dep.department_code JOIN staff st ON sec.staff_ssn = st.staff_ssn JOIN sectioninrooms secro ON c.course_number = secro.course_number JOIN buildings bu ON secro.building_id = bu.building_id ORDER BY s.student_last_name ASC";
        return template.query(query, registrationRowMapper);
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
        return template.query(query, studentRowMapper, limit);
    }

    @Override
    public Student get(Long id) throws DataAccessException {
        String query = "SELECT * FROM Students WHERE student_id = ?";
        return template.queryForObject(query, studentRowMapper, id);
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

    RowMapper<Student> studentRowMapper = (resultSet, rowNumber) -> Student.builder()
            .id(resultSet.getLong("student_id"))
            .ssn(resultSet.getString("student_ssn"))
            .firstName(resultSet.getString("student_first_name"))
            .lastName(resultSet.getString("student_last_name"))
            .address(resultSet.getString("student_address"))
            .highSchool(resultSet.getString("student_high_school"))
            .major(resultSet.getString("student_major"))
            .build();

    RowMapper<Section> sectionRowMapper = (resultSet, rowNumber) -> Section.builder()
            .id(resultSet.getLong("section_number"))
            .courseId(resultSet.getLong("course_number"))
            .year(resultSet.getInt("section_year"))
            .semester(resultSet.getString("section_semester"))
            .maxEnroll(resultSet.getInt("section_max_enroll"))
            .instructorSnn(resultSet.getLong("staff_ssn"))
            .build();

    RowMapper<Registration> registrationRowMapper = (resultSet, rowNumber) -> Registration.builder()
            .courseCode(resultSet.getLong("course_code"))
            .sectionCode(resultSet.getLong("section_code"))
            .courseName(resultSet.getString("course_name"))
            .weekDay(resultSet.getString("week_day"))
            .time(resultSet.getString("time"))
            .buildingName(resultSet.getString("building_name"))
            .buildingLocation(resultSet.getString("building_location"))
            .instructor(resultSet.getString("instructor"))
            .studentId(resultSet.getLong("student_id"))
            .studentFirstName(resultSet.getString("student_first_name"))
            .studentLastName(resultSet.getString("student_last_name"))
            .studentMajor(resultSet.getString("student_major"))
            .studentYear(resultSet.getString("student_year") + " Year")
            .build();
}
