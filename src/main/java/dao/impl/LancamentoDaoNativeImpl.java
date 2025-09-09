// path: src/main/java/dao/impl/LancamentoDaoNativeImpl.java
package dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.LancamentoDao;
import exception.LancamentoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Lancamento;

public class LancamentoDaoNativeImpl implements LancamentoDao {

    @Override
    public void create(Lancamento lancamento) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Lancamento (valor, fixo, data_pagamento, status, id_movimentacao, id_evento) " +
                    "VALUES (:valor, :fixo, :dataPagamento, :status, CAST(:idMovimentacao AS INTEGER), CAST(:idEvento AS INTEGER))";
            Query query = em.createNativeQuery(sql);
            query.setParameter("valor", lancamento.getValor());
            query.setParameter("fixo", lancamento.getFixo());
            query.setParameter("dataPagamento", lancamento.getDataPagamento());
            query.setParameter("status", lancamento.getStatus());
            query.setParameter("idMovimentacao", lancamento.getMovimentacao() != null ? lancamento.getMovimentacao().getIdMovimentacao() : null);
            query.setParameter("idEvento", lancamento.getEvento() != null ? lancamento.getEvento().getIdEvento() : null);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("LancamentoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("LancamentoDaoNativeImpl.create - erro", e);
            throw new LancamentoException("Erro ao criar Lancamento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Lancamento update(Lancamento lancamento) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Lancamento SET valor=:valor, fixo=:fixo, data_pagamento=:dataPagamento, status=:status, " +
                    "id_movimentacao=CAST(:idMovimentacao AS INTEGER), id_evento=CAST(:idEvento AS INTEGER) WHERE id_lancamento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("valor", lancamento.getValor());
            query.setParameter("fixo", lancamento.getFixo());
            query.setParameter("dataPagamento", lancamento.getDataPagamento());
            query.setParameter("status", lancamento.getStatus());
            query.setParameter("idMovimentacao", lancamento.getMovimentacao() != null ? lancamento.getMovimentacao().getIdMovimentacao() : null);
            query.setParameter("idEvento", lancamento.getEvento() != null ? lancamento.getEvento().getIdEvento() : null);
            query.setParameter("id", lancamento.getIdLancamento());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new LancamentoException("Lancamento não encontrado: id=" + lancamento.getIdLancamento());
            }
            em.getTransaction().commit();
            Logger.info("LancamentoDaoNativeImpl.update - sucesso");
            return findById(lancamento.getIdLancamento());
        } catch (LancamentoException e) {
            em.getTransaction().rollback();
            Logger.error("LancamentoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("LancamentoDaoNativeImpl.update - erro", e);
            throw new LancamentoException("Erro ao atualizar Lancamento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Lancamento WHERE id_lancamento=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new LancamentoException("Lancamento não encontrado: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("LancamentoDaoNativeImpl.deleteById - sucesso");
        } catch (LancamentoException e) {
            em.getTransaction().rollback();
            Logger.error("LancamentoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("LancamentoDaoNativeImpl.deleteById - erro", e);
            throw new LancamentoException("Erro ao deletar Lancamento", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Lancamento findById(Integer id) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_lancamento=:id";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("id", id);
            Lancamento l = (Lancamento) query.getSingleResult();
            Logger.info("LancamentoDaoNativeImpl.findById - sucesso");
            return l;
        } catch (Exception e) {
            Logger.error("LancamentoDaoNativeImpl.findById - erro", e);
            throw new LancamentoException("Lancamento não encontrado: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findAll() {
        Logger.info("LancamentoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findAll(int page, int size) {
        Logger.info("LancamentoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByValor(java.math.BigDecimal valor) {
        Logger.info("LancamentoDaoNativeImpl.findByValor - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE valor=:valor";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("valor", valor);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByValor - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByFixo(Boolean fixo) {
        Logger.info("LancamentoDaoNativeImpl.findByFixo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE fixo=:fixo";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("fixo", fixo);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByFixo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByDataPagamento(java.time.LocalDate dataPagamento) {
        Logger.info("LancamentoDaoNativeImpl.findByDataPagamento - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE data_pagamento=:dataPagamento";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("dataPagamento", dataPagamento);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByDataPagamento - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByStatus(Integer status) {
        Logger.info("LancamentoDaoNativeImpl.findByStatus - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE status=:status";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("status", status);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByStatus - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByIdMovimentacao(Integer idMovimentacao) {
        Logger.info("LancamentoDaoNativeImpl.findByIdMovimentacao - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_movimentacao=:idMovimentacao";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("idMovimentacao", idMovimentacao);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByIdMovimentacao - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> findByIdEvento(Integer idEvento) {
        Logger.info("LancamentoDaoNativeImpl.findByIdEvento - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_evento=:idEvento";
            Query query = em.createNativeQuery(sql, Lancamento.class);
            query.setParameter("idEvento", idEvento);
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.findByIdEvento - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Lancamento> search(Lancamento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Lancamento> search(Lancamento filtro, int page, int size) {
        Logger.info("LancamentoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getValor() != null) {
                sb.append(" AND valor=:valor");
                params.put("valor", filtro.getValor());
            }
            if (filtro.getFixo() != null) {
                sb.append(" AND fixo=:fixo");
                params.put("fixo", filtro.getFixo());
            }
            if (filtro.getDataPagamento() != null) {
                sb.append(" AND data_pagamento=:dataPagamento");
                params.put("dataPagamento", filtro.getDataPagamento());
            }
            if (filtro.getStatus() != null) {
                sb.append(" AND status=:status");
                params.put("status", filtro.getStatus());
            }
            if (filtro.getMovimentacao() != null && filtro.getMovimentacao().getIdMovimentacao() != null) {
                sb.append(" AND id_movimentacao=:idMovimentacao");
                params.put("idMovimentacao", filtro.getMovimentacao().getIdMovimentacao());
            }
            if (filtro.getEvento() != null && filtro.getEvento().getIdEvento() != null) {
                sb.append(" AND id_evento=:idEvento");
                params.put("idEvento", filtro.getEvento().getIdEvento());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Lancamento.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Lancamento> list = query.getResultList();
            Logger.info("LancamentoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
