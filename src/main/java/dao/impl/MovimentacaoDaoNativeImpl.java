// path: src/main/java/dao/impl/MovimentacaoDaoNativeImpl.java
package dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.MovimentacaoDao;
import exception.MovimentacaoException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Movimentacao;

public class MovimentacaoDaoNativeImpl implements MovimentacaoDao {

    @Override
    public void create(Movimentacao movimentacao) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "INSERT INTO Movimentacao (id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo) " +
                    "VALUES (:id, :desconto, :vantagem, :liquido, :tipo, :status, :ponto, :idUsuario, :idCaixa, :idPeriodo)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", movimentacao.getIdMovimentacao());
            query.setParameter("desconto", movimentacao.getDesconto());
            query.setParameter("vantagem", movimentacao.getVantagem());
            query.setParameter("liquido", movimentacao.getLiquido());
            query.setParameter("tipo", movimentacao.getTipo());
            query.setParameter("status", movimentacao.getStatus());
            query.setParameter("ponto", movimentacao.getPonto());
            query.setParameter("idUsuario", movimentacao.getUsuario() != null ? movimentacao.getUsuario().getIdUsuario() : null);
            query.setParameter("idCaixa", movimentacao.getCaixa() != null ? movimentacao.getCaixa().getIdCaixa() : null);
            query.setParameter("idPeriodo", movimentacao.getPeriodo() != null ? movimentacao.getPeriodo().getIdPeriodo() : null);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("MovimentacaoDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MovimentacaoDaoNativeImpl.create - erro", e);
            throw new MovimentacaoException("Erro ao criar Movimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Movimentacao update(Movimentacao movimentacao) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "UPDATE Movimentacao SET desconto=:desconto, vantagem=:vantagem, liquido=:liquido, tipo=:tipo, status=:status, ponto=:ponto, id_usuario=:idUsuario, id_caixa=:idCaixa, id_periodo=:idPeriodo WHERE id_movimentacao=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("desconto", movimentacao.getDesconto());
            query.setParameter("vantagem", movimentacao.getVantagem());
            query.setParameter("liquido", movimentacao.getLiquido());
            query.setParameter("tipo", movimentacao.getTipo());
            query.setParameter("status", movimentacao.getStatus());
            query.setParameter("ponto", movimentacao.getPonto());
            query.setParameter("idUsuario", movimentacao.getUsuario() != null ? movimentacao.getUsuario().getIdUsuario() : null);
            query.setParameter("idCaixa", movimentacao.getCaixa() != null ? movimentacao.getCaixa().getIdCaixa() : null);
            query.setParameter("idPeriodo", movimentacao.getPeriodo() != null ? movimentacao.getPeriodo().getIdPeriodo() : null);
            query.setParameter("id", movimentacao.getIdMovimentacao());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new MovimentacaoException("Movimentacao não encontrada: id=" + movimentacao.getIdMovimentacao());
            }
            em.getTransaction().commit();
            Logger.info("MovimentacaoDaoNativeImpl.update - sucesso");
            return findById(movimentacao.getIdMovimentacao());
        } catch (MovimentacaoException e) {
            em.getTransaction().rollback();
            Logger.error("MovimentacaoDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MovimentacaoDaoNativeImpl.update - erro", e);
            throw new MovimentacaoException("Erro ao atualizar Movimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Movimentacao WHERE id_movimentacao=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new MovimentacaoException("Movimentacao não encontrada: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("MovimentacaoDaoNativeImpl.deleteById - sucesso");
        } catch (MovimentacaoException e) {
            em.getTransaction().rollback();
            Logger.error("MovimentacaoDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("MovimentacaoDaoNativeImpl.deleteById - erro", e);
            throw new MovimentacaoException("Erro ao deletar Movimentacao", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Movimentacao findById(Integer id) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_movimentacao=:id";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("id", id);
            Movimentacao m = (Movimentacao) query.getSingleResult();
            Logger.info("MovimentacaoDaoNativeImpl.findById - sucesso");
            return m;
        } catch (Exception e) {
            Logger.error("MovimentacaoDaoNativeImpl.findById - erro", e);
            throw new MovimentacaoException("Movimentacao não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findAll() {
        Logger.info("MovimentacaoDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findAll(int page, int size) {
        Logger.info("MovimentacaoDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByDesconto(BigDecimal desconto) {
        Logger.info("MovimentacaoDaoNativeImpl.findByDesconto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE desconto=:desconto";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("desconto", desconto);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByDesconto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByVantagem(BigDecimal vantagem) {
        Logger.info("MovimentacaoDaoNativeImpl.findByVantagem - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE vantagem=:vantagem";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("vantagem", vantagem);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByVantagem - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByLiquido(BigDecimal liquido) {
        Logger.info("MovimentacaoDaoNativeImpl.findByLiquido - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE liquido=:liquido";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("liquido", liquido);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByLiquido - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByTipo(Integer tipo) {
        Logger.info("MovimentacaoDaoNativeImpl.findByTipo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE tipo=:tipo";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("tipo", tipo);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByTipo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByStatus(Integer status) {
        Logger.info("MovimentacaoDaoNativeImpl.findByStatus - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE status=:status";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("status", status);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByStatus - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByPonto(Integer ponto) {
        Logger.info("MovimentacaoDaoNativeImpl.findByPonto - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE ponto=:ponto";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("ponto", ponto);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByPonto - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByIdUsuario(Integer idUsuario) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdUsuario - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_usuario=:idUsuario";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("idUsuario", idUsuario);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByIdUsuario - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByIdCaixa(Integer idCaixa) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdCaixa - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_caixa=:idCaixa";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("idCaixa", idCaixa);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByIdCaixa - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> findByIdPeriodo(Integer idPeriodo) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdPeriodo - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_periodo=:idPeriodo";
            Query query = em.createNativeQuery(sql, Movimentacao.class);
            query.setParameter("idPeriodo", idPeriodo);
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.findByIdPeriodo - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Movimentacao> search(Movimentacao filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Movimentacao> search(Movimentacao filtro, int page, int size) {
        Logger.info("MovimentacaoDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getDesconto() != null) {
                sb.append(" AND desconto=:desconto");
                params.put("desconto", filtro.getDesconto());
            }
            if (filtro.getVantagem() != null) {
                sb.append(" AND vantagem=:vantagem");
                params.put("vantagem", filtro.getVantagem());
            }
            if (filtro.getLiquido() != null) {
                sb.append(" AND liquido=:liquido");
                params.put("liquido", filtro.getLiquido());
            }
            if (filtro.getTipo() != null) {
                sb.append(" AND tipo=:tipo");
                params.put("tipo", filtro.getTipo());
            }
            if (filtro.getStatus() != null) {
                sb.append(" AND status=:status");
                params.put("status", filtro.getStatus());
            }
            if (filtro.getPonto() != null) {
                sb.append(" AND ponto=:ponto");
                params.put("ponto", filtro.getPonto());
            }
            if (filtro.getUsuario() != null && filtro.getUsuario().getIdUsuario() != null) {
                sb.append(" AND id_usuario=:idUsuario");
                params.put("idUsuario", filtro.getUsuario().getIdUsuario());
            }
            if (filtro.getCaixa() != null && filtro.getCaixa().getIdCaixa() != null) {
                sb.append(" AND id_caixa=:idCaixa");
                params.put("idCaixa", filtro.getCaixa().getIdCaixa());
            }
            if (filtro.getPeriodo() != null && filtro.getPeriodo().getIdPeriodo() != null) {
                sb.append(" AND id_periodo=:idPeriodo");
                params.put("idPeriodo", filtro.getPeriodo().getIdPeriodo());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Movimentacao.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Movimentacao> list = query.getResultList();
            Logger.info("MovimentacaoDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
