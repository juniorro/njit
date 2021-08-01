package com.njtechdatamanagement.mapper;

import com.njtechdatamanagement.domain.Course;
import com.njtechdatamanagement.domain.Section;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/28/2021
 */

public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return Course.builder()
                .id(resultSet.getLong("course_number"))
                .name(resultSet.getString("course_name"))
                .credit(resultSet.getDouble("course_credit"))
                .tARequiredHrs(resultSet.getString("student_ta_hr_required"))
                .departmentCode(resultSet.getString("department_code"))
                .build();
    }
}
