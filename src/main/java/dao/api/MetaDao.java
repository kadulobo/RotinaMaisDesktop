package dao.api;

import model.Meta;
import java.util.List;

public interface MetaDao {
    void create(Meta meta);
    void update(Meta meta);
    void deleteById(Integer id);
    Meta findById(Integer id);
    Meta findWithFotoById(Integer id);
    List<Meta> findAll();
    List<Meta> findAll(int page, int size);
    List<Meta> findByPontoMinimo(Integer pontoMinimo);
    List<Meta> findByPontoMedio(Integer pontoMedio);
    List<Meta> findByPontoMaximo(Integer pontoMaximo);
    List<Meta> findByStatus(Integer status);
    List<Meta> findByIdPeriodo(Integer idPeriodo);
    List<Meta> search(Meta filtro);
    List<Meta> search(Meta filtro, int page, int size);
}
