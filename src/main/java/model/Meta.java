package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Meta", schema = "rotinamais")
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meta")
    private Integer idMeta;

    @Column(name = "ponto_minimo")
    private Integer pontoMinimo;

    @Column(name = "ponto_medio")
    private Integer pontoMedio;

    @Column(name = "ponto_maximo")
    private Integer pontoMaximo;

    @Column(name = "status")
    private Integer status;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "id_periodo")
    private Integer idPeriodo;

    // getters and setters
    public Integer getIdMeta() { return idMeta; }
    public void setIdMeta(Integer idMeta) { this.idMeta = idMeta; }
    public Integer getPontoMinimo() { return pontoMinimo; }
    public void setPontoMinimo(Integer pontoMinimo) { this.pontoMinimo = pontoMinimo; }
    public Integer getPontoMedio() { return pontoMedio; }
    public void setPontoMedio(Integer pontoMedio) { this.pontoMedio = pontoMedio; }
    public Integer getPontoMaximo() { return pontoMaximo; }
    public void setPontoMaximo(Integer pontoMaximo) { this.pontoMaximo = pontoMaximo; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
    public Integer getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(Integer idPeriodo) { this.idPeriodo = idPeriodo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meta meta = (Meta) o;
        return Objects.equals(idMeta, meta.idMeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMeta);
    }

    @Override
    public String toString() {
        return "Meta{" +
                "idMeta=" + idMeta +
                ", pontoMinimo=" + pontoMinimo +
                ", pontoMedio=" + pontoMedio +
                ", pontoMaximo=" + pontoMaximo +
                ", status=" + status +
                ", idPeriodo=" + idPeriodo +
                '}';
    }
}
