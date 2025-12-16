package com.example.instituicao.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.Tarefa;

public interface TarefaRepository extends MongoRepository<Tarefa, String> {
    
    List<Tarefa> findByGestorId(String gestorId);
    List<Tarefa> findByInstituicaoId(String instituicaoId);
}
