package com.hackgov.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, String> {
    // Abstrai todos os comandos INSERT/SELECT automáticos do banco
}