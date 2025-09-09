package dao.impl;

import conexao.ConnectionFactory;
import dao.api.MonitoramentoObjetoDao;
import exception.MonitoramentoObjetoException;
import infra.Logger;
import model.MonitoramentoObjeto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonitoramentoObjetoDaoNativeImpl implements MonitoramentoObjetoDao {

    private MonitoramentoObjeto map(ResultSet rs) throws SQLException {
        MonitoramentoObjeto mo = new MonitoramentoObjeto();
        int id = rs.getInt("id_monitoramento_objeto");
        if (!rs.wasNull()) {
            mo.setIdMonitoramentoObjeto(id);
        }
        Date data = rs.getDate("data");
        if (data != null) {
            mo.setData(data.toLocalDate());
        }
        int idMon = rs.getInt("id_monitoramento");
        if (!rs.wasNull()) {
            mo.setIdMonitoramento(idMon);
        }
        int idObj = rs.getInt("id_objeto");
        if (!rs.wasNull()) {
            mo.setIdObjeto(idObj);
        }
        return mo;
    }

    @Override
    public void create(MonitoramentoObjeto e) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.create");
        String sql = "INSERT INTO Monitoramento_Objeto (data, id_monitoramento, id_objeto) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (e.getData() != null) {
                    ps.setDate(1, Date.valueOf(e.getData()));
                } else {
                    ps.setNull(1, Types.DATE);
                }
                if (e.getIdMonitoramento() != null) {
                    ps.setInt(2, e.getIdMonitoramento());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (e.getIdObjeto() != null) {
                    ps.setInt(3, e.getIdObjeto());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback create", e1); }
            }
            Logger.error("MonitoramentoObjetoDaoNativeImpl.create", ex);
            throw new MonitoramentoObjetoException("Erro ao criar MonitoramentoObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(MonitoramentoObjeto e) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.update");
        String sql = "UPDATE Monitoramento_Objeto SET data=?, id_monitoramento=?, id_objeto=? WHERE id_monitoramento_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (e.getData() != null) {
                    ps.setDate(1, Date.valueOf(e.getData()));
                } else {
                    ps.setNull(1, Types.DATE);
                }
                if (e.getIdMonitoramento() != null) {
                    ps.setInt(2, e.getIdMonitoramento());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (e.getIdObjeto() != null) {
                    ps.setInt(3, e.getIdObjeto());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                if (e.getIdMonitoramentoObjeto() != null) {
                    ps.setInt(4, e.getIdMonitoramentoObjeto());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                int upd = ps.executeUpdate();
                if (upd == 0) {
                    throw new MonitoramentoObjetoException("MonitoramentoObjeto nao encontrado: id=" + e.getIdMonitoramentoObjeto());
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback update", e1); }
            }
            Logger.error("MonitoramentoObjetoDaoNativeImpl.update", ex);
            throw new MonitoramentoObjetoException("Erro ao atualizar MonitoramentoObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("MonitoramentoObjetoDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Monitoramento_Objeto WHERE id_monitoramento_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int del = ps.executeUpdate();
                if (del == 0) {
                    throw new MonitoramentoObjetoException("MonitoramentoObjeto nao encontrado: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback delete", e1); }
            }
            Logger.error("MonitoramentoObjetoDaoNativeImpl.deleteById", ex);
            throw new MonitoramentoObjetoException("Erro ao deletar MonitoramentoObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public MonitoramentoObjeto findById(Integer id) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE id_monitoramento_objeto = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            throw new MonitoramentoObjetoException("MonitoramentoObjeto nao encontrado: id=" + id);
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findById", e1);
            throw new MonitoramentoObjetoException("MonitoramentoObjeto nao encontrado: id=" + id, e1);
        }
    }

    @Override
    public List<MonitoramentoObjeto> findAll() {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto";
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findAll", e1);
        }
        return list;
    }

    @Override
    public List<MonitoramentoObjeto> findAll(int page, int size) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto LIMIT ? OFFSET ?";
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findAll(page)", e1);
        }
        return list;
    }

    @Override
    public List<MonitoramentoObjeto> findByData(LocalDate data) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE data = ?";
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findByData", e1);
        }
        return list;
    }

    @Override
    public List<MonitoramentoObjeto> findByIdMonitoramento(Integer idMonitoramento) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE id_monitoramento = ?";
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMonitoramento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findByIdMonitoramento", e1);
        }
        return list;
    }

    @Override
    public List<MonitoramentoObjeto> findByIdObjeto(Integer idObjeto) {
        String sql = "SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE id_objeto = ?";
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idObjeto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.findByIdObjeto", e1);
        }
        return list;
    }

    @Override
    public List<MonitoramentoObjeto> search(MonitoramentoObjeto f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<MonitoramentoObjeto> search(MonitoramentoObjeto f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_monitoramento_objeto, data, id_monitoramento, id_objeto FROM Monitoramento_Objeto WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getData() != null) {
            sb.append(" AND data = ?");
            params.add(Date.valueOf(f.getData()));
        }
        if (f.getIdMonitoramento() != null) {
            sb.append(" AND id_monitoramento = ?");
            params.add(f.getIdMonitoramento());
        }
        if (f.getIdObjeto() != null) {
            sb.append(" AND id_objeto = ?");
            params.add(f.getIdObjeto());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        List<MonitoramentoObjeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e1) {
            Logger.error("MonitoramentoObjetoDaoNativeImpl.search", e1);
        }
        return list;
    }
}
