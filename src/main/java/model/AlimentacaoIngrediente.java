package model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Alimentacao_Ingrediente")
public class AlimentacaoIngrediente {
    @Id
    @Column(name = "id_alimentacao_ingrediente")
    private Integer idAlimentacaoIngrediente;

    
    @Column(name = "quantidade")
    private Integer quantidade;

    
    @Column(name = "id_alimentacao")
    private Integer idAlimentacao;

    
    @Column(name = "id_ingrediente")
    private Integer idIngrediente;

    public Integer getIdAlimentacaoIngrediente() { return idAlimentacaoIngrediente; }
    public void setIdAlimentacaoIngrediente(Integer idAlimentacaoIngrediente) { this.idAlimentacaoIngrediente = idAlimentacaoIngrediente; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Integer getIdAlimentacao() { return idAlimentacao; }
    public void setIdAlimentacao(Integer idAlimentacao) { this.idAlimentacao = idAlimentacao; }

    public Integer getIdIngrediente() { return idIngrediente; }
    public void setIdIngrediente(Integer idIngrediente) { this.idIngrediente = idIngrediente; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlimentacaoIngrediente that = (AlimentacaoIngrediente) o;
        return Objects.equals(idAlimentacaoIngrediente, that.idAlimentacaoIngrediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlimentacaoIngrediente);
    }

    @Override
    public String toString() {
        return "AlimentacaoIngrediente{" +
                "idAlimentacaoIngrediente=" + idAlimentacaoIngrediente +
                ", quantidade=" + quantidade +
                ", idAlimentacao=" + idAlimentacao +
                ", idIngrediente=" + idIngrediente
                '}';
    }
}