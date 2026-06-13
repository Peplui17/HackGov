package com.hackgov.api;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessaoService {
    
    // Armazena temporariamente os tokens ativos (Token -> Objeto Usuário do Banco)
    private final Map<String, Usuario> sessoesAtivas = new ConcurrentHashMap<>();

    public String criarSessao(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        sessoesAtivas.put(token, usuario);
        return token;
    }

    public Usuario obterUsuarioPorToken(String token) {
        if (token == null) return null;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Limpa o prefixo HTTP padrão
        }
        return sessoesAtivas.get(token);
    }

    public void encerrarSessao(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        sessoesAtivas.remove(token);
    }
}