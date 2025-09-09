package dao.impl;

import dao.api.AlimentacaoIngredienteDao;
import exception.AlimentacaoIngredienteException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.AlimentacaoIngrediente;

import java.util.List;

public class AlimentacaoIngredienteDaoNativeImpl implements AlimentacaoIngredienteDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(AlimentacaoIngrediente e) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(AlimentacaoIngrediente e) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.deleteById");
        AlimentacaoIngrediente e = em.find(AlimentacaoIngrediente.class, id);
        if (e == null) throw new AlimentacaoIngredienteException("AlimentacaoIngrediente nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public AlimentacaoIngrediente findById(Integer id) {
        String sql = "SELECT {select_cols} FROM {table} WHERE {e['fields'][0][2]} = :id";
        Query q = em.createNativeQuery(sql, {cname}.class).setParameter("id", id);
        return ({cname}) q.getSingleResult();
    }

    @Override
    public List<AlimentacaoIngrediente> findAll() {
        String sql = "SELECT {select_cols} FROM {table}";
        return em.createNativeQuery(sql, {cname}.class).getResultList();
    }

    @Override
    public List<AlimentacaoIngrediente> findAll(int page, int size) {
        String sql = "SELECT {select_cols} FROM {table} LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, {cname}.class).setParameter("size", size).setParameter("off", page * size).getResultList();
    }

    @Override
    public List<AlimentacaoIngrediente> findByQuantidade(Integer quantidade) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE quantidade = :v";
        return em.createNativeQuery(sql, AlimentacaoIngrediente.class).setParameter("v", quantidade).getResultList();
    }

    @Override
    public List<AlimentacaoIngrediente> findByIdAlimentacao(Integer idAlimentacao) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE id_alimentacao = :v";
        return em.createNativeQuery(sql, AlimentacaoIngrediente.class).setParameter("v", idAlimentacao).getResultList();
    }

    @Override
    public List<AlimentacaoIngrediente> findByIdIngrediente(Integer idIngrediente) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE id_ingrediente = :v";
        return em.createNativeQuery(sql, AlimentacaoIngrediente.class).setParameter("v", idIngrediente).getResultList();
    }

    @Override
    public List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE 1=1");
        if (f.getQuantidade() != null) sb.append(" AND quantidade = :quantidade");
        if (f.getIdAlimentacao() != null) sb.append(" AND id_alimentacao = :idAlimentacao");
        if (f.getIdIngrediente() != null) sb.append(" AND id_ingrediente = :idIngrediente");
        Query q = em.createNativeQuery(sb.toString(), AlimentacaoIngrediente.class);
        if (f.getQuantidade() != null) q.setParameter("quantidade", f.getQuantidade());
        if (f.getIdAlimentacao() != null) q.setParameter("idAlimentacao", f.getIdAlimentacao());
        if (f.getIdIngrediente() != null) q.setParameter("idIngrediente", f.getIdIngrediente());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}