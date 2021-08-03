package com.njtechdatamanagement.mapper;

import com.njtechdatamanagement.domain.ClassInfo;
import com.njtechdatamanagement.domain.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/28/2021
 */

public class ClassInfoRowMapper implements RowMapper<ClassInfo> {
    @Override
    public ClassInfo mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return ClassInfo.builder()
                .courseId(resultSet.getLong("course_number"))
                .sectionId(resultSet.getLong("section_number"))
                .courseName(resultSet.getString("course_name"))
                .courseCredit(resultSet.getInt("course_credit"))
                .weekDay(resultSet.getString("weekday"))
                .time(resultSet.getString("time"))
                .build();
    }
}
