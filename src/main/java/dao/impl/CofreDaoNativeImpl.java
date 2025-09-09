package dao.impl;

import dao.api.CofreDao;
import exception.CofreException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Cofre;

import java.util.List;

public class CofreDaoNativeImpl implements CofreDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(Cofre c) {
        Logger.info("CofreDao.create");
        em.getTransaction().begin();
        c.setIdCofre(null);
        em.persist(c);
        em.getTransaction().commit();
    }

    @Override
    public void update(Cofre c) {
        Logger.info("CofreDao.update");
        em.getTransaction().begin();
        em.merge(c);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("CofreDao.deleteById");
        Cofre c = em.find(Cofre.class, id);
        if (c == null) throw new CofreException("Cofre nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
    }

    @Override
    public Cofre findById(Integer id) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE id_cofre = :id";
        Query q = em.createNativeQuery(sql, Cofre.class).setParameter("id", id);
        return (Cofre) q.getSingleResult();
    }

    @Override
    public List<Cofre> findAll() {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre";
        return em.createNativeQuery(sql, Cofre.class).getResultList();
    }

    @Override
    public List<Cofre> findAll(int page, int size) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, Cofre.class)
                .setParameter("size", size)
                .setParameter("off", page * size)
                .getResultList();
    }

    @Override
    public List<Cofre> findByLogin(String login) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE login = :v";
        return em.createNativeQuery(sql, Cofre.class).setParameter("v", login).getResultList();
    }

    @Override
    public List<Cofre> findByTipo(Integer tipo) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE tipo = :v";
        return em.createNativeQuery(sql, Cofre.class).setParameter("v", tipo).getResultList();
    }

    @Override
    public List<Cofre> findByIdUsuario(Integer idUsuario) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE id_usuario = :v";
        return em.createNativeQuery(sql, Cofre.class).setParameter("v", idUsuario).getResultList();
    }

    @Override
    public List<Cofre> search(Cofre f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Cofre> search(Cofre f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE 1=1");
        if (f.getLogin() != null && !f.getLogin().isEmpty()) sb.append(" AND login = :login");
        if (f.getTipo() != null) sb.append(" AND tipo = :tipo");
        if (f.getIdUsuario() != null) sb.append(" AND id_usuario = :idUsuario");
        Query q = em.createNativeQuery(sb.toString(), Cofre.class);
        if (f.getLogin() != null && !f.getLogin().isEmpty()) q.setParameter("login", f.getLogin());
        if (f.getTipo() != null) q.setParameter("tipo", f.getTipo());
        if (f.getIdUsuario() != null) q.setParameter("idUsuario", f.getIdUsuario());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}
