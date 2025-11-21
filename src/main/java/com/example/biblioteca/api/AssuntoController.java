package com.example.biblioteca.api;

import com.example.biblioteca.dto.AssuntoDTO;
import com.example.biblioteca.service.AssuntoService;
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
@RequestMapping("/api/v1/assuntos")
@Tag(name = "Assuntos", description = "API para gerenciamento de assuntos")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @GetMapping("/paged")
    @Operation(summary = "Listar todos os assuntos de forma paginada")
    public ResponseEntity<Page<AssuntoDTO>> findAllPaged(Pageable pageable) {
        return ResponseEntity.ok(assuntoService.findAll(pageable));
    }

    @GetMapping
    @Operation(summary = "Listar todos os assuntos")
    public ResponseEntity<List<AssuntoDTO>> findAll() {
        return ResponseEntity.ok(assuntoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar assunto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assunto encontrado"),
            @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    })
    public ResponseEntity<AssuntoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(assuntoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar um novo assunto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Assunto criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<AssuntoDTO> create(@Valid @RequestBody AssuntoDTO assuntoDTO) {
        AssuntoDTO createdAssunto = assuntoService.create(assuntoDTO);
        return new ResponseEntity<>(createdAssunto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um assunto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assunto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Assunto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<AssuntoDTO> update(@PathVariable Integer id, @Valid @RequestBody AssuntoDTO assuntoDTO) {
        return ResponseEntity.ok(assuntoService.update(id, assuntoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um assunto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assunto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
