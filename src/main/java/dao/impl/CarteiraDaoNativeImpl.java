package dao.impl;

import conexao.ConnectionFactory;
import dao.api.CarteiraDao;
import exception.CarteiraException;
import infra.Logger;
import model.Carteira;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarteiraDaoNativeImpl implements CarteiraDao {

    private Carteira mapCarteira(ResultSet rs) throws SQLException {
        Carteira c = new Carteira();
        c.setIdCarteira(rs.getInt("id_carteira"));
        c.setNome(rs.getString("nome"));
        c.setTipo(rs.getString("tipo"));
        Date d = rs.getDate("dataInicio");
        c.setDataInicio(d != null ? d.toLocalDate() : null);
        int idUsuario = rs.getInt("id_usuario");
        c.setIdUsuario(rs.wasNull() ? null : idUsuario);
        return c;
    }

    @Override
    public void create(Carteira e) {
        Logger.info("CarteiraDaoNativeImpl.create");
        String sql = "INSERT INTO Carteira (nome, tipo, dataInicio, id_usuario) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getNome());
                ps.setString(2, e.getTipo());
                if (e.getDataInicio() != null) ps.setDate(3, Date.valueOf(e.getDataInicio())); else ps.setNull(3, java.sql.Types.DATE);
                if (e.getIdUsuario() != null) ps.setInt(4, e.getIdUsuario()); else ps.setNull(4, java.sql.Types.INTEGER);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback create", re); }
            }
            Logger.error("CarteiraDao.create - erro", ex);
            throw new CarteiraException("Erro ao criar Carteira", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Carteira e) {
        Logger.info("CarteiraDaoNativeImpl.update");
        String sql = "UPDATE Carteira SET nome=?, tipo=?, dataInicio=?, id_usuario=? WHERE id_carteira=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getNome());
                ps.setString(2, e.getTipo());
                if (e.getDataInicio() != null) ps.setDate(3, Date.valueOf(e.getDataInicio())); else ps.setNull(3, java.sql.Types.DATE);
                if (e.getIdUsuario() != null) ps.setInt(4, e.getIdUsuario()); else ps.setNull(4, java.sql.Types.INTEGER);
                ps.setInt(5, e.getIdCarteira());
                int upd = ps.executeUpdate();
                if (upd == 0) throw new CarteiraException("Carteira nao encontrado: id=" + e.getIdCarteira());
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback update", re); }
            }
            Logger.error("CarteiraDao.update - erro", ex);
            throw new CarteiraException("Erro ao atualizar Carteira", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("CarteiraDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Carteira WHERE id_carteira=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int del = ps.executeUpdate();
                if (del == 0) throw new CarteiraException("Carteira nao encontrado: id=" + id);
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback delete", re); }
            }
            Logger.error("CarteiraDao.delete - erro", ex);
            throw new CarteiraException("Erro ao deletar Carteira", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Carteira findById(Integer id) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE id_carteira = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCarteira(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.error("CarteiraDao.findById - erro", ex);
        }
        return null;
    }

    private List<Carteira> listBySql(String sql, Object... params) {
        List<Carteira> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCarteira(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.error("CarteiraDao.listBySql - erro", ex);
        }
        return list;
    }

    @Override
    public List<Carteira> findAll() {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira";
        return listBySql(sql);
    }

    @Override
    public List<Carteira> findAll(int page, int size) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira LIMIT ? OFFSET ?";
        return listBySql(sql, size, page * size);
    }

    @Override
    public List<Carteira> findByNome(String nome) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE nome = ?";
        return listBySql(sql, nome);
    }

    @Override
    public List<Carteira> findByTipo(String tipo) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE tipo = ?";
        return listBySql(sql, tipo);
    }

    @Override
    public List<Carteira> findByDataInicio(LocalDate dataInicio) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE dataInicio = ?";
        return listBySql(sql, Date.valueOf(dataInicio));
    }

    @Override
    public List<Carteira> findByIdUsuario(Integer idUsuario) {
        String sql = "SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE id_usuario = ?";
        return listBySql(sql, idUsuario);
    }

    @Override
    public List<Carteira> search(Carteira f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Carteira> search(Carteira f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_carteira, nome, tipo, dataInicio, id_usuario FROM Carteira WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getNome() != null && !f.getNome().isEmpty()) {
            sb.append(" AND nome = ?");
            params.add(f.getNome());
        }
        if (f.getTipo() != null && !f.getTipo().isEmpty()) {
            sb.append(" AND tipo = ?");
            params.add(f.getTipo());
        }
        if (f.getDataInicio() != null) {
            sb.append(" AND dataInicio = ?");
            params.add(Date.valueOf(f.getDataInicio()));
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