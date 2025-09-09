// path: src/main/java/dao/api/MonitoramentoDao.java
package dao.api;

import java.util.List;

import exception.MonitoramentoException;
import model.Monitoramento;

public interface MonitoramentoDao {

    void create(Monitoramento monitoramento) throws MonitoramentoException;

    Monitoramento update(Monitoramento monitoramento) throws MonitoramentoException;

    void deleteById(Integer id) throws MonitoramentoException;

    Monitoramento findById(Integer id) throws MonitoramentoException;

    Monitoramento findWithFotoById(Integer id) throws MonitoramentoException;

    List<Monitoramento> findAll();

    List<Monitoramento> findAll(int page, int size);

    List<Monitoramento> findByStatus(Integer status);

    List<Monitoramento> findByNome(String nome);

    List<Monitoramento> findByDescricao(String descricao);

    List<Monitoramento> findByFoto(byte[] foto);

    List<Monitoramento> findByIdPeriodo(Integer idPeriodo);

    List<Monitoramento> search(Monitoramento filtro);

    List<Monitoramento> search(Monitoramento filtro, int page, int size);
}
