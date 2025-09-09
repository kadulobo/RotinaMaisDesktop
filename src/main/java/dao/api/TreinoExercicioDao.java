package dao.api;

import model.TreinoExercicio;
import java.util.List;

public interface TreinoExercicioDao {
    void create(TreinoExercicio e);
    void update(TreinoExercicio e);
    void deleteById(Integer id);
    TreinoExercicio findById(Integer id);
    List<TreinoExercicio> findAll();
    List<TreinoExercicio> findAll(int page, int size);
    List<TreinoExercicio> findByQtdRepeticao(Integer qtdRepeticao);
    List<TreinoExercicio> findByTempoDescanso(String tempoDescanso);
    List<TreinoExercicio> findByOrdem(Integer ordem);
    List<TreinoExercicio> findByFeito(Boolean feito);
    List<TreinoExercicio> findByIdExercicio(Integer idExercicio);
    List<TreinoExercicio> findByIdTreino(Integer idTreino);
    List<TreinoExercicio> search(TreinoExercicio f);
    List<TreinoExercicio> search(TreinoExercicio f, int page, int size);
}