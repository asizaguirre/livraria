package com.example.biblioteca.api;

import com.example.biblioteca.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/relatorios")
@Tag(name = "Relatórios", description = "API para geração de relatórios")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/livros-por-autor")
    @Operation(summary = "Gerar relatório de livros por autor em PDF")
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
            content = @Content(mediaType = "application/pdf",
                    schema = @Schema(type = "string", format = "binary")))
    public ResponseEntity<byte[]> generateReport() {
        try {
            byte[] reportContent = reportService.generateReport();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio_livros_por_autor.pdf");
            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (JRException | FileNotFoundException | SQLException e) {
            // O GlobalExceptionHandler pegará isso como uma Exception genérica (500)
            throw new RuntimeException("Falha ao gerar o relatório: " + e.getMessage(), e);
        }
    }
}
