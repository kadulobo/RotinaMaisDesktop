// path: src/main/java/model/Movimentacao.java
package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Movimentacao", schema = "rotinamais")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caixa")
    private Caixa caixa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    @OneToMany(mappedBy = "movimentacao")
    private List<Lancamento> lancamentos = new ArrayList<>();

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
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
                ", usuario=" + usuario +
                ", caixa=" + caixa +
                ", periodo=" + periodo +
                '}';
    }
}
