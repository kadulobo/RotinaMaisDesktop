package dao.impl;

import conexao.ConnectionFactory;
import dao.api.CategoriaDao;
import exception.CategoriaException;
import infra.Logger;
import model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoNativeImpl implements CategoriaDao {
    private Categoria map(ResultSet rs, boolean withFoto) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNome(rs.getString("nome"));
        c.setDescricao(rs.getString("descricao"));
        if (withFoto) {
            c.setFoto(rs.getBytes("foto"));
        }
        Date dt = rs.getDate("data_criacao");
        if (dt != null) {
            c.setDataCriacao(dt.toLocalDate());
        }
        return c;
    }

    @Override
    public void create(Categoria categoria) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Categoria (nome, descricao, foto, data_criacao) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, categoria.getNome());
                ps.setString(2, categoria.getDescricao());
                ps.setBytes(3, categoria.getFoto());
                if (categoria.getDataCriacao() != null) {
                    ps.setDate(4, Date.valueOf(categoria.getDataCriacao()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("CategoriaDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("CategoriaDaoNativeImpl.create - erro", e);
            throw new CategoriaException("Erro ao criar Categoria", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Categoria update(Categoria categoria) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.update - inicio");
        String sql = "UPDATE Categoria SET nome=?, descricao=?, foto=?, data_criacao=? WHERE id_categoria=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, categoria.getNome());
                ps.setString(2, categoria.getDescricao());
                ps.setBytes(3, categoria.getFoto());
                if (categoria.getDataCriacao() != null) {
                    ps.setDate(4, Date.valueOf(categoria.getDataCriacao()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                ps.setInt(5, categoria.getIdCategoria());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new CategoriaException("Categoria não encontrada: id=" + categoria.getIdCategoria());
                }
            }
            conn.commit();
            Logger.info("CategoriaDaoNativeImpl.update - sucesso");
            return findById(categoria.getIdCategoria());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("CategoriaDaoNativeImpl.update - erro", e);
            throw new CategoriaException("Erro ao atualizar Categoria", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Categoria WHERE id_categoria=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new CategoriaException("Categoria não encontrada: id=" + id);
                }
            }
            conn.commit();
            Logger.info("CategoriaDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("CategoriaDaoNativeImpl.deleteById - erro", e);
            throw new CategoriaException("Erro ao deletar Categoria", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Categoria findById(Integer id) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria WHERE id_categoria=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("CategoriaDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new CategoriaException("Categoria não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findById - erro", e);
            throw new CategoriaException("Categoria não encontrada: id=" + id, e);
        }
    }

    @Override
    public Categoria findWithBlobsById(Integer id) throws CategoriaException {
        Logger.info("CategoriaDaoNativeImpl.findWithBlobsById - inicio");
        String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE id_categoria=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("CategoriaDaoNativeImpl.findWithBlobsById - sucesso");
                    return map(rs, true);
                }
            }
            throw new CategoriaException("Categoria não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findWithBlobsById - erro", e);
            throw new CategoriaException("Categoria não encontrada: id=" + id, e);
        }
    }

    @Override
    public List<Categoria> findAll() {
        Logger.info("CategoriaDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("CategoriaDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> findAll(int page, int size) {
        Logger.info("CategoriaDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria LIMIT ? OFFSET ?";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("CategoriaDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> findByNome(String nome) {
        Logger.info("CategoriaDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria WHERE nome=?";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("CategoriaDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> findByDescricao(String descricao) {
        Logger.info("CategoriaDaoNativeImpl.findByDescricao - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria WHERE descricao=?";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("CategoriaDaoNativeImpl.findByDescricao - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findByDescricao - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> findByDataCriacao(java.time.LocalDate dataCriacao) {
        Logger.info("CategoriaDaoNativeImpl.findByDataCriacao - inicio");
        String sql = "SELECT id_categoria, nome, descricao, data_criacao FROM Categoria WHERE data_criacao=?";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dataCriacao));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("CategoriaDaoNativeImpl.findByDataCriacao - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findByDataCriacao - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> findByFoto(byte[] foto) {
        Logger.info("CategoriaDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_categoria, nome, descricao, foto, data_criacao FROM Categoria WHERE foto=?";
        List<Categoria> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("CategoriaDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Categoria> search(Categoria filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Categoria> search(Categoria filtro, int page, int size) {
        Logger.info("CategoriaDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_categoria, nome, descricao, data_criacao FROM Categoria WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
        }
        if (filtro.getDataCriacao() != null) {
            sb.append(" AND data_criacao=?");
            params.add(Date.valueOf(filtro.getDataCriacao()));
        }
        if (filtro.getFoto() != null) {
            sb.append(" AND foto=?");
            params.add(filtro.getFoto());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Categoria> list = new ArrayList<>();
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
            Logger.info("CategoriaDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("CategoriaDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
