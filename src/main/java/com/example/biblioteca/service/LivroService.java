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
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Page<LivroDTO> findAll(Pageable pageable, String titulo) {
        if (StringUtils.hasText(titulo)) {
            return livroRepository.findByTituloContainingIgnoreCase(titulo, pageable).map(livroMapper::toDTO);
        }
        return livroRepository.findAll(pageable).map(livroMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public LivroDTO findById(Integer id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com o id: " + id));
    }

    @Transactional
    public LivroDTO create(LivroDTO livroDTO) {
        Livro livro = livroMapper.toEntity(livroDTO);
        updateRelationships(livro, livroDTO);
        livro = livroRepository.save(livro);
        return livroMapper.toDTO(livro);
    }

    @Transactional
    public LivroDTO update(Integer id, LivroDTO livroDTO) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com o id: " + id));

        livro.setTitulo(livroDTO.getTitulo());
        livro.setEditora(livroDTO.getEditora());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setValor(livroDTO.getValor());

        updateRelationships(livro, livroDTO);
        livro = livroRepository.save(livro);
        return livroMapper.toDTO(livro);
    }

    @Transactional
    public void delete(Integer id) {
        if (!livroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro não encontrado com o id: " + id);
        }
        livroRepository.deleteById(id);
    }

    private void updateRelationships(Livro livro, LivroDTO livroDTO) {
        List<Autor> autores = autorRepository.findAllById(livroDTO.getAutoresIds());
        if (autores.size() != livroDTO.getAutoresIds().size()) {
            throw new ResourceNotFoundException("Um ou mais autores não foram encontrados.");
        }
        livro.setAutores(new HashSet<>(autores));

        List<Assunto> assuntos = assuntoRepository.findAllById(livroDTO.getAssuntosIds());
        if (assuntos.size() != livroDTO.getAssuntosIds().size()) {
            throw new ResourceNotFoundException("Um ou mais assuntos não foram encontrados.");
        }
        livro.setAssuntos(new HashSet<>(assuntos));
    }
}
