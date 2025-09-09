package dao.impl;

import dao.api.TreinoExercicioDao;
import exception.TreinoExercicioException;
import infra.Logger;
import model.TreinoExercicio;

import conexao.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-based implementation of TreinoExercicioDao.
 */
public class TreinoExercicioDaoNativeImpl implements TreinoExercicioDao {
    private TreinoExercicio map(ResultSet rs) throws SQLException {
        TreinoExercicio t = new TreinoExercicio();
        t.setIdTreinoExercicio(rs.getInt("id_treino_exercicio"));
        t.setQtdRepeticao(rs.getInt("qtd_repeticao"));
        t.setTempoDescanso(rs.getString("tempo_descanso"));
        t.setOrdem(rs.getInt("ordem"));
        Object feitoObj = rs.getObject("feito");
        t.setFeito(feitoObj != null ? rs.getBoolean("feito") : null);
        t.setIdExercicio(rs.getInt("id_exercicio"));
        t.setIdTreino(rs.getInt("id_treino"));
        return t;
    }

    @Override
    public void create(TreinoExercicio e) {
        Logger.info("TreinoExercicioDaoNativeImpl.create");
        String sql = "INSERT INTO Treino_Exercicio (qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino) VALUES (?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getQtdRepeticao());
                ps.setString(2, e.getTempoDescanso());
                ps.setInt(3, e.getOrdem());
                if (e.getFeito() != null) {
                    ps.setBoolean(4, e.getFeito());
                } else {
                    ps.setNull(4, java.sql.Types.BOOLEAN);
                }
                ps.setInt(5, e.getIdExercicio());
                ps.setInt(6, e.getIdTreino());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("TreinoExercicioDaoNativeImpl.create - erro", ex);
            throw new TreinoExercicioException("Erro ao criar TreinoExercicio", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(TreinoExercicio e) {
        Logger.info("TreinoExercicioDaoNativeImpl.update");
        String sql = "UPDATE Treino_Exercicio SET qtd_repeticao=?, tempo_descanso=?, ordem=?, feito=?, id_exercicio=?, id_treino=? WHERE id_treino_exercicio=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getQtdRepeticao());
                ps.setString(2, e.getTempoDescanso());
                ps.setInt(3, e.getOrdem());
                if (e.getFeito() != null) {
                    ps.setBoolean(4, e.getFeito());
                } else {
                    ps.setNull(4, java.sql.Types.BOOLEAN);
                }
                ps.setInt(5, e.getIdExercicio());
                ps.setInt(6, e.getIdTreino());
                ps.setInt(7, e.getIdTreinoExercicio());
                if (ps.executeUpdate() == 0) {
                    throw new TreinoExercicioException("TreinoExercicio nao encontrado: id=" + e.getIdTreinoExercicio());
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("TreinoExercicioDaoNativeImpl.update - erro", ex);
            throw new TreinoExercicioException("Erro ao atualizar TreinoExercicio", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("TreinoExercicioDaoNativeImpl.deleteById");
        String sql = "DELETE FROM Treino_Exercicio WHERE id_treino_exercicio=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 0) {
                    throw new TreinoExercicioException("TreinoExercicio nao encontrado: id=" + id);
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) { }
            }
            Logger.error("TreinoExercicioDaoNativeImpl.deleteById - erro", ex);
            throw new TreinoExercicioException("Erro ao deletar TreinoExercicio", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public TreinoExercicio findById(Integer id) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE id_treino_exercicio = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findById - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar TreinoExercicio", ex);
        }
        throw new TreinoExercicioException("TreinoExercicio nao encontrado: id=" + id);
    }

    @Override
    public List<TreinoExercicio> findAll() {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<TreinoExercicio> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findAll - erro", ex);
            throw new TreinoExercicioException("Erro ao listar TreinoExercicio", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findAll(int page, int size) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findAll(page) - erro", ex);
            throw new TreinoExercicioException("Erro ao paginar TreinoExercicio", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByQtdRepeticao(Integer qtdRepeticao) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE qtd_repeticao = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qtdRepeticao);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByQtdRepeticao - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por qtdRepeticao", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByTempoDescanso(String tempoDescanso) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE tempo_descanso = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tempoDescanso);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByTempoDescanso - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por tempoDescanso", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByOrdem(Integer ordem) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE ordem = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ordem);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByOrdem - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por ordem", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByFeito(Boolean feito) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE feito = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (feito != null) {
                ps.setBoolean(1, feito);
            } else {
                ps.setNull(1, java.sql.Types.BOOLEAN);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByFeito - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por feito", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByIdExercicio(Integer idExercicio) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE id_exercicio = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idExercicio);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByIdExercicio - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por idExercicio", ex);
        }
    }

    @Override
    public List<TreinoExercicio> findByIdTreino(Integer idTreino) {
        String sql = "SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE id_treino = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTreino);
            try (ResultSet rs = ps.executeQuery()) {
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.findByIdTreino - erro", ex);
            throw new TreinoExercicioException("Erro ao buscar por idTreino", ex);
        }
    }

    @Override
    public List<TreinoExercicio> search(TreinoExercicio f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<TreinoExercicio> search(TreinoExercicio f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_treino_exercicio, qtd_repeticao, tempo_descanso, ordem, feito, id_exercicio, id_treino FROM Treino_Exercicio WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (f.getQtdRepeticao() != null) {
            sb.append(" AND qtd_repeticao = ?");
            params.add(f.getQtdRepeticao());
        }
        if (f.getTempoDescanso() != null && !f.getTempoDescanso().isEmpty()) {
            sb.append(" AND tempo_descanso = ?");
            params.add(f.getTempoDescanso());
        }
        if (f.getOrdem() != null) {
            sb.append(" AND ordem = ?");
            params.add(f.getOrdem());
        }
        if (f.getFeito() != null) {
            sb.append(" AND feito = ?");
            params.add(f.getFeito());
        }
        if (f.getIdExercicio() != null) {
            sb.append(" AND id_exercicio = ?");
            params.add(f.getIdExercicio());
        }
        if (f.getIdTreino() != null) {
            sb.append(" AND id_treino = ?");
            params.add(f.getIdTreino());
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
                List<TreinoExercicio> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.error("TreinoExercicioDaoNativeImpl.search - erro", ex);
            throw new TreinoExercicioException("Erro na busca de TreinoExercicio", ex);
        }
    }
}