// path: src/main/java/model/Lancamento.java
package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Lancamento")
public class Lancamento {

    @Id
    @Column(name = "id_lancamento")
    private Integer idLancamento;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "fixo")
    private Boolean fixo;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "status")
    private Integer status;

    @Column(name = "id_movimentacao")
    private Integer idMovimentacao;

    @Column(name = "id_evento")
    private Integer idEvento;

    public Integer getIdLancamento() {
        return idLancamento;
    }

    public void setIdLancamento(Integer idLancamento) {
        this.idLancamento = idLancamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getFixo() {
        return fixo;
    }

    public void setFixo(Boolean fixo) {
        this.fixo = fixo;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Integer idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lancamento)) return false;
        Lancamento that = (Lancamento) o;
        return Objects.equals(idLancamento, that.idLancamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLancamento);
    }

    @Override
    public String toString() {
        return "Lancamento{" +
                "idLancamento=" + idLancamento +
                ", valor=" + valor +
                ", fixo=" + fixo +
                ", dataPagamento=" + dataPagamento +
                ", status=" + status +
                ", idMovimentacao=" + idMovimentacao +
                ", idEvento=" + idEvento +
                '}';
    }
}
