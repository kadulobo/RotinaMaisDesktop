// path: src/main/java/model/Exercicio.java
package model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "Exercicio", schema = "rotinamais")
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exercicio")
    private Integer idExercicio;

    @Column(name = "nome")
    private String nome;

    @Column(name = "carga_leve")
    private Integer cargaLeve;

    @Column(name = "carga_media")
    private Integer cargaMedia;

    @Column(name = "carga_maxima")
    private Integer cargaMaxima;

    public Integer getIdExercicio() {
        return idExercicio;
    }

    public void setIdExercicio(Integer idExercicio) {
        this.idExercicio = idExercicio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCargaLeve() {
        return cargaLeve;
    }

    public void setCargaLeve(Integer cargaLeve) {
        this.cargaLeve = cargaLeve;
    }

    public Integer getCargaMedia() {
        return cargaMedia;
    }

    public void setCargaMedia(Integer cargaMedia) {
        this.cargaMedia = cargaMedia;
    }

    public Integer getCargaMaxima() {
        return cargaMaxima;
    }

    public void setCargaMaxima(Integer cargaMaxima) {
        this.cargaMaxima = cargaMaxima;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercicio)) return false;
        Exercicio that = (Exercicio) o;
        return Objects.equals(idExercicio, that.idExercicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExercicio);
    }

    @Override
    public String toString() {
        return "Exercicio{" +
                "idExercicio=" + idExercicio +
                ", nome='" + nome + '\'' +
                ", cargaLeve=" + cargaLeve +
                ", cargaMedia=" + cargaMedia +
                ", cargaMaxima=" + cargaMaxima +
                '}';
    }
}
