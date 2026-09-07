package com.hackgov.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitacoes")
@CrossOrigin(origins = "*") 
public class SolicitacaoController {

    @Autowired
    private SolicitacaoRepository repository;

    @Autowired
    private SessaoService sessaoService;

    // 1. Injeção do novo serviço de Fila e Pilha
    @Autowired
    private SolicitacaoService solicitacaoService;

    private final String UPLOAD_DIR = "uploads/evidencias/";

    @PostMapping
    public ResponseEntity<?> receberSolicitacao(
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "endereco", required = false) String endereco,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        
        if (tipo == null || tipo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro de Segurança: O tipo de ocorrência é obrigatório.");
        }
        if (descricao == null || descricao.trim().length() < 10) {
            return ResponseEntity.badRequest().body("Erro de Segurança: A descrição deve conter pelo menos 10 caracteres.");
        }
        if (descricao.length() > 4000) {
            return ResponseEntity.badRequest().body("Erro de Segurança: A descrição excedeu o limite seguro de caracteres da coluna.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro de Segurança: O endereço/logradouro é obrigatório.");
        }

        String protocolo = "HCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setProtocolo(protocolo);
        solicitacao.setDataAbertura(LocalDate.now());
        solicitacao.setDescricao(descricao.trim());
        solicitacao.setLogradouro(endereco.trim());
        
        if (latitude != null) solicitacao.setLatitude(latitude);
        if (longitude != null) solicitacao.setLongitude(longitude);
        
        try {
            solicitacao.setTipoOcorrenciaIdTipo(Long.parseLong(tipo));
        } catch (NumberFormatException e) {
            solicitacao.setTipoOcorrenciaIdTipo(1L); 
        }

        if (imagem != null && !imagem.isEmpty()) {
            try {
                if (imagem.getSize() > 5 * 1024 * 1024) { 
                    return ResponseEntity.badRequest().body("Erro: A imagem excede o tamanho máximo de 5MB.");
                }

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String nomeArquivo = protocolo + "_" + imagem.getOriginalFilename();
                Path filePath = uploadPath.resolve(nomeArquivo);
                
                Files.copy(imagem.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                solicitacao.setCaminhoImagem(filePath.toString());
                
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro interno ao processar o upload da imagem.");
            }
        }
        
        repository.save(solicitacao);
        
        // 2. Integração: Enfileira a solicitação recém-salva no banco para atendimento
        if (solicitacaoService != null) {
            solicitacaoService.enfileirarParaAtendimento(solicitacao);
        }

        return ResponseEntity.ok(protocolo); 
    }

    @GetMapping
    public ResponseEntity<?> listarTodas(@RequestHeader(value = "Authorization", required = false) String token) {
        Usuario usuarioLogado = sessaoService.obterUsuarioPorToken(token);
        
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de Segurança: Acesso negado. Faça login no sistema.");
        }

        if (!"FUNCIONARIO".equals(usuarioLogado.getPerfil()) && !"GESTOR".equals(usuarioLogado.getPerfil())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de Segurança: O seu perfil não tem autorização para ver este painel.");
        }

        List<Solicitacao> lista = repository.findAll();
        return ResponseEntity.ok(lista); 
    }

    // 3. Novos Endpoints para Estruturas de Dados Avançadas

    @GetMapping("/fila/proxima")
    public ResponseEntity<?> despacharProxima() {
        Solicitacao proxima = solicitacaoService.despacharProximaEquipe();
        if (proxima == null) {
            return ResponseEntity.ok("A fila está vazia. Não há chamados pendentes.");
        }
        return ResponseEntity.ok(proxima);
    }

    @PostMapping("/{protocolo}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable String protocolo, @RequestParam String novoStatus) {
        String statusAtual = solicitacaoService.atualizarStatus(protocolo, novoStatus);
        return ResponseEntity.ok("Status atualizado para: " + statusAtual);
    }

    @PostMapping("/{protocolo}/status/desfazer")
    public ResponseEntity<?> desfazerStatus(@PathVariable String protocolo) {
        String statusRevertido = solicitacaoService.desfazerUltimoStatus(protocolo);
        return ResponseEntity.ok("Ação desfeita. O status retornou para: " + statusRevertido);
    }
}