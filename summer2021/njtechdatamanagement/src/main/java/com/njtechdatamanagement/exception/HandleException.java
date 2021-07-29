package com.njtechdatamanagement.exception;

/**
 * @author Get Arrays (https://www.getarrays.io/)
 * @version 1.0
 * @since 7/29/2021
 */

import com.njtechdatamanagement.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 4/12/2021
 */

@RestControllerAdvice @Slf4j
public class HandleException extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .developerMessage(exception.getMessage())
                        .reason(exception.getMessage())
                        .status(status)
                        .statusCode(status.value()).build()
                , status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<?>> internalServerErrorException(Exception exception) {
        log.error(exception.getMessage());
        return createErrorHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG, exception);
    }

    private ResponseEntity<HttpResponse<?>> createErrorHttpResponse(HttpStatus httpStatus, String reason, Exception exception) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .developerMessage(exception.getMessage())
                        .reason(reason)
                        .status(httpStatus)
                        .statusCode(httpStatus.value()).build()
                , httpStatus);
    }

}
