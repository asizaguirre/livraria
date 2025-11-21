package com.example.biblioteca.service;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.mapper.LivroMapper;
import com.example.biblioteca.repository.AssuntoRepository;
import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private LivroMapper livroMapper;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;
    private LivroDTO livroDTO;
    private Autor autor;
    private Assunto assunto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        autor = new Autor(1L, "Machado de Assis");
        assunto = new Assunto(1L, "Romance");
        livro = new Livro(1L, "Dom Casmurro", "Editora A", "1ª", "1899", new BigDecimal("50.00"), new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(assunto)));
        livroDTO = new LivroDTO(1L, "Dom Casmurro", "Editora A", "1ª", "1899", new BigDecimal("50.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findAll_ReturnsPageOfLivroDTOs() {
        Page<Livro> livroPage = new PageImpl<>(Arrays.asList(livro), pageable, 1);
        when(livroRepository.findAll(pageable)).thenReturn(livroPage);
        when(livroMapper.toDto(livro)).thenReturn(livroDTO);

        Page<LivroDTO> result = livroService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(livroDTO, result.getContent().get(0));
        verify(livroRepository, times(1)).findAll(pageable);
        verify(livroMapper, times(1)).toDto(livro);
    }

    @Test
    void findById_ExistingId_ReturnsLivroDTO() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroMapper.toDto(livro)).thenReturn(livroDTO);

        LivroDTO result = livroService.findById(1L);

        assertNotNull(result);
        assertEquals(livroDTO, result);
        verify(livroRepository, times(1)).findById(1L);
        verify(livroMapper, times(1)).toDto(livro);
    }

    @Test
    void findById_NonExistingId_ThrowsResourceNotFoundException() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> livroService.findById(99L));
        verify(livroRepository, times(1)).findById(99L);
        verify(livroMapper, never()).toDto(any(Livro.class));
    }

    @Test
    void create_ValidLivroDTO_ReturnsCreatedLivroDTO() {
        when(livroMapper.toEntity(livroDTO)).thenReturn(livro);
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(assunto));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        when(livroMapper.toDto(livro)).thenReturn(livroDTO);

        LivroDTO result = livroService.create(livroDTO);

        assertNotNull(result);
        assertEquals(livroDTO, result);
        verify(livroMapper, times(1)).toEntity(livroDTO);
        verify(autorRepository, times(1)).findById(1L);
        verify(assuntoRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(livroMapper, times(1)).toDto(livro);
    }

    @Test
    void create_InvalidAutorId_ThrowsResourceNotFoundException() {
        livroDTO.setAutoresIds(new HashSet<>(Set.of(99L)));
        when(livroMapper.toEntity(livroDTO)).thenReturn(livro);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> livroService.create(livroDTO));
        verify(livroMapper, times(1)).toEntity(livroDTO);
        verify(autorRepository, times(1)).findById(99L);
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void update_ExistingIdAndValidLivroDTO_ReturnsUpdatedLivroDTO() {
        LivroDTO updatedLivroDTO = new LivroDTO(1L, "Memórias Póstumas", "Editora B", "2ª", "1881", new BigDecimal("60.00"), new HashSet<>(Set.of(1L)), new HashSet<>(Set.of(1L)));
        Livro updatedLivro = new Livro(1L, "Memórias Póstumas", "Editora B", "2ª", "1881", new BigDecimal("60.00"), new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(assunto)));

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(assunto));
        when(livroRepository.save(any(Livro.class))).thenReturn(updatedLivro);
        when(livroMapper.toDto(updatedLivro)).thenReturn(updatedLivroDTO);

        LivroDTO result = livroService.update(1L, updatedLivroDTO);

        assertNotNull(result);
        assertEquals(updatedLivroDTO.getTitulo(), result.getTitulo());
        verify(livroRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).findById(1L);
        verify(assuntoRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(livroMapper, times(1)).toDto(updatedLivro);
    }

    @Test
    void update_NonExistingId_ThrowsResourceNotFoundException() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> livroService.update(99L, livroDTO));
        verify(livroRepository, times(1)).findById(99L);
        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    void delete_ExistingId_DeletesLivro() {
        when(livroRepository.existsById(1L)).thenReturn(true);
        doNothing().when(livroRepository).deleteById(1L);

        assertDoesNotThrow(() -> livroService.delete(1L));
        verify(livroRepository, times(1)).existsById(1L);
        verify(livroRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ThrowsResourceNotFoundException() {
        when(livroRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> livroService.delete(99L));
        verify(livroRepository, times(1)).existsById(99L);
        verify(livroRepository, never()).deleteById(anyLong());
    }
}
