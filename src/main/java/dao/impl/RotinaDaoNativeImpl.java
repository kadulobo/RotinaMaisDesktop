package dao.impl;

import conexao.ConnectionFactory;
import dao.api.RotinaDao;
import exception.RotinaException;
import infra.Logger;
import model.Rotina;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RotinaDaoNativeImpl implements RotinaDao {

    private Rotina map(ResultSet rs) throws SQLException {
        Rotina r = new Rotina();
        r.setIdRotina(rs.getInt("id_rotina"));
        r.setNome(rs.getString("nome"));
        Date di = rs.getDate("inicio");
        if (di != null) r.setInicio(di.toLocalDate());
        Date df = rs.getDate("fim");
        if (df != null) r.setFim(df.toLocalDate());
        r.setDescricao(rs.getString("descricao"));
        r.setStatus((Integer) rs.getObject("status"));
        r.setPonto((Integer) rs.getObject("ponto"));
        r.setIdUsuario((Integer) rs.getObject("id_usuario"));
        return r;
    }

    private List<Rotina> listBySql(String sql, Object... params) {
        List<Rotina> list = new ArrayList<>();
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
            Logger.error("RotinaDaoNativeImpl.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public void create(Rotina rotina) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Rotina (nome, inicio, fim, descricao, status, ponto, id_usuario) VALUES (?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, rotina.getNome());
                if (rotina.getInicio() != null) {
                    ps.setDate(2, Date.valueOf(rotina.getInicio()));
                } else {
                    ps.setNull(2, Types.DATE);
                }
                if (rotina.getFim() != null) {
                    ps.setDate(3, Date.valueOf(rotina.getFim()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                ps.setString(4, rotina.getDescricao());
                if (rotina.getStatus() != null) {
                    ps.setInt(5, rotina.getStatus());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (rotina.getPonto() != null) {
                    ps.setInt(6, rotina.getPonto());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                if (rotina.getIdUsuario() != null) {
                    ps.setInt(7, rotina.getIdUsuario());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("RotinaDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("RotinaDaoNativeImpl.create - erro", e);
            throw new RotinaException("Erro ao criar Rotina", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Rotina update(Rotina rotina) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.update - inicio");
        String sql = "UPDATE Rotina SET nome=?, inicio=?, fim=?, descricao=?, status=?, ponto=?, id_usuario=? WHERE id_rotina=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, rotina.getNome());
                if (rotina.getInicio() != null) {
                    ps.setDate(2, Date.valueOf(rotina.getInicio()));
                } else {
                    ps.setNull(2, Types.DATE);
                }
                if (rotina.getFim() != null) {
                    ps.setDate(3, Date.valueOf(rotina.getFim()));
                } else {
                    ps.setNull(3, Types.DATE);
                }
                ps.setString(4, rotina.getDescricao());
                if (rotina.getStatus() != null) {
                    ps.setInt(5, rotina.getStatus());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                if (rotina.getPonto() != null) {
                    ps.setInt(6, rotina.getPonto());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                if (rotina.getIdUsuario() != null) {
                    ps.setInt(7, rotina.getIdUsuario());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.setInt(8, rotina.getIdRotina());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new RotinaException("Rotina não encontrada: id=" + rotina.getIdRotina());
                }
            }
            conn.commit();
            Logger.info("RotinaDaoNativeImpl.update - sucesso");
            return findById(rotina.getIdRotina());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("RotinaDaoNativeImpl.update - erro", e);
            throw new RotinaException("Erro ao atualizar Rotina", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Rotina WHERE id_rotina=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new RotinaException("Rotina não encontrada: id=" + id);
                }
            }
            conn.commit();
            Logger.info("RotinaDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("RotinaDaoNativeImpl.deleteById - erro", e);
            throw new RotinaException("Erro ao deletar Rotina", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Rotina findById(Integer id) throws RotinaException {
        Logger.info("RotinaDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina WHERE id_rotina=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("RotinaDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new RotinaException("Rotina não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("RotinaDaoNativeImpl.findById - erro", e);
            throw new RotinaException("Rotina não encontrada: id=" + id, e);
        }
    }

    private static final String BASE_SELECT = "SELECT id_rotina, nome, inicio, fim, descricao, status, ponto, id_usuario FROM Rotina";

    @Override
    public List<Rotina> findAll() {
        Logger.info("RotinaDaoNativeImpl.findAll - inicio");
        List<Rotina> list = listBySql(BASE_SELECT);
        Logger.info("RotinaDaoNativeImpl.findAll - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findAll(int page, int size) {
        Logger.info("RotinaDaoNativeImpl.findAll(page) - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " LIMIT ? OFFSET ?", size, page * size);
        Logger.info("RotinaDaoNativeImpl.findAll(page) - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByNome(String nome) {
        Logger.info("RotinaDaoNativeImpl.findByNome - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE nome=?", nome);
        Logger.info("RotinaDaoNativeImpl.findByNome - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByInicio(LocalDate inicio) {
        Logger.info("RotinaDaoNativeImpl.findByInicio - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE inicio=?", Date.valueOf(inicio));
        Logger.info("RotinaDaoNativeImpl.findByInicio - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByFim(LocalDate fim) {
        Logger.info("RotinaDaoNativeImpl.findByFim - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE fim=?", Date.valueOf(fim));
        Logger.info("RotinaDaoNativeImpl.findByFim - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByDescricao(String descricao) {
        Logger.info("RotinaDaoNativeImpl.findByDescricao - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE descricao=?", descricao);
        Logger.info("RotinaDaoNativeImpl.findByDescricao - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByStatus(Integer status) {
        Logger.info("RotinaDaoNativeImpl.findByStatus - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE status=?", status);
        Logger.info("RotinaDaoNativeImpl.findByStatus - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByPonto(Integer ponto) {
        Logger.info("RotinaDaoNativeImpl.findByPonto - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE ponto=?", ponto);
        Logger.info("RotinaDaoNativeImpl.findByPonto - sucesso");
        return list;
    }

    @Override
    public List<Rotina> findByIdUsuario(Integer idUsuario) {
        Logger.info("RotinaDaoNativeImpl.findByIdUsuario - inicio");
        List<Rotina> list = listBySql(BASE_SELECT + " WHERE id_usuario=?", idUsuario);
        Logger.info("RotinaDaoNativeImpl.findByIdUsuario - sucesso");
        return list;
    }

    @Override
    public List<Rotina> search(Rotina filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Rotina> search(Rotina filtro, int page, int size) {
        Logger.info("RotinaDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getInicio() != null) {
            sb.append(" AND inicio=?");
            params.add(Date.valueOf(filtro.getInicio()));
        }
        if (filtro.getFim() != null) {
            sb.append(" AND fim=?");
            params.add(Date.valueOf(filtro.getFim()));
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
        }
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getPonto() != null) {
            sb.append(" AND ponto=?");
            params.add(filtro.getPonto());
        }
        if (filtro.getIdUsuario() != null) {
            sb.append(" AND id_usuario=?");
            params.add(filtro.getIdUsuario());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Rotina> list = listBySql(sb.toString(), params.toArray());
        Logger.info("RotinaDaoNativeImpl.search - sucesso");
        return list;
    }
}

