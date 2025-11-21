package com.example.biblioteca.api;

import com.example.biblioteca.dto.AssuntoDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.service.AssuntoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssuntoController.class)
public class AssuntoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssuntoService assuntoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAssuntos_ReturnsPageOfAssuntoDTOs() throws Exception {
        AssuntoDTO assuntoDTO = new AssuntoDTO(1L, "Ficção Científica");
        List<AssuntoDTO> assuntoDTOList = Arrays.asList(assuntoDTO);
        Pageable pageable = PageRequest.of(0, 10);
        Page<AssuntoDTO> assuntoDTOPage = new PageImpl<>(assuntoDTOList, pageable, 1);

        when(assuntoService.findAll(any(Pageable.class))).thenReturn(assuntoDTOPage);

        mockMvc.perform(get("/api/v1/assuntos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].descricao").value("Ficção Científica"));

        verify(assuntoService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAssuntoById_ExistingId_ReturnsAssuntoDTO() throws Exception {
        AssuntoDTO assuntoDTO = new AssuntoDTO(1L, "Ficção Científica");

        when(assuntoService.findById(1L)).thenReturn(assuntoDTO);

        mockMvc.perform(get("/api/v1/assuntos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descricao").value("Ficção Científica"));

        verify(assuntoService, times(1)).findById(1L);
    }

    @Test
    void getAssuntoById_NonExistingId_ReturnsNotFound() throws Exception {
        when(assuntoService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Assunto not found"));

        mockMvc.perform(get("/api/v1/assuntos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(assuntoService, times(1)).findById(99L);
    }

    @Test
    void createAssunto_ValidAssuntoDTO_ReturnsCreatedAssuntoDTO() throws Exception {
        AssuntoDTO assuntoDTO = new AssuntoDTO(null, "Fantasia");
        AssuntoDTO createdAssuntoDTO = new AssuntoDTO(2L, "Fantasia");

        when(assuntoService.create(any(AssuntoDTO.class))).thenReturn(createdAssuntoDTO);

        mockMvc.perform(post("/api/v1/assuntos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assuntoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.descricao").value("Fantasia"));

        verify(assuntoService, times(1)).create(any(AssuntoDTO.class));
    }

    @Test
    void createAssunto_InvalidAssuntoDTO_ReturnsBadRequest() throws Exception {
        AssuntoDTO invalidAssuntoDTO = new AssuntoDTO(null, ""); // Invalid description

        mockMvc.perform(post("/api/v1/assuntos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAssuntoDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(assuntoService, never()).create(any(AssuntoDTO.class));
    }

    @Test
    void updateAssunto_ExistingIdAndValidAssuntoDTO_ReturnsUpdatedAssuntoDTO() throws Exception {
        AssuntoDTO updatedAssuntoDTO = new AssuntoDTO(1L, "Romance Histórico");

        when(assuntoService.update(anyLong(), any(AssuntoDTO.class))).thenReturn(updatedAssuntoDTO);

        mockMvc.perform(put("/api/v1/assuntos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAssuntoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descricao").value("Romance Histórico"));

        verify(assuntoService, times(1)).update(anyLong(), any(AssuntoDTO.class));
    }

    @Test
    void updateAssunto_NonExistingId_ReturnsNotFound() throws Exception {
        AssuntoDTO assuntoDTO = new AssuntoDTO(99L, "Assunto Inexistente");
        when(assuntoService.update(anyLong(), any(AssuntoDTO.class))).thenThrow(new ResourceNotFoundException("Assunto not found"));

        mockMvc.perform(put("/api/v1/assuntos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assuntoDTO)))
                .andExpect(status().isNotFound());

        verify(assuntoService, times(1)).update(anyLong(), any(AssuntoDTO.class));
    }

    @Test
    void deleteAssunto_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(assuntoService).delete(1L);

        mockMvc.perform(delete("/api/v1/assuntos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(assuntoService, times(1)).delete(1L);
    }

    @Test
    void deleteAssunto_NonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Assunto not found")).when(assuntoService).delete(anyLong());

        mockMvc.perform(delete("/api/v1/assuntos/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(assuntoService, times(1)).delete(99L);
    }
}
