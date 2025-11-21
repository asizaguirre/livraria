package com.example.biblioteca.service;

import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.mapper.AutorMapper;
import com.example.biblioteca.repository.AutorRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private AutorDTO autorDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        autor = new Autor(1L, "Machado de Assis");
        autorDTO = new AutorDTO(1L, "Machado de Assis");
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findAll_ReturnsPageOfAutorDTOs() {
        Page<Autor> autorPage = new PageImpl<>(Arrays.asList(autor), pageable, 1);
        when(autorRepository.findAll(pageable)).thenReturn(autorPage);
        when(autorMapper.toDto(autor)).thenReturn(autorDTO);

        Page<AutorDTO> result = autorService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(autorDTO, result.getContent().get(0));
        verify(autorRepository, times(1)).findAll(pageable);
        verify(autorMapper, times(1)).toDto(autor);
    }

    @Test
    void findById_ExistingId_ReturnsAutorDTO() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorMapper.toDto(autor)).thenReturn(autorDTO);

        AutorDTO result = autorService.findById(1L);

        assertNotNull(result);
        assertEquals(autorDTO, result);
        verify(autorRepository, times(1)).findById(1L);
        verify(autorMapper, times(1)).toDto(autor);
    }

    @Test
    void findById_NonExistingId_ThrowsResourceNotFoundException() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> autorService.findById(99L));
        verify(autorRepository, times(1)).findById(99L);
        verify(autorMapper, never()).toDto(any(Autor.class));
    }

    @Test
    void create_ValidAutorDTO_ReturnsCreatedAutorDTO() {
        when(autorMapper.toEntity(autorDTO)).thenReturn(autor);
        when(autorRepository.save(autor)).thenReturn(autor);
        when(autorMapper.toDto(autor)).thenReturn(autorDTO);

        AutorDTO result = autorService.create(autorDTO);

        assertNotNull(result);
        assertEquals(autorDTO, result);
        verify(autorMapper, times(1)).toEntity(autorDTO);
        verify(autorRepository, times(1)).save(autor);
        verify(autorMapper, times(1)).toDto(autor);
    }

    @Test
    void update_ExistingIdAndValidAutorDTO_ReturnsUpdatedAutorDTO() {
        AutorDTO updatedAutorDTO = new AutorDTO(1L, "João Guimarães Rosa");
        Autor updatedAutor = new Autor(1L, "João Guimarães Rosa");

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorRepository.save(any(Autor.class))).thenReturn(updatedAutor);
        when(autorMapper.toDto(updatedAutor)).thenReturn(updatedAutorDTO);

        AutorDTO result = autorService.update(1L, updatedAutorDTO);

        assertNotNull(result);
        assertEquals(updatedAutorDTO.getNome(), result.getNome());
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).save(any(Autor.class));
        verify(autorMapper, times(1)).toDto(updatedAutor);
    }

    @Test
    void update_NonExistingId_ThrowsResourceNotFoundException() {
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> autorService.update(99L, autorDTO));
        verify(autorRepository, times(1)).findById(99L);
        verify(autorRepository, never()).save(any(Autor.class));
        verify(autorMapper, never()).toDto(any(Autor.class));
    }

    @Test
    void delete_ExistingId_DeletesAutor() {
        when(autorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(autorRepository).deleteById(1L);

        assertDoesNotThrow(() -> autorService.delete(1L));
        verify(autorRepository, times(1)).existsById(1L);
        verify(autorRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ThrowsResourceNotFoundException() {
        when(autorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> autorService.delete(99L));
        verify(autorRepository, times(1)).existsById(99L);
        verify(autorRepository, never()).deleteById(anyLong());
    }
}
