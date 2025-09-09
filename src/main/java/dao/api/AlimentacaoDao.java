// path: src/main/java/dao/api/AlimentacaoDao.java
package dao.api;

import java.util.List;

import exception.AlimentacaoException;
import model.Alimentacao;

public interface AlimentacaoDao {

    void create(Alimentacao alimentacao) throws AlimentacaoException;

    Alimentacao update(Alimentacao alimentacao) throws AlimentacaoException;

    void deleteById(Integer id) throws AlimentacaoException;

    Alimentacao findById(Integer id) throws AlimentacaoException;

    Alimentacao findWithVideoById(Integer id) throws AlimentacaoException;

    List<Alimentacao> findAll();

    List<Alimentacao> findAll(int page, int size);

    List<Alimentacao> findByStatus(Integer status);

    List<Alimentacao> findByNome(String nome);

    List<Alimentacao> findByLink(String link);

    List<Alimentacao> findByVideo(byte[] video);

    List<Alimentacao> findByPreparo(String preparo);

    List<Alimentacao> findByIdRotina(Integer idRotina);

    List<Alimentacao> search(Alimentacao filtro);

    List<Alimentacao> search(Alimentacao filtro, int page, int size);
}
