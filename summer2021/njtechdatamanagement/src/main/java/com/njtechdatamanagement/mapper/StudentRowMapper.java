package com.njtechdatamanagement.mapper;

import com.njtechdatamanagement.domain.Course;
import com.njtechdatamanagement.domain.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/28/2021
 */

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return Student.builder()
                .id(resultSet.getLong("student_id"))
                .ssn(resultSet.getString("student_ssn"))
                .firstName(resultSet.getString("student_first_name"))
                .lastName(resultSet.getString("student_last_name"))
                .address(resultSet.getString("student_address"))
                .highSchool(resultSet.getString("student_high_school"))
                .major(resultSet.getString("student_major"))
                .build();
    }
}
