package com.hackgov.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, String> {
    // Método para buscar a solicitação utilizando o protocolo como chave
    Optional<Solicitacao> findByProtocolo(String protocolo);
}