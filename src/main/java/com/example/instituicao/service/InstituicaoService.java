package com.example.instituicao.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.instituicao.model.Instituicao;
import com.example.instituicao.model.UnidadesEscolares;
import com.example.instituicao.repository.InstituicaoRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

   
    public Instituicao criar(Instituicao instituicao) {

        return instituicaoRepository.save(instituicao);
    }
    public void deletar(String id) {
        instituicaoRepository.deleteById(id);
    }

   
    
    public List<Instituicao> listarTodas() {
        return instituicaoRepository.findAll();
    }

  
    
    public Instituicao buscarPorId(String id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada!"));
    }

    
    
    public List<UnidadesEscolares> listarEscolas(String idInstituicao) {
        Instituicao instituicao = buscarPorId(idInstituicao);
        return instituicao.getEscolas();
    }
    

    public byte[] criarPdfDasInstituicoes(List<Instituicao> instituicoes) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (

                PdfWriter writer = new PdfWriter(baos);

                PdfDocument pdf = new PdfDocument(writer);

                Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório Geral de Instituições").setFontSize(18).setBold());
            document.add(new Paragraph("Total de Instituições: " + instituicoes.size()));
            document.add(new Paragraph("\n"));

            for (Instituicao inst : instituicoes) {

                document.add(new Paragraph("======================================="));
                document.add(new Paragraph("Instituição: " + inst.getNome())
                        .setFontSize(14).setBold());

                document.add(new Paragraph(" "));

                document.add(new Paragraph("Escolas Cadastradas:")
                        .setItalic());

                Table table = new Table(UnitValue.createPercentArray(new float[] { 1, 3, 2, 2 }));
                table.setWidth(UnitValue.createPercentValue(95));

                table.addHeaderCell(new Paragraph("Nome da Escola").setBold());
                table.addHeaderCell(new Paragraph("Endereço").setBold());
                table.addHeaderCell(new Paragraph("Cidade").setBold()); // Novo Cabeçalho
                table.addHeaderCell(new Paragraph("CEP").setBold()); // Novo Cabeçalho

                if (inst.getEscolas() != null && !inst.getEscolas().isEmpty()) {
                    inst.getEscolas().forEach(escola -> {

                        table.addCell(escola.getNome());
                        table.addCell(escola.getEndereco());
                        table.addCell(escola.getCidade() != null ? escola.getCidade() : "N/A");
                        table.addCell(escola.getCep() != null ? escola.getCep() : "N/A");

                    });
                } else {

                }
                document.add(table);
                document.add(new Paragraph("\n"));
            }

        } catch (Exception e) {

            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
public Instituicao adicionarEscola(String instituicaoId, UnidadesEscolares novaEscola) {
        Instituicao instituicao = buscarPorId(instituicaoId);
        
      
        if (instituicao.getEscolas() == null) {
            instituicao.setEscolas(new ArrayList<>());
        }
        
     
        instituicao.getEscolas().add(novaEscola);
        
       
        return instituicaoRepository.save(instituicao);
    }
}