package com.example.biblioteca.report;

import com.example.biblioteca.domain.Livro;
import com.example.biblioteca.repository.LivroRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private LivroRepository livroRepository;

    public byte[] exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Livro> livros = livroRepository.findAll();

        List<RelatorioLivroDTO> data = livros.stream().flatMap(livro ->
                livro.getAutores().stream().map(autor -> {
                    String assuntos = livro.getAssuntos().stream()
                            .map(a -> a.getDescricao())
                            .collect(Collectors.joining(", "));
                    return new RelatorioLivroDTO(
                            autor.getNome(),
                            livro.getTitulo(),
                            livro.getEditora(),
                            livro.getEdicao(),
                            livro.getAnoPublicacao(),
                            livro.getValor(),
                            assuntos
                    );
                })
        ).collect(Collectors.toList());

        // Load file and compile it
        File file = ResourceUtils.getFile("classpath:reports/livros_por_autor.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Biblioteca API");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            return JasperExportManager.exportReportToHtmlFile(jasperPrint).getBytes();
        }
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}