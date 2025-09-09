package dao.api;

import model.MonitoramentoObjeto;
import java.util.List;

public interface MonitoramentoObjetoDao {
    void create(MonitoramentoObjeto e);
    void update(MonitoramentoObjeto e);
    void deleteById(LocalDate id);
    MonitoramentoObjeto findById(LocalDate id);
    List<MonitoramentoObjeto> findAll();
    List<MonitoramentoObjeto> findAll(int page, int size);
    List<MonitoramentoObjeto> findByData(LocalDate data);
    List<MonitoramentoObjeto> findByIdMonitoramento(Integer idMonitoramento);
    List<MonitoramentoObjeto> findByIdObjeto(Integer idObjeto);
    List<MonitoramentoObjeto> search(MonitoramentoObjeto f);
    List<MonitoramentoObjeto> search(MonitoramentoObjeto f, int page, int size);
}