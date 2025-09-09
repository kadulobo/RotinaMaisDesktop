package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Papel", schema = "rotinamais")
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_papel")
    private Integer idPapel;

    
    @Column(name = "codigo")
    private String codigo;

    
    @Column(name = "tipo")
    private String tipo;

    
    @Column(name = "vencimento")
    private LocalDate vencimento;

    public Integer getIdPapel() { return idPapel; }
    public void setIdPapel(Integer idPapel) { this.idPapel = idPapel; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getVencimento() { return vencimento; }
    public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Papel that = (Papel) o;
        return Objects.equals(idPapel, that.idPapel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPapel);
    }

    @Override
    public String toString() {
        return "Papel{" +
                "idPapel=" + idPapel +
                ", codigo=" + codigo +
                ", tipo=" + tipo +
                ", vencimento=" + vencimento +
                '}';
    }
}