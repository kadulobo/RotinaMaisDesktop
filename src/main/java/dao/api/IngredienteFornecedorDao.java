package dao.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import model.IngredienteFornecedor;

public interface IngredienteFornecedorDao {
    void create(IngredienteFornecedor e);
    void update(IngredienteFornecedor e);
    void deleteById(Integer id);
    IngredienteFornecedor findById(Integer id);
    List<IngredienteFornecedor> findAll();
    List<IngredienteFornecedor> findAll(int page, int size);
    List<IngredienteFornecedor> findByValor(BigDecimal valor);
    List<IngredienteFornecedor> findByData(LocalDate data);
    List<IngredienteFornecedor> findByIdFornecedor(Integer idFornecedor);
    List<IngredienteFornecedor> findByIdIngrediente(Integer idIngrediente);
    List<IngredienteFornecedor> search(IngredienteFornecedor f);
    List<IngredienteFornecedor> search(IngredienteFornecedor f, int page, int size);
}