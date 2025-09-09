// path: src/main/java/model/Movimentacao.java
package model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Movimentacao")
public class Movimentacao {

    @Id
    @Column(name = "id_movimentacao")
    private Integer idMovimentacao;

    @Column(name = "desconto")
    private BigDecimal desconto;

    @Column(name = "vantagem")
    private BigDecimal vantagem;

    @Column(name = "liquido")
    private BigDecimal liquido;

    @Column(name = "tipo")
    private Integer tipo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "ponto")
    private Integer ponto;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_caixa")
    private Integer idCaixa;

    @Column(name = "id_periodo")
    private Integer idPeriodo;

    public Integer getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Integer idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getVantagem() {
        return vantagem;
    }

    public void setVantagem(BigDecimal vantagem) {
        this.vantagem = vantagem;
    }

    public BigDecimal getLiquido() {
        return liquido;
    }

    public void setLiquido(BigDecimal liquido) {
        this.liquido = liquido;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPonto() {
        return ponto;
    }

    public void setPonto(Integer ponto) {
        this.ponto = ponto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Integer idCaixa) {
        this.idCaixa = idCaixa;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movimentacao)) return false;
        Movimentacao that = (Movimentacao) o;
        return Objects.equals(idMovimentacao, that.idMovimentacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimentacao);
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "idMovimentacao=" + idMovimentacao +
                ", desconto=" + desconto +
                ", vantagem=" + vantagem +
                ", liquido=" + liquido +
                ", tipo=" + tipo +
                ", status=" + status +
                ", ponto=" + ponto +
                ", idUsuario=" + idUsuario +
                ", idCaixa=" + idCaixa +
                ", idPeriodo=" + idPeriodo +
                '}';
    }
}
