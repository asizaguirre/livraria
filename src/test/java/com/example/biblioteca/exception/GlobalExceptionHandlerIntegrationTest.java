package com.example.biblioteca.exception;

import com.example.biblioteca.api.AutorController;
import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutorController.class) // Use any controller to trigger handlers
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleResourceNotFoundException_ReturnsNotFoundAndErrorResponse() throws Exception {
        when(autorService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Autor not found for id: 1"));

        mockMvc.perform(get("/api/v1/autores/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Autor not found for id: 1"));
    }

    @Test
    void handleMethodArgumentNotValidException_ReturnsUnprocessableEntityAndErrorResponse() throws Exception {
        AutorDTO invalidAutorDTO = new AutorDTO(null, ""); // Empty name, which is invalid

        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAutorDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
                // Detailed message check might be brittle due to Map.toString(),
                // but checking for presence of validation message is good.
    }

    // DataIntegrityViolationException is harder to simulate without a real DB context
    // or by mocking repository behavior to throw it, but it would essentially follow
    // the same pattern as ResourceNotFoundException in the handler.

    @Test
    void handleGlobalException_ReturnsInternalServerErrorAndErrorResponse() throws Exception {
        when(autorService.findById(anyLong())).thenThrow(new RuntimeException("Something unexpected happened"));

        mockMvc.perform(get("/api/v1/autores/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Something unexpected happened"));
    }
}
