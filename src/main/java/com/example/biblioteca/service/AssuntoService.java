package com.example.biblioteca.service;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.dto.AssuntoDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.mapper.AssuntoMapper;
import com.example.biblioteca.repository.AssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssuntoMapper assuntoMapper;

    @Transactional(readOnly = true)
    public Page<AssuntoDTO> findAll(Pageable pageable) {
        return assuntoRepository.findAll(pageable).map(assuntoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public AssuntoDTO findById(Long id) {
        Assunto assunto = assuntoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assunto not found with id " + id));
        return assuntoMapper.toDto(assunto);
    }

    @Transactional
    public AssuntoDTO create(AssuntoDTO assuntoDTO) {
        Assunto assunto = assuntoMapper.toEntity(assuntoDTO);
        assunto = assuntoRepository.save(assunto);
        return assuntoMapper.toDto(assunto);
    }

    @Transactional
    public AssuntoDTO update(Long id, AssuntoDTO assuntoDTO) {
        Assunto assunto = assuntoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assunto not found with id " + id));
        assunto.setDescricao(assuntoDTO.getDescricao());
        assunto = assuntoRepository.save(assunto);
        return assuntoMapper.toDto(assunto);
    }

    @Transactional
    public void delete(Long id) {
        if (!assuntoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assunto not found with id " + id);
        }
        assuntoRepository.deleteById(id);
    }
}