package dao.impl;

import conexao.ConnectionFactory;
import dao.api.MetaDao;
import exception.MetaException;
import infra.Logger;
import model.Meta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link MetaDao}.
 */
public class MetaDaoNativeImpl implements MetaDao {

    private Meta map(ResultSet rs, boolean withFoto) throws SQLException {
        Meta m = new Meta();
        m.setIdMeta(rs.getInt("id_meta"));
        m.setPontoMinimo((Integer) rs.getObject("ponto_minimo"));
        m.setPontoMedio((Integer) rs.getObject("ponto_medio"));
        m.setPontoMaximo((Integer) rs.getObject("ponto_maximo"));
        m.setStatus((Integer) rs.getObject("status"));
        if (withFoto) {
            m.setFoto(rs.getBytes("foto"));
        }
        m.setIdPeriodo((Integer) rs.getObject("id_periodo"));
        return m;
    }

    @Override
    public void create(Meta meta) {
        Logger.info("MetaDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Meta (ponto_minimo, ponto_medio, ponto_maximo, status, foto, id_periodo) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (meta.getPontoMinimo() != null) {
                    ps.setInt(1, meta.getPontoMinimo());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                if (meta.getPontoMedio() != null) {
                    ps.setInt(2, meta.getPontoMedio());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (meta.getPontoMaximo() != null) {
                    ps.setInt(3, meta.getPontoMaximo());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                if (meta.getStatus() != null) {
                    ps.setInt(4, meta.getStatus());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setBytes(5, meta.getFoto());
                if (meta.getIdPeriodo() != null) {
                    ps.setInt(6, meta.getIdPeriodo());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("MetaDaoNativeImpl.create - erro", e);
            throw new MetaException("Erro ao criar Meta", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Meta meta) {
        Logger.info("MetaDaoNativeImpl.update - inicio");
        String sql = "UPDATE Meta SET ponto_minimo=?, ponto_medio=?, ponto_maximo=?, status=?, foto=?, id_periodo=? WHERE id_meta=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (meta.getPontoMinimo() != null) {
                    ps.setInt(1, meta.getPontoMinimo());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                if (meta.getPontoMedio() != null) {
                    ps.setInt(2, meta.getPontoMedio());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (meta.getPontoMaximo() != null) {
                    ps.setInt(3, meta.getPontoMaximo());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                if (meta.getStatus() != null) {
                    ps.setInt(4, meta.getStatus());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setBytes(5, meta.getFoto());
                if (meta.getIdPeriodo() != null) {
                    ps.setInt(6, meta.getIdPeriodo());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.setInt(7, meta.getIdMeta());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new MetaException("Meta nao encontrada: id=" + meta.getIdMeta());
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("MetaDaoNativeImpl.update - erro", e);
            throw new MetaException("Erro ao atualizar Meta", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("MetaDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Meta WHERE id_meta=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new MetaException("Meta nao encontrada: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("MetaDaoNativeImpl.deleteById - erro", e);
            throw new MetaException("Erro ao deletar Meta", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Meta findById(Integer id) {
        Logger.info("MetaDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE id_meta=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("MetaDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new MetaException("Meta nao encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findById - erro", e);
            throw new MetaException("Meta nao encontrada: id=" + id, e);
        }
    }

    @Override
    public Meta findWithFotoById(Integer id) {
        Logger.info("MetaDaoNativeImpl.findWithFotoById - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, foto, id_periodo FROM Meta WHERE id_meta=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("MetaDaoNativeImpl.findWithFotoById - sucesso");
                    return map(rs, true);
                }
            }
            throw new MetaException("Meta nao encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findWithFotoById - erro", e);
            throw new MetaException("Meta nao encontrada: id=" + id, e);
        }
    }

    @Override
    public List<Meta> findAll() {
        Logger.info("MetaDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("MetaDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findAll(int page, int size) {
        Logger.info("MetaDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta LIMIT ? OFFSET ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findByPontoMinimo(Integer pontoMinimo) {
        Logger.info("MetaDaoNativeImpl.findByPontoMinimo - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE ponto_minimo = ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (pontoMinimo != null) {
                ps.setInt(1, pontoMinimo);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findByPontoMinimo - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findByPontoMinimo - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findByPontoMedio(Integer pontoMedio) {
        Logger.info("MetaDaoNativeImpl.findByPontoMedio - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE ponto_medio = ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (pontoMedio != null) {
                ps.setInt(1, pontoMedio);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findByPontoMedio - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findByPontoMedio - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findByPontoMaximo(Integer pontoMaximo) {
        Logger.info("MetaDaoNativeImpl.findByPontoMaximo - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE ponto_maximo = ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (pontoMaximo != null) {
                ps.setInt(1, pontoMaximo);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findByPontoMaximo - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findByPontoMaximo - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findByStatus(Integer status) {
        Logger.info("MetaDaoNativeImpl.findByStatus - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE status = ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null) {
                ps.setInt(1, status);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findByStatus - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findByStatus - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> findByIdPeriodo(Integer idPeriodo) {
        Logger.info("MetaDaoNativeImpl.findByIdPeriodo - inicio");
        String sql = "SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE id_periodo = ?";
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.findByIdPeriodo - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.findByIdPeriodo - erro", e);
        }
        return list;
    }

    @Override
    public List<Meta> search(Meta f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Meta> search(Meta f, int page, int size) {
        Logger.info("MetaDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_meta, ponto_minimo, ponto_medio, ponto_maximo, status, id_periodo FROM Meta WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getPontoMinimo() != null) {
            sb.append(" AND ponto_minimo = ?");
            params.add(f.getPontoMinimo());
        }
        if (f.getPontoMedio() != null) {
            sb.append(" AND ponto_medio = ?");
            params.add(f.getPontoMedio());
        }
        if (f.getPontoMaximo() != null) {
            sb.append(" AND ponto_maximo = ?");
            params.add(f.getPontoMaximo());
        }
        if (f.getStatus() != null) {
            sb.append(" AND status = ?");
            params.add(f.getStatus());
        }
        if (f.getIdPeriodo() != null) {
            sb.append(" AND id_periodo = ?");
            params.add(f.getIdPeriodo());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        List<Meta> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("MetaDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("MetaDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}

