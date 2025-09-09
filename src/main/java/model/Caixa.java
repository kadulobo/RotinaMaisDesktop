// path: src/main/java/model/Caixa.java
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
@Table(name = "Caixa")
public class Caixa {

    @Id
    @Column(name = "id_caixa")
    private Integer idCaixa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "reserva_emergencia")
    private BigDecimal reservaEmergencia;

    @Column(name = "salario_medio")
    private BigDecimal salarioMedio;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "caixa")
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    public Integer getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Integer idCaixa) {
        this.idCaixa = idCaixa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getReservaEmergencia() {
        return reservaEmergencia;
    }

    public void setReservaEmergencia(BigDecimal reservaEmergencia) {
        this.reservaEmergencia = reservaEmergencia;
    }

    public BigDecimal getSalarioMedio() {
        return salarioMedio;
    }

    public void setSalarioMedio(BigDecimal salarioMedio) {
        this.salarioMedio = salarioMedio;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<Movimentacao> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caixa)) return false;
        Caixa caixa = (Caixa) o;
        return Objects.equals(idCaixa, caixa.idCaixa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCaixa);
    }

    @Override
    public String toString() {
        return "Caixa{" +
                "idCaixa=" + idCaixa +
                ", nome='" + nome + '\'' +
                ", reservaEmergencia=" + reservaEmergencia +
                ", salarioMedio=" + salarioMedio +
                ", valorTotal=" + valorTotal +
                ", usuario=" + usuario +
                '}';
    }
}
