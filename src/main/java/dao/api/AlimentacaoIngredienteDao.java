package dao.api;

import model.AlimentacaoIngrediente;
import java.util.List;

public interface AlimentacaoIngredienteDao {
    void create(AlimentacaoIngrediente e);
    void update(AlimentacaoIngrediente e);
    void deleteById(Integer id);
    AlimentacaoIngrediente findById(Integer id);
    List<AlimentacaoIngrediente> findAll();
    List<AlimentacaoIngrediente> findAll(int page, int size);
    List<AlimentacaoIngrediente> findByQuantidade(Integer quantidade);
    List<AlimentacaoIngrediente> findByIdAlimentacao(Integer idAlimentacao);
    List<AlimentacaoIngrediente> findByIdIngrediente(Integer idIngrediente);
    List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f);
    List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f, int page, int size);
}