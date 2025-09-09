package dao.api;

import model.Cofre;
import java.util.List;

public interface CofreDao {
    void create(Cofre c);
    void update(Cofre c);
    void deleteById(Integer id);
    Cofre findById(Integer id);
    List<Cofre> findAll();
    List<Cofre> findAll(int page, int size);
    List<Cofre> findByLogin(String login);
    List<Cofre> findByTipo(Integer tipo);
    List<Cofre> findByIdUsuario(Integer idUsuario);
    List<Cofre> search(Cofre filtro);
    List<Cofre> search(Cofre filtro, int page, int size);
}
