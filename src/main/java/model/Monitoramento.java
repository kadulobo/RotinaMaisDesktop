// path: src/main/java/model/Monitoramento.java
package model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "Monitoramento")
public class Monitoramento {

    @Id
    @Column(name = "id_monitoramento")
    private Integer idMonitoramento;

    @Column(name = "status")
    private Integer status;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "id_periodo")
    private Integer idPeriodo;

    public Integer getIdMonitoramento() {
        return idMonitoramento;
    }

    public void setIdMonitoramento(Integer idMonitoramento) {
        this.idMonitoramento = idMonitoramento;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monitoramento)) return false;
        Monitoramento that = (Monitoramento) o;
        return Objects.equals(idMonitoramento, that.idMonitoramento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMonitoramento);
    }

    @Override
    public String toString() {
        return "Monitoramento{" +
                "idMonitoramento=" + idMonitoramento +
                ", status=" + status +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", idPeriodo=" + idPeriodo +
                '}';
    }
}
