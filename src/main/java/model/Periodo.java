// path: src/main/java/model/Periodo.java
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Periodo", schema = "rotinamais")
public class Periodo {

    @Id
    @Column(name = "id_periodo")
    private Integer idPeriodo;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "mes")
    private Integer mes;

    @OneToMany(mappedBy = "periodo")
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
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
        if (!(o instanceof Periodo)) return false;
        Periodo periodo = (Periodo) o;
        return Objects.equals(idPeriodo, periodo.idPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPeriodo);
    }

    @Override
    public String toString() {
        return "Periodo{" +
                "idPeriodo=" + idPeriodo +
                ", ano=" + ano +
                ", mes=" + mes +
                '}';
    }
}
