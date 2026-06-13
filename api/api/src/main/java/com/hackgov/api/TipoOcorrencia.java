package com.hackgov.api;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_ocorrencia")
public class TipoOcorrencia {

    @Id
    @Column(name = "id_tipo")
    private Long idTipo;

    @Column(name = "nome_tipo", nullable = false, length = 50)
    private String nomeTipo;

    @Column(name = "prazo_sla_dias", nullable = false)
    private Integer prazoSlaDias;

    // Getters e Setters
    public Long getIdTipo() { return idTipo; }
    public void setIdTipo(Long idTipo) { this.idTipo = idTipo; }
    public String getNomeTipo() { return nomeTipo; }
    public void setNomeTipo(String nomeTipo) { this.nomeTipo = nomeTipo; }
    public Integer getPrazoSlaDias() { return prazoSlaDias; }
    public void setPrazoSlaDias(Integer prazoSlaDias) { this.prazoSlaDias = prazoSlaDias; }
}