package com.hackgov.api;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "solicitacao")
public class Solicitacao {

    @Id
    @Column(name = "protocolo", length = 20)
    private String protocolo;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "descricao", nullable = false, length = 4000)
    private String descricao;

    @Column(name = "latitude", nullable = false)
    private Double latitude = -23.550520; // Valor padrão simulado

    @Column(name = "longitude", nullable = false)
    private Double longitude = -46.633308; // Valor padrão simulado

    @Column(name = "logradouro", nullable = false, length = 150)
    private String logradouro;

    @Column(name = "bairro", nullable = false, length = 50)
    private String bairro = "Centro"; // Valor padrão simulado

    @JsonProperty("statusAtual")
    @Column(name = "status_atual", nullable = false, length = 20)
    private String statusAtual = "ABERTO";

    @Column(name = "id_foto", nullable = false)
    private Integer idFoto = 1;

    @Column(name = "id_historico", nullable = false)
    private Integer idHistorico = 1;

    @Column(name = "tipo_ocorrencia_id_tipo", nullable = false)
    private Long tipoOcorrenciaIdTipo;

    // Getters e Setters
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    
    @JsonProperty("statusAtual")
    public String getStatusAtual() { return statusAtual; }
    @JsonProperty("statusAtual")
    public void setStatusAtual(String statusAtual) { this.statusAtual = statusAtual; }

    public Long getTipoOcorrenciaIdTipo() { return tipoOcorrenciaIdTipo; }
    public void setTipoOcorrenciaIdTipo(Long tipoOcorrenciaIdTipo) { this.tipoOcorrenciaIdTipo = tipoOcorrenciaIdTipo; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public Integer getIdFoto() { return idFoto; }
    public void setIdFoto(Integer idFoto) { this.idFoto = idFoto; }

    public Integer getIdHistorico() { return idHistorico; }
    public void setIdHistorico(Integer idHistorico) { this.idHistorico = idHistorico; }
}