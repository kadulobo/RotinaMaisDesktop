package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Cofre", schema = "rotinamais")
public class Cofre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cofre")
    private Integer idCofre;

    @Column(name = "login")
    private String login;

    @Column(name = "senha")
    private String senha;

    @Column(name = "tipo")
    private Integer tipo;

    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "plataforma")
    private String plataforma;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    // getters and setters
    public Integer getIdCofre() { return idCofre; }
    public void setIdCofre(Integer idCofre) { this.idCofre = idCofre; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Integer getTipo() { return tipo; }
    public void setTipo(Integer tipo) { this.tipo = tipo; }
    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cofre cofre = (Cofre) o;
        return Objects.equals(idCofre, cofre.idCofre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCofre);
    }

    @Override
    public String toString() {
        return "Cofre{" +
                "idCofre=" + idCofre +
                ", login='" + login + '\'' +
                ", tipo=" + tipo +
                ", plataforma='" + plataforma + '\'' +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
