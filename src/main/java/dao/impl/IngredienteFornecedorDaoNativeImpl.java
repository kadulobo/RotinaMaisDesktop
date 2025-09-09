package dao.impl;

import dao.api.IngredienteFornecedorDao;
import exception.IngredienteFornecedorException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.IngredienteFornecedor;

import java.util.List;

public class IngredienteFornecedorDaoNativeImpl implements IngredienteFornecedorDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(IngredienteFornecedor e) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(IngredienteFornecedor e) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.deleteById");
        IngredienteFornecedor e = em.find(IngredienteFornecedor.class, id);
        if (e == null) throw new IngredienteFornecedorException("IngredienteFornecedor nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public IngredienteFornecedor findById(Integer id) {
        String sql = "SELECT {select_cols} FROM {table} WHERE {e['fields'][0][2]} = :id";
        Query q = em.createNativeQuery(sql, {cname}.class).setParameter("id", id);
        return ({cname}) q.getSingleResult();
    }

    @Override
    public List<IngredienteFornecedor> findAll() {
        String sql = "SELECT {select_cols} FROM {table}";
        return em.createNativeQuery(sql, {cname}.class).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> findAll(int page, int size) {
        String sql = "SELECT {select_cols} FROM {table} LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, {cname}.class).setParameter("size", size).setParameter("off", page * size).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> findByValor(BigDecimal valor) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE valor = :v";
        return em.createNativeQuery(sql, IngredienteFornecedor.class).setParameter("v", valor).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> findByData(LocalDate data) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE data = :v";
        return em.createNativeQuery(sql, IngredienteFornecedor.class).setParameter("v", data).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> findByIdFornecedor(Integer idFornecedor) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE id_fornecedor = :v";
        return em.createNativeQuery(sql, IngredienteFornecedor.class).setParameter("v", idFornecedor).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> findByIdIngrediente(Integer idIngrediente) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE id_ingrediente = :v";
        return em.createNativeQuery(sql, IngredienteFornecedor.class).setParameter("v", idIngrediente).getResultList();
    }

    @Override
    public List<IngredienteFornecedor> search(IngredienteFornecedor f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<IngredienteFornecedor> search(IngredienteFornecedor f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE 1=1");
        if (f.getValor() != null) sb.append(" AND valor = :valor");
        if (f.getData() != null) sb.append(" AND data = :data");
        if (f.getIdFornecedor() != null) sb.append(" AND id_fornecedor = :idFornecedor");
        if (f.getIdIngrediente() != null) sb.append(" AND id_ingrediente = :idIngrediente");
        Query q = em.createNativeQuery(sb.toString(), IngredienteFornecedor.class);
        if (f.getValor() != null) q.setParameter("valor", f.getValor());
        if (f.getData() != null) q.setParameter("data", f.getData());
        if (f.getIdFornecedor() != null) q.setParameter("idFornecedor", f.getIdFornecedor());
        if (f.getIdIngrediente() != null) q.setParameter("idIngrediente", f.getIdIngrediente());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}