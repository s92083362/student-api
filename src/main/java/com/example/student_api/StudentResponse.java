
package com.example.student_api;

public record StudentResponse(
        Long id,
        String name,
        String email,
        String course
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse()
        );
    }
}
