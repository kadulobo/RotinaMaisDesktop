// path: src/main/java/model/Caixa.java
package model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    @Column(name = "id_usuario")
    private Integer idUsuario;

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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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
                ", idUsuario=" + idUsuario +
                '}';
    }
}
