package com.example.biblioteca.api;

import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "API para gerenciamento de autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping("/paged")
    @Operation(summary = "Listar todos os autores de forma paginada")
    public ResponseEntity<Page<AutorDTO>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok(autorService.findAll(pageable));
    }

    @GetMapping
    @Operation(summary = "Listar todos os autores")
    public ResponseEntity<List<AutorDTO>> findAll() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<AutorDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<AutorDTO> create(@Valid @RequestBody AutorDTO autorDTO) {
        AutorDTO createdAutor = autorService.create(autorDTO);
        return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um autor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<AutorDTO> update(@PathVariable Integer id, @Valid @RequestBody AutorDTO autorDTO) {
        return ResponseEntity.ok(autorService.update(id, autorDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
