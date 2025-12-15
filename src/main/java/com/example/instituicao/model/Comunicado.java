package com.example.instituicao.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Document(collection = "comunicados")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comunicado {
    @Id

    private String instituicaoId;
    private String gestorId;
    private String assunto;
    private String corpo;
    private LocalDateTime dataEnvio;


    
}
