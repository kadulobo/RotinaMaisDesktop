// path: src/main/java/model/Documento.java
package model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Documento", schema = "rotinamais")
public class Documento {

    @Id
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "nome")
    private String nome;

    @Lob
    @Column(name = "arquivo")
    private byte[] arquivo;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
        if (!(o instanceof Documento)) return false;
        Documento that = (Documento) o;
        return Objects.equals(idDocumento, that.idDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocumento);
    }

    @Override
    public String toString() {
        return "Documento{" +
                "idDocumento=" + idDocumento +
                ", nome='" + nome + '\'' +
                ", data=" + data +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
