// path: src/main/java/model/Lancamento.java
package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Lancamento", schema = "rotinamais")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimentacao")
    private Movimentacao movimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento evento;

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

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
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
                ", movimentacao=" + movimentacao +
                ", evento=" + evento +
                '}';
    }
}
