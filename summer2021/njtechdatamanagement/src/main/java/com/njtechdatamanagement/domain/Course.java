package com.njtechdatamanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/25/2021
 */

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Course {
    private Long id;
    private String name;
    private Double credit;
    private String tARequiredHrs;
    private String departmentCode;
}
