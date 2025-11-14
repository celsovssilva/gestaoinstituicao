package com.example.instituicao.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Document(collection = "escolas")
@AllArgsConstructor
@NoArgsConstructor
public class Escola {
    private String nome;
    private String endereco;
    private int numeroDeAlunos;
    
}
