package com.example.biblioteca.api;

import com.example.biblioteca.dto.LivroDTO;
import com.example.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Livros", description = "Gerenciamento de livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Operation(summary = "Lista todos os livros", description = "Retorna uma lista paginada de livros")
    @GetMapping
    public ResponseEntity<Page<LivroDTO>> getAllLivros(Pageable pageable) {
        return ResponseEntity.ok(livroService.findAll(pageable));
    }

    @Operation(summary = "Obtém um livro por ID", description = "Retorna um livro específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Livro encontrado")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> getLivroById(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.findById(id));
    }

    @Operation(summary = "Cria um novo livro", description = "Cria um novo livro no sistema")
    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @PostMapping
    public ResponseEntity<LivroDTO> createLivro(@RequestBody @Valid LivroDTO livroDTO) {
        LivroDTO createdLivro = livroService.create(livroDTO);
        return new ResponseEntity<>(createdLivro, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza um livro existente", description = "Atualiza os dados de um livro pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<LivroDTO> updateLivro(@PathVariable Long id, @RequestBody @Valid LivroDTO livroDTO) {
        LivroDTO updatedLivro = livroService.update(id, livroDTO);
        return ResponseEntity.ok(updatedLivro);
    }

    @Operation(summary = "Exclui um livro", description = "Remove um livro do sistema pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}