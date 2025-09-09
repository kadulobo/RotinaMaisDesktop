// path: src/main/java/dao/impl/FornecedorDaoNativeImpl.java
package dao.impl;

import conexao.ConnectionFactory;
import dao.api.FornecedorDao;
import exception.FornecedorException;
import infra.Logger;
import model.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link FornecedorDao}.
 */
public class FornecedorDaoNativeImpl implements FornecedorDao {

    private Fornecedor map(ResultSet rs, boolean withFoto) throws SQLException {
        Fornecedor f = new Fornecedor();
        f.setIdFornecedor(rs.getInt("id_fornecedor"));
        f.setNome(rs.getString("nome"));
        if (withFoto) {
            f.setFoto(rs.getBytes("foto"));
        }
        f.setEndereco(rs.getString("endereco"));
        f.setOnline((Boolean) rs.getObject("online"));
        return f;
    }

    @Override
    public void create(Fornecedor fornecedor) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Fornecedor (nome, foto, endereco, online) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fornecedor.getNome());
                ps.setBytes(2, fornecedor.getFoto());
                ps.setString(3, fornecedor.getEndereco());
                if (fornecedor.getOnline() != null) {
                    ps.setBoolean(4, fornecedor.getOnline());
                } else {
                    ps.setNull(4, Types.BOOLEAN);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("FornecedorDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("FornecedorDaoNativeImpl.create - erro", e);
            throw new FornecedorException("Erro ao criar Fornecedor", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Fornecedor update(Fornecedor fornecedor) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.update - inicio");
        String sql = "UPDATE Fornecedor SET nome=?, foto=?, endereco=?, online=? WHERE id_fornecedor=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fornecedor.getNome());
                ps.setBytes(2, fornecedor.getFoto());
                ps.setString(3, fornecedor.getEndereco());
                if (fornecedor.getOnline() != null) {
                    ps.setBoolean(4, fornecedor.getOnline());
                } else {
                    ps.setNull(4, Types.BOOLEAN);
                }
                ps.setInt(5, fornecedor.getIdFornecedor());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new FornecedorException("Fornecedor não encontrado: id=" + fornecedor.getIdFornecedor());
                }
            }
            conn.commit();
            Logger.info("FornecedorDaoNativeImpl.update - sucesso");
            return findById(fornecedor.getIdFornecedor());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("FornecedorDaoNativeImpl.update - erro", e);
            throw new FornecedorException("Erro ao atualizar Fornecedor", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Fornecedor WHERE id_fornecedor=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new FornecedorException("Fornecedor não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("FornecedorDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("FornecedorDaoNativeImpl.deleteById - erro", e);
            throw new FornecedorException("Erro ao deletar Fornecedor", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Fornecedor findById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE id_fornecedor=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("FornecedorDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new FornecedorException("Fornecedor não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findById - erro", e);
            throw new FornecedorException("Fornecedor não encontrado: id=" + id, e);
        }
    }

    @Override
    public Fornecedor findWithFotoById(Integer id) throws FornecedorException {
        Logger.info("FornecedorDaoNativeImpl.findWithFotoById - inicio");
        String sql = "SELECT id_fornecedor, nome, foto, endereco, online FROM Fornecedor WHERE id_fornecedor=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("FornecedorDaoNativeImpl.findWithFotoById - sucesso");
                    return map(rs, true);
                }
            }
            throw new FornecedorException("Fornecedor não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findWithFotoById - erro", e);
            throw new FornecedorException("Fornecedor não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Fornecedor> findAll() {
        Logger.info("FornecedorDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("FornecedorDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> findAll(int page, int size) {
        Logger.info("FornecedorDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor LIMIT ? OFFSET ?";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("FornecedorDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> findByNome(String nome) {
        Logger.info("FornecedorDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE nome=?";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("FornecedorDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> findByFoto(byte[] foto) {
        Logger.info("FornecedorDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_fornecedor, nome, foto, endereco, online FROM Fornecedor WHERE foto=?";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("FornecedorDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> findByEndereco(String endereco) {
        Logger.info("FornecedorDaoNativeImpl.findByEndereco - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE endereco=?";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, endereco);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("FornecedorDaoNativeImpl.findByEndereco - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findByEndereco - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> findByOnline(Boolean online) {
        Logger.info("FornecedorDaoNativeImpl.findByOnline - inicio");
        String sql = "SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE online=?";
        List<Fornecedor> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (online != null) {
                ps.setBoolean(1, online);
            } else {
                ps.setNull(1, Types.BOOLEAN);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("FornecedorDaoNativeImpl.findByOnline - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.findByOnline - erro", e);
        }
        return list;
    }

    @Override
    public List<Fornecedor> search(Fornecedor filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Fornecedor> search(Fornecedor filtro, int page, int size) {
        Logger.info("FornecedorDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_fornecedor, nome, endereco, online FROM Fornecedor WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getFoto() != null) {
            sb.append(" AND foto=?");
            params.add(filtro.getFoto());
        }
        if (filtro.getEndereco() != null && !filtro.getEndereco().isEmpty()) {
            sb.append(" AND endereco=?");
            params.add(filtro.getEndereco());
        }
        if (filtro.getOnline() != null) {
            sb.append(" AND online=?");
            params.add(filtro.getOnline());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Fornecedor> list = new ArrayList<>();
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
            Logger.info("FornecedorDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("FornecedorDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}

