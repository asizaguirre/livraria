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

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    @Transactional(readOnly = true)
    public Page<AutorDTO> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable).map(autorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public AutorDTO findById(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor not found with id " + id));
        return autorMapper.toDto(autor);
    }

    @Transactional
    public AutorDTO create(AutorDTO autorDTO) {
        Autor autor = autorMapper.toEntity(autorDTO);
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }

    @Transactional
    public AutorDTO update(Long id, AutorDTO autorDTO) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor not found with id " + id));
        autor.setNome(autorDTO.getNome());
        autor = autorRepository.save(autor);
        return autorMapper.toDto(autor);
    }

    @Transactional
    public void delete(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autor not found with id " + id);
        }
        autorRepository.deleteById(id);
    }
}