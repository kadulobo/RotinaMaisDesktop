package dao.impl;

import conexao.ConnectionFactory;
import dao.api.CofreDao;
import exception.CofreException;
import infra.Logger;
import model.Cofre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CofreDaoNativeImpl implements CofreDao {

    private Cofre mapCofre(ResultSet rs) throws SQLException {
        Cofre c = new Cofre();
        c.setIdCofre(rs.getInt("id_cofre"));
        c.setLogin(rs.getString("login"));
        c.setSenha(rs.getString("senha"));
        int tipo = rs.getInt("tipo");
        c.setTipo(rs.wasNull() ? null : tipo);
        c.setPlataforma(rs.getString("plataforma"));
        int idUsuario = rs.getInt("id_usuario");
        c.setIdUsuario(rs.wasNull() ? null : idUsuario);
        return c;
    }

    @Override
    public void create(Cofre c) {
        Logger.info("CofreDao.create");
        String sql = "INSERT INTO Cofre (login, senha, tipo, plataforma, id_usuario) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getLogin());
                ps.setString(2, c.getSenha());
                if (c.getTipo() != null) ps.setInt(3, c.getTipo()); else ps.setNull(3, java.sql.Types.INTEGER);
                ps.setString(4, c.getPlataforma());
                if (c.getIdUsuario() != null) ps.setInt(5, c.getIdUsuario()); else ps.setNull(5, java.sql.Types.INTEGER);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("CofreDao.create - erro", e);
            throw new CofreException("Erro ao criar Cofre", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Cofre c) {
        Logger.info("CofreDao.update");
        String sql = "UPDATE Cofre SET login=?, senha=?, tipo=?, plataforma=?, id_usuario=? WHERE id_cofre=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getLogin());
                ps.setString(2, c.getSenha());
                if (c.getTipo() != null) ps.setInt(3, c.getTipo()); else ps.setNull(3, java.sql.Types.INTEGER);
                ps.setString(4, c.getPlataforma());
                if (c.getIdUsuario() != null) ps.setInt(5, c.getIdUsuario()); else ps.setNull(5, java.sql.Types.INTEGER);
                ps.setInt(6, c.getIdCofre());
                int upd = ps.executeUpdate();
                if (upd == 0) throw new CofreException("Cofre nao encontrado: id=" + c.getIdCofre());
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("CofreDao.update - erro", e);
            throw new CofreException("Erro ao atualizar Cofre", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("CofreDao.deleteById");
        String sql = "DELETE FROM Cofre WHERE id_cofre=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int del = ps.executeUpdate();
                if (del == 0) throw new CofreException("Cofre nao encontrado: id=" + id);
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("CofreDao.deleteById - erro", e);
            throw new CofreException("Erro ao deletar Cofre", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Cofre findById(Integer id) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE id_cofre = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCofre(rs);
                }
            }
        } catch (SQLException e) {
            Logger.error("CofreDao.findById - erro", e);
        }
        return null;
    }

    private List<Cofre> listBySql(String sql, Object... params) {
        List<Cofre> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCofre(rs));
                }
            }
        } catch (SQLException e) {
            Logger.error("CofreDao.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public List<Cofre> findAll() {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre";
        return listBySql(sql);
    }

    @Override
    public List<Cofre> findAll(int page, int size) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre LIMIT ? OFFSET ?";
        return listBySql(sql, size, page * size);
    }

    @Override
    public List<Cofre> findByLogin(String login) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE login = ?";
        return listBySql(sql, login);
    }

    @Override
    public List<Cofre> findByTipo(Integer tipo) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE tipo = ?";
        return listBySql(sql, tipo);
    }

    @Override
    public List<Cofre> findByIdUsuario(Integer idUsuario) {
        String sql = "SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE id_usuario = ?";
        return listBySql(sql, idUsuario);
    }

    @Override
    public List<Cofre> search(Cofre f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Cofre> search(Cofre f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_cofre, login, senha, tipo, plataforma, id_usuario FROM Cofre WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getLogin() != null && !f.getLogin().isEmpty()) {
            sb.append(" AND login = ?");
            params.add(f.getLogin());
        }
        if (f.getTipo() != null) {
            sb.append(" AND tipo = ?");
            params.add(f.getTipo());
        }
        if (f.getIdUsuario() != null) {
            sb.append(" AND id_usuario = ?");
            params.add(f.getIdUsuario());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        return listBySql(sb.toString(), params.toArray());
    }
}
