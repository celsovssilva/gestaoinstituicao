package com.example.instituicao.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarefaRequest {
    private String titulo;
    private String descricao;
    private String gestorId;
    private LocalDate dataLimite;
    
}
