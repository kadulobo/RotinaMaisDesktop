// path: src/main/java/model/Objeto.java
package model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Objeto")
public class Objeto {

    @Id
    @Column(name = "id_objeto")
    private Integer idObjeto;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private Integer tipo;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Objeto)) return false;
        Objeto objeto = (Objeto) o;
        return Objects.equals(idObjeto, objeto.idObjeto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idObjeto);
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "idObjeto=" + idObjeto +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
