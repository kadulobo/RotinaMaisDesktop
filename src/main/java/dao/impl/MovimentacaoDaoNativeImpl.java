package dao.impl;

import conexao.ConnectionFactory;
import dao.api.MovimentacaoDao;
import exception.MovimentacaoException;
import infra.Logger;
import model.Caixa;
import model.Movimentacao;
import model.Periodo;
import model.Usuario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDaoNativeImpl implements MovimentacaoDao {

    private Movimentacao map(ResultSet rs) throws SQLException {
        Movimentacao m = new Movimentacao();
        m.setIdMovimentacao(rs.getInt("id_movimentacao"));
        m.setDesconto(rs.getBigDecimal("desconto"));
        m.setVantagem(rs.getBigDecimal("vantagem"));
        m.setLiquido(rs.getBigDecimal("liquido"));
        int tipo = rs.getInt("tipo");
        if (!rs.wasNull()) {
            m.setTipo(tipo);
        }
        int status = rs.getInt("status");
        if (!rs.wasNull()) {
            m.setStatus(status);
        }
        int ponto = rs.getInt("ponto");
        if (!rs.wasNull()) {
            m.setPonto(ponto);
        }
        int idUsuario = rs.getInt("id_usuario");
        if (!rs.wasNull()) {
            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            m.setUsuario(u);
        }
        int idCaixa = rs.getInt("id_caixa");
        if (!rs.wasNull()) {
            Caixa c = new Caixa();
            c.setIdCaixa(idCaixa);
            m.setCaixa(c);
        }
        int idPeriodo = rs.getInt("id_periodo");
        if (!rs.wasNull()) {
            Periodo p = new Periodo();
            p.setIdPeriodo(idPeriodo);
            m.setPeriodo(p);
        }
        return m;
    }

    @Override
    public void create(Movimentacao movimentacao) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Movimentacao (desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo) VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, movimentacao.getDesconto());
                ps.setBigDecimal(2, movimentacao.getVantagem());
                ps.setBigDecimal(3, movimentacao.getLiquido());
                if (movimentacao.getTipo() != null) {
                    ps.setInt(4, movimentacao.getTipo());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                if (movimentacao.getStatus() != null) {
                    ps.setInt(5, movimentacao.getStatus());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (movimentacao.getPonto() != null) {
                    ps.setInt(6, movimentacao.getPonto());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                if (movimentacao.getUsuario() != null && movimentacao.getUsuario().getIdUsuario() != null) {
                    ps.setInt(7, movimentacao.getUsuario().getIdUsuario());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                if (movimentacao.getCaixa() != null && movimentacao.getCaixa().getIdCaixa() != null) {
                    ps.setInt(8, movimentacao.getCaixa().getIdCaixa());
                } else {
                    ps.setNull(8, Types.INTEGER);
                }
                if (movimentacao.getPeriodo() != null && movimentacao.getPeriodo().getIdPeriodo() != null) {
                    ps.setInt(9, movimentacao.getPeriodo().getIdPeriodo());
                } else {
                    ps.setNull(9, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("MovimentacaoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("MovimentacaoDaoNativeImpl.create - erro", e);
            throw new MovimentacaoException("Erro ao criar Movimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Movimentacao update(Movimentacao movimentacao) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Movimentacao SET desconto=?, vantagem=?, liquido=?, tipo=?, status=?, ponto=?, id_usuario=?, id_caixa=?, id_periodo=? WHERE id_movimentacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, movimentacao.getDesconto());
                ps.setBigDecimal(2, movimentacao.getVantagem());
                ps.setBigDecimal(3, movimentacao.getLiquido());
                if (movimentacao.getTipo() != null) {
                    ps.setInt(4, movimentacao.getTipo());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                if (movimentacao.getStatus() != null) {
                    ps.setInt(5, movimentacao.getStatus());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (movimentacao.getPonto() != null) {
                    ps.setInt(6, movimentacao.getPonto());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                if (movimentacao.getUsuario() != null && movimentacao.getUsuario().getIdUsuario() != null) {
                    ps.setInt(7, movimentacao.getUsuario().getIdUsuario());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                if (movimentacao.getCaixa() != null && movimentacao.getCaixa().getIdCaixa() != null) {
                    ps.setInt(8, movimentacao.getCaixa().getIdCaixa());
                } else {
                    ps.setNull(8, Types.INTEGER);
                }
                if (movimentacao.getPeriodo() != null && movimentacao.getPeriodo().getIdPeriodo() != null) {
                    ps.setInt(9, movimentacao.getPeriodo().getIdPeriodo());
                } else {
                    ps.setNull(9, Types.INTEGER);
                }
                ps.setInt(10, movimentacao.getIdMovimentacao());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new MovimentacaoException("Movimentacao não encontrada: id=" + movimentacao.getIdMovimentacao());
                }
            }
            conn.commit();
            Logger.info("MovimentacaoDaoNativeImpl.update - sucesso");
            return findById(movimentacao.getIdMovimentacao());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("MovimentacaoDaoNativeImpl.update - erro", e);
            throw new MovimentacaoException("Erro ao atualizar Movimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Movimentacao WHERE id_movimentacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new MovimentacaoException("Movimentacao não encontrada: id=" + id);
                }
            }
            conn.commit();
            Logger.info("MovimentacaoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("MovimentacaoDaoNativeImpl.deleteById - erro", e);
            throw new MovimentacaoException("Erro ao deletar Movimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Movimentacao findById(Integer id) throws MovimentacaoException {
        Logger.info("MovimentacaoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_movimentacao=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("MovimentacaoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new MovimentacaoException("Movimentacao não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findById - erro", e);
            throw new MovimentacaoException("Movimentacao não encontrada: id=" + id, e);
        }
    }

    @Override
    public List<Movimentacao> findAll() {
        Logger.info("MovimentacaoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            Logger.info("MovimentacaoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findAll(int page, int size) {
        Logger.info("MovimentacaoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao LIMIT ? OFFSET ?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByDesconto(BigDecimal desconto) {
        Logger.info("MovimentacaoDaoNativeImpl.findByDesconto - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE desconto=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, desconto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByDesconto - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByDesconto - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByVantagem(BigDecimal vantagem) {
        Logger.info("MovimentacaoDaoNativeImpl.findByVantagem - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE vantagem=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, vantagem);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByVantagem - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByVantagem - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByLiquido(BigDecimal liquido) {
        Logger.info("MovimentacaoDaoNativeImpl.findByLiquido - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE liquido=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, liquido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByLiquido - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByLiquido - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByTipo(Integer tipo) {
        Logger.info("MovimentacaoDaoNativeImpl.findByTipo - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE tipo=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByTipo - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByTipo - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByStatus(Integer status) {
        Logger.info("MovimentacaoDaoNativeImpl.findByStatus - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE status=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByStatus - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByStatus - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByPonto(Integer ponto) {
        Logger.info("MovimentacaoDaoNativeImpl.findByPonto - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE ponto=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ponto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByPonto - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByPonto - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByIdUsuario(Integer idUsuario) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdUsuario - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_usuario=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByIdUsuario - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByIdUsuario - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByIdCaixa(Integer idCaixa) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdCaixa - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_caixa=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCaixa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByIdCaixa - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByIdCaixa - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> findByIdPeriodo(Integer idPeriodo) {
        Logger.info("MovimentacaoDaoNativeImpl.findByIdPeriodo - inicio");
        String sql = "SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE id_periodo=?";
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.findByIdPeriodo - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.findByIdPeriodo - erro", e);
        }
        return list;
    }

    @Override
    public List<Movimentacao> search(Movimentacao filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Movimentacao> search(Movimentacao filtro, int page, int size) {
        Logger.info("MovimentacaoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_movimentacao, desconto, vantagem, liquido, tipo, status, ponto, id_usuario, id_caixa, id_periodo FROM Movimentacao WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getDesconto() != null) {
            sb.append(" AND desconto=?");
            params.add(filtro.getDesconto());
        }
        if (filtro.getVantagem() != null) {
            sb.append(" AND vantagem=?");
            params.add(filtro.getVantagem());
        }
        if (filtro.getLiquido() != null) {
            sb.append(" AND liquido=?");
            params.add(filtro.getLiquido());
        }
        if (filtro.getTipo() != null) {
            sb.append(" AND tipo=?");
            params.add(filtro.getTipo());
        }
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getPonto() != null) {
            sb.append(" AND ponto=?");
            params.add(filtro.getPonto());
        }
        if (filtro.getUsuario() != null && filtro.getUsuario().getIdUsuario() != null) {
            sb.append(" AND id_usuario=?");
            params.add(filtro.getUsuario().getIdUsuario());
        }
        if (filtro.getCaixa() != null && filtro.getCaixa().getIdCaixa() != null) {
            sb.append(" AND id_caixa=?");
            params.add(filtro.getCaixa().getIdCaixa());
        }
        if (filtro.getPeriodo() != null && filtro.getPeriodo().getIdPeriodo() != null) {
            sb.append(" AND id_periodo=?");
            params.add(filtro.getPeriodo().getIdPeriodo());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Movimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("MovimentacaoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("MovimentacaoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
