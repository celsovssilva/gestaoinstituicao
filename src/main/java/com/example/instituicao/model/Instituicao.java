package com.example.instituicao.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "instituicoes")
@AllArgsConstructor
@NoArgsConstructor
public class Instituicao {

    @Id
    private String id; 
    private String nome;
    private String cidade;
    private String Estado;
    private String cep;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<UnidadesEscolares> escolas; 


}