package com.example.instituicao.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios {
    private String id;
    private String nome;
    private String email;
    private String senha_hash;
    private String papel; 
    private Boolean status;
    private LocalDateTime criado_em;
    private LocalDateTime atualizado_em;

    @Field("instituicao_id")
    private String instituicaoId;
    @Field("unidade_escolar_id")
    private String unidadeEscolarId;
    
    
}
