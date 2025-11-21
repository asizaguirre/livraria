package com.example.biblioteca.service;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.dto.AssuntoDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.mapper.AssuntoMapper;
import com.example.biblioteca.repository.AssuntoRepository;
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
public class AssuntoServiceTest {

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private AssuntoMapper assuntoMapper;

    @InjectMocks
    private AssuntoService assuntoService;

    private Assunto assunto;
    private AssuntoDTO assuntoDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        assunto = new Assunto(1L, "Ficção Científica");
        assuntoDTO = new AssuntoDTO(1L, "Ficção Científica");
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void findAll_ReturnsPageOfAssuntoDTOs() {
        Page<Assunto> assuntoPage = new PageImpl<>(Arrays.asList(assunto), pageable, 1);
        when(assuntoRepository.findAll(pageable)).thenReturn(assuntoPage);
        when(assuntoMapper.toDto(assunto)).thenReturn(assuntoDTO);

        Page<AssuntoDTO> result = assuntoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(assuntoDTO, result.getContent().get(0));
        verify(assuntoRepository, times(1)).findAll(pageable);
        verify(assuntoMapper, times(1)).toDto(assunto);
    }

    @Test
    void findById_ExistingId_ReturnsAssuntoDTO() {
        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(assunto));
        when(assuntoMapper.toDto(assunto)).thenReturn(assuntoDTO);

        AssuntoDTO result = assuntoService.findById(1L);

        assertNotNull(result);
        assertEquals(assuntoDTO, result);
        verify(assuntoRepository, times(1)).findById(1L);
        verify(assuntoMapper, times(1)).toDto(assunto);
    }

    @Test
    void findById_NonExistingId_ThrowsResourceNotFoundException() {
        when(assuntoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assuntoService.findById(99L));
        verify(assuntoRepository, times(1)).findById(99L);
        verify(assuntoMapper, never()).toDto(any(Assunto.class));
    }

    @Test
    void create_ValidAssuntoDTO_ReturnsCreatedAssuntoDTO() {
        when(assuntoMapper.toEntity(assuntoDTO)).thenReturn(assunto);
        when(assuntoRepository.save(assunto)).thenReturn(assunto);
        when(assuntoMapper.toDto(assunto)).thenReturn(assuntoDTO);

        AssuntoDTO result = assuntoService.create(assuntoDTO);

        assertNotNull(result);
        assertEquals(assuntoDTO, result);
        verify(assuntoMapper, times(1)).toEntity(assuntoDTO);
        verify(assuntoRepository, times(1)).save(assunto);
        verify(assuntoMapper, times(1)).toDto(assunto);
    }

    @Test
    void update_ExistingIdAndValidAssuntoDTO_ReturnsUpdatedAssuntoDTO() {
        AssuntoDTO updatedAssuntoDTO = new AssuntoDTO(1L, "Fantasia");
        Assunto updatedAssunto = new Assunto(1L, "Fantasia");

        when(assuntoRepository.findById(1L)).thenReturn(Optional.of(assunto));
        when(assuntoRepository.save(any(Assunto.class))).thenReturn(updatedAssunto);
        when(assuntoMapper.toDto(updatedAssunto)).thenReturn(updatedAssuntoDTO);

        AssuntoDTO result = assuntoService.update(1L, updatedAssuntoDTO);

        assertNotNull(result);
        assertEquals(updatedAssuntoDTO.getDescricao(), result.getDescricao());
        verify(assuntoRepository, times(1)).findById(1L);
        verify(assuntoRepository, times(1)).save(any(Assunto.class));
        verify(assuntoMapper, times(1)).toDto(updatedAssunto);
    }

    @Test
    void update_NonExistingId_ThrowsResourceNotFoundException() {
        when(assuntoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assuntoService.update(99L, assuntoDTO));
        verify(assuntoRepository, times(1)).findById(99L);
        verify(assuntoRepository, never()).save(any(Assunto.class));
        verify(assuntoMapper, never()).toDto(any(Assunto.class));
    }

    @Test
    void delete_ExistingId_DeletesAssunto() {
        when(assuntoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(assuntoRepository).deleteById(1L);

        assertDoesNotThrow(() -> assuntoService.delete(1L));
        verify(assuntoRepository, times(1)).existsById(1L);
        verify(assuntoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ThrowsResourceNotFoundException() {
        when(assuntoRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> assuntoService.delete(99L));
        verify(assuntoRepository, times(1)).existsById(99L);
        verify(assuntoRepository, never()).deleteById(anyLong());
    }
}
