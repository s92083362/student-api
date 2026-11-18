
package com.example.student_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void cleanDatabase() {
        studentRepository.deleteAll();
    }

    private Long createTestStudent() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Test Student",
                            "email": "test@gmail.com",
                            "course": "Software Engineering"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        return Long.parseLong(id);
    }

    @Test
    void createStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Test Student",
                            "email": "test@gmail.com",
                            "course": "Software Engineering"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void getAllStudents() throws Exception {
        createTestStudent();

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getStudentById() throws Exception {
        Long id = createTestStudent();

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void getStudentByInvalidId() throws Exception {
        mockMvc.perform(get("/api/students/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent() throws Exception {
        Long id = createTestStudent();

        mockMvc.perform(put("/api/students/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Updated Student",
                            "email": "updated@gmail.com",
                            "course": "Artificial Intelligence"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Student"))
                .andExpect(jsonPath("$.course").value("Artificial Intelligence"));
    }

    @Test
    void updateInvalidStudent() throws Exception {
        mockMvc.perform(put("/api/students/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Updated Student",
                            "email": "updated@gmail.com",
                            "course": "Artificial Intelligence"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = createTestStudent();

        mockMvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteInvalidStudent() throws Exception {
        mockMvc.perform(delete("/api/students/9999"))
                .andExpect(status().isNotFound());
    }
}
