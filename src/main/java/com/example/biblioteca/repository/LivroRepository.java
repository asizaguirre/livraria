package com.example.biblioteca.repository;

import com.example.biblioteca.domain.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}
