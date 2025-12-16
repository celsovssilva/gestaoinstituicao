package com.example.instituicao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.instituicao.dto.TarefaRequest;
import com.example.instituicao.model.Tarefa;
import com.example.instituicao.model.TarefaEnum;
import com.example.instituicao.repository.TarefaRepository;

@Service
public class TarefaService {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    
    public Tarefa criarTarefa(String instituicaoId, TarefaRequest request) {
        Tarefa novaTarefa = new Tarefa();
       
        novaTarefa.setInstituicaoId(instituicaoId);
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
}