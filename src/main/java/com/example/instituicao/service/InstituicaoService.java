package com.example.instituicao.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
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



     public byte[] gerarPdfInstituicoes() {
  
    List<Instituicao> instituicoes = instituicaoRepository.findAll(); 
    
   
    for (Instituicao inst : instituicoes) {
        if (inst.getEscolas() != null) {
            inst.getEscolas().size(); 
        }
    }
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    try (PdfWriter writer = new PdfWriter(baos);
         PdfDocument pdf = new PdfDocument(writer);
         Document document = new Document(pdf, PageSize.A4)) {
        
        document.setMargins(20, 20, 20, 20);
        
   
        Paragraph titulo = new Paragraph("Relatório Geral de Instituições")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);
        
        document.add(new Paragraph(" ")); 
        
       
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataGeracao = sdf.format(new Date());
        document.add(new Paragraph("Gerado em: " + dataGeracao)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY));
        
        document.add(new Paragraph("Total de Instituições: " + instituicoes.size())
                .setBold());
        
        document.add(new Paragraph("\n"));
        
        int totalEscolas = 0;
        
        
        for (int i = 0; i < instituicoes.size(); i++) {
            Instituicao inst = instituicoes.get(i);
            
       
            if (i > 0) {
                document.add(new Paragraph(" "));
            }
            
          
            Paragraph nomeInstituicao = new Paragraph((i + 1) + ". " + inst.getNome())
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE);
            document.add(nomeInstituicao);
            
          
            if (inst.getId() != null) {
                document.add(new Paragraph("ID: " + inst.getId())
                        .setFontSize(10)
                        .setFontColor(ColorConstants.DARK_GRAY));
            }
            
            document.add(new Paragraph(" "));
            
          
            List<UnidadesEscolares> escolas = inst.getEscolas();
            
            if (escolas != null && !escolas.isEmpty()) {
                totalEscolas += escolas.size();
                
                Paragraph subtitulo = new Paragraph("Escolas Cadastradas (" + escolas.size() + "):")
                        .setFontSize(12)
                        .setBold()
                        .setItalic();
                document.add(subtitulo);
                
                document.add(new Paragraph(" "));
                
               
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 2, 2}));
                table.setWidth(UnitValue.createPercentValue(100));
                table.setMarginBottom(10);
     
                table.addHeaderCell(new Cell().add(new Paragraph("Nome").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Endereço").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Cidade").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("CEP").setBold()));
              
                for (UnidadesEscolares escola : escolas) {
                    table.addCell(new Cell().add(new Paragraph(escola.getNome() != null ? escola.getNome() : "")));
                    table.addCell(new Cell().add(new Paragraph(escola.getEndereco() != null ? escola.getEndereco() : "")));
                    table.addCell(new Cell().add(new Paragraph(escola.getCidade() != null ? escola.getCidade() : "N/A")));
                    table.addCell(new Cell().add(new Paragraph(escola.getCep() != null ? escola.getCep() : "N/A")));
                }
                
                document.add(table);
            } else {
                document.add(new Paragraph("Nenhuma escola cadastrada")
                        .setFontColor(ColorConstants.RED)
                        .setItalic());
            }
            
           
            if ((i + 1) % 3 == 0 && i < instituicoes.size() - 1) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        
      
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("=".repeat(50))
                .setTextAlignment(TextAlignment.CENTER));
        
        Paragraph resumo = new Paragraph("RESUMO GERAL")
                .setFontSize(12)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(resumo);
        
        document.add(new Paragraph("Total de Instituições: " + instituicoes.size())
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Total de Escolas: " + totalEscolas)
                .setTextAlignment(TextAlignment.CENTER));
        
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
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
