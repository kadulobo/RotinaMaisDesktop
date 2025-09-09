package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Rotina_Periodo")
public class RotinaPeriodo {
    @Id
    @Column(name = "id_rotina_periodo")
    private Integer idRotinaPeriodo;

    
    @Column(name = "id_rotina")
    private Integer idRotina;

    
    @Column(name = "id_periodo")
    private Integer idPeriodo;

    public Integer getIdRotinaPeriodo() { return idRotinaPeriodo; }
    public void setIdRotinaPeriodo(Integer idRotinaPeriodo) { this.idRotinaPeriodo = idRotinaPeriodo; }

    public Integer getIdRotina() { return idRotina; }
    public void setIdRotina(Integer idRotina) { this.idRotina = idRotina; }

    public Integer getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(Integer idPeriodo) { this.idPeriodo = idPeriodo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RotinaPeriodo that = (RotinaPeriodo) o;
        return Objects.equals(idRotinaPeriodo, that.idRotinaPeriodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRotinaPeriodo);
    }

    @Override
    public String toString() {
        return "RotinaPeriodo{" +
                "idRotinaPeriodo=" + idRotinaPeriodo +
                ", idRotina=" + idRotina +
                ", idPeriodo=" + idPeriodo +
                '}';
    }
}