package com.example.biblioteca.api;

import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.service.LivroService;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
public class LivroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllLivros_ReturnsPageOfLivroDTOs() throws Exception {
        LivroDTO livroDTO = new LivroDTO(1L, "Dom Casmurro", "Editora A", "1ª", "1899", new BigDecimal("50.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));
        List<LivroDTO> livroDTOList = Arrays.asList(livroDTO);
        Pageable pageable = PageRequest.of(0, 10);
        Page<LivroDTO> livroDTOPage = new PageImpl<>(livroDTOList, pageable, 1);

        when(livroService.findAll(any(Pageable.class))).thenReturn(livroDTOPage);

        mockMvc.perform(get("/api/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].titulo").value("Dom Casmurro"));

        verify(livroService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getLivroById_ExistingId_ReturnsLivroDTO() throws Exception {
        LivroDTO livroDTO = new LivroDTO(1L, "Dom Casmurro", "Editora A", "1ª", "1899", new BigDecimal("50.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));

        when(livroService.findById(1L)).thenReturn(livroDTO);

        mockMvc.perform(get("/api/v1/livros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"));

        verify(livroService, times(1)).findById(1L);
    }

    @Test
    void getLivroById_NonExistingId_ReturnsNotFound() throws Exception {
        when(livroService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Livro not found"));

        mockMvc.perform(get("/api/v1/livros/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).findById(99L);
    }

    @Test
    void createLivro_ValidLivroDTO_ReturnsCreatedLivroDTO() throws Exception {
        LivroDTO livroDTO = new LivroDTO(null, "Memórias Póstumas", "Editora B", "2ª", "1881", new BigDecimal("60.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));
        LivroDTO createdLivroDTO = new LivroDTO(2L, "Memórias Póstumas", "Editora B", "2ª", "1881", new BigDecimal("60.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));

        when(livroService.create(any(LivroDTO.class))).thenReturn(createdLivroDTO);

        mockMvc.perform(post("/api/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.titulo").value("Memórias Póstumas"));

        verify(livroService, times(1)).create(any(LivroDTO.class));
    }

    @Test
    void createLivro_InvalidLivroDTO_ReturnsBadRequest() throws Exception {
        LivroDTO invalidLivroDTO = new LivroDTO(null, "", "Editora B", "2ª", "1881", new BigDecimal("60.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L))); // Invalid title

        mockMvc.perform(post("/api/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLivroDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(livroService, never()).create(any(LivroDTO.class));
    }

    @Test
    void updateLivro_ExistingIdAndValidLivroDTO_ReturnsUpdatedLivroDTO() throws Exception {
        LivroDTO updatedLivroDTO = new LivroDTO(1L, "Quincas Borba", "Editora C", "1ª", "1891", new BigDecimal("55.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));

        when(livroService.update(anyLong(), any(LivroDTO.class))).thenReturn(updatedLivroDTO);

        mockMvc.perform(put("/api/v1/livros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLivroDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Quincas Borba"));

        verify(livroService, times(1)).update(anyLong(), any(LivroDTO.class));
    }

    @Test
    void updateLivro_NonExistingId_ReturnsNotFound() throws Exception {
        LivroDTO livroDTO = new LivroDTO(99L, "Livro Inexistente", "Editora X", "1ª", "2000", new BigDecimal("10.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));
        when(livroService.update(anyLong(), any(LivroDTO.class))).thenThrow(new ResourceNotFoundException("Livro not found"));

        mockMvc.perform(put("/api/v1/livros/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livroDTO)))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).update(anyLong(), any(LivroDTO.class));
    }

    @Test
    void deleteLivro_ExistingId_ReturnsNoContent() throws Exception {
        doNothing().when(livroService).delete(1L);

        mockMvc.perform(delete("/api/v1/livros/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(livroService, times(1)).delete(1L);
    }

    @Test
    void deleteLivro_NonExistingId_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Livro not found")).when(livroService).delete(anyLong());

        mockMvc.perform(delete("/api/v1/livros/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(livroService, times(1)).delete(99L);
    }
}
