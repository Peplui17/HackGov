package com.hackgov.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private SessaoService sessaoService; // Injeção do validador de sessão real

    // VALIDAÇÃO DE ENTRADAS 
    @PostMapping
    public ResponseEntity<?> receberSolicitacao(@RequestBody SolicitacaoDTO dados) {
        System.out.println("Recebido pedido de inserção para o tipo: " + dados.tipo());
        
        if (dados.tipo() == null || dados.tipo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro de Segurança: O tipo de ocorrência é obrigatório.");
        }
        if (dados.descricao() == null || dados.descricao().trim().length() < 10) {
            return ResponseEntity.badRequest().body("Erro de Segurança: A descrição deve conter pelo menos 10 caracteres.");
        }
        if (dados.descricao().length() > 4000) {
            return ResponseEntity.badRequest().body("Erro de Segurança: A descrição excedeu o limite seguro de caracteres da coluna.");
        }
        if (dados.endereco() == null || dados.endereco().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro de Segurança: O endereço/logradouro é obrigatório.");
        }

        String protocolo = "HCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setProtocolo(protocolo);
        solicitacao.setDataAbertura(LocalDate.now());
        solicitacao.setDescricao(dados.descricao().trim());
        solicitacao.setLogradouro(dados.endereco().trim());
        
        try {
            solicitacao.setTipoOcorrenciaIdTipo(Long.parseLong(dados.tipo()));
        } catch (NumberFormatException e) {
            solicitacao.setTipoOcorrenciaIdTipo(1L); 
        }
        
        repository.save(solicitacao);
        return ResponseEntity.ok(protocolo); 
    }

    // LISTAR TODAS AS SOLICITAÇÕES (Protegido com Controle de Acesso Real)
    @GetMapping
    public ResponseEntity<?> listarTodas(@RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("Tentativa de acesso ao painel do Dashboard...");

        // Procura o utilizador dono do token na memória do servidor
        Usuario usuarioLogado = sessaoService.obterUsuarioPorToken(token);
        
        // Bloqueio 1: Se o token for inválido ou não enviado
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de Segurança: Acesso negado. Faça login no sistema.");
        }

        // Bloqueio 2: Verifica os perfis válidos da base de dados Oracle (FUNCIONARIO ou GESTOR)
        if (!"FUNCIONARIO".equals(usuarioLogado.getPerfil()) && !"GESTOR".equals(usuarioLogado.getPerfil())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro de Segurança: O seu perfil não tem autorização para ver este painel.");
        }

        System.out.println("Acesso autorizado para: " + usuarioLogado.getLogin() + " [" + usuarioLogado.getPerfil() + "]");
        List<Solicitacao> lista = repository.findAll();
        return ResponseEntity.ok(lista); // Retorna a lista segura tipada dentro do ResponseEntity
    }
}