// path: src/main/java/model/Rotina.java
package model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Rotina", schema = "rotinamais")
public class Rotina {

    @Id
    @Column(name = "id_rotina")
    private Integer idRotina;

    @Column(name = "nome")
    private String nome;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fim")
    private LocalDate fim;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private Integer status;

    @Column(name = "ponto")
    private Integer ponto;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Integer getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(Integer idRotina) {
        this.idRotina = idRotina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public void setFim(LocalDate fim) {
        this.fim = fim;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rotina)) return false;
        Rotina rotina = (Rotina) o;
        return Objects.equals(idRotina, rotina.idRotina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRotina);
    }

    @Override
    public String toString() {
        return "Rotina{" +
                "idRotina=" + idRotina +
                ", nome='" + nome + '\'' +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                ", ponto=" + ponto +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
