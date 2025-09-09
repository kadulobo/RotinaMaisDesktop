package dao.impl;

import dao.api.AlimentacaoIngredienteDao;
import exception.AlimentacaoIngredienteException;
import infra.Logger;
import model.AlimentacaoIngrediente;

import conexao.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-based implementation of AlimentacaoIngredienteDao.
 */
public class AlimentacaoIngredienteDaoNativeImpl implements AlimentacaoIngredienteDao {
    private AlimentacaoIngrediente map(ResultSet rs) throws SQLException {
        AlimentacaoIngrediente ai = new AlimentacaoIngrediente();
        ai.setIdAlimentacaoIngrediente(rs.getInt("id_alimentacao_ingrediente"));
        ai.setQuantidade(rs.getInt("quantidade"));
        ai.setIdAlimentacao(rs.getInt("id_alimentacao"));
        ai.setIdIngrediente(rs.getInt("id_ingrediente"));
        return ai;
    }

    @Override
    public void create(AlimentacaoIngrediente e) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.create");
        String sql = "INSERT INTO Alimentacao_Ingrediente (quantidade, id_alimentacao, id_ingrediente) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getQuantidade());
                ps.setInt(2, e.getIdAlimentacao());
                ps.setInt(3, e.getIdIngrediente());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.create - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao criar AlimentacaoIngrediente", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(AlimentacaoIngrediente e) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.update");
        String sql = "UPDATE Alimentacao_Ingrediente SET quantidade=?, id_alimentacao=?, id_ingrediente=? WHERE id_alimentacao_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getQuantidade());
                ps.setInt(2, e.getIdAlimentacao());
                ps.setInt(3, e.getIdIngrediente());
                ps.setInt(4, e.getIdAlimentacaoIngrediente());
                if (ps.executeUpdate() == 0) {
                    throw new AlimentacaoIngredienteException("AlimentacaoIngrediente nao encontrado: id=" + e.getIdAlimentacaoIngrediente());
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.update - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao atualizar AlimentacaoIngrediente", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("AlimentacaoIngredienteDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Alimentacao_Ingrediente WHERE id_alimentacao_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 0) {
                    throw new AlimentacaoIngredienteException("AlimentacaoIngrediente nao encontrado: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.deleteById - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao deletar AlimentacaoIngrediente", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public AlimentacaoIngrediente findById(Integer id) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE id_alimentacao_ingrediente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findById - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao buscar AlimentacaoIngrediente", ex);
        }
        throw new AlimentacaoIngredienteException("AlimentacaoIngrediente nao encontrado: id=" + id);
    }

    @Override
    public List<AlimentacaoIngrediente> findAll() {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<AlimentacaoIngrediente> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findAll - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao listar AlimentacaoIngrediente", ex);
        }
    }

    @Override
    public List<AlimentacaoIngrediente> findAll(int page, int size) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<AlimentacaoIngrediente> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findAll(page) - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao paginar AlimentacaoIngrediente", ex);
        }
    }

    @Override
    public List<AlimentacaoIngrediente> findByQuantidade(Integer quantidade) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE quantidade = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantidade);
            try (ResultSet rs = ps.executeQuery()) {
                List<AlimentacaoIngrediente> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findByQuantidade - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao buscar por quantidade", ex);
        }
    }

    @Override
    public List<AlimentacaoIngrediente> findByIdAlimentacao(Integer idAlimentacao) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE id_alimentacao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAlimentacao);
            try (ResultSet rs = ps.executeQuery()) {
                List<AlimentacaoIngrediente> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findByIdAlimentacao - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao buscar por idAlimentacao", ex);
        }
    }

    @Override
    public List<AlimentacaoIngrediente> findByIdIngrediente(Integer idIngrediente) {
        String sql = "SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE id_ingrediente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idIngrediente);
            try (ResultSet rs = ps.executeQuery()) {
                List<AlimentacaoIngrediente> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.findByIdIngrediente - erro", ex);
            throw new AlimentacaoIngredienteException("Erro ao buscar por idIngrediente", ex);
        }
    }

    @Override
    public List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<AlimentacaoIngrediente> search(AlimentacaoIngrediente f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_alimentacao_ingrediente, quantidade, id_alimentacao, id_ingrediente FROM Alimentacao_Ingrediente WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getQuantidade() != null) {
            sb.append(" AND quantidade = ?");
            params.add(f.getQuantidade());
        }
        if (f.getIdAlimentacao() != null) {
            sb.append(" AND id_alimentacao = ?");
            params.add(f.getIdAlimentacao());
        }
        if (f.getIdIngrediente() != null) {
            sb.append(" AND id_ingrediente = ?");
            params.add(f.getIdIngrediente());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<AlimentacaoIngrediente> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("AlimentacaoIngredienteDaoNativeImpl.search - erro", ex);
            throw new AlimentacaoIngredienteException("Erro na busca de AlimentacaoIngrediente", ex);
        }
    }
}