package dao.impl;

import conexao.ConnectionFactory;
import dao.api.PeriodoDao;
import exception.PeriodoException;
import infra.Logger;
import model.Periodo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodoDaoNativeImpl implements PeriodoDao {

    private Periodo map(ResultSet rs) throws SQLException {
        Periodo p = new Periodo();
        p.setIdPeriodo(rs.getInt("id_periodo"));
        p.setAno((Integer) rs.getObject("ano"));
        p.setMes((Integer) rs.getObject("mes"));
        return p;
    }

    private List<Periodo> listBySql(String sql, Object... params) {
        List<Periodo> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            Logger.error("PeriodoDaoNativeImpl.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public void create(Periodo periodo) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Periodo (ano, mes) VALUES (?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, periodo.getAno());
                ps.setInt(2, periodo.getMes());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("PeriodoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("PeriodoDaoNativeImpl.create - erro", e);
            throw new PeriodoException("Erro ao criar Periodo", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Periodo update(Periodo periodo) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Periodo SET ano=?, mes=? WHERE id_periodo=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, periodo.getAno());
                ps.setInt(2, periodo.getMes());
                ps.setInt(3, periodo.getIdPeriodo());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new PeriodoException("Periodo não encontrado: id=" + periodo.getIdPeriodo());
                }
            }
            conn.commit();
            Logger.info("PeriodoDaoNativeImpl.update - sucesso");
            return findById(periodo.getIdPeriodo());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("PeriodoDaoNativeImpl.update - erro", e);
            throw new PeriodoException("Erro ao atualizar Periodo", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Periodo WHERE id_periodo=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new PeriodoException("Periodo não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("PeriodoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("PeriodoDaoNativeImpl.deleteById - erro", e);
            throw new PeriodoException("Erro ao deletar Periodo", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Periodo findById(Integer id) throws PeriodoException {
        Logger.info("PeriodoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_periodo, ano, mes FROM Periodo WHERE id_periodo=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("PeriodoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new PeriodoException("Periodo não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("PeriodoDaoNativeImpl.findById - erro", e);
            throw new PeriodoException("Periodo não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Periodo> findAll() {
        Logger.info("PeriodoDaoNativeImpl.findAll - inicio");
        List<Periodo> list = listBySql("SELECT id_periodo, ano, mes FROM Periodo");
        Logger.info("PeriodoDaoNativeImpl.findAll - sucesso");
        return list;
    }

    @Override
    public List<Periodo> findAll(int page, int size) {
        Logger.info("PeriodoDaoNativeImpl.findAll(page) - inicio");
        List<Periodo> list = listBySql("SELECT id_periodo, ano, mes FROM Periodo LIMIT ? OFFSET ?", size, page * size);
        Logger.info("PeriodoDaoNativeImpl.findAll(page) - sucesso");
        return list;
    }

    @Override
    public List<Periodo> findByAno(Integer ano) {
        Logger.info("PeriodoDaoNativeImpl.findByAno - inicio");
        List<Periodo> list = listBySql("SELECT id_periodo, ano, mes FROM Periodo WHERE ano=?", ano);
        Logger.info("PeriodoDaoNativeImpl.findByAno - sucesso");
        return list;
    }

    @Override
    public List<Periodo> findByMes(Integer mes) {
        Logger.info("PeriodoDaoNativeImpl.findByMes - inicio");
        List<Periodo> list = listBySql("SELECT id_periodo, ano, mes FROM Periodo WHERE mes=?", mes);
        Logger.info("PeriodoDaoNativeImpl.findByMes - sucesso");
        return list;
    }

    @Override
    public List<Periodo> search(Periodo filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Periodo> search(Periodo filtro, int page, int size) {
        Logger.info("PeriodoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_periodo, ano, mes FROM Periodo WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getAno() != null) {
            sb.append(" AND ano=?");
            params.add(filtro.getAno());
        }
        if (filtro.getMes() != null) {
            sb.append(" AND mes=?");
            params.add(filtro.getMes());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Periodo> list = listBySql(sb.toString(), params.toArray());
        Logger.info("PeriodoDaoNativeImpl.search - sucesso");
        return list;
    }
}

