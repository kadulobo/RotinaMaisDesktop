package dao.api;

import model.Carteira;
import java.util.List;

public interface CarteiraDao {
    void create(Carteira e);
    void update(Carteira e);
    void deleteById(Integer id);
    Carteira findById(Integer id);
    List<Carteira> findAll();
    List<Carteira> findAll(int page, int size);
    List<Carteira> findByNome(String nome);
    List<Carteira> findByTipo(String tipo);
    List<Carteira> findByDataInicio(LocalDate dataInicio);
    List<Carteira> findByIdUsuario(Integer idUsuario);
    List<Carteira> search(Carteira f);
    List<Carteira> search(Carteira f, int page, int size);
}