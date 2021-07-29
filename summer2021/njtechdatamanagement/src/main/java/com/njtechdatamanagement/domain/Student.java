package com.njtechdatamanagement.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/24/2021
 */

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Student {
    private Long id;
    private String ssn;
    private String firstName;
    private String lastName;
    private String address;
    private String highSchool;
    private String year;
    private String major;
}
