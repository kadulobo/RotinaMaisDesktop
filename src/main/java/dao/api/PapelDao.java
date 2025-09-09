package dao.api;

import model.Papel;
import java.util.List;

public interface PapelDao {
    void create(Papel e);
    void update(Papel e);
    void deleteById(Integer id);
    Papel findById(Integer id);
    List<Papel> findAll();
    List<Papel> findAll(int page, int size);
    List<Papel> findByCodigo(String codigo);
    List<Papel> findByTipo(String tipo);
    List<Papel> findByVencimento(LocalDate vencimento);
    List<Papel> search(Papel f);
    List<Papel> search(Papel f, int page, int size);
}