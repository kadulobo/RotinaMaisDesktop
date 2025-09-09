package dao.impl;

import conexao.ConnectionFactory;
import dao.api.RotinaPeriodoDao;
import exception.RotinaPeriodoException;
import infra.Logger;
import model.RotinaPeriodo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RotinaPeriodoDaoNativeImpl implements RotinaPeriodoDao {
    private RotinaPeriodo map(ResultSet rs) throws SQLException {
        RotinaPeriodo rp = new RotinaPeriodo();
        rp.setIdRotinaPeriodo(rs.getInt("id_rotina_periodo"));
        rp.setIdRotina(rs.getInt("id_rotina"));
        rp.setIdPeriodo(rs.getInt("id_periodo"));
        return rp;
    }

    @Override
    public void create(RotinaPeriodo e) {
        Logger.info("RotinaPeriodoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Rotina_Periodo (id_rotina, id_periodo) VALUES (?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getIdRotina());
                ps.setInt(2, e.getIdPeriodo());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("RotinaPeriodoDaoNativeImpl.create - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback create", e1); }
            }
            Logger.error("RotinaPeriodoDaoNativeImpl.create - erro", ex);
            throw new RotinaPeriodoException("Erro ao criar RotinaPeriodo", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(RotinaPeriodo e) {
        Logger.info("RotinaPeriodoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Rotina_Periodo SET id_rotina=?, id_periodo=? WHERE id_rotina_periodo=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getIdRotina());
                ps.setInt(2, e.getIdPeriodo());
                ps.setInt(3, e.getIdRotinaPeriodo());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new RotinaPeriodoException("RotinaPeriodo não encontrado: id=" + e.getIdRotinaPeriodo());
                }
            }
            conn.commit();
            Logger.info("RotinaPeriodoDaoNativeImpl.update - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback update", e1); }
            }
            Logger.error("RotinaPeriodoDaoNativeImpl.update - erro", ex);
            throw new RotinaPeriodoException("Erro ao atualizar RotinaPeriodo", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("RotinaPeriodoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Rotina_Periodo WHERE id_rotina_periodo=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new RotinaPeriodoException("RotinaPeriodo não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("RotinaPeriodoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback delete", e1); }
            }
            Logger.error("RotinaPeriodoDaoNativeImpl.deleteById - erro", ex);
            throw new RotinaPeriodoException("Erro ao deletar RotinaPeriodo", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public RotinaPeriodo findById(Integer id) {
        Logger.info("RotinaPeriodoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_rotina_periodo=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("RotinaPeriodoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new RotinaPeriodoException("RotinaPeriodo não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("RotinaPeriodoDaoNativeImpl.findById - erro", e);
            throw new RotinaPeriodoException("RotinaPeriodo não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<RotinaPeriodo> findAll() {
        Logger.info("RotinaPeriodoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<RotinaPeriodo> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(map(rs));
            }
            Logger.info("RotinaPeriodoDaoNativeImpl.findAll - sucesso");
            return lista;
        } catch (SQLException e) {
            Logger.error("RotinaPeriodoDaoNativeImpl.findAll - erro", e);
            throw new RotinaPeriodoException("Erro ao listar RotinaPeriodo", e);
        }
    }

    @Override
    public List<RotinaPeriodo> findAll(int page, int size) {
        Logger.info("RotinaPeriodoDaoNativeImpl.findAll(paginated) - inicio");
        String sql = "SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<RotinaPeriodo> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(map(rs));
                }
                Logger.info("RotinaPeriodoDaoNativeImpl.findAll(paginated) - sucesso");
                return lista;
            }
        } catch (SQLException e) {
            Logger.error("RotinaPeriodoDaoNativeImpl.findAll(paginated) - erro", e);
            throw new RotinaPeriodoException("Erro ao listar RotinaPeriodo", e);
        }
    }

    private List<RotinaPeriodo> findByField(String sql, Integer value) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                List<RotinaPeriodo> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("RotinaPeriodoDaoNativeImpl.findByField - erro", e);
            throw new RotinaPeriodoException("Erro na consulta de RotinaPeriodo", e);
        }
    }

    @Override
    public List<RotinaPeriodo> findByIdRotina(Integer idRotina) {
        return findByField("SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_rotina = ?", idRotina);
    }

    @Override
    public List<RotinaPeriodo> findByIdPeriodo(Integer idPeriodo) {
        return findByField("SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE id_periodo = ?", idPeriodo);
    }

    @Override
    public List<RotinaPeriodo> search(RotinaPeriodo f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<RotinaPeriodo> search(RotinaPeriodo f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_rotina_periodo, id_rotina, id_periodo FROM Rotina_Periodo WHERE 1=1");
        List<Integer> params = new ArrayList<>();
        if (f.getIdRotina() != null) {
            sb.append(" AND id_rotina = ?");
            params.add(f.getIdRotina());
        }
        if (f.getIdPeriodo() != null) {
            sb.append(" AND id_periodo = ?");
            params.add(f.getIdPeriodo());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setInt(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<RotinaPeriodo> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("RotinaPeriodoDaoNativeImpl.search - erro", e);
            throw new RotinaPeriodoException("Erro na busca de RotinaPeriodo", e);
        }
    }
}
