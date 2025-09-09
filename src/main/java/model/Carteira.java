package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Carteira", schema = "rotinamais")
public class Carteira {
    @Id
    @Column(name = "id_carteira")
    private Integer idCarteira;

    
    @Column(name = "nome")
    private String nome;

    
    @Column(name = "tipo")
    private String tipo;

    
    @Column(name = "dataInicio")
    private LocalDate dataInicio;

    
    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Integer getIdCarteira() { return idCarteira; }
    public void setIdCarteira(Integer idCarteira) { this.idCarteira = idCarteira; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carteira that = (Carteira) o;
        return Objects.equals(idCarteira, that.idCarteira);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarteira);
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "idCarteira=" + idCarteira +
                ", nome=" + nome +
                ", tipo=" + tipo +
                ", dataInicio=" + dataInicio +
                ", idUsuario=" + idUsuario +
                '}';
    }
}