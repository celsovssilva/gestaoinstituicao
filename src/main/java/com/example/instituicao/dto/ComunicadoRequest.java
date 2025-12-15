package com.example.instituicao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComunicadoRequest {
    private String gestorId;
    private String assunto;
    private String corpo;
    
}
