package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Monitoramento_Objeto", schema = "rotinamais")
public class MonitoramentoObjeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monitoramento_objeto")
    private LocalDate idMonitoramentoObjeto;

    
    @Column(name = "data")
    private LocalDate data;

    
    @Column(name = "id_monitoramento")
    private Integer idMonitoramento;

    
    @Column(name = "id_objeto")
    private Integer idObjeto;

    public LocalDate getIdMonitoramentoObjeto() { return idMonitoramentoObjeto; }
    public void setIdMonitoramentoObjeto(LocalDate idMonitoramentoObjeto) { this.idMonitoramentoObjeto = idMonitoramentoObjeto; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getIdMonitoramento() { return idMonitoramento; }
    public void setIdMonitoramento(Integer idMonitoramento) { this.idMonitoramento = idMonitoramento; }

    public Integer getIdObjeto() { return idObjeto; }
    public void setIdObjeto(Integer idObjeto) { this.idObjeto = idObjeto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoramentoObjeto that = (MonitoramentoObjeto) o;
        return Objects.equals(idMonitoramentoObjeto, that.idMonitoramentoObjeto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMonitoramentoObjeto);
    }

    @Override
    public String toString() {
        return "MonitoramentoObjeto{" +
                "idMonitoramentoObjeto=" + idMonitoramentoObjeto +
                ", data=" + data +
                ", idMonitoramento=" + idMonitoramento +
                ", idObjeto=" + idObjeto +
                '}';
    }
}