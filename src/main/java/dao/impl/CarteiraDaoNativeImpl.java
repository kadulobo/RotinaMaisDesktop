package dao.impl;

import dao.api.CarteiraDao;
import exception.CarteiraException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Carteira;

import java.time.LocalDate;
import java.util.List;

public class CarteiraDaoNativeImpl implements CarteiraDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(Carteira e) {
        Logger.info("CarteiraDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(Carteira e) {
        Logger.info("CarteiraDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("CarteiraDaoNativeImpl.deleteById");
        Carteira e = em.find(Carteira.class, id);
        if (e == null) throw new CarteiraException("Carteira nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public Carteira findById(Integer id) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE id_carteira = :id";
        Query q = em.createNativeQuery(sql, Carteira.class).setParameter("id", id);
        return (Carteira) q.getSingleResult();
    }

    @Override
    public List<Carteira> findAll() {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira";
        return em.createNativeQuery(sql, Carteira.class).getResultList();
    }

    @Override
    public List<Carteira> findAll(int page, int size) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, Carteira.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<Carteira> findByNome(String nome) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE nome = :v";
        return em.createNativeQuery(sql, Carteira.class).setParameter("v", nome).getResultList();
    }

    @Override
    public List<Carteira> findByTipo(String tipo) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE tipo = :v";
        return em.createNativeQuery(sql, Carteira.class).setParameter("v", tipo).getResultList();
    }

    @Override
    public List<Carteira> findByDataInicio(LocalDate dataInicio) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE dataInicio = :v";
        return em.createNativeQuery(sql, Carteira.class).setParameter("v", dataInicio).getResultList();
    }

    @Override
    public List<Carteira> findByIdUsuario(Integer idUsuario) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE id_usuario = :v";
        return em.createNativeQuery(sql, Carteira.class).setParameter("v", idUsuario).getResultList();
    }

    @Override
    public List<Carteira> search(Carteira f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Carteira> search(Carteira f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE 1=1");
        if (f.getNome() != null && !f.getNome().isEmpty()) sb.append(" AND nome = :nome");
        if (f.getTipo() != null && !f.getTipo().isEmpty()) sb.append(" AND tipo = :tipo");
        if (f.getDataInicio() != null) sb.append(" AND dataInicio = :dataInicio");
        if (f.getIdUsuario() != null) sb.append(" AND id_usuario = :idUsuario");
        Query q = em.createNativeQuery(sb.toString(), Carteira.class);
        if (f.getNome() != null && !f.getNome().isEmpty()) q.setParameter("nome", f.getNome());
        if (f.getTipo() != null && !f.getTipo().isEmpty()) q.setParameter("tipo", f.getTipo());
        if (f.getDataInicio() != null) q.setParameter("dataInicio", f.getDataInicio());
        if (f.getIdUsuario() != null) q.setParameter("idUsuario", f.getIdUsuario());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}