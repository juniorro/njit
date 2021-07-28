package com.njtechdatamanagement.exception;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/23/2021
 */
public class DataNotFound extends RuntimeException {
    public DataNotFound(String message) {
        super(message);
    }
}
