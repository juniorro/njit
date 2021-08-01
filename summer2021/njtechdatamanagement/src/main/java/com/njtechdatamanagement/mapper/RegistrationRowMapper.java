package com.njtechdatamanagement.mapper;

import com.njtechdatamanagement.domain.Registration;
import com.njtechdatamanagement.domain.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/28/2021
 */

public class RegistrationRowMapper implements RowMapper<Registration> {
    @Override
    public Registration mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return Registration.builder()
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
}
