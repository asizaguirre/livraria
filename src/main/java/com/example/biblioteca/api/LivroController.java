package com.example.biblioteca.api;

import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/livros")
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    @Operation(summary = "Listar todos os livros de forma paginada")
    public ResponseEntity<Page<LivroDTO>> findAll(
            Pageable pageable,
            @Parameter(description = "Filtrar livros por título") @RequestParam(required = false) String titulo) {
        return ResponseEntity.ok(livroService.findAll(pageable, titulo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<LivroDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(livroService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<LivroDTO> create(@Valid @RequestBody LivroDTO livroDTO) {
        LivroDTO createdLivro = livroService.create(livroDTO);
        return new ResponseEntity<>(createdLivro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<LivroDTO> update(@PathVariable Integer id, @Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.ok(livroService.update(id, livroDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
