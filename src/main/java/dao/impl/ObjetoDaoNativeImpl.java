package dao.impl;

import conexao.ConnectionFactory;
import dao.api.ObjetoDao;
import exception.ObjetoException;
import infra.Logger;
import model.Objeto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ObjetoDaoNativeImpl implements ObjetoDao {

    private Objeto mapObjeto(ResultSet rs, boolean withFoto) throws SQLException {
        Objeto o = new Objeto();
        o.setIdObjeto(rs.getInt("id_objeto"));
        o.setNome(rs.getString("nome"));
        int tipo = rs.getInt("tipo");
        if (!rs.wasNull()) {
            o.setTipo(tipo);
        }
        o.setValor(rs.getBigDecimal("valor"));
        o.setDescricao(rs.getString("descricao"));
        if (withFoto) {
            o.setFoto(rs.getBytes("foto"));
        }
        return o;
    }

    @Override
    public void create(Objeto objeto) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Objeto (nome, tipo, valor, descricao, foto) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, objeto.getNome());
                if (objeto.getTipo() != null) {
                    ps.setInt(2, objeto.getTipo());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                ps.setBigDecimal(3, objeto.getValor());
                ps.setString(4, objeto.getDescricao());
                ps.setBytes(5, objeto.getFoto());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("ObjetoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("ObjetoDaoNativeImpl.create - erro", e);
            throw new ObjetoException("Erro ao criar Objeto", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Objeto update(Objeto objeto) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Objeto SET nome=?, tipo=?, valor=?, descricao=?, foto=? WHERE id_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, objeto.getNome());
                if (objeto.getTipo() != null) {
                    ps.setInt(2, objeto.getTipo());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                ps.setBigDecimal(3, objeto.getValor());
                ps.setString(4, objeto.getDescricao());
                ps.setBytes(5, objeto.getFoto());
                ps.setInt(6, objeto.getIdObjeto());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new ObjetoException("Objeto não encontrado: id=" + objeto.getIdObjeto());
                }
            }
            conn.commit();
            Logger.info("ObjetoDaoNativeImpl.update - sucesso");
            return findById(objeto.getIdObjeto());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("ObjetoDaoNativeImpl.update - erro", e);
            throw new ObjetoException("Erro ao atualizar Objeto", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Objeto WHERE id_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new ObjetoException("Objeto não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("ObjetoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("ObjetoDaoNativeImpl.deleteById - erro", e);
            throw new ObjetoException("Erro ao deletar Objeto", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Objeto findById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE id_objeto=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("ObjetoDaoNativeImpl.findById - sucesso");
                    return mapObjeto(rs, false);
                }
            }
            throw new ObjetoException("Objeto não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findById - erro", e);
            throw new ObjetoException("Objeto não encontrado: id=" + id, e);
        }
    }

    @Override
    public Objeto findWithFotoById(Integer id) throws ObjetoException {
        Logger.info("ObjetoDaoNativeImpl.findWithFotoById - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao, foto FROM Objeto WHERE id_objeto=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("ObjetoDaoNativeImpl.findWithFotoById - sucesso");
                    return mapObjeto(rs, true);
                }
            }
            throw new ObjetoException("Objeto não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findWithFotoById - erro", e);
            throw new ObjetoException("Objeto não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Objeto> findAll() {
        Logger.info("ObjetoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapObjeto(rs, false));
            }
            Logger.info("ObjetoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findAll(int page, int size) {
        Logger.info("ObjetoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto LIMIT ? OFFSET ?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findByNome(String nome) {
        Logger.info("ObjetoDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE nome=?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findByTipo(Integer tipo) {
        Logger.info("ObjetoDaoNativeImpl.findByTipo - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE tipo=?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findByTipo - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findByTipo - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findByValor(BigDecimal valor) {
        Logger.info("ObjetoDaoNativeImpl.findByValor - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE valor=?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findByValor - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findByValor - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findByDescricao(String descricao) {
        Logger.info("ObjetoDaoNativeImpl.findByDescricao - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE descricao=?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findByDescricao - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findByDescricao - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> findByFoto(byte[] foto) {
        Logger.info("ObjetoDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_objeto, nome, tipo, valor, descricao, foto FROM Objeto WHERE foto=?";
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, true));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Objeto> search(Objeto filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Objeto> search(Objeto filtro, int page, int size) {
        Logger.info("ObjetoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_objeto, nome, tipo, valor, descricao FROM Objeto WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getTipo() != null) {
            sb.append(" AND tipo=?");
            params.add(filtro.getTipo());
        }
        if (filtro.getValor() != null) {
            sb.append(" AND valor=?");
            params.add(filtro.getValor());
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
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
        List<Objeto> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapObjeto(rs, false));
                }
            }
            Logger.info("ObjetoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("ObjetoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
