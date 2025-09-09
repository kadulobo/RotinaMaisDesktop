package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Operacao", schema = "rotinamais")
public class Operacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacao")
    private Integer idOperacao;

    
    @Column(name = "fechamento")
    private BigDecimal fechamento;

    
    @Column(name = "tempo_operacao")
    private LocalTime tempoOperacao;

    
    @Column(name = "qtd_compra")
    private Integer qtdCompra;

    
    @Column(name = "abertura")
    private BigDecimal abertura;

    
    @Column(name = "qtd_venda")
    private Integer qtdVenda;

    
    @Column(name = "lado")
    private String lado;

    
    @Column(name = "preco_compra")
    private BigDecimal precoCompra;

    
    @Column(name = "preco_venda")
    private BigDecimal precoVenda;

    
    @Column(name = "preco_medio")
    private BigDecimal precoMedio;

    
    @Column(name = "res_intervalo")
    private String resIntervalo;

    
    @Column(name = "numero_operacao")
    private BigDecimal numeroOperacao;

    
    @Column(name = "res_operacao")
    private String resOperacao;

    
    @Column(name = "drawdon")
    private BigDecimal drawdon;

    
    @Column(name = "ganhoMax")
    private BigDecimal ganhoMax;

    
    @Column(name = "perdaMax")
    private BigDecimal perdaMax;

    
    @Column(name = "tet")
    private String tet;

    
    @Column(name = "total")
    private BigDecimal total;

    
    @Column(name = "id_carteira")
    private Integer idCarteira;

    
    @Column(name = "id_papel")
    private Integer idPapel;

    public Integer getIdOperacao() { return idOperacao; }
    public void setIdOperacao(Integer idOperacao) { this.idOperacao = idOperacao; }

    public BigDecimal getFechamento() { return fechamento; }
    public void setFechamento(BigDecimal fechamento) { this.fechamento = fechamento; }

    public LocalTime getTempoOperacao() { return tempoOperacao; }
    public void setTempoOperacao(LocalTime tempoOperacao) { this.tempoOperacao = tempoOperacao; }

    public Integer getQtdCompra() { return qtdCompra; }
    public void setQtdCompra(Integer qtdCompra) { this.qtdCompra = qtdCompra; }

    public BigDecimal getAbertura() { return abertura; }
    public void setAbertura(BigDecimal abertura) { this.abertura = abertura; }

    public Integer getQtdVenda() { return qtdVenda; }
    public void setQtdVenda(Integer qtdVenda) { this.qtdVenda = qtdVenda; }

    public String getLado() { return lado; }
    public void setLado(String lado) { this.lado = lado; }

    public BigDecimal getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(BigDecimal precoCompra) { this.precoCompra = precoCompra; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public BigDecimal getPrecoMedio() { return precoMedio; }
    public void setPrecoMedio(BigDecimal precoMedio) { this.precoMedio = precoMedio; }

    public String getResIntervalo() { return resIntervalo; }
    public void setResIntervalo(String resIntervalo) { this.resIntervalo = resIntervalo; }

    public BigDecimal getNumeroOperacao() { return numeroOperacao; }
    public void setNumeroOperacao(BigDecimal numeroOperacao) { this.numeroOperacao = numeroOperacao; }

    public String getResOperacao() { return resOperacao; }
    public void setResOperacao(String resOperacao) { this.resOperacao = resOperacao; }

    public BigDecimal getDrawdon() { return drawdon; }
    public void setDrawdon(BigDecimal drawdon) { this.drawdon = drawdon; }

    public BigDecimal getGanhoMax() { return ganhoMax; }
    public void setGanhoMax(BigDecimal ganhoMax) { this.ganhoMax = ganhoMax; }

    public BigDecimal getPerdaMax() { return perdaMax; }
    public void setPerdaMax(BigDecimal perdaMax) { this.perdaMax = perdaMax; }

    public String getTet() { return tet; }
    public void setTet(String tet) { this.tet = tet; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getIdCarteira() { return idCarteira; }
    public void setIdCarteira(Integer idCarteira) { this.idCarteira = idCarteira; }

    public Integer getIdPapel() { return idPapel; }
    public void setIdPapel(Integer idPapel) { this.idPapel = idPapel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operacao that = (Operacao) o;
        return Objects.equals(idOperacao, that.idOperacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOperacao);
    }

    @Override
    public String toString() {
        return "Operacao{" +
                "idOperacao=" + idOperacao +
                ", fechamento=" + fechamento +
                ", tempoOperacao=" + tempoOperacao +
                ", qtdCompra=" + qtdCompra +
                ", abertura=" + abertura +
                ", qtdVenda=" + qtdVenda +
                ", lado=" + lado +
                ", precoCompra=" + precoCompra +
                ", precoVenda=" + precoVenda +
                ", precoMedio=" + precoMedio +
                ", resIntervalo=" + resIntervalo +
                ", numeroOperacao=" + numeroOperacao +
                ", resOperacao=" + resOperacao +
                ", drawdon=" + drawdon +
                ", ganhoMax=" + ganhoMax +
                ", perdaMax=" + perdaMax +
                ", tet=" + tet +
                ", total=" + total +
                ", idCarteira=" + idCarteira +
                ", idPapel=" + idPapel +
                '}';
    }
}