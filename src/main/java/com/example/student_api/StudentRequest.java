
package com.example.student_api;

public record StudentRequest(
        String name,
        String email,
        String course
) {
}
