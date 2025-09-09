// path: src/main/java/dao/impl/CaixaDaoNativeImpl.java
package dao.impl;

import conexao.ConnectionFactory;
import dao.api.CaixaDao;
import exception.CaixaException;
import infra.Logger;
import model.Caixa;
import model.Usuario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação de {@link CaixaDao} utilizando JDBC em vez de {@code EntityManager}.
 * Este padrão segue o que foi aplicado em {@code UsuarioDaoNativeImpl}.
 */
public class CaixaDaoNativeImpl implements CaixaDao {

    private Caixa mapCaixa(ResultSet rs) throws SQLException {
        Caixa c = new Caixa();
        c.setIdCaixa(rs.getInt("id_caixa"));
        c.setNome(rs.getString("nome"));
        c.setReservaEmergencia(rs.getBigDecimal("reserva_emergencia"));
        c.setSalarioMedio(rs.getBigDecimal("salario_medio"));
        c.setValorTotal(rs.getBigDecimal("valor_total"));
        int idUsuario = rs.getInt("id_usuario");
        if (!rs.wasNull()) {
            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            c.setUsuario(u);
        }
        return c;
    }

    @Override
    public void create(Caixa caixa) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Caixa (nome, reserva_emergencia, salario_medio, valor_total, id_usuario) VALUES (?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, caixa.getNome());
                ps.setBigDecimal(2, caixa.getReservaEmergencia());
                ps.setBigDecimal(3, caixa.getSalarioMedio());
                ps.setBigDecimal(4, caixa.getValorTotal());
                if (caixa.getUsuario() != null && caixa.getUsuario().getIdUsuario() != null) {
                    ps.setInt(5, caixa.getUsuario().getIdUsuario());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("CaixaDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("CaixaDaoNativeImpl.create - erro", e);
            throw new CaixaException("Erro ao criar Caixa", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    public Caixa update(Caixa caixa) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.update - inicio");
        String sql = "UPDATE Caixa SET nome=?, reserva_emergencia=?, salario_medio=?, valor_total=?, id_usuario=? WHERE id_caixa=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, caixa.getNome());
                ps.setBigDecimal(2, caixa.getReservaEmergencia());
                ps.setBigDecimal(3, caixa.getSalarioMedio());
                ps.setBigDecimal(4, caixa.getValorTotal());
                if (caixa.getUsuario() != null && caixa.getUsuario().getIdUsuario() != null) {
                    ps.setInt(5, caixa.getUsuario().getIdUsuario());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setInt(6, caixa.getIdCaixa());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new CaixaException("Caixa não encontrada: id=" + caixa.getIdCaixa());
                }
            }
            conn.commit();
            Logger.info("CaixaDaoNativeImpl.update - sucesso");
            return findById(caixa.getIdCaixa());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("CaixaDaoNativeImpl.update - erro", e);
            throw new CaixaException("Erro ao atualizar Caixa", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Caixa WHERE id_caixa=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new CaixaException("Caixa não encontrada: id=" + id);
                }
            }
            conn.commit();
            Logger.info("CaixaDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("CaixaDaoNativeImpl.deleteById - erro", e);
            throw new CaixaException("Erro ao deletar Caixa", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    public Caixa findById(Integer id) throws CaixaException {
        Logger.info("CaixaDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE id_caixa=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("CaixaDaoNativeImpl.findById - sucesso");
                    return mapCaixa(rs);
                }
            }
            throw new CaixaException("Caixa não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findById - erro", e);
            throw new CaixaException("Caixa não encontrada: id=" + id, e);
        }
    }

    @Override
    public List<Caixa> findAll() {
        Logger.info("CaixaDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCaixa(rs));
            }
            Logger.info("CaixaDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findAll(int page, int size) {
        Logger.info("CaixaDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa LIMIT ? OFFSET ?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findByNome(String nome) {
        Logger.info("CaixaDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE nome=?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findByReservaEmergencia(BigDecimal reservaEmergencia) {
        Logger.info("CaixaDaoNativeImpl.findByReservaEmergencia - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE reserva_emergencia=?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, reservaEmergencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findByReservaEmergencia - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findByReservaEmergencia - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findBySalarioMedio(BigDecimal salarioMedio) {
        Logger.info("CaixaDaoNativeImpl.findBySalarioMedio - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE salario_medio=?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, salarioMedio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findBySalarioMedio - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findBySalarioMedio - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findByValorTotal(BigDecimal valorTotal) {
        Logger.info("CaixaDaoNativeImpl.findByValorTotal - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE valor_total=?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, valorTotal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findByValorTotal - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findByValorTotal - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> findByIdUsuario(Integer idUsuario) {
        Logger.info("CaixaDaoNativeImpl.findByIdUsuario - inicio");
        String sql = "SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE id_usuario=?";
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (idUsuario != null) {
                ps.setInt(1, idUsuario);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.findByIdUsuario - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.findByIdUsuario - erro", e);
        }
        return list;
    }

    @Override
    public List<Caixa> search(Caixa filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Caixa> search(Caixa filtro, int page, int size) {
        Logger.info("CaixaDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_caixa, nome, reserva_emergencia, salario_medio, valor_total, id_usuario FROM Caixa WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getReservaEmergencia() != null) {
            sb.append(" AND reserva_emergencia=?");
            params.add(filtro.getReservaEmergencia());
        }
        if (filtro.getSalarioMedio() != null) {
            sb.append(" AND salario_medio=?");
            params.add(filtro.getSalarioMedio());
        }
        if (filtro.getValorTotal() != null) {
            sb.append(" AND valor_total=?");
            params.add(filtro.getValorTotal());
        }
        if (filtro.getUsuario() != null && filtro.getUsuario().getIdUsuario() != null) {
            sb.append(" AND id_usuario=?");
            params.add(filtro.getUsuario().getIdUsuario());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Caixa> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCaixa(rs));
                }
            }
            Logger.info("CaixaDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("CaixaDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}

