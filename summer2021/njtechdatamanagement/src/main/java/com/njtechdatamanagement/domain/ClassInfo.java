package com.njtechdatamanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 8/3/2021
 */

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ClassInfo {
    private Long courseId;
    private Long sectionId;
    private int courseCredit;
    private String courseName;
    private String weekDay;
    private String time;
}
