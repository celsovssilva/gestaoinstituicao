package com.example.instituicao.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.instituicao.dto.ComunicadoRequest;
import com.example.instituicao.dto.EscolaRequest;
import com.example.instituicao.model.Comunicado;
import com.example.instituicao.model.Instituicao;
import com.example.instituicao.model.UnidadesEscolares;
import com.example.instituicao.model.Usuarios;
import com.example.instituicao.repository.ComunicadoRepository;
import com.example.instituicao.repository.InstituicaoRepository;
import com.example.instituicao.repository.UnidadesEscolaresRepository;
import com.example.instituicao.repository.UsuariosRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;


@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private UnidadesEscolaresRepository unidadesEscolaresRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    public Instituicao autenticarInstituicao(String nome, String senha) {

        Instituicao instituicao = instituicaoRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Nome ou senha da Instituição inválidos."));

        if (!instituicao.getSenha().equals(senha)) {
            throw new RuntimeException("Nome ou senha da Instituição inválidos.");
        }

        return instituicao;
    }
    
    public Instituicao adicionarEscola(String instituicaoId, EscolaRequest request) {

        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
            .orElseThrow(() -> new RuntimeException("Instituição com ID " + instituicaoId + " não encontrada"));
        
        UnidadesEscolares novaEscola = request.getEscola();
        String gestorId = request.getGestorId();

        
        if (gestorId != null && !gestorId.isEmpty()) {
            Usuarios gestor = usuariosRepository.findById(gestorId)
                .orElseThrow(() -> new RuntimeException("Gestor com ID " + gestorId + " não encontrado."));
            
                    if (!"GESTOR".equals(gestor.getPapel())) {
                 throw new RuntimeException("Usuário selecionado não tem o papel de GESTOR.");
            }
            
            
            novaEscola.setGestorId(gestorId);
        }

        novaEscola.setInstituicaoId(instituicaoId); 
        
        UnidadesEscolares escolaSalva = unidadesEscolaresRepository.save(novaEscola); 

        
        if (instituicao.getEscolas() == null) {
            instituicao.setEscolas(new ArrayList<>());
        }
        instituicao.getEscolas().add(escolaSalva);


        return instituicaoRepository.save(instituicao); 
    }
    
    public Instituicao criar(Instituicao instituicao) {

        return instituicaoRepository.save(instituicao);
    }
    public void deletar(String id) {
        instituicaoRepository.deleteById(id);
    }

   
    
    public List<Instituicao> listarTodas() {
        return instituicaoRepository.findAll();
    }

  
    
    public Instituicao buscarPorId(String id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada!"));
    }

    
    
    
    public List<UnidadesEscolares> listarEscolas(String idInstituicao) {
        Instituicao instituicao = buscarPorId(idInstituicao);
        return instituicao.getEscolas();
    }

   public Usuarios criarGestor(String instituicaoId, Usuarios gestor) {
    System.out.println("🔍 SERVICE - Criando gestor...");
    

    if (!instituicaoRepository.existsById(instituicaoId)) {
        throw new RuntimeException("Instituição não encontrada");
    }

 
    System.out.println("- Nome: " + gestor.getNome());
    System.out.println("- Email: " + gestor.getEmail());
    System.out.println("- Senha: " + gestor.getSenha_hash());

    gestor.setInstituicaoId(instituicaoId);
    gestor.setPapel("GESTOR");
    gestor.setStatus(true);
    gestor.setCriado_em(LocalDateTime.now());
    gestor.setAtualizado_em(LocalDateTime.now());

    try {
        Usuarios gestorSalvo = usuariosRepository.save(gestor);
        System.out.println("💾 GESTOR SALVO NO BANCO - ID: " + gestorSalvo.getId());
        return gestorSalvo;
    } catch (Exception e) {
        System.out.println("💥 ERRO AO SALVAR NO BANCO: " + e.getMessage());
        throw e;
    }
}


