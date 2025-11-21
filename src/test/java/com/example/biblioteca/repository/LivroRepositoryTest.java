package com.example.biblioteca.repository;

import com.example.biblioteca.domain.Assunto;
import com.example.biblioteca.domain.Autor;
import com.example.biblioteca.domain.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findById_ExistingLivro_ReturnsLivro() {
        Autor autor = new Autor(null, "Autor Livro Teste");
        Assunto assunto = new Assunto(null, "Assunto Livro Teste");
        entityManager.persistAndFlush(autor);
        entityManager.persistAndFlush(assunto);

        Livro livro = new Livro(null, "Titulo Livro Teste", "Editora Teste", "1a", "2023", new BigDecimal("99.99"), new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(assunto)));
        entityManager.persistAndFlush(livro);

        Optional<Livro> foundLivro = livroRepository.findById(livro.getId());

        assertTrue(foundLivro.isPresent());
        assertEquals(livro.getTitulo(), foundLivro.get().getTitulo());
        assertEquals(1, foundLivro.get().getAutores().size());
        assertEquals(1, foundLivro.get().getAssuntos().size());
    }

    @Test
    void save_NewLivro_PersistsLivroWithRelationships() {
        Autor autor = new Autor(null, "Autor Teste Save");
        Assunto assunto = new Assunto(null, "Assunto Teste Save");
        entityManager.persistAndFlush(autor);
        entityManager.persistAndFlush(assunto);

        Livro livro = new Livro(null, "Novo Livro", "Nova Editora", "2a", "2024", new BigDecimal("120.00"), new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(assunto)));
        Livro savedLivro = livroRepository.save(livro);

        assertNotNull(savedLivro.getId());
        assertEquals("Novo Livro", savedLivro.getTitulo());
        assertEquals(1, savedLivro.getAutores().size());
        assertEquals(1, savedLivro.getAssuntos().size());
    }

    @Test
    void delete_ExistingLivro_RemovesLivro() {
        Autor autor = new Autor(null, "Autor Delete");
        Assunto assunto = new Assunto(null, "Assunto Delete");
        entityManager.persistAndFlush(autor);
        entityManager.persistAndFlush(assunto);

        Livro livro = new Livro(null, "Livro para Excluir", "Editora Delete", "3a", "2020", new BigDecimal("75.50"), new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(assunto)));
        entityManager.persistAndFlush(livro);

        livroRepository.deleteById(livro.getId());

        Optional<Livro> deletedLivro = livroRepository.findById(livro.getId());
        assertFalse(deletedLivro.isPresent());
    }
}
