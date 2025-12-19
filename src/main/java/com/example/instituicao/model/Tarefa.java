package com.example.instituicao.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Document(collection = "tarefa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tarefa {
  
    private String id;
    private String titulo;
    private String descricao;
    
    
    private String instituicaoId; 
    private String gestorId;
    
    @DBRef
    private Usuarios gestor;
    
    private LocalDate dataLimite;
    private TarefaEnum status; 
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao; 
    
}
