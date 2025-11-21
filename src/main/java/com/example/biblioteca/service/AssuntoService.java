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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssuntoMapper assuntoMapper;

    @Transactional(readOnly = true)
    public Page<AssuntoDTO> findAll(Pageable pageable) {
        return assuntoRepository.findAll(pageable).map(assuntoMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public List<AssuntoDTO> findAll() {
        return assuntoRepository.findAll().stream()
                .map(assuntoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AssuntoDTO findById(Integer id) {
        return assuntoRepository.findById(id)
                .map(assuntoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Assunto não encontrado com o id: " + id));
    }

    @Transactional
    public AssuntoDTO create(AssuntoDTO assuntoDTO) {
        Assunto assunto = assuntoMapper.toEntity(assuntoDTO);
        assunto = assuntoRepository.save(assunto);
        return assuntoMapper.toDTO(assunto);
    }

    @Transactional
    public AssuntoDTO update(Integer id, AssuntoDTO assuntoDTO) {
        Assunto assunto = assuntoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assunto não encontrado com o id: " + id));
        assunto.setDescricao(assuntoDTO.getDescricao());
        assunto = assuntoRepository.save(assunto);
        return assuntoMapper.toDTO(assunto);
    }

    @Transactional
    public void delete(Integer id) {
        if (!assuntoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assunto não encontrado com o id: " + id);
        }
        assuntoRepository.deleteById(id);
    }
}
