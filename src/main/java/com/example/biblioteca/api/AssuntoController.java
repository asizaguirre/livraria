package com.example.biblioteca.api;

import com.example.biblioteca.dto.AssuntoDTO;
import com.example.biblioteca.service.AssuntoService;
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
@RequestMapping("/api/v1/assuntos")
@Tag(name = "Assuntos", description = "Gerenciamento de assuntos")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @Operation(summary = "Lista todos os assuntos", description = "Retorna uma lista paginada de assuntos")
    @GetMapping
    public ResponseEntity<Page<AssuntoDTO>> getAllAssuntos(Pageable pageable) {
        return ResponseEntity.ok(assuntoService.findAll(pageable));
    }

    @Operation(summary = "Obtém um assunto por ID", description = "Retorna um assunto específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Assunto encontrado")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<AssuntoDTO> getAssuntoById(@PathVariable Long id) {
        return ResponseEntity.ok(assuntoService.findById(id));
    }

    @Operation(summary = "Cria um novo assunto", description = "Cria um novo assunto no sistema")
    @ApiResponse(responseCode = "201", description = "Assunto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @PostMapping
    public ResponseEntity<AssuntoDTO> createAssunto(@RequestBody @Valid AssuntoDTO assuntoDTO) {
        AssuntoDTO createdAssunto = assuntoService.create(assuntoDTO);
        return new ResponseEntity<>(createdAssunto, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza um assunto existente", description = "Atualiza os dados de um assunto pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Assunto atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<AssuntoDTO> updateAssunto(@PathVariable Long id, @RequestBody @Valid AssuntoDTO assuntoDTO) {
        AssuntoDTO updatedAssunto = assuntoService.update(id, assuntoDTO);
        return ResponseEntity.ok(updatedAssunto);
    }

    @Operation(summary = "Exclui um assunto", description = "Remove um assunto do sistema pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Assunto excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Assunto não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssunto(@PathVariable Long id) {
        assuntoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}