// path: src/main/java/dao/impl/CaixaDaoNativeImpl.java
package dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.api.CaixaDao;
import exception.CaixaException;
import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Caixa;

public class CaixaDaoNativeImpl implements CaixaDao {

    @Override
    public void create(Caixa caixa) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.create - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Postgres não consegue inferir o tipo de um parâmetro nulo automaticamente.
            // Ao efetuar o CAST para INTEGER garantimos que o JDBC defina o tipo correto
            // mesmo quando o id do usuário for nulo, evitando o erro "column is of type integer but expression is of type bytea".
            String sql = "INSERT INTO Caixa (nome, reserva_emergencia, salario_medio, valor_total, id_usuario) VALUES (:nome, :reserva, :salario, :valor, CAST(:idUsuario AS INTEGER))";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", caixa.getNome());
            query.setParameter("reserva", caixa.getReservaEmergencia());
            query.setParameter("salario", caixa.getSalarioMedio());
            query.setParameter("valor", caixa.getValorTotal());
            query.setParameter("idUsuario", caixa.getUsuario() != null ? caixa.getUsuario().getIdUsuario() : null);
            query.executeUpdate();
            em.getTransaction().commit();
            Logger.info("CaixaDaoNativeImpl.create - sucesso");
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CaixaDaoNativeImpl.create - erro", e);
            throw new CaixaException("Erro ao criar Caixa", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Caixa update(Caixa caixa) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.update - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Aplicar o mesmo CAST na atualização para evitar problemas de tipagem quando o usuário for nulo
            String sql = "UPDATE Caixa SET nome=:nome, reserva_emergencia=:reserva, salario_medio=:salario, valor_total=:valor, id_usuario=CAST(:idUsuario AS INTEGER) WHERE id_caixa=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("nome", caixa.getNome());
            query.setParameter("reserva", caixa.getReservaEmergencia());
            query.setParameter("salario", caixa.getSalarioMedio());
            query.setParameter("valor", caixa.getValorTotal());
            query.setParameter("idUsuario", caixa.getUsuario() != null ? caixa.getUsuario().getIdUsuario() : null);
            query.setParameter("id", caixa.getIdCaixa());
            int updated = query.executeUpdate();
            if (updated == 0) {
                throw new CaixaException("Caixa não encontrada: id=" + caixa.getIdCaixa());
            }
            em.getTransaction().commit();
            Logger.info("CaixaDaoNativeImpl.update - sucesso");
            return findById(caixa.getIdCaixa());
        } catch (CaixaException e) {
            em.getTransaction().rollback();
            Logger.error("CaixaDaoNativeImpl.update - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CaixaDaoNativeImpl.update - erro", e);
            throw new CaixaException("Erro ao atualizar Caixa", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.deleteById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String sql = "DELETE FROM Caixa WHERE id_caixa=:id";
            Query query = em.createNativeQuery(sql);
            query.setParameter("id", id);
            int deleted = query.executeUpdate();
            if (deleted == 0) {
                throw new CaixaException("Caixa não encontrada: id=" + id);
            }
            em.getTransaction().commit();
            Logger.info("CaixaDaoNativeImpl.deleteById - sucesso");
        } catch (CaixaException e) {
            em.getTransaction().rollback();
            Logger.error("CaixaDaoNativeImpl.deleteById - erro", e);
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            Logger.error("CaixaDaoNativeImpl.deleteById - erro", e);
            throw new CaixaException("Erro ao deletar Caixa", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Caixa findById(Integer id) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.findById - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE id_caixa=:id";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("id", id);
            Caixa c = (Caixa) query.getSingleResult();
            Logger.info("CaixaDaoNativeImpl.findById - sucesso");
            return c;
        } catch (Exception e) {
            Logger.error("CaixaDaoNativeImpl.findById - erro", e);
            throw new CaixaException("Caixa não encontrada: id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findAll() {
        Logger.info("CaixaDaoNativeImpl.findAll - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa";
            Query query = em.createNativeQuery(sql, Caixa.class);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findAll - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findAll(int page, int size) {
        Logger.info("CaixaDaoNativeImpl.findAll(page) - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa LIMIT :limit OFFSET :offset";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("limit", size);
            query.setParameter("offset", page * size);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findAll(page) - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findByNome(String nome) {
        Logger.info("CaixaDaoNativeImpl.findByNome - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE nome=:nome";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("nome", nome);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findByNome - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findByReservaEmergencia(BigDecimal reservaEmergencia) {
        Logger.info("CaixaDaoNativeImpl.findByReservaEmergencia - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE reserva_emergencia=:reserva";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("reserva", reservaEmergencia);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findByReservaEmergencia - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findBySalarioMedio(BigDecimal salarioMedio) {
        Logger.info("CaixaDaoNativeImpl.findBySalarioMedio - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE salario_medio=:salario";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("salario", salarioMedio);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findBySalarioMedio - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findByValorTotal(BigDecimal valorTotal) {
        Logger.info("CaixaDaoNativeImpl.findByValorTotal - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE valor_total=:valor";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("valor", valorTotal);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findByValorTotal - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> findByIdUsuario(Integer idUsuario) {
        Logger.info("CaixaDaoNativeImpl.findByIdUsuario - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE id_usuario=:idUsuario";
            Query query = em.createNativeQuery(sql, Caixa.class);
            query.setParameter("idUsuario", idUsuario);
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.findByIdUsuario - sucesso");
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Caixa> search(Caixa filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Caixa> search(Caixa filtro, int page, int size) {
        Logger.info("CaixaDaoNativeImpl.search - inicio");
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            StringBuilder sb = new StringBuilder("SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE 1=1");
            Map<String, Object> params = new HashMap<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                sb.append(" AND nome=:nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getReservaEmergencia() != null) {
                sb.append(" AND reserva_emergencia=:reserva");
                params.put("reserva", filtro.getReservaEmergencia());
            }
            if (filtro.getSalarioMedio() != null) {
                sb.append(" AND salario_medio=:salario");
                params.put("salario", filtro.getSalarioMedio());
            }
            if (filtro.getValorTotal() != null) {
                sb.append(" AND valor_total=:valor");
                params.put("valor", filtro.getValorTotal());
            }
            if (filtro.getUsuario() != null && filtro.getUsuario().getIdUsuario() != null) {
                sb.append(" AND id_usuario=:idUsuario");
                params.put("idUsuario", filtro.getUsuario().getIdUsuario());
            }
            if (page >= 0 && size > 0) {
                sb.append(" LIMIT :limit OFFSET :offset");
            }
            Query query = em.createNativeQuery(sb.toString(), Caixa.class);
            for (Map.Entry<String, Object> e : params.entrySet()) {
                query.setParameter(e.getKey(), e.getValue());
            }
            if (page >= 0 && size > 0) {
                query.setParameter("limit", size);
                query.setParameter("offset", page * size);
            }
            List<Caixa> list = query.getResultList();
            Logger.info("CaixaDaoNativeImpl.search - sucesso");
            return list;
        } finally {
            em.close();
        }
    }
}
