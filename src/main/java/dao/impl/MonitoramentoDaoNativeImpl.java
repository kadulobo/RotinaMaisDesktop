package dao.impl;

import conexao.ConnectionFactory;
import dao.api.MonitoramentoDao;
import exception.MonitoramentoException;
import infra.Logger;
import model.Monitoramento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MonitoramentoDaoNativeImpl implements MonitoramentoDao {

    private Monitoramento mapMonitoramento(ResultSet rs, boolean withFoto) throws SQLException {
        Monitoramento m = new Monitoramento();
        m.setIdMonitoramento(rs.getInt("id_monitoramento"));
        int status = rs.getInt("status");
        if (!rs.wasNull()) {
            m.setStatus(status);
        }
        m.setNome(rs.getString("nome"));
        m.setDescricao(rs.getString("descricao"));
        if (withFoto) {
            m.setFoto(rs.getBytes("foto"));
        }
        int idPeriodo = rs.getInt("id_periodo");
        if (!rs.wasNull()) {
            m.setIdPeriodo(idPeriodo);
        }
        return m;
    }

    @Override
    public void create(Monitoramento monitoramento) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Monitoramento (status, nome, descricao, foto, id_periodo) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (monitoramento.getStatus() != null) {
                    ps.setInt(1, monitoramento.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setString(2, monitoramento.getNome());
                ps.setString(3, monitoramento.getDescricao());
                ps.setBytes(4, monitoramento.getFoto());
                if (monitoramento.getIdPeriodo() != null) {
                    ps.setInt(5, monitoramento.getIdPeriodo());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("MonitoramentoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("MonitoramentoDaoNativeImpl.create - erro", e);
            throw new MonitoramentoException("Erro ao criar Monitoramento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Monitoramento update(Monitoramento monitoramento) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Monitoramento SET status=?, nome=?, descricao=?, foto=?, id_periodo=? WHERE id_monitoramento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (monitoramento.getStatus() != null) {
                    ps.setInt(1, monitoramento.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setString(2, monitoramento.getNome());
                ps.setString(3, monitoramento.getDescricao());
                ps.setBytes(4, monitoramento.getFoto());
                if (monitoramento.getIdPeriodo() != null) {
                    ps.setInt(5, monitoramento.getIdPeriodo());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setInt(6, monitoramento.getIdMonitoramento());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new MonitoramentoException("Monitoramento não encontrado: id=" + monitoramento.getIdMonitoramento());
                }
            }
            conn.commit();
            Logger.info("MonitoramentoDaoNativeImpl.update - sucesso");
            return findById(monitoramento.getIdMonitoramento());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("MonitoramentoDaoNativeImpl.update - erro", e);
            throw new MonitoramentoException("Erro ao atualizar Monitoramento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Monitoramento WHERE id_monitoramento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new MonitoramentoException("Monitoramento não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("MonitoramentoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("MonitoramentoDaoNativeImpl.deleteById - erro", e);
            throw new MonitoramentoException("Erro ao deletar Monitoramento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Monitoramento findById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE id_monitoramento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("MonitoramentoDaoNativeImpl.findById - sucesso");
                    return mapMonitoramento(rs, false);
                }
            }
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findById - erro", e);
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id, e);
        }
    }

    @Override
    public Monitoramento findWithFotoById(Integer id) throws MonitoramentoException {
        Logger.info("MonitoramentoDaoNativeImpl.findWithFotoById - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, foto, id_periodo FROM Monitoramento WHERE id_monitoramento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("MonitoramentoDaoNativeImpl.findWithFotoById - sucesso");
                    return mapMonitoramento(rs, true);
                }
            }
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findWithFotoById - erro", e);
            throw new MonitoramentoException("Monitoramento não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Monitoramento> findAll() {
        Logger.info("MonitoramentoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapMonitoramento(rs, false));
            }
            Logger.info("MonitoramentoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findAll(int page, int size) {
        Logger.info("MonitoramentoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento LIMIT ? OFFSET ?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findByStatus(Integer status) {
        Logger.info("MonitoramentoDaoNativeImpl.findByStatus - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE status=?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findByStatus - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findByStatus - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findByNome(String nome) {
        Logger.info("MonitoramentoDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE nome=?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findByDescricao(String descricao) {
        Logger.info("MonitoramentoDaoNativeImpl.findByDescricao - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE descricao=?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findByDescricao - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findByDescricao - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findByFoto(byte[] foto) {
        Logger.info("MonitoramentoDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, foto, id_periodo FROM Monitoramento WHERE foto=?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, true));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> findByIdPeriodo(Integer idPeriodo) {
        Logger.info("MonitoramentoDaoNativeImpl.findByIdPeriodo - inicio");
        String sql = "SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE id_periodo=?";
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.findByIdPeriodo - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.findByIdPeriodo - erro", e);
        }
        return list;
    }

    @Override
    public List<Monitoramento> search(Monitoramento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Monitoramento> search(Monitoramento filtro, int page, int size) {
        Logger.info("MonitoramentoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_monitoramento, status, nome, descricao, id_periodo FROM Monitoramento WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
        }
        if (filtro.getFoto() != null) {
            sb.append(" AND foto=?");
            params.add(filtro.getFoto());
        }
        if (filtro.getIdPeriodo() != null) {
            sb.append(" AND id_periodo=?");
            params.add(filtro.getIdPeriodo());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Monitoramento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMonitoramento(rs, false));
                }
            }
            Logger.info("MonitoramentoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("MonitoramentoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
