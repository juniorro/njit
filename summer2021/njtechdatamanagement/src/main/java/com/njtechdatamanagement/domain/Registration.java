package com.njtechdatamanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Get Arrays (https://www.getarrays.io/)
 * @version 1.0
 * @since 7/28/2021
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class Registration {
    private Long courseCode;
    private Long sectionCode;
    private String courseName;
    private String weekDay;
    private String time;
    private String buildingName;
    private String buildingLocation;
    private String instructor;
    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentMajor;
    private String studentYear;
}
