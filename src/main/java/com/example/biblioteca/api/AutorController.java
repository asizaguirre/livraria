package com.example.biblioteca.api;

import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.service.AutorService;
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
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "Gerenciamento de autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Operation(summary = "Lista todos os autores", description = "Retorna uma lista paginada de autores")
    @GetMapping
    public ResponseEntity<Page<AutorDTO>> getAllAutores(Pageable pageable) {
        return ResponseEntity.ok(autorService.findAll(pageable));
    }

    @Operation(summary = "Obtém um autor por ID", description = "Retorna um autor específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Autor encontrado")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> getAutorById(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @Operation(summary = "Cria um novo autor", description = "Cria um novo autor no sistema")
    @ApiResponse(responseCode = "201", description = "Autor criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @PostMapping
    public ResponseEntity<AutorDTO> createAutor(@RequestBody @Valid AutorDTO autorDTO) {
        AutorDTO createdAutor = autorService.create(autorDTO);
        return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza um autor existente", description = "Atualiza os dados de um autor pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> updateAutor(@PathVariable Long id, @RequestBody @Valid AutorDTO autorDTO) {
        AutorDTO updatedAutor = autorService.update(id, autorDTO);
        return ResponseEntity.ok(updatedAutor);
    }

    @Operation(summary = "Exclui um autor", description = "Remove um autor do sistema pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}