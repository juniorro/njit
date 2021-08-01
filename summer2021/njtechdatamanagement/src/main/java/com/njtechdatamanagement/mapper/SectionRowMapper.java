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

public class SectionRowMapper implements RowMapper<Section> {
    @Override
    public Section mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return Section.builder()
                .id(resultSet.getLong("section_number"))
                .courseId(resultSet.getLong("course_number"))
                .year(resultSet.getInt("section_year"))
                .semester(resultSet.getString("section_semester"))
                .maxEnroll(resultSet.getInt("section_max_enroll"))
                .instructorSnn(resultSet.getLong("staff_ssn"))
                .build();
    }
}
