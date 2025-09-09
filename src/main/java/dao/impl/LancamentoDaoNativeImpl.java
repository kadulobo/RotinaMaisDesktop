// path: src/main/java/dao/impl/LancamentoDaoNativeImpl.java
package dao.impl;

import conexao.ConnectionFactory;
import dao.api.LancamentoDao;
import exception.LancamentoException;
import infra.Logger;
import model.Evento;
import model.Lancamento;
import model.Movimentacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link LancamentoDao}.
 */
public class LancamentoDaoNativeImpl implements LancamentoDao {

    private Lancamento map(ResultSet rs) throws SQLException {
        Lancamento l = new Lancamento();
        l.setIdLancamento(rs.getInt("id_lancamento"));
        l.setValor(rs.getBigDecimal("valor"));
        l.setFixo((Boolean) rs.getObject("fixo"));
        Date dt = rs.getDate("data_pagamento");
        if (dt != null) {
            l.setDataPagamento(dt.toLocalDate());
        }
        l.setStatus((Integer) rs.getObject("status"));
        Integer idMov = (Integer) rs.getObject("id_movimentacao");
        if (idMov != null) {
            Movimentacao m = new Movimentacao();
            m.setIdMovimentacao(idMov);
            l.setMovimentacao(m);
        }
        Integer idEvt = (Integer) rs.getObject("id_evento");
        if (idEvt != null) {
            Evento e = new Evento();
            e.setIdEvento(idEvt);
            l.setEvento(e);
        }
        return l;
    }

    @Override
    public void create(Lancamento lancamento) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Lancamento (valor, fixo, data_pagamento, status, id_movimentacao, id_evento) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, lancamento.getValor());
                if (lancamento.getFixo() != null) {
                    ps.setBoolean(2, lancamento.getFixo());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                if (lancamento.getDataPagamento() != null) {
                    ps.setDate(3, Date.valueOf(lancamento.getDataPagamento()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                if (lancamento.getStatus() != null) {
                    ps.setInt(4, lancamento.getStatus());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                if (lancamento.getMovimentacao() != null && lancamento.getMovimentacao().getIdMovimentacao() != null) {
                    ps.setInt(5, lancamento.getMovimentacao().getIdMovimentacao());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (lancamento.getEvento() != null && lancamento.getEvento().getIdEvento() != null) {
                    ps.setInt(6, lancamento.getEvento().getIdEvento());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("LancamentoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("LancamentoDaoNativeImpl.create - erro", e);
            throw new LancamentoException("Erro ao criar Lancamento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Lancamento update(Lancamento lancamento) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Lancamento SET valor=?, fixo=?, data_pagamento=?, status=?, id_movimentacao=?, id_evento=? WHERE id_lancamento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, lancamento.getValor());
                if (lancamento.getFixo() != null) {
                    ps.setBoolean(2, lancamento.getFixo());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                if (lancamento.getDataPagamento() != null) {
                    ps.setDate(3, Date.valueOf(lancamento.getDataPagamento()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                if (lancamento.getStatus() != null) {
                    ps.setInt(4, lancamento.getStatus());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                if (lancamento.getMovimentacao() != null && lancamento.getMovimentacao().getIdMovimentacao() != null) {
                    ps.setInt(5, lancamento.getMovimentacao().getIdMovimentacao());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (lancamento.getEvento() != null && lancamento.getEvento().getIdEvento() != null) {
                    ps.setInt(6, lancamento.getEvento().getIdEvento());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.setInt(7, lancamento.getIdLancamento());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new LancamentoException("Lancamento não encontrado: id=" + lancamento.getIdLancamento());
                }
            }
            conn.commit();
            Logger.info("LancamentoDaoNativeImpl.update - sucesso");
            return findById(lancamento.getIdLancamento());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("LancamentoDaoNativeImpl.update - erro", e);
            throw new LancamentoException("Erro ao atualizar Lancamento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Lancamento WHERE id_lancamento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new LancamentoException("Lancamento não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("LancamentoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("LancamentoDaoNativeImpl.deleteById - erro", e);
            throw new LancamentoException("Erro ao deletar Lancamento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Lancamento findById(Integer id) throws LancamentoException {
        Logger.info("LancamentoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_lancamento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("LancamentoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new LancamentoException("Lancamento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findById - erro", e);
            throw new LancamentoException("Lancamento não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Lancamento> findAll() {
        Logger.info("LancamentoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            Logger.info("LancamentoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findAll(int page, int size) {
        Logger.info("LancamentoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento LIMIT ? OFFSET ?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByValor(java.math.BigDecimal valor) {
        Logger.info("LancamentoDaoNativeImpl.findByValor - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE valor=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByValor - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByValor - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByFixo(Boolean fixo) {
        Logger.info("LancamentoDaoNativeImpl.findByFixo - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE fixo=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (fixo != null) {
                ps.setBoolean(1, fixo);
            } else {
                ps.setNull(1, Types.BOOLEAN);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByFixo - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByFixo - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByDataPagamento(java.time.LocalDate dataPagamento) {
        Logger.info("LancamentoDaoNativeImpl.findByDataPagamento - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE data_pagamento=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (dataPagamento != null) {
                ps.setDate(1, Date.valueOf(dataPagamento));
            } else {
                ps.setNull(1, Types.DATE);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByDataPagamento - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByDataPagamento - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByStatus(Integer status) {
        Logger.info("LancamentoDaoNativeImpl.findByStatus - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE status=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null) {
                ps.setInt(1, status);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByStatus - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByStatus - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByIdMovimentacao(Integer idMovimentacao) {
        Logger.info("LancamentoDaoNativeImpl.findByIdMovimentacao - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_movimentacao=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMovimentacao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByIdMovimentacao - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByIdMovimentacao - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> findByIdEvento(Integer idEvento) {
        Logger.info("LancamentoDaoNativeImpl.findByIdEvento - inicio");
        String sql = "SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE id_evento=?";
        List<Lancamento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("LancamentoDaoNativeImpl.findByIdEvento - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.findByIdEvento - erro", e);
        }
        return list;
    }

    @Override
    public List<Lancamento> search(Lancamento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Lancamento> search(Lancamento filtro, int page, int size) {
        Logger.info("LancamentoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_lancamento, valor, fixo, data_pagamento, status, id_movimentacao, id_evento FROM Lancamento WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getValor() != null) {
            sb.append(" AND valor=?");
            params.add(filtro.getValor());
        }
        if (filtro.getFixo() != null) {
            sb.append(" AND fixo=?");
            params.add(filtro.getFixo());
        }
        if (filtro.getDataPagamento() != null) {
            sb.append(" AND data_pagamento=?");
            params.add(Date.valueOf(filtro.getDataPagamento()));
        }
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getMovimentacao() != null && filtro.getMovimentacao().getIdMovimentacao() != null) {
            sb.append(" AND id_movimentacao=?");
            params.add(filtro.getMovimentacao().getIdMovimentacao());
        }
        if (filtro.getEvento() != null && filtro.getEvento().getIdEvento() != null) {
            sb.append(" AND id_evento=?");
            params.add(filtro.getEvento().getIdEvento());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Lancamento> list = new ArrayList<>();
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
            Logger.info("LancamentoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("LancamentoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}