public Usuarios buscarGestorPorId(String gestorId) {
    return usuariosRepository.findById(gestorId)
            .orElseThrow(() -> new RuntimeException("Gestor não encontrado com ID: " + gestorId));
}
    public List<Usuarios> listarGestoresPorInstituicao(String instituicaoId) {
   
    return usuariosRepository.findByInstituicaoIdAndPapel(instituicaoId, "GESTOR");
}

    public Usuarios atualizarGestor(String instituicaoId, String gestorId, Usuarios dadosAtualizados) {
        Usuarios gestorExistente = usuariosRepository.findById(gestorId)
                .orElseThrow(() -> new RuntimeException("Gestor não encontrado com ID: " + gestorId));

        if (!gestorExistente.getInstituicaoId().equals(instituicaoId)) {
            throw new RuntimeException("O gestor não pertence à instituição com ID: " + instituicaoId);
        }

        gestorExistente.setNome(dadosAtualizados.getNome());
        gestorExistente.setEmail(dadosAtualizados.getEmail());
        gestorExistente.setSenha_hash(dadosAtualizados.getSenha_hash());

        return usuariosRepository.save(gestorExistente);
    }
    
    public void deletarGestor(String gestorId) {
        
        usuariosRepository.deleteById(gestorId);
    }



    public byte[] criarPdfDasInstituicoes(List<Instituicao> instituicoes) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (

                PdfWriter writer = new PdfWriter(baos);

                PdfDocument pdf = new PdfDocument(writer);

                Document document = new Document(pdf)) {

            document.add(new Paragraph("Relatório Geral de Instituições").setFontSize(18).setBold());
            document.add(new Paragraph("Total de Instituições: " + instituicoes.size()));
            document.add(new Paragraph("\n"));

            for (Instituicao inst : instituicoes) {

                document.add(new Paragraph("======================================="));
                document.add(new Paragraph("Instituição: " + inst.getNome())
                        .setFontSize(14).setBold());

                document.add(new Paragraph(" "));

                document.add(new Paragraph("Escolas Cadastradas:")
                        .setItalic());

                Table table = new Table(UnitValue.createPercentArray(new float[] { 1, 3, 2, 2 }));
                table.setWidth(UnitValue.createPercentValue(95));

                table.addHeaderCell(new Paragraph("Nome da Escola").setBold());
                table.addHeaderCell(new Paragraph("Endereço").setBold());
                table.addHeaderCell(new Paragraph("Cidade").setBold()); // Novo Cabeçalho
                table.addHeaderCell(new Paragraph("CEP").setBold()); // Novo Cabeçalho

                if (inst.getEscolas() != null && !inst.getEscolas().isEmpty()) {
                    inst.getEscolas().forEach(escola -> {

                        table.addCell(escola.getNome());
                        table.addCell(escola.getEndereco());
                        table.addCell(escola.getCidade() != null ? escola.getCidade() : "N/A");
                        table.addCell(escola.getCep() != null ? escola.getCep() : "N/A");

                    });
                } else {

                }
                document.add(table);
                document.add(new Paragraph("\n"));
            }

        } catch (Exception e) {

            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public Comunicado enviarComunicado(@PathVariable String instituicaoId, ComunicadoRequest comunicadoDTO) {
      
        if (!instituicaoRepository.existsById(instituicaoId)) {
             throw new RuntimeException("Instituição remetente não encontrada.");
        }
        
        
        if (!usuariosRepository.existsById(comunicadoDTO.getGestorId())) { 
             throw new RuntimeException("Gestor destinatário (usuário) não encontrado.");
        }

  
        Comunicado novoComunicado = new Comunicado();
        novoComunicado.setInstituicaoId(instituicaoId); // Remetente
        novoComunicado.setGestorId(comunicadoDTO.getGestorId()); // Destinatário
        novoComunicado.setAssunto(comunicadoDTO.getAssunto());
        novoComunicado.setCorpo(comunicadoDTO.getCorpo());
        
      
        return comunicadoRepository.save(novoComunicado);
    }
    }
