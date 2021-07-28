package com.njtechdatamanagement.dao.implementation;

import com.njtechdatamanagement.dao.DataDao;
import com.njtechdatamanagement.domain.Course;
import com.njtechdatamanagement.domain.Registration;
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
import java.util.List;
import java.util.Map;

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

    RowMapper<Student> studentRowMapper = (resultSet, rowNumber) -> Student.builder()
            .id(resultSet.getLong("student_id"))
            .ssn(resultSet.getString("student_ssn"))
            .name(resultSet.getString("student_name"))
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

    RowMapper<Course> mapRowMapper = (resultSet, rowNumber) -> Course.builder()
            .id(resultSet.getLong("id"))
            .build();

    public Registration registerStudent(Long studentId, String courseNumber, int sectionNumber) {
        Registration registration;
        Student student = get(studentId);
            if(sectionNumber > 0) {
                String sectionQuery = "SELECT * FROM Section WHERE section_number = ?";
                Section section = template.queryForObject(sectionQuery, sectionRowMapper, sectionNumber);
                if(section == null) {
                    throw new RuntimeException("Section does not exist");
                } else if(section.getMaxEnroll() > 25){
                    throw new RuntimeException("Section is at full capacity");
                } else {
                    String insertQuery = "INSERT INTO Registration (student_id, section_number, course_number) VALUES (?, ?, ?)";
                    template.update(insertQuery, student.getId(), section.getId(), section.getCourseId());
                    registration = new Registration(student.getId(), section.getId(), section.getCourseId());
                }
            } else {
                throw new RuntimeException("A section number is required to enroll in a course");
        }
        return registration;
    }

    public Map<Object, Object> listSection() {
        String query = "USE njtechdata; SELECT c.course_number AS 'course_code', sec.section_number AS 'section_code', c.course_name, secro.section_in_room_weekday AS 'week_day', secro.section_in_room_time AS 'time', bu.building_name, bu.building_location, st.staff_name AS 'instructor', s.student_id, s.student_first_name, s.student_last_name, s.student_major, s.student_year FROM students s JOIN registrations reg ON s.student_id = reg.student_id JOIN sections sec ON reg.section_number = sec.section_number JOIN courses c ON reg.course_number = c.course_number JOIN departments dep ON c.department_code = dep.department_code JOIN staff st ON sec.staff_ssn = st.staff_ssn JOIN sectioninrooms secro ON c.course_number = secro.course_number JOIN buildings bu ON secro.building_id = bu.building_id ORDER BY s.student_last_name ASC;";
        return null; //template.queryForObject(query, mapRowMapper);
    }

    @Override
    public Student create(Student student) throws DataAccessException {
        String query = "INSERT INTO Student (student_ssn, student_name, student_address, student_high_school, student_year, student_major) VALUES (?, ?, ?, ?, ?)";
        template.update(query, student.getSsn(), student.getName(), student.getAddress(), student.getYear(), student.getHighSchool(), student.getMajor());
        return student;
    }

    @Override
    public Collection<Student> list(int limit) throws DataAccessException {
        String query = "SELECT * FROM Student LIMIT ?";
        return template.query(query, studentRowMapper, limit);
    }

    @Override
    public Student get(Long id) throws DataAccessException {
        String query = "SELECT * FROM Student WHERE student_id = ?";
        return template.queryForObject(query, studentRowMapper, id);
    }

    @Override
    public Student update(Student student) throws DataAccessException {
        String query = "UPDATE Student SET student_ssn = ?, student_name = ?, student_address = ?, student_year = ?, student_high_school = ?, student_major = ? WHERE student_id = ?";
        template.update(query, student.getSsn(), student.getName(), student.getAddress(), student.getYear(), student.getHighSchool(), student.getMajor(), student.getId());
        return student;
    }

    @Override
    public boolean delete(Long id) throws DataAccessException {
        String query = "DELETE FROM Student WHERE student_id = ?";
        return template.update(query, id) > 0;
    }
}
