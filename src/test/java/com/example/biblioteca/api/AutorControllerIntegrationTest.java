package com.example.biblioteca.api;

import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.service.AutorService;
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

@WebMvcTest(AutorController.class)
public class AutorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAutores_ReturnsPageOfAutorDTOs() throws Exception {
        AutorDTO autorDTO = new AutorDTO(1L, "Machado de Assis");
        List<AutorDTO> autorDTOList = Arrays.asList(autorDTO);
        Pageable pageable = PageRequest.of(0, 10);
        Page<AutorDTO> autorDTOPage = new PageImpl<>(autorDTOList, pageable, 1);

        when(autorService.findAll(any(Pageable.class))).thenReturn(autorDTOPage);

        mockMvc.perform(get("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Machado de Assis"));

        verify(autorService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAutorById_ExistingId_ReturnsAutorDTO() throws Exception {
        AutorDTO autorDTO = new AutorDTO(1L, "Machado de Assis");

        when(autorService.findById(1L)).thenReturn(autorDTO);

        mockMvc.perform(get("/api/v1/autores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));

        verify(autorService, times(1)).findById(1L);
    }

    @Test
    void getAutorById_NonExistingId_ReturnsNotFound() throws Exception {
        when(autorService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Autor not found"));

        mockMvc.perform(get("/api/v1/autores/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(autorService, times(1)).findById(99L);
    }

    @Test
    void createAutor_ValidAutorDTO_ReturnsCreatedAutorDTO() throws Exception {
        AutorDTO autorDTO = new AutorDTO(null, "Guimarães Rosa");
        AutorDTO createdAutorDTO = new AutorDTO(2L, "Guimarães Rosa");

        when(autorService.create(any(AutorDTO.class))).thenReturn(createdAutorDTO);

        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nome").value("Guimarães Rosa"));

        verify(autorService, times(1)).create(any(AutorDTO.class));
    }

    @Test
    void createAutor_InvalidAutorDTO_ReturnsBadRequest() throws Exception {
        AutorDTO invalidAutorDTO = new AutorDTO(null, ""); // Invalid name

        mockMvc.perform(post("/api/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAutorDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(autorService, never()).create(any(AutorDTO.class));
    }

    @Test
    void updateAutor_ExistingIdAndValidAutorDTO_ReturnsUpdatedAutorDTO() throws Exception {
        AutorDTO updatedAutorDTO = new AutorDTO(1L, "Carlos Drummond");

        when(autorService.update(anyLong(), any(AutorDTO.class))).thenReturn(updatedAutorDTO);

        mockMvc.perform(put("/api/v1/autores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAutorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Carlos Drummond"));

        verify(autorService, times(1)).update(anyLong(), any(AutorDTO.class));
    }

    @Test
    void updateAutor_NonExistingId_ReturnsNotFound() throws Exception {
        AutorDTO autorDTO = new AutorDTO(99L, "Autor Inexistente");
        when(autorService.update(anyLong(), any(AutorDTO.class))).thenThrow(new ResourceNotFoundException("Autor not found"));

        mockMvc.perform(put("/api/v1/autores/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autorDTO)))
                .andExpect(status().isNotFound());

        verify(autorService, times(1)).update(anyLong(), any(AutorDTO.class));
    }

    @Test
    void deleteAutor_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(autorService).delete(1L);

        mockMvc.perform(delete("/api/v1/autores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(autorService, times(1)).delete(1L);
    }

    @Test
    void deleteAutor_NonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Autor not found")).when(autorService).delete(anyLong());

        mockMvc.perform(delete("/api/v1/autores/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(autorService, times(1)).delete(99L);
    }
}
