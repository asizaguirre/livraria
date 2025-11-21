package com.example.biblioteca.repository;

import com.example.biblioteca.domain.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AutorRepositoryTest {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findById_ExistingAutor_ReturnsAutor() {
        Autor autor = new Autor(null, "Autor Teste");
        entityManager.persistAndFlush(autor);

        Optional<Autor> foundAutor = autorRepository.findById(autor.getId());

        assertTrue(foundAutor.isPresent());
        assertEquals(autor.getNome(), foundAutor.get().getNome());
    }

    @Test
    void save_NewAutor_PersistsAutor() {
        Autor autor = new Autor(null, "Novo Autor");
        Autor savedAutor = autorRepository.save(autor);

        assertNotNull(savedAutor.getId());
        assertEquals("Novo Autor", savedAutor.getNome());
    }

    @Test
    void delete_ExistingAutor_RemovesAutor() {
        Autor autor = new Autor(null, "Autor para Excluir");
        entityManager.persistAndFlush(autor);

        autorRepository.deleteById(autor.getId());

        Optional<Autor> deletedAutor = autorRepository.findById(autor.getId());
        assertFalse(deletedAutor.isPresent());
    }
}
