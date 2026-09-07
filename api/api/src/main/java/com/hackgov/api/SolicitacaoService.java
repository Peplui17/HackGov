package com.hackgov.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Optional;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository; // Repositório JPA para persistência

    // FILA (FIFO): Thread-safe para gerenciar a ordem de chegada das solicitações
    private final Queue<Solicitacao> filaDeDespacho = new ConcurrentLinkedQueue<>();

    // PILHA (LIFO): Mapeia o Protocolo para uma Pilha de Status
    private final Map<String, Stack<String>> historicoStatus = new ConcurrentHashMap<>();

    // Adiciona o chamado no final da fila, salva no banco e inicia a pilha
    public void enfileirarParaAtendimento(Solicitacao solicitacao) {
        solicitacaoRepository.save(solicitacao); // Persiste no banco
        filaDeDespacho.offer(solicitacao);
        
        Stack<String> statusInicial = new Stack<>();
        statusInicial.push(solicitacao.getStatusAtual() != null ? solicitacao.getStatusAtual() : "Aberto");
        historicoStatus.put(solicitacao.getProtocolo(), statusInicial);
    }

    public Solicitacao despacharProximaEquipe() {
        return filaDeDespacho.poll();
    }

    public int obterTamanhoFila() {
        return filaDeDespacho.size();
    }

    // Avança o status, empilha a nova etapa E atualiza o banco de dados
    public String atualizarStatus(String protocolo, String novoStatus) {
        Stack<String> pilha = historicoStatus.computeIfAbsent(protocolo, k -> {
            Stack<String> statusInicial = new Stack<>();
            statusInicial.push("Aberto");
            return statusInicial;
        });
        
        pilha.push(novoStatus);

        // Sincroniza a mudança de status com o Banco de Dados
        Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findByProtocolo(protocolo);
        if (optionalSolicitacao.isPresent()) {
            Solicitacao solicitacao = optionalSolicitacao.get();
            solicitacao.setStatusAtual(novoStatus); // Certifique-se de que o campo/getter/setter exista na entidade
            solicitacaoRepository.save(solicitacao);
        }

        return pilha.peek();
    }

    // Função Desfazer (LIFO): Remove o último status, volta para o anterior e atualiza o banco
    public String desfazerUltimoStatus(String protocolo) {
        Stack<String> pilha = historicoStatus.computeIfAbsent(protocolo, k -> {
            Stack<String> statusInicial = new Stack<>();
            statusInicial.push("Aberto");
            return statusInicial;
        });
        
        if (pilha.size() > 1) {
            pilha.pop(); // Remove o status atual
            String statusAnterior = pilha.peek(); // Pega o estado anterior

            // Sincroniza o retrocesso com o Banco de Dados
            Optional<Solicitacao> optionalSolicitacao = solicitacaoRepository.findByProtocolo(protocolo);
            if (optionalSolicitacao.isPresent()) {
                Solicitacao solicitacao = optionalSolicitacao.get();
                solicitacao.setStatusAtual(statusAnterior);
                solicitacaoRepository.save(solicitacao);
            }

            return statusAnterior;
        }
        
        return pilha.peek();
    }
    
    public String consultarStatusAtual(String protocolo) {
        Stack<String> pilha = historicoStatus.computeIfAbsent(protocolo, k -> {
            Stack<String> statusInicial = new Stack<>();
            statusInicial.push("Aberto");
            return statusInicial;
        });
        return pilha.peek();
    }
}