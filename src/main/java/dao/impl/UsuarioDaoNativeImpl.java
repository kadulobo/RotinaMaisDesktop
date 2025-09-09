// path: src/main/java/dao/impl/UsuarioDaoNativeImpl.java
package dao.impl;

import conexao.ConnectionFactory;
import dao.api.UsuarioDao;
import exception.UsuarioException;
import infra.Logger;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoNativeImpl implements UsuarioDao {
    private Usuario mapUsuario(ResultSet rs, boolean withFoto) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setSenha(rs.getString("senha"));
        if (withFoto) {
            u.setFoto(rs.getBytes("foto"));
        }
        u.setEmail(rs.getString("email"));
        return u;
    }

    @Override
    public void create(Usuario usuario) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Usuario (nome, senha, foto, email) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, usuario.getNome());
                ps.setString(2, usuario.getSenha());
                ps.setBytes(3, usuario.getFoto());
                ps.setString(4, usuario.getEmail());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("UsuarioDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("UsuarioDaoNativeImpl.create - erro", e);
            throw new UsuarioException("Erro ao criar Usuario", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Usuario update(Usuario usuario) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.update - inicio");
        String sql = "UPDATE Usuario SET nome=?, senha=?, foto=?, email=? WHERE id_usuario=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, usuario.getNome());
                ps.setString(2, usuario.getSenha());
                ps.setBytes(3, usuario.getFoto());
                ps.setString(4, usuario.getEmail());
                ps.setInt(5, usuario.getIdUsuario());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new UsuarioException("Usuario não encontrado: id=" + usuario.getIdUsuario());
                }
            }
            conn.commit();
            Logger.info("UsuarioDaoNativeImpl.update - sucesso");
            return findById(usuario.getIdUsuario());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("UsuarioDaoNativeImpl.update - erro", e);
            throw new UsuarioException("Erro ao atualizar Usuario", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Usuario WHERE id_usuario=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new UsuarioException("Usuario não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("UsuarioDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("UsuarioDaoNativeImpl.deleteById - erro", e);
            throw new UsuarioException("Erro ao deletar Usuario", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Usuario findById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE id_usuario=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("UsuarioDaoNativeImpl.findById - sucesso");
                    return mapUsuario(rs, false);
                }
            }
            throw new UsuarioException("Usuario não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findById - erro", e);
            throw new UsuarioException("Usuario não encontrado: id=" + id, e);
        }
    }

    @Override
    public Usuario findWithBlobsById(Integer id) throws UsuarioException {
        Logger.info("UsuarioDaoNativeImpl.findWithBlobsById - inicio");
        String sql = "SELECT id_usuario, nome, senha, foto, email FROM Usuario WHERE id_usuario=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("UsuarioDaoNativeImpl.findWithBlobsById - sucesso");
                    return mapUsuario(rs, true);
                }
            }
            throw new UsuarioException("Usuario não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findWithBlobsById - erro", e);
            throw new UsuarioException("Usuario não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Usuario> findAll() {
        Logger.info("UsuarioDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapUsuario(rs, false));
            }
            Logger.info("UsuarioDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> findAll(int page, int size) {
        Logger.info("UsuarioDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario LIMIT ? OFFSET ?";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, false));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> findByNome(String nome) {
        Logger.info("UsuarioDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE nome=?";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, false));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> findBySenha(String senha) {
        Logger.info("UsuarioDaoNativeImpl.findBySenha - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE senha=?";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, senha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, false));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.findBySenha - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findBySenha - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> findByEmail(String email) {
        Logger.info("UsuarioDaoNativeImpl.findByEmail - inicio");
        String sql = "SELECT id_usuario, nome, senha, email FROM Usuario WHERE email=?";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, false));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.findByEmail - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findByEmail - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> findByFoto(byte[] foto) {
        Logger.info("UsuarioDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_usuario, nome, senha, foto, email FROM Usuario WHERE foto=?";
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, true));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Usuario> search(Usuario filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Usuario> search(Usuario filtro, int page, int size) {
        Logger.info("UsuarioDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_usuario, nome, senha, email FROM Usuario WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getSenha() != null && !filtro.getSenha().isEmpty()) {
            sb.append(" AND senha=?");
            params.add(filtro.getSenha());
        }
        if (filtro.getEmail() != null && !filtro.getEmail().isEmpty()) {
            sb.append(" AND email=?");
            params.add(filtro.getEmail());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Usuario> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUsuario(rs, false));
                }
            }
            Logger.info("UsuarioDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("UsuarioDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
