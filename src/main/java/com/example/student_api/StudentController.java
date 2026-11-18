
package com.example.student_api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add a new student
    @PostMapping
    public StudentResponse addStudent(
            @RequestBody StudentRequest request) {

        Student student = new Student();
        student.setName(request.name());
        student.setEmail(request.email());
        student.setCourse(request.course());

        Student savedStudent = studentRepository.save(student);

        return StudentResponse.from(savedStudent);
    }

    // Get all students
    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::from)
                .toList();
    }

    // Get one student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(
            @PathVariable Long id) {

        return studentRepository.findById(id)
                .map(StudentResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a student
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequest request) {

        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(request.name());
                    student.setEmail(request.email());
                    student.setCourse(request.course());

                    Student updatedStudent =
                            studentRepository.save(student);

                    return ResponseEntity.ok(
                            StudentResponse.from(updatedStudent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
