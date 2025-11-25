package com.example.instituicao.dto;

import com.example.instituicao.model.UnidadesEscolares;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscolaRequest {
    private UnidadesEscolares escola;
    private String gestorId;
}