package dao.impl;

import conexao.ConnectionFactory;
import dao.api.DocumentoDao;
import exception.DocumentoException;
import infra.Logger;
import model.Documento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDaoNativeImpl implements DocumentoDao {
    private Documento map(ResultSet rs, boolean withBlobs) throws SQLException {
        Documento d = new Documento();
        d.setIdDocumento(rs.getInt("id_documento"));
        d.setNome(rs.getString("nome"));
        if (withBlobs) {
            d.setArquivo(rs.getBytes("arquivo"));
            d.setFoto(rs.getBytes("foto"));
            d.setVideo(rs.getBytes("video"));
        }
        Date dt = rs.getDate("data");
        if (dt != null) {
            d.setData(dt.toLocalDate());
        }
        d.setIdUsuario((Integer) rs.getObject("id_usuario"));
        return d;
    }

    @Override
    public void create(Documento documento) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Documento (nome, arquivo, foto, video, data, id_usuario) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, documento.getNome());
                ps.setBytes(2, documento.getArquivo());
                ps.setBytes(3, documento.getFoto());
                ps.setBytes(4, documento.getVideo());
                if (documento.getData() != null) {
                    ps.setDate(5, Date.valueOf(documento.getData()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
                if (documento.getIdUsuario() != null) {
                    ps.setInt(6, documento.getIdUsuario());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("DocumentoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("DocumentoDaoNativeImpl.create - erro", e);
            throw new DocumentoException("Erro ao criar Documento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Documento update(Documento documento) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Documento SET nome=?, arquivo=?, foto=?, video=?, data=?, id_usuario=? WHERE id_documento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, documento.getNome());
                ps.setBytes(2, documento.getArquivo());
                ps.setBytes(3, documento.getFoto());
                ps.setBytes(4, documento.getVideo());
                if (documento.getData() != null) {
                    ps.setDate(5, Date.valueOf(documento.getData()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
                if (documento.getIdUsuario() != null) {
                    ps.setInt(6, documento.getIdUsuario());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.setInt(7, documento.getIdDocumento());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new DocumentoException("Documento não encontrado: id=" + documento.getIdDocumento());
                }
            }
            conn.commit();
            Logger.info("DocumentoDaoNativeImpl.update - sucesso");
            return findById(documento.getIdDocumento());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("DocumentoDaoNativeImpl.update - erro", e);
            throw new DocumentoException("Erro ao atualizar Documento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Documento WHERE id_documento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new DocumentoException("Documento não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("DocumentoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("DocumentoDaoNativeImpl.deleteById - erro", e);
            throw new DocumentoException("Erro ao deletar Documento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Documento findById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE id_documento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("DocumentoDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new DocumentoException("Documento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findById - erro", e);
            throw new DocumentoException("Documento não encontrado: id=" + id, e);
        }
    }

    @Override
    public Documento findWithBlobsById(Integer id) throws DocumentoException {
        Logger.info("DocumentoDaoNativeImpl.findWithBlobsById - inicio");
        String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE id_documento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("DocumentoDaoNativeImpl.findWithBlobsById - sucesso");
                    return map(rs, true);
                }
            }
            throw new DocumentoException("Documento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findWithBlobsById - erro", e);
            throw new DocumentoException("Documento não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Documento> findAll() {
        Logger.info("DocumentoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("DocumentoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findAll(int page, int size) {
        Logger.info("DocumentoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento LIMIT ? OFFSET ?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByNome(String nome) {
        Logger.info("DocumentoDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE nome=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByArquivo(byte[] arquivo) {
        Logger.info("DocumentoDaoNativeImpl.findByArquivo - inicio");
        String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE arquivo=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, arquivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByArquivo - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByArquivo - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByFoto(byte[] foto) {
        Logger.info("DocumentoDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE foto=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByVideo(byte[] video) {
        Logger.info("DocumentoDaoNativeImpl.findByVideo - inicio");
        String sql = "SELECT id_documento, nome, arquivo, foto, video, data, id_usuario FROM Documento WHERE video=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, video);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByVideo - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByVideo - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByData(java.time.LocalDate data) {
        Logger.info("DocumentoDaoNativeImpl.findByData - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE data=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByData - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByData - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> findByIdUsuario(Integer idUsuario) {
        Logger.info("DocumentoDaoNativeImpl.findByIdUsuario - inicio");
        String sql = "SELECT id_documento, nome, data, id_usuario FROM Documento WHERE id_usuario=?";
        List<Documento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("DocumentoDaoNativeImpl.findByIdUsuario - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.findByIdUsuario - erro", e);
        }
        return list;
    }

    @Override
    public List<Documento> search(Documento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Documento> search(Documento filtro, int page, int size) {
        Logger.info("DocumentoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_documento, nome, data, id_usuario FROM Documento WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getArquivo() != null) {
            sb.append(" AND arquivo=?");
            params.add(filtro.getArquivo());
        }
        if (filtro.getFoto() != null) {
            sb.append(" AND foto=?");
            params.add(filtro.getFoto());
        }
        if (filtro.getVideo() != null) {
            sb.append(" AND video=?");
            params.add(filtro.getVideo());
        }
        if (filtro.getData() != null) {
            sb.append(" AND data=?");
            params.add(Date.valueOf(filtro.getData()));
        }
        if (filtro.getIdUsuario() != null) {
            sb.append(" AND id_usuario=?");
            params.add(filtro.getIdUsuario());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Documento> list = new ArrayList<>();
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
            Logger.info("DocumentoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("DocumentoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
