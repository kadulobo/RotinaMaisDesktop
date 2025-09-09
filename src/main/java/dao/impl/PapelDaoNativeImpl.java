package dao.impl;

import conexao.ConnectionFactory;
import dao.api.PapelDao;
import exception.PapelException;
import infra.Logger;
import model.Papel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PapelDaoNativeImpl implements PapelDao {
    private Papel mapPapel(ResultSet rs) throws SQLException {
        Papel p = new Papel();
        p.setIdPapel(rs.getInt("id_papel"));
        p.setCodigo(rs.getString("codigo"));
        p.setTipo(rs.getString("tipo"));
        Date v = rs.getDate("vencimento");
        if (v != null) {
            p.setVencimento(v.toLocalDate());
        }
        return p;
    }

    @Override
    public void create(Papel papel) {
        Logger.info("PapelDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Papel (codigo, tipo, vencimento) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, papel.getCodigo());
                ps.setString(2, papel.getTipo());
                if (papel.getVencimento() != null) {
                    ps.setDate(3, Date.valueOf(papel.getVencimento()));
                } else {
                    ps.setNull(3, java.sql.Types.DATE);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("PapelDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("PapelDaoNativeImpl.create - erro", e);
            throw new PapelException("Erro ao criar Papel", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Papel papel) {
        Logger.info("PapelDaoNativeImpl.update - inicio");
        String sql = "UPDATE Papel SET codigo=?, tipo=?, vencimento=? WHERE id_papel=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, papel.getCodigo());
                ps.setString(2, papel.getTipo());
                if (papel.getVencimento() != null) {
                    ps.setDate(3, Date.valueOf(papel.getVencimento()));
                } else {
                    ps.setNull(3, java.sql.Types.DATE);
                }
                ps.setInt(4, papel.getIdPapel());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new PapelException("Papel não encontrado: id=" + papel.getIdPapel());
                }
            }
            conn.commit();
            Logger.info("PapelDaoNativeImpl.update - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("PapelDaoNativeImpl.update - erro", e);
            throw new PapelException("Erro ao atualizar Papel", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("PapelDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Papel WHERE id_papel=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new PapelException("Papel não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("PapelDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("PapelDaoNativeImpl.deleteById - erro", e);
            throw new PapelException("Erro ao deletar Papel", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Papel findById(Integer id) {
        Logger.info("PapelDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE id_papel=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("PapelDaoNativeImpl.findById - sucesso");
                    return mapPapel(rs);
                }
            }
            throw new PapelException("Papel não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("PapelDaoNativeImpl.findById - erro", e);
            throw new PapelException("Papel não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Papel> findAll() {
        Logger.info("PapelDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Papel> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapPapel(rs));
            }
            Logger.info("PapelDaoNativeImpl.findAll - sucesso");
            return lista;
        } catch (SQLException e) {
            Logger.error("PapelDaoNativeImpl.findAll - erro", e);
            throw new PapelException("Erro ao listar Papel", e);
        }
    }

    @Override
    public List<Papel> findAll(int page, int size) {
        Logger.info("PapelDaoNativeImpl.findAll(paginated) - inicio");
        String sql = "SELECT id_papel, codigo, tipo, vencimento FROM Papel LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<Papel> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapPapel(rs));
                }
                Logger.info("PapelDaoNativeImpl.findAll(paginated) - sucesso");
                return lista;
            }
        } catch (SQLException e) {
            Logger.error("PapelDaoNativeImpl.findAll(paginated) - erro", e);
            throw new PapelException("Erro ao listar Papel", e);
        }
    }

    private List<Papel> findByField(String sql, Object value) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (value instanceof LocalDate) {
                ps.setDate(1, Date.valueOf((LocalDate) value));
            } else {
                ps.setObject(1, value);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Papel> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapPapel(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("PapelDaoNativeImpl.findByField - erro", e);
            throw new PapelException("Erro na consulta de Papel", e);
        }
    }

    @Override
    public List<Papel> findByCodigo(String codigo) {
        return findByField("SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE codigo = ?", codigo);
    }

    @Override
    public List<Papel> findByTipo(String tipo) {
        return findByField("SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE tipo = ?", tipo);
    }

    @Override
    public List<Papel> findByVencimento(LocalDate vencimento) {
        return findByField("SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE vencimento = ?", vencimento);
    }

    @Override
    public List<Papel> search(Papel f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Papel> search(Papel f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_papel, codigo, tipo, vencimento FROM Papel WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getCodigo() != null && !f.getCodigo().isEmpty()) {
            sb.append(" AND codigo = ?");
            params.add(f.getCodigo());
        }
        if (f.getTipo() != null && !f.getTipo().isEmpty()) {
            sb.append(" AND tipo = ?");
            params.add(f.getTipo());
        }
        if (f.getVencimento() != null) {
            sb.append(" AND vencimento = ?");
            params.add(f.getVencimento());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object val = params.get(i);
                if (val instanceof LocalDate) {
                    ps.setDate(i + 1, Date.valueOf((LocalDate) val));
                } else if (val instanceof Integer) {
                    ps.setInt(i + 1, (Integer) val);
                } else {
                    ps.setString(i + 1, val.toString());
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Papel> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapPapel(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("PapelDaoNativeImpl.search - erro", e);
            throw new PapelException("Erro na busca de Papel", e);
        }
    }
}
