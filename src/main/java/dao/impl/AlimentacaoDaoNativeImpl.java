package dao.impl;

import conexao.ConnectionFactory;
import dao.api.AlimentacaoDao;
import exception.AlimentacaoException;
import infra.Logger;
import model.Alimentacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlimentacaoDaoNativeImpl implements AlimentacaoDao {
    private Alimentacao map(ResultSet rs, boolean withVideo) throws SQLException {
        Alimentacao a = new Alimentacao();
        a.setIdAlimentacao(rs.getInt("id_alimentacao"));
        a.setStatus((Integer) rs.getObject("status"));
        a.setNome(rs.getString("nome"));
        a.setLink(rs.getString("link"));
        if (withVideo) {
            a.setVideo(rs.getBytes("video"));
        }
        a.setPreparo(rs.getString("preparo"));
        a.setIdRotina((Integer) rs.getObject("id_rotina"));
        return a;
    }

    @Override
    public void create(Alimentacao alimentacao) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Alimentacao (status, nome, link, video, preparo, id_rotina) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (alimentacao.getStatus() != null) {
                    ps.setInt(1, alimentacao.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setString(2, alimentacao.getNome());
                ps.setString(3, alimentacao.getLink());
                ps.setBytes(4, alimentacao.getVideo());
                ps.setString(5, alimentacao.getPreparo());
                if (alimentacao.getIdRotina() != null) {
                    ps.setInt(6, alimentacao.getIdRotina());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("AlimentacaoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("AlimentacaoDaoNativeImpl.create - erro", e);
            throw new AlimentacaoException("Erro ao criar Alimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Alimentacao update(Alimentacao alimentacao) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Alimentacao SET status=?, nome=?, link=?, video=?, preparo=?, id_rotina=? WHERE id_alimentacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (alimentacao.getStatus() != null) {
                    ps.setInt(1, alimentacao.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                ps.setString(2, alimentacao.getNome());
                ps.setString(3, alimentacao.getLink());
                ps.setBytes(4, alimentacao.getVideo());
                ps.setString(5, alimentacao.getPreparo());
                if (alimentacao.getIdRotina() != null) {
                    ps.setInt(6, alimentacao.getIdRotina());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                ps.setInt(7, alimentacao.getIdAlimentacao());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new AlimentacaoException("Alimentacao não encontrada: id=" + alimentacao.getIdAlimentacao());
                }
            }
            conn.commit();
            Logger.info("AlimentacaoDaoNativeImpl.update - sucesso");
            return findById(alimentacao.getIdAlimentacao());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("AlimentacaoDaoNativeImpl.update - erro", e);
            throw new AlimentacaoException("Erro ao atualizar Alimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Alimentacao WHERE id_alimentacao=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new AlimentacaoException("Alimentacao não encontrada: id=" + id);
                }
            }
            conn.commit();
            Logger.info("AlimentacaoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("AlimentacaoDaoNativeImpl.deleteById - erro", e);
            throw new AlimentacaoException("Erro ao deletar Alimentacao", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Alimentacao findById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE id_alimentacao=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("AlimentacaoDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findById - erro", e);
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id, e);
        }
    }

    @Override
    public Alimentacao findWithVideoById(Integer id) throws AlimentacaoException {
        Logger.info("AlimentacaoDaoNativeImpl.findWithVideoById - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, video, preparo, id_rotina FROM Alimentacao WHERE id_alimentacao=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("AlimentacaoDaoNativeImpl.findWithVideoById - sucesso");
                    return map(rs, true);
                }
            }
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id);
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findWithVideoById - erro", e);
            throw new AlimentacaoException("Alimentacao não encontrada: id=" + id, e);
        }
    }

    @Override
    public List<Alimentacao> findAll() {
        Logger.info("AlimentacaoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("AlimentacaoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findAll(int page, int size) {
        Logger.info("AlimentacaoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao LIMIT ? OFFSET ?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByStatus(Integer status) {
        Logger.info("AlimentacaoDaoNativeImpl.findByStatus - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE status=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByStatus - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByStatus - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByNome(String nome) {
        Logger.info("AlimentacaoDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE nome=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByLink(String link) {
        Logger.info("AlimentacaoDaoNativeImpl.findByLink - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE link=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, link);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByLink - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByLink - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByVideo(byte[] video) {
        Logger.info("AlimentacaoDaoNativeImpl.findByVideo - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, video, preparo, id_rotina FROM Alimentacao WHERE video=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, video);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByVideo - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByVideo - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByPreparo(String preparo) {
        Logger.info("AlimentacaoDaoNativeImpl.findByPreparo - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE preparo=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, preparo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByPreparo - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByPreparo - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> findByIdRotina(Integer idRotina) {
        Logger.info("AlimentacaoDaoNativeImpl.findByIdRotina - inicio");
        String sql = "SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE id_rotina=?";
        List<Alimentacao> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRotina);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("AlimentacaoDaoNativeImpl.findByIdRotina - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.findByIdRotina - erro", e);
        }
        return list;
    }

    @Override
    public List<Alimentacao> search(Alimentacao filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Alimentacao> search(Alimentacao filtro, int page, int size) {
        Logger.info("AlimentacaoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_alimentacao, status, nome, link, preparo, id_rotina FROM Alimentacao WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getLink() != null && !filtro.getLink().isEmpty()) {
            sb.append(" AND link=?");
            params.add(filtro.getLink());
        }
        if (filtro.getVideo() != null) {
            sb.append(" AND video=?");
            params.add(filtro.getVideo());
        }
        if (filtro.getPreparo() != null && !filtro.getPreparo().isEmpty()) {
            sb.append(" AND preparo=?");
            params.add(filtro.getPreparo());
        }
        if (filtro.getIdRotina() != null) {
            sb.append(" AND id_rotina=?");
            params.add(filtro.getIdRotina());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Alimentacao> list = new ArrayList<>();
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
            Logger.info("AlimentacaoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("AlimentacaoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
