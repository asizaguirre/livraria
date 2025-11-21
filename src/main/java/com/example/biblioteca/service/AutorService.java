package com.example.biblioteca.service;

import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.exception.ResourceNotFoundException;
import com.example.biblioteca.mapper.AutorMapper;
import com.example.biblioteca.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    @Transactional(readOnly = true)
    public Page<AutorDTO> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable).map(autorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<AutorDTO> findAll() {
        return autorRepository.findAll().stream()
                .map(autorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AutorDTO findById(Integer id) {
        return autorRepository.findById(id)
                .map(autorMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o id: " + id));
    }

    @Transactional
    public AutorDTO create(AutorDTO autorDTO) {
        Autor autor = autorMapper.toEntity(autorDTO);
        autor = autorRepository.save(autor);
        return autorMapper.toDTO(autor);
    }

    @Transactional
    public AutorDTO update(Integer id, AutorDTO autorDTO) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o id: " + id));
        autor.setNome(autorDTO.getNome());
        autor = autorRepository.save(autor);
        return autorMapper.toDTO(autor);
    }

    @Transactional
    public void delete(Integer id) {
        if (!autorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autor não encontrado com o id: " + id);
        }
        autorRepository.deleteById(id);
    }
}
