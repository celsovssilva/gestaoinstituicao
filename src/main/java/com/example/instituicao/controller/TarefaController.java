package com.example.instituicao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.instituicao.dto.TarefaRequest;
import com.example.instituicao.model.Tarefa;
import com.example.instituicao.service.TarefaService;


@RestController 
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criarTarefa(@RequestBody TarefaRequest request, @RequestHeader("X-INSTITUICAO-ID") String instituicaoId) {
   
        return tarefaService.criarTarefa(instituicaoId, request);
    }
    
    
    @GetMapping("/gestor/{gestorId}")
    public List<Tarefa> listarTarefasPorGestor(@PathVariable String gestorId) {
        return tarefaService.buscarTarefasPorGestor(gestorId);
    }

    
    @PutMapping("/{id}/concluir")
    public Tarefa concluirTarefa(@PathVariable String id) {
        return tarefaService.concluirTarefa(id);
    }

@GetMapping("/download-csv")
    public ResponseEntity<byte[]> exportarTarefasParaPdf() {
        
        List<Tarefa> listaDeTarefas = tarefaService.buscarTarefasPorGestor("all");

       
        byte[] pdfBytes = tarefaService.criarPdfDasTarefas(listaDeTarefas);

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        
      
        headers.setContentDispositionFormData("attachment", "relatorio_tarefas.pdf");

       
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
    
}
