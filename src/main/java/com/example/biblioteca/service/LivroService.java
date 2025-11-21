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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private LivroMapper livroMapper;

    @Transactional(readOnly = true)
    public Page<LivroDTO> findAll(Pageable pageable) {
        return livroRepository.findAll(pageable).map(livroMapper::toDto);
    }

    @Transactional(readOnly = true)
    public LivroDTO findById(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro not found with id " + id));
        return livroMapper.toDto(livro);
    }

    @Transactional
    public LivroDTO create(LivroDTO livroDTO) {
        Livro livro = livroMapper.toEntity(livroDTO);
        livro.setAutores(getAutoresFromIds(livroDTO.getAutoresIds()));
        livro.setAssuntos(getAssuntosFromIds(livroDTO.getAssuntosIds()));
        livro = livroRepository.save(livro);
        return livroMapper.toDto(livro);
    }

    @Transactional
    public LivroDTO update(Long id, LivroDTO livroDTO) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro not found with id " + id));

        livro.setTitulo(livroDTO.getTitulo());
        livro.setEditora(livroDTO.getEditora());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setValor(livroDTO.getValor());
        livro.setAutores(getAutoresFromIds(livroDTO.getAutoresIds()));
        livro.setAssuntos(getAssuntosFromIds(livroDTO.getAssuntosIds()));

        livro = livroRepository.save(livro);
        return livroMapper.toDto(livro);
    }

    @Transactional
    public void delete(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro not found with id " + id);
        }
        livroRepository.deleteById(id);
    }

    private Set<Autor> getAutoresFromIds(Set<Long> autoresIds) {
        if (autoresIds == null || autoresIds.isEmpty()) {
            return new HashSet<>();
        }
        return autoresIds.stream()
                .map(autorId -> autorRepository.findById(autorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Autor not found with id " + autorId)))
                .collect(Collectors.toSet());
    }

    private Set<Assunto> getAssuntosFromIds(Set<Long> assuntosIds) {
        if (assuntosIds == null || assuntosIds.isEmpty()) {
            return new HashSet<>();
        }
        return assuntosIds.stream()
                .map(assuntoId -> assuntoRepository.findById(assuntoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Assunto not found with id " + assuntoId)))
                .collect(Collectors.toSet());
    }
}