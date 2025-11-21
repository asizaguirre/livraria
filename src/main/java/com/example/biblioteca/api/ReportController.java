package com.example.biblioteca.api;

import com.example.biblioteca.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/v1/relatorios")
@Tag(name = "Relatórios", description = "Geração de relatórios")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Gera relatório de livros por autor", description = "Retorna um relatório em PDF agrupado por autor")
    @GetMapping("/livros-por-autor")
    public ResponseEntity<byte[]> getLivrosPorAutorReport() throws JRException, FileNotFoundException {
        byte[] reportContent = reportService.exportReport("pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "livros-por-autor.pdf");
        return ResponseEntity.ok().headers(headers).body(reportContent);
    }
}