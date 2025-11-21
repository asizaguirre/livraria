package com.example.biblioteca.repository;

import com.example.biblioteca.domain.Assunto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AssuntoRepositoryTest {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findById_ExistingAssunto_ReturnsAssunto() {
        Assunto assunto = new Assunto(null, "Assunto Teste");
        entityManager.persistAndFlush(assunto);

        Optional<Assunto> foundAssunto = assuntoRepository.findById(assunto.getId());

        assertTrue(foundAssunto.isPresent());
        assertEquals(assunto.getDescricao(), foundAssunto.get().getDescricao());
    }

    @Test
    void save_NewAssunto_PersistsAssunto() {
        Assunto assunto = new Assunto(null, "Novo Assunto");
        Assunto savedAssunto = assuntoRepository.save(assunto);

        assertNotNull(savedAssunto.getId());
        assertEquals("Novo Assunto", savedAssunto.getDescricao());
    }

    @Test
    void delete_ExistingAssunto_RemovesAssunto() {
        Assunto assunto = new Assunto(null, "Assunto para Excluir");
        entityManager.persistAndFlush(assunto);

        assuntoRepository.deleteById(assunto.getId());

        Optional<Assunto> deletedAssunto = assuntoRepository.findById(assunto.getId());
        assertFalse(deletedAssunto.isPresent());
    }
}
