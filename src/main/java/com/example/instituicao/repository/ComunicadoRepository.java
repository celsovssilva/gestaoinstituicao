package com.example.instituicao.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.instituicao.model.Comunicado;
public interface ComunicadoRepository extends MongoRepository<Comunicado, String> {
    
}
