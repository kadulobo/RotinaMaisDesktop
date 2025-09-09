package dao.impl;

import dao.api.PapelDao;
import exception.PapelException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Papel;

import java.time.LocalDate;
import java.util.List;

public class PapelDaoNativeImpl implements PapelDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(Papel e) {
        Logger.info("PapelDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(Papel e) {
        Logger.info("PapelDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("PapelDaoNativeImpl.deleteById");
        Papel e = em.find(Papel.class, id);
        if (e == null) throw new PapelException("Papel nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public Papel findById(Integer id) {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE id_papel = :id";
        Query q = em.createNativeQuery(sql, Papel.class).setParameter("id", id);
        return (Papel) q.getSingleResult();
    }

    @Override
    public List<Papel> findAll() {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel";
        return em.createNativeQuery(sql, Papel.class).getResultList();
    }

    @Override
    public List<Papel> findAll(int page, int size) {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, Papel.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<Papel> findByCodigo(String codigo) {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE codigo = :v";
        return em.createNativeQuery(sql, Papel.class).setParameter("v", codigo).getResultList();
    }

    @Override
    public List<Papel> findByTipo(String tipo) {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE tipo = :v";
        return em.createNativeQuery(sql, Papel.class).setParameter("v", tipo).getResultList();
    }

    @Override
    public List<Papel> findByVencimento(LocalDate vencimento) {
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE vencimento = :v";
        return em.createNativeQuery(sql, Papel.class).setParameter("v", vencimento).getResultList();
    }

    @Override
    public List<Papel> search(Papel f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Papel> search(Papel f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE 1=1");
        if (f.getCodigo() != null && !f.getCodigo().isEmpty()) sb.append(" AND codigo = :codigo");
        if (f.getTipo() != null && !f.getTipo().isEmpty()) sb.append(" AND tipo = :tipo");
        if (f.getVencimento() != null) sb.append(" AND vencimento = :vencimento");
        Query q = em.createNativeQuery(sb.toString(), Papel.class);
        if (f.getCodigo() != null && !f.getCodigo().isEmpty()) q.setParameter("codigo", f.getCodigo());
        if (f.getTipo() != null && !f.getTipo().isEmpty()) q.setParameter("tipo", f.getTipo());
        if (f.getVencimento() != null) q.setParameter("vencimento", f.getVencimento());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}