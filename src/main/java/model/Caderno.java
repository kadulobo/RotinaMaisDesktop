package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Caderno", schema = "rotinamais")
public class Caderno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caderno")
    private Integer idCaderno;

    @Column(name = "nome_ia")
    private String nomeIa;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "comando")
    private String comando;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "resultado_imagem")
    private byte[] resultadoImagem;

    @Column(name = "resultado_video")
    private byte[] resultadoVideo;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_categoria")
    private Integer idCategoria;

    public Integer getIdCaderno() { return idCaderno; }
    public void setIdCaderno(Integer idCaderno) { this.idCaderno = idCaderno; }

    public String getNomeIa() { return nomeIa; }
    public void setNomeIa(String nomeIa) { this.nomeIa = nomeIa; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getComando() { return comando; }
    public void setComando(String comando) { this.comando = comando; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public byte[] getResultadoImagem() { return resultadoImagem; }
    public void setResultadoImagem(byte[] resultadoImagem) { this.resultadoImagem = resultadoImagem; }

    public byte[] getResultadoVideo() { return resultadoVideo; }
    public void setResultadoVideo(byte[] resultadoVideo) { this.resultadoVideo = resultadoVideo; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caderno caderno = (Caderno) o;
        return Objects.equals(idCaderno, caderno.idCaderno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCaderno);
    }

    @Override
    public String toString() {
        return "Caderno{" +
                "idCaderno=" + idCaderno +
                ", nomeIa='" + nomeIa + '\'' +
                ", titulo='" + titulo + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", comando='" + comando + '\'' +
                ", data=" + data +
                ", idUsuario=" + idUsuario +
                ", idCategoria=" + idCategoria +
                '}';
    }
}
