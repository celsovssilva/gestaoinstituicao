package com.example.instituicao.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "unidades_escolares")
@AllArgsConstructor
@NoArgsConstructor
public class UnidadesEscolares {
    
    @Id
    private String id; 
    

    @Field("instituicaoId")
    private String instituicaoId; 
 
    private String nome;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private int numeroDeAlunos;
    private String gestorId;
    
 
    @CreatedDate
    private Instant criado_em;

    @LastModifiedDate
    private Instant atualizado_em;
}