package dao.api;

import model.RotinaPeriodo;
import java.util.List;

public interface RotinaPeriodoDao {
    void create(RotinaPeriodo e);
    void update(RotinaPeriodo e);
    void deleteById(Integer id);
    RotinaPeriodo findById(Integer id);
    List<RotinaPeriodo> findAll();
    List<RotinaPeriodo> findAll(int page, int size);
    List<RotinaPeriodo> findByIdRotina(Integer idRotina);
    List<RotinaPeriodo> findByIdPeriodo(Integer idPeriodo);
    List<RotinaPeriodo> search(RotinaPeriodo f);
    List<RotinaPeriodo> search(RotinaPeriodo f, int page, int size);
}