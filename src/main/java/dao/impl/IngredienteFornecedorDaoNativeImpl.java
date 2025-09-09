package dao.impl;

import dao.api.IngredienteFornecedorDao;
import exception.IngredienteFornecedorException;
import infra.Logger;
import model.IngredienteFornecedor;

import conexao.ConnectionFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-based implementation of IngredienteFornecedorDao.
 */
public class IngredienteFornecedorDaoNativeImpl implements IngredienteFornecedorDao {
    private IngredienteFornecedor map(ResultSet rs) throws SQLException {
        IngredienteFornecedor obj = new IngredienteFornecedor();
        obj.setIdFornecedorIngrediente(rs.getInt("id_fornecedor_ingrediente"));
        obj.setValor(rs.getBigDecimal("valor"));
        Date d = rs.getDate("data");
        if (d != null) obj.setData(d.toLocalDate());
        obj.setIdFornecedor(rs.getInt("id_fornecedor"));
        obj.setIdIngrediente(rs.getInt("id_ingrediente"));
        return obj;
    }

    @Override
    public void create(IngredienteFornecedor e) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.create");
        String sql = "INSERT INTO Ingrediente_fornecedor (valor, data, id_fornecedor, id_ingrediente) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, e.getValor());
                ps.setDate(2, e.getData() != null ? Date.valueOf(e.getData()) : null);
                ps.setInt(3, e.getIdFornecedor());
                ps.setInt(4, e.getIdIngrediente());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("IngredienteFornecedorDaoNativeImpl.create - erro", ex);
            throw new IngredienteFornecedorException("Erro ao criar IngredienteFornecedor", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(IngredienteFornecedor e) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.update");
        String sql = "UPDATE Ingrediente_fornecedor SET valor=?, data=?, id_fornecedor=?, id_ingrediente=? WHERE id_fornecedor_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, e.getValor());
                ps.setDate(2, e.getData() != null ? Date.valueOf(e.getData()) : null);
                ps.setInt(3, e.getIdFornecedor());
                ps.setInt(4, e.getIdIngrediente());
                ps.setInt(5, e.getIdFornecedorIngrediente());
                if (ps.executeUpdate() == 0) {
                    throw new IngredienteFornecedorException("IngredienteFornecedor nao encontrado: id=" + e.getIdFornecedorIngrediente());
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("IngredienteFornecedorDaoNativeImpl.update - erro", ex);
            throw new IngredienteFornecedorException("Erro ao atualizar IngredienteFornecedor", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("IngredienteFornecedorDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Ingrediente_fornecedor WHERE id_fornecedor_ingrediente=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 0) {
                    throw new IngredienteFornecedorException("IngredienteFornecedor nao encontrado: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("IngredienteFornecedorDaoNativeImpl.deleteById - erro", ex);
            throw new IngredienteFornecedorException("Erro ao deletar IngredienteFornecedor", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public IngredienteFornecedor findById(Integer id) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE id_fornecedor_ingrediente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findById - erro", ex);
            throw new IngredienteFornecedorException("Erro ao buscar IngredienteFornecedor", ex);
        }
        throw new IngredienteFornecedorException("IngredienteFornecedor nao encontrado: id=" + id);
    }

    @Override
    public List<IngredienteFornecedor> findAll() {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<IngredienteFornecedor> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findAll - erro", ex);
            throw new IngredienteFornecedorException("Erro ao listar IngredienteFornecedor", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> findAll(int page, int size) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findAll(page) - erro", ex);
            throw new IngredienteFornecedorException("Erro ao paginar IngredienteFornecedor", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> findByValor(BigDecimal valor) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE valor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findByValor - erro", ex);
            throw new IngredienteFornecedorException("Erro ao buscar por valor", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> findByData(LocalDate data) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE data = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, data != null ? Date.valueOf(data) : null);
            try (ResultSet rs = ps.executeQuery()) {
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findByData - erro", ex);
            throw new IngredienteFornecedorException("Erro ao buscar por data", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> findByIdFornecedor(Integer idFornecedor) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE id_fornecedor = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFornecedor);
            try (ResultSet rs = ps.executeQuery()) {
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findByIdFornecedor - erro", ex);
            throw new IngredienteFornecedorException("Erro ao buscar por idFornecedor", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> findByIdIngrediente(Integer idIngrediente) {
        String sql = "SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE id_ingrediente = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idIngrediente);
            try (ResultSet rs = ps.executeQuery()) {
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.findByIdIngrediente - erro", ex);
            throw new IngredienteFornecedorException("Erro ao buscar por idIngrediente", ex);
        }
    }

    @Override
    public List<IngredienteFornecedor> search(IngredienteFornecedor f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<IngredienteFornecedor> search(IngredienteFornecedor f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_fornecedor_ingrediente, valor, data, id_fornecedor, id_ingrediente FROM Ingrediente_fornecedor WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getValor() != null) {
            sb.append(" AND valor = ?");
            params.add(f.getValor());
        }
        if (f.getData() != null) {
            sb.append(" AND data = ?");
            params.add(Date.valueOf(f.getData()));
        }
        if (f.getIdFornecedor() != null) {
            sb.append(" AND id_fornecedor = ?");
            params.add(f.getIdFornecedor());
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
                List<IngredienteFornecedor> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("IngredienteFornecedorDaoNativeImpl.search - erro", ex);
            throw new IngredienteFornecedorException("Erro na busca de IngredienteFornecedor", ex);
        }
    }
}