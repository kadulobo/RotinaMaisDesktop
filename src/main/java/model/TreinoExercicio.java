package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Treino_Exercicio")
public class TreinoExercicio {
    @Id
    @Column(name = "id_treino_exercicio")
    private Integer idTreinoExercicio;

    
    @Column(name = "qtd_repeticao")
    private Integer qtdRepeticao;

    
    @Column(name = "tempo_descanso")
    private String tempoDescanso;

    
    @Column(name = "ordem")
    private Integer ordem;

    
    @Column(name = "feito")
    private Boolean feito;

    
    @Column(name = "id_exercicio")
    private Integer idExercicio;

    
    @Column(name = "id_treino")
    private Integer idTreino;

    public Integer getIdTreinoExercicio() { return idTreinoExercicio; }
    public void setIdTreinoExercicio(Integer idTreinoExercicio) { this.idTreinoExercicio = idTreinoExercicio; }

    public Integer getQtdRepeticao() { return qtdRepeticao; }
    public void setQtdRepeticao(Integer qtdRepeticao) { this.qtdRepeticao = qtdRepeticao; }

    public String getTempoDescanso() { return tempoDescanso; }
    public void setTempoDescanso(String tempoDescanso) { this.tempoDescanso = tempoDescanso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getFeito() { return feito; }
    public void setFeito(Boolean feito) { this.feito = feito; }

    public Integer getIdExercicio() { return idExercicio; }
    public void setIdExercicio(Integer idExercicio) { this.idExercicio = idExercicio; }

    public Integer getIdTreino() { return idTreino; }
    public void setIdTreino(Integer idTreino) { this.idTreino = idTreino; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreinoExercicio that = (TreinoExercicio) o;
        return Objects.equals(idTreinoExercicio, that.idTreinoExercicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTreinoExercicio);
    }

    @Override
    public String toString() {
        return "TreinoExercicio{" +
                "idTreinoExercicio=" + idTreinoExercicio +
                ", qtdRepeticao=" + qtdRepeticao +
                ", tempoDescanso=" + tempoDescanso +
                ", ordem=" + ordem +
                ", feito=" + feito +
                ", idExercicio=" + idExercicio +
                ", idTreino=" + idTreino +
                '}';
    }
}