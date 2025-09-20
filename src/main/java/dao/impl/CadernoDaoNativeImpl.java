package dao.impl;

import conexao.ConnectionFactory;
import dao.api.CadernoDao;
import exception.CadernoException;
import infra.Logger;
import model.Caderno;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CadernoDaoNativeImpl implements CadernoDao {

    private Caderno map(ResultSet rs) throws SQLException {
        Caderno c = new Caderno();
        c.setIdCaderno(rs.getInt("id_caderno"));
        c.setNomeIa(rs.getString("nome_ia"));
        c.setTitulo(rs.getString("titulo"));
        c.setObjetivo(rs.getString("objetivo"));
        c.setComando(rs.getString("comando"));
        c.setResultado(rs.getString("resultado"));
        Date d = rs.getDate("data");
        c.setData(d != null ? d.toLocalDate() : null);
        c.setResultadoImagem(rs.getBytes("resultado_imagem"));
        c.setResultadoVideo(rs.getBytes("resultado_video"));
        int idUsuario = rs.getInt("id_usuario");
        c.setIdUsuario(rs.wasNull() ? null : idUsuario);
        int idCategoria = rs.getInt("id_categoria");
        c.setIdCategoria(rs.wasNull() ? null : idCategoria);
        return c;
    }

    @Override
    public void create(Caderno caderno) {
        Logger.info("CadernoDao.create");
        String sql = "INSERT INTO Caderno (nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, caderno.getNomeIa());
                ps.setString(2, caderno.getTitulo());
                ps.setString(3, caderno.getObjetivo());
                ps.setString(4, caderno.getComando());
                ps.setString(5, caderno.getResultado());
                if (caderno.getData() != null) ps.setDate(6, Date.valueOf(caderno.getData())); else ps.setNull(6, java.sql.Types.DATE);
                if (caderno.getResultadoImagem() != null) ps.setBytes(7, caderno.getResultadoImagem()); else ps.setNull(7, java.sql.Types.BINARY);
                if (caderno.getResultadoVideo() != null) ps.setBytes(8, caderno.getResultadoVideo()); else ps.setNull(8, java.sql.Types.BINARY);
                if (caderno.getIdUsuario() != null) ps.setInt(9, caderno.getIdUsuario()); else ps.setNull(9, java.sql.Types.INTEGER);
                if (caderno.getIdCategoria() != null) ps.setInt(10, caderno.getIdCategoria()); else ps.setNull(10, java.sql.Types.INTEGER);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback create", re); }
            }
            Logger.error("CadernoDao.create - erro", ex);
            throw new CadernoException("Erro ao criar Caderno", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Caderno caderno) {
        Logger.info("CadernoDao.update");
        String sql = "UPDATE Caderno SET nome_ia=?, titulo=?, objetivo=?, comando=?, resultado=?, data=?, resultado_imagem=?, resultado_video=?, id_usuario=?, id_categoria=? WHERE id_caderno=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, caderno.getNomeIa());
                ps.setString(2, caderno.getTitulo());
                ps.setString(3, caderno.getObjetivo());
                ps.setString(4, caderno.getComando());
                ps.setString(5, caderno.getResultado());
                if (caderno.getData() != null) ps.setDate(6, Date.valueOf(caderno.getData())); else ps.setNull(6, java.sql.Types.DATE);
                if (caderno.getResultadoImagem() != null) ps.setBytes(7, caderno.getResultadoImagem()); else ps.setNull(7, java.sql.Types.BINARY);
                if (caderno.getResultadoVideo() != null) ps.setBytes(8, caderno.getResultadoVideo()); else ps.setNull(8, java.sql.Types.BINARY);
                if (caderno.getIdUsuario() != null) ps.setInt(9, caderno.getIdUsuario()); else ps.setNull(9, java.sql.Types.INTEGER);
                if (caderno.getIdCategoria() != null) ps.setInt(10, caderno.getIdCategoria()); else ps.setNull(10, java.sql.Types.INTEGER);
                ps.setInt(11, caderno.getIdCaderno());
                int upd = ps.executeUpdate();
                if (upd == 0) throw new CadernoException("Caderno nao encontrado: id=" + caderno.getIdCaderno());
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback update", re); }
            }
            Logger.error("CadernoDao.update - erro", ex);
            throw new CadernoException("Erro ao atualizar Caderno", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("CadernoDao.deleteById");
        String sql = "DELETE FROM Caderno WHERE id_caderno=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int del = ps.executeUpdate();
                if (del == 0) throw new CadernoException("Caderno nao encontrado: id=" + id);
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException re) { Logger.error("Rollback delete", re); }
            }
            Logger.error("CadernoDao.delete - erro", ex);
            throw new CadernoException("Erro ao deletar Caderno", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Caderno findById(Integer id) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE id_caderno = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.error("CadernoDao.findById - erro", ex);
        }
        return null;
    }

    private List<Caderno> listBySql(String sql, Object... params) {
        List<Caderno> list = new ArrayList<>();
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
        } catch (SQLException ex) {
            Logger.error("CadernoDao.listBySql - erro", ex);
        }
        return list;
    }

    @Override
    public List<Caderno> findAll() {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno";
        return listBySql(sql);
    }

    @Override
    public List<Caderno> findAll(int page, int size) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno LIMIT ? OFFSET ?";
        return listBySql(sql, size, page * size);
    }

    @Override
    public List<Caderno> findByTitulo(String titulo) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE titulo = ?";
        return listBySql(sql, titulo);
    }

    @Override
    public List<Caderno> findByData(LocalDate data) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE data = ?";
        return listBySql(sql, Date.valueOf(data));
    }

    @Override
    public List<Caderno> findByIdUsuario(Integer idUsuario) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE id_usuario = ?";
        return listBySql(sql, idUsuario);
    }

    @Override
    public List<Caderno> findByIdCategoria(Integer idCategoria) {
        String sql = "SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE id_categoria = ?";
        return listBySql(sql, idCategoria);
    }

    @Override
    public List<Caderno> search(Caderno filtro) {
        return search(filtro, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Caderno> search(Caderno filtro, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_caderno, nome_ia, titulo, objetivo, comando, resultado, data, resultado_imagem, resultado_video, id_usuario, id_categoria FROM Caderno WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getTitulo() != null && !filtro.getTitulo().isEmpty()) {
            sb.append(" AND titulo = ?");
            params.add(filtro.getTitulo());
        }
        if (filtro.getNomeIa() != null && !filtro.getNomeIa().isEmpty()) {
            sb.append(" AND nome_ia = ?");
            params.add(filtro.getNomeIa());
        }
        if (filtro.getData() != null) {
            sb.append(" AND data = ?");
            params.add(Date.valueOf(filtro.getData()));
        }
        if (filtro.getIdUsuario() != null) {
            sb.append(" AND id_usuario = ?");
            params.add(filtro.getIdUsuario());
        }
        if (filtro.getIdCategoria() != null) {
            sb.append(" AND id_categoria = ?");
            params.add(filtro.getIdCategoria());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        return listBySql(sb.toString(), params.toArray());
    }
}
