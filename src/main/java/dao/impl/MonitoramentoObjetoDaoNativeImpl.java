package dao.impl;

import dao.api.MonitoramentoObjetoDao;
import exception.MonitoramentoObjetoException;
import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.MonitoramentoObjeto;

import java.util.List;

public class MonitoramentoObjetoDaoNativeImpl implements MonitoramentoObjetoDao {
    private final EntityManager em = EntityManagerUtil.getEntityManager();

    @Override
    public void create(MonitoramentoObjeto e) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.create");
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
    }

    @Override
    public void update(MonitoramentoObjeto e) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.update");
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(LocalDate id) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.deleteById");
        MonitoramentoObjeto e = em.find(MonitoramentoObjeto.class, id);
        if (e == null) throw new MonitoramentoObjetoException("MonitoramentoObjeto nao encontrado: id=" + id);
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
    }

    @Override
    public MonitoramentoObjeto findById(LocalDate id) {
        String sql = "SELECT {select_cols} FROM {table} WHERE {e['fields'][0][2]} = :id";
        Query q = em.createNativeQuery(sql, {cname}.class).setParameter("id", id);
        return ({cname}) q.getSingleResult();
    }

    @Override
    public List<MonitoramentoObjeto> findAll() {
        String sql = "SELECT {select_cols} FROM {table}";
        return em.createNativeQuery(sql, {cname}.class).getResultList();
    }

    @Override
    public List<MonitoramentoObjeto> findAll(int page, int size) {
        String sql = "SELECT {select_cols} FROM {table} LIMIT :size OFFSET :off";
        return em.createNativeQuery(sql, {cname}.class).setParameter("size", size).setParameter("off", page * size).getResultList();
    }

    @Override
    public List<MonitoramentoObjeto> findByData(LocalDate data) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE data = :v";
        return em.createNativeQuery(sql, MonitoramentoObjeto.class).setParameter("v", data).getResultList();
    }

    @Override
    public List<MonitoramentoObjeto> findByIdMonitoramento(Integer idMonitoramento) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE id_monitoramento = :v";
        return em.createNativeQuery(sql, MonitoramentoObjeto.class).setParameter("v", idMonitoramento).getResultList();
    }

    @Override
    public List<MonitoramentoObjeto> findByIdObjeto(Integer idObjeto) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE id_objeto = :v";
        return em.createNativeQuery(sql, MonitoramentoObjeto.class).setParameter("v", idObjeto).getResultList();
    }

    @Override
    public List<MonitoramentoObjeto> search(MonitoramentoObjeto f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<MonitoramentoObjeto> search(MonitoramentoObjeto f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE 1=1");
        if (f.getData() != null) sb.append(" AND data = :data");
        if (f.getIdMonitoramento() != null) sb.append(" AND id_monitoramento = :idMonitoramento");
        if (f.getIdObjeto() != null) sb.append(" AND id_objeto = :idObjeto");
        Query q = em.createNativeQuery(sb.toString(), MonitoramentoObjeto.class);
        if (f.getData() != null) q.setParameter("data", f.getData());
        if (f.getIdMonitoramento() != null) q.setParameter("idMonitoramento", f.getIdMonitoramento());
        if (f.getIdObjeto() != null) q.setParameter("idObjeto", f.getIdObjeto());
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }
}