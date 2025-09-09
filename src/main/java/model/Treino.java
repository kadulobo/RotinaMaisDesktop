// path: src/main/java/model/Treino.java
package model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Treino")
public class Treino {

    @Id
    @Column(name = "id_treino")
    private Integer idTreino;

    @Column(name = "nome")
    private String nome;

    @Column(name = "classe")
    private String classe;

    @Column(name = "id_rotina")
    private Integer idRotina;

    public Integer getIdTreino() {
        return idTreino;
    }

    public void setIdTreino(Integer idTreino) {
        this.idTreino = idTreino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Integer getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(Integer idRotina) {
        this.idRotina = idRotina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Treino)) return false;
        Treino treino = (Treino) o;
        return Objects.equals(idTreino, treino.idTreino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTreino);
    }

    @Override
    public String toString() {
        return "Treino{" +
                "idTreino=" + idTreino +
                ", nome='" + nome + '\'' +
                ", classe='" + classe + '\'' +
                ", idRotina=" + idRotina +
                '}';
    }
}
