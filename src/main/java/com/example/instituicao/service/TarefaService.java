package com.example.instituicao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.instituicao.dto.TarefaRequest;
import com.example.instituicao.model.Tarefa;
import com.example.instituicao.model.TarefaEnum;
import com.example.instituicao.repository.TarefaRepository;
import com.example.instituicao.repository.UsuariosRepository;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class TarefaService {
    
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private UsuariosRepository gestorRepository;

    
    public Tarefa criarTarefa(String instituicaoId, TarefaRequest request) {
        Tarefa novaTarefa = new Tarefa();
    
   
    novaTarefa.setInstituicaoId(instituicaoId);
    
    
    novaTarefa.setGestorId(request.getGestorId()); 
    novaTarefa.setTitulo(request.getTitulo());     
    novaTarefa.setDescricao(request.getDescricao());
    novaTarefa.setDataLimite(request.getDataLimite());
    novaTarefa.setStatus(TarefaEnum.PENDENTE);
    novaTarefa.setDataCriacao(LocalDateTime.now());
    
    return tarefaRepository.save(novaTarefa);
    }

   
    public List<Tarefa> buscarTarefasPorGestor(String gestorId) {
        
        return tarefaRepository.findByGestorId(gestorId);
    }
    
    
    public Tarefa concluirTarefa(String tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setStatus(TarefaEnum.CONCLUIDA);
        tarefa.setDataConclusao(LocalDateTime.now());

        return tarefaRepository.save(tarefa);
    }
    
 public byte[] criarPdfDasTarefas(List<Tarefa> tarefas) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try (
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf)) {

        document.add(new Paragraph("Relatório de Tarefas por Instituição").setFontSize(18).setBold());
        document.add(new Paragraph("Total de Tarefas: " + tarefas.size()));
        document.add(new Paragraph("\n"));

      
        Table table = new Table(UnitValue.createPercentArray(new float[] { 3, 2, 2, 2 }));
        table.setWidth(UnitValue.createPercentValue(100));

        
        table.addHeaderCell(new Paragraph("Descrição").setBold());
        table.addHeaderCell(new Paragraph("Gestor Responsável").setBold());
        table.addHeaderCell(new Paragraph("Prazo").setBold());
        table.addHeaderCell(new Paragraph("Status").setBold());

        if (tarefas != null && !tarefas.isEmpty()) {
            for (Tarefa tarefa : tarefas) {
                
                table.addCell(tarefa.getDescricao() != null ? tarefa.getDescricao() : "N/A");

               
                String nomeGestor = (tarefa.getGestor() != null) ? tarefa.getGestor().getNome() : "Não atribuído";
                table.addCell(nomeGestor);

           
                table.addCell(tarefa.getDataLimite() != null ? tarefa.getDataLimite().toString() : "-");

               
                table.addCell(tarefa.getStatus() != null ? tarefa.getStatus().toString() : "Pendente");
            }
        }

        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Fim do Relatório").setFontSize(10).setItalic());

    } catch (Exception e) {
        System.err.println("Erro ao gerar PDF de tarefas: " + e.getMessage());
        e.printStackTrace();
    }

    return baos.toByteArray();
}
}