package com.njtechdatamanagement.web;

import com.njtechdatamanagement.dao.implementation.StudentDao;
import com.njtechdatamanagement.domain.Registration;
import com.njtechdatamanagement.domain.RegistrationPayload;
import com.njtechdatamanagement.domain.Student;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
    public ResponseEntity<Collection<Student>> getStudents() {
        return ResponseEntity.ok().body(studentDao.list(5));
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationPayload> registerStudent(@RequestBody RegisterForm form) {
        return ResponseEntity.ok().body(studentDao.registerStudent(form.getStudentId(), form.getCourseCode(), form.getSectionNumber()));
    }

    @GetMapping("/registrations")
    public ResponseEntity<Collection<Registration>> getRegistrations() {
        return ResponseEntity.ok().body(studentDao.registration());
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

@Data
class RegisterForm {
    private Long studentId;
    private String courseCode;
    private int sectionNumber;
}
