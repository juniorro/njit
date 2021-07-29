package com.njtechdatamanagement.web;

import com.njtechdatamanagement.dao.implementation.StudentDao;
import com.njtechdatamanagement.domain.HttpResponse;
import com.njtechdatamanagement.domain.Registration;
import com.njtechdatamanagement.domain.RegistrationPayload;
import com.njtechdatamanagement.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singleton;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 7/24/2021
 */

@RestController
@RequestMapping(path = "/student")
@RequiredArgsConstructor
public class StudentResource {
    private final StudentDao studentDao;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse<?, ?>>getStudents() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                .timeStamp(LocalDateTime.now())
                .students(studentDao.list(30))
                .developerMessage("All students retrieved")
                .message("All students retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationPayload> registerStudent(@RequestBody RegistrationPayload form) {
        return ResponseEntity.ok().body(studentDao.registerStudent(form.getStudentId(), form.getCourseId(), form.getSectionId()));
    }

    @GetMapping("/registrations")
    public ResponseEntity<HttpResponse<?, ?>> getRegistrations() {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now())
                        .registrations(singleton(studentDao.registration()))
                        .developerMessage("Registrations retrieved")
                        .message("Registrations retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/save")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        return ResponseEntity.ok().body(studentDao.create(student));
    }

    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        return ResponseEntity.ok().body(studentDao.update(student));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(studentDao.get(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(studentDao.delete(id));
    }

}