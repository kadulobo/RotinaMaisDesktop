package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Ingrediente_fornecedor")
public class IngredienteFornecedor {
    @Id
    @Column(name = "id_fornecedor_ingrediente")
    private Integer idFornecedorIngrediente;

    
    @Column(name = "valor")
    private BigDecimal valor;

    
    @Column(name = "data")
    private LocalDate data;

    
    @Column(name = "id_fornecedor")
    private Integer idFornecedor;

    
    @Column(name = "id_ingrediente")
    private Integer idIngrediente;

    public Integer getIdFornecedorIngrediente() { return idFornecedorIngrediente; }
    public void setIdFornecedorIngrediente(Integer idFornecedorIngrediente) { this.idFornecedorIngrediente = idFornecedorIngrediente; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Integer idFornecedor) { this.idFornecedor = idFornecedor; }

    public Integer getIdIngrediente() { return idIngrediente; }
    public void setIdIngrediente(Integer idIngrediente) { this.idIngrediente = idIngrediente; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredienteFornecedor that = (IngredienteFornecedor) o;
        return Objects.equals(idFornecedorIngrediente, that.idFornecedorIngrediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFornecedorIngrediente);
    }

    @Override
    public String toString() {
        return "IngredienteFornecedor{" +
                "idFornecedorIngrediente=" + idFornecedorIngrediente +
                ", valor=" + valor +
                ", data=" + data +
                ", idFornecedor=" + idFornecedor +
                ", idIngrediente=" + idIngrediente +
                '}';
    }
}