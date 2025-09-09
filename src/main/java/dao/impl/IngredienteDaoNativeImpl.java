package dao.impl;

import conexao.ConnectionFactory;
import dao.api.IngredienteDao;
import exception.IngredienteException;
import infra.Logger;
import model.Ingrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC de {@link IngredienteDao} que evita o uso de JPA
 * {@link javax.persistence.EntityManager}. Todos os comandos SQL são
 * executados diretamente via {@link java.sql.Connection} obtida do
 * {@link ConnectionFactory}, seguindo o padrão demonstrado em
 * {@code UsuarioDaoNativeImpl}.
 */
public class IngredienteDaoNativeImpl implements IngredienteDao {

    private Ingrediente mapIngrediente(ResultSet rs, boolean withFoto) throws SQLException {
        Ingrediente i = new Ingrediente();
        i.setIdIngrediente(rs.getInt("id_ingrediente"));
        i.setNome(rs.getString("nome"));
        i.setDescricao(rs.getString("descricao"));
        if (withFoto) {
            i.setFoto(rs.getBytes("foto"));
        }
        return i;
    }

    @Override
    public void create(Ingrediente ingrediente) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Ingrediente (nome, descricao, foto) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ingrediente.getNome());
                ps.setString(2, ingrediente.getDescricao());
                ps.setBytes(3, ingrediente.getFoto());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("IngredienteDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("IngredienteDaoNativeImpl.create - erro", e);
            throw new IngredienteException("Erro ao criar Ingrediente", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Ingrediente update(Ingrediente ingrediente) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.update - inicio");
        String sql = "UPDATE Ingrediente SET nome=?, descricao=?, foto=? WHERE id_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ingrediente.getNome());
                ps.setString(2, ingrediente.getDescricao());
                ps.setBytes(3, ingrediente.getFoto());
                ps.setInt(4, ingrediente.getIdIngrediente());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new IngredienteException("Ingrediente não encontrado: id=" + ingrediente.getIdIngrediente());
                }
            }
            conn.commit();
            Logger.info("IngredienteDaoNativeImpl.update - sucesso");
            return findById(ingrediente.getIdIngrediente());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("IngredienteDaoNativeImpl.update - erro", e);
            throw new IngredienteException("Erro ao atualizar Ingrediente", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Ingrediente WHERE id_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new IngredienteException("Ingrediente não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("IngredienteDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("IngredienteDaoNativeImpl.deleteById - erro", e);
            throw new IngredienteException("Erro ao deletar Ingrediente", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Ingrediente findById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE id_ingrediente=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("IngredienteDaoNativeImpl.findById - sucesso");
                    return mapIngrediente(rs, false);
                }
            }
            throw new IngredienteException("Ingrediente não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findById - erro", e);
            throw new IngredienteException("Ingrediente não encontrado: id=" + id, e);
        }
    }

    @Override
    public Ingrediente findWithFotoById(Integer id) throws IngredienteException {
        Logger.info("IngredienteDaoNativeImpl.findWithFotoById - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao, foto FROM Ingrediente WHERE id_ingrediente=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("IngredienteDaoNativeImpl.findWithFotoById - sucesso");
                    return mapIngrediente(rs, true);
                }
            }
            throw new IngredienteException("Ingrediente não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findWithFotoById - erro", e);
            throw new IngredienteException("Ingrediente não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Ingrediente> findAll() {
        Logger.info("IngredienteDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente";
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapIngrediente(rs, false));
            }
            Logger.info("IngredienteDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Ingrediente> findAll(int page, int size) {
        Logger.info("IngredienteDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente LIMIT ? OFFSET ?";
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIngrediente(rs, false));
                }
            }
            Logger.info("IngredienteDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Ingrediente> findByFoto(byte[] foto) {
        Logger.info("IngredienteDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao, foto FROM Ingrediente WHERE foto=?";
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIngrediente(rs, true));
                }
            }
            Logger.info("IngredienteDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Ingrediente> findByNome(String nome) {
        Logger.info("IngredienteDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE nome=?";
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIngrediente(rs, false));
                }
            }
            Logger.info("IngredienteDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Ingrediente> findByDescricao(String descricao) {
        Logger.info("IngredienteDaoNativeImpl.findByDescricao - inicio");
        String sql = "SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE descricao=?";
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIngrediente(rs, false));
                }
            }
            Logger.info("IngredienteDaoNativeImpl.findByDescricao - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.findByDescricao - erro", e);
        }
        return list;
    }

    @Override
    public List<Ingrediente> search(Ingrediente filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Ingrediente> search(Ingrediente filtro, int page, int size) {
        Logger.info("IngredienteDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_ingrediente, nome, descricao FROM Ingrediente WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getFoto() != null) {
            sb.append(" AND foto=?");
            params.add(filtro.getFoto());
        }
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Ingrediente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapIngrediente(rs, false));
                }
            }
            Logger.info("IngredienteDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("IngredienteDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
