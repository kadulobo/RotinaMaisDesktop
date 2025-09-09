package dao.impl;

import conexao.ConnectionFactory;
import dao.api.OperacaoDao;
import exception.OperacaoException;
import infra.Logger;
import model.Operacao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OperacaoDaoNativeImpl implements OperacaoDao {

    private Operacao map(ResultSet rs) throws SQLException {
        Operacao o = new Operacao();
        o.setIdOperacao(rs.getInt("id_operacao"));
        o.setFechamento(rs.getBigDecimal("fechamento"));
        Time t = rs.getTime("tempo_operacao");
        if (t != null) o.setTempoOperacao(t.toLocalTime());
        o.setQtdCompra((Integer) rs.getObject("qtd_compra"));
        o.setAbertura(rs.getBigDecimal("abertura"));
        o.setQtdVenda((Integer) rs.getObject("qtd_venda"));
        o.setLado(rs.getString("lado"));
        o.setPrecoCompra(rs.getBigDecimal("preco_compra"));
        o.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        o.setPrecoMedio(rs.getBigDecimal("preco_medio"));
        o.setResIntervalo(rs.getString("res_intervalo"));
        o.setNumeroOperacao(rs.getBigDecimal("numero_operacao"));
        o.setResOperacao(rs.getString("res_operacao"));
        o.setDrawdon(rs.getBigDecimal("drawdon"));
        o.setGanhoMax(rs.getBigDecimal("ganhoMax"));
        o.setPerdaMax(rs.getBigDecimal("perdaMax"));
        o.setTet(rs.getString("tet"));
        o.setTotal(rs.getBigDecimal("total"));
        o.setIdCarteira((Integer) rs.getObject("id_carteira"));
        o.setIdPapel((Integer) rs.getObject("id_papel"));
        return o;
    }

    private List<Operacao> listBySql(String sql, Object... params) {
        List<Operacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            Logger.error("OperacaoDaoNativeImpl.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public void create(Operacao e) {
        Logger.info("OperacaoDaoNativeImpl.create");
        String sql = "INSERT INTO Operacao (fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int i = 1;
                ps.setBigDecimal(i++, e.getFechamento());
                if (e.getTempoOperacao() != null) {
                    ps.setTime(i++, Time.valueOf(e.getTempoOperacao()));
                } else {
                    ps.setNull(i++, Types.TIME);
                }
                if (e.getQtdCompra() != null) {
                    ps.setInt(i++, e.getQtdCompra());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.setBigDecimal(i++, e.getAbertura());
                if (e.getQtdVenda() != null) {
                    ps.setInt(i++, e.getQtdVenda());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.setString(i++, e.getLado());
                ps.setBigDecimal(i++, e.getPrecoCompra());
                ps.setBigDecimal(i++, e.getPrecoVenda());
                ps.setBigDecimal(i++, e.getPrecoMedio());
                ps.setString(i++, e.getResIntervalo());
                ps.setBigDecimal(i++, e.getNumeroOperacao());
                ps.setString(i++, e.getResOperacao());
                ps.setBigDecimal(i++, e.getDrawdon());
                ps.setBigDecimal(i++, e.getGanhoMax());
                ps.setBigDecimal(i++, e.getPerdaMax());
                ps.setString(i++, e.getTet());
                ps.setBigDecimal(i++, e.getTotal());
                if (e.getIdCarteira() != null) {
                    ps.setInt(i++, e.getIdCarteira());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                if (e.getIdPapel() != null) {
                    ps.setInt(i++, e.getIdPapel());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback create", e1); }
            }
            Logger.error("OperacaoDaoNativeImpl.create - erro", ex);
            throw new OperacaoException("Erro ao criar Operacao", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(Operacao e) {
        Logger.info("OperacaoDaoNativeImpl.update");
        String sql = "UPDATE Operacao SET fechamento=?, tempo_operacao=?, qtd_compra=?, abertura=?, qtd_venda=?, lado=?, preco_compra=?, preco_venda=?, preco_medio=?, res_intervalo=?, numero_operacao=?, res_operacao=?, drawdon=?, ganhoMax=?, perdaMax=?, tet=?, total=?, id_carteira=?, id_papel=? WHERE id_operacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                int i = 1;
                ps.setBigDecimal(i++, e.getFechamento());
                if (e.getTempoOperacao() != null) {
                    ps.setTime(i++, Time.valueOf(e.getTempoOperacao()));
                } else {
                    ps.setNull(i++, Types.TIME);
                }
                if (e.getQtdCompra() != null) {
                    ps.setInt(i++, e.getQtdCompra());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.setBigDecimal(i++, e.getAbertura());
                if (e.getQtdVenda() != null) {
                    ps.setInt(i++, e.getQtdVenda());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.setString(i++, e.getLado());
                ps.setBigDecimal(i++, e.getPrecoCompra());
                ps.setBigDecimal(i++, e.getPrecoVenda());
                ps.setBigDecimal(i++, e.getPrecoMedio());
                ps.setString(i++, e.getResIntervalo());
                ps.setBigDecimal(i++, e.getNumeroOperacao());
                ps.setString(i++, e.getResOperacao());
                ps.setBigDecimal(i++, e.getDrawdon());
                ps.setBigDecimal(i++, e.getGanhoMax());
                ps.setBigDecimal(i++, e.getPerdaMax());
                ps.setString(i++, e.getTet());
                ps.setBigDecimal(i++, e.getTotal());
                if (e.getIdCarteira() != null) {
                    ps.setInt(i++, e.getIdCarteira());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                if (e.getIdPapel() != null) {
                    ps.setInt(i++, e.getIdPapel());
                } else {
                    ps.setNull(i++, Types.INTEGER);
                }
                ps.setInt(i, e.getIdOperacao());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new OperacaoException("Operacao nao encontrado: id=" + e.getIdOperacao());
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback update", e1); }
            }
            Logger.error("OperacaoDaoNativeImpl.update - erro", ex);
            throw new OperacaoException("Erro ao atualizar Operacao", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("OperacaoDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Operacao WHERE id_operacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new OperacaoException("Operacao nao encontrado: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback delete", e1); }
            }
            Logger.error("OperacaoDaoNativeImpl.deleteById - erro", ex);
            throw new OperacaoException("Erro ao deletar Operacao", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Operacao findById(Integer id) {
        String sql = BASE_SELECT + " WHERE id_operacao=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            throw new OperacaoException("Operacao não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("OperacaoDaoNativeImpl.findById - erro", e);
            throw new OperacaoException("Operacao não encontrado: id=" + id, e);
        }
    }

    private static final String BASE_SELECT = "SELECT id_operacao, fechamento, tempo_operacao, qtd_compra, abertura, qtd_venda, lado, preco_compra, preco_venda, preco_medio, res_intervalo, numero_operacao, res_operacao, drawdon, ganhoMax, perdaMax, tet, total, id_carteira, id_papel FROM Operacao";

    @Override
    public List<Operacao> findAll() {
        return listBySql(BASE_SELECT);
    }

    @Override
    public List<Operacao> findAll(int page, int size) {
        return listBySql(BASE_SELECT + " LIMIT ? OFFSET ?", size, page * size);
    }

    @Override
    public List<Operacao> findByFechamento(BigDecimal fechamento) {
        return listBySql(BASE_SELECT + " WHERE fechamento = ?", fechamento);
    }

    @Override
    public List<Operacao> findByTempoOperacao(LocalTime tempoOperacao) {
        return listBySql(BASE_SELECT + " WHERE tempo_operacao = ?", Time.valueOf(tempoOperacao));
    }

    @Override
    public List<Operacao> findByQtdCompra(Integer qtdCompra) {
        return listBySql(BASE_SELECT + " WHERE qtd_compra = ?", qtdCompra);
    }

    @Override
    public List<Operacao> findByAbertura(BigDecimal abertura) {
        return listBySql(BASE_SELECT + " WHERE abertura = ?", abertura);
    }

    @Override
    public List<Operacao> findByQtdVenda(Integer qtdVenda) {
        return listBySql(BASE_SELECT + " WHERE qtd_venda = ?", qtdVenda);
    }

    @Override
    public List<Operacao> findByLado(String lado) {
        return listBySql(BASE_SELECT + " WHERE lado = ?", lado);
    }

    @Override
    public List<Operacao> findByPrecoCompra(BigDecimal precoCompra) {
        return listBySql(BASE_SELECT + " WHERE preco_compra = ?", precoCompra);
    }

    @Override
    public List<Operacao> findByPrecoVenda(BigDecimal precoVenda) {
        return listBySql(BASE_SELECT + " WHERE preco_venda = ?", precoVenda);
    }

    @Override
    public List<Operacao> findByPrecoMedio(BigDecimal precoMedio) {
        return listBySql(BASE_SELECT + " WHERE preco_medio = ?", precoMedio);
    }

    @Override
    public List<Operacao> findByResIntervalo(String resIntervalo) {
        return listBySql(BASE_SELECT + " WHERE res_intervalo = ?", resIntervalo);
    }

    @Override
    public List<Operacao> findByNumeroOperacao(BigDecimal numeroOperacao) {
        return listBySql(BASE_SELECT + " WHERE numero_operacao = ?", numeroOperacao);
    }

    @Override
    public List<Operacao> findByResOperacao(String resOperacao) {
        return listBySql(BASE_SELECT + " WHERE res_operacao = ?", resOperacao);
    }

    @Override
    public List<Operacao> findByDrawdon(BigDecimal drawdon) {
        return listBySql(BASE_SELECT + " WHERE drawdon = ?", drawdon);
    }

    @Override
    public List<Operacao> findByGanhoMax(BigDecimal ganhoMax) {
        return listBySql(BASE_SELECT + " WHERE ganhoMax = ?", ganhoMax);
    }

    @Override
    public List<Operacao> findByPerdaMax(BigDecimal perdaMax) {
        return listBySql(BASE_SELECT + " WHERE perdaMax = ?", perdaMax);
    }

    @Override
    public List<Operacao> findByTet(String tet) {
        return listBySql(BASE_SELECT + " WHERE tet = ?", tet);
    }

    @Override
    public List<Operacao> findByTotal(BigDecimal total) {
        return listBySql(BASE_SELECT + " WHERE total = ?", total);
    }

    @Override
    public List<Operacao> findByIdCarteira(Integer idCarteira) {
        return listBySql(BASE_SELECT + " WHERE id_carteira = ?", idCarteira);
    }

    @Override
    public List<Operacao> findByIdPapel(Integer idPapel) {
        return listBySql(BASE_SELECT + " WHERE id_papel = ?", idPapel);
    }

    @Override
    public List<Operacao> search(Operacao f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Operacao> search(Operacao f, int page, int size) {
        StringBuilder sb = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getFechamento() != null) { sb.append(" AND fechamento = ?"); params.add(f.getFechamento()); }
        if (f.getTempoOperacao() != null) { sb.append(" AND tempo_operacao = ?"); params.add(Time.valueOf(f.getTempoOperacao())); }
        if (f.getQtdCompra() != null) { sb.append(" AND qtd_compra = ?"); params.add(f.getQtdCompra()); }
        if (f.getAbertura() != null) { sb.append(" AND abertura = ?"); params.add(f.getAbertura()); }
        if (f.getQtdVenda() != null) { sb.append(" AND qtd_venda = ?"); params.add(f.getQtdVenda()); }
        if (f.getLado() != null && !f.getLado().isEmpty()) { sb.append(" AND lado = ?"); params.add(f.getLado()); }
        if (f.getPrecoCompra() != null) { sb.append(" AND preco_compra = ?"); params.add(f.getPrecoCompra()); }
        if (f.getPrecoVenda() != null) { sb.append(" AND preco_venda = ?"); params.add(f.getPrecoVenda()); }
        if (f.getPrecoMedio() != null) { sb.append(" AND preco_medio = ?"); params.add(f.getPrecoMedio()); }
        if (f.getResIntervalo() != null && !f.getResIntervalo().isEmpty()) { sb.append(" AND res_intervalo = ?"); params.add(f.getResIntervalo()); }
        if (f.getNumeroOperacao() != null) { sb.append(" AND numero_operacao = ?"); params.add(f.getNumeroOperacao()); }
        if (f.getResOperacao() != null && !f.getResOperacao().isEmpty()) { sb.append(" AND res_operacao = ?"); params.add(f.getResOperacao()); }
        if (f.getDrawdon() != null) { sb.append(" AND drawdon = ?"); params.add(f.getDrawdon()); }
        if (f.getGanhoMax() != null) { sb.append(" AND ganhoMax = ?"); params.add(f.getGanhoMax()); }
        if (f.getPerdaMax() != null) { sb.append(" AND perdaMax = ?"); params.add(f.getPerdaMax()); }
        if (f.getTet() != null && !f.getTet().isEmpty()) { sb.append(" AND tet = ?"); params.add(f.getTet()); }
        if (f.getTotal() != null) { sb.append(" AND total = ?"); params.add(f.getTotal()); }
        if (f.getIdCarteira() != null) { sb.append(" AND id_carteira = ?"); params.add(f.getIdCarteira()); }
        if (f.getIdPapel() != null) { sb.append(" AND id_papel = ?"); params.add(f.getIdPapel()); }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        return listBySql(sb.toString(), params.toArray());
    }
}

