package dao.impl;

import conexao.ConnectionFactory;
import dao.api.TreinoDao;
import exception.TreinoException;
import infra.Logger;
import model.Treino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreinoDaoNativeImpl implements TreinoDao {

    private Treino map(ResultSet rs) throws SQLException {
        Treino t = new Treino();
        t.setIdTreino(rs.getInt("id_treino"));
        t.setNome(rs.getString("nome"));
        t.setClasse(rs.getString("classe"));
        t.setIdRotina((Integer) rs.getObject("id_rotina"));
        return t;
    }

    private List<Treino> listBySql(String sql, Object... params) {
        List<Treino> list = new ArrayList<>();
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
            Logger.error("TreinoDaoNativeImpl.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public void create(Treino treino) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Treino (nome, classe, id_rotina) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, treino.getNome());
                ps.setString(2, treino.getClasse());
                if (treino.getIdRotina() != null) {
                    ps.setInt(3, treino.getIdRotina());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("TreinoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("TreinoDaoNativeImpl.create - erro", e);
            throw new TreinoException("Erro ao criar Treino", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Treino update(Treino treino) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Treino SET nome=?, classe=?, id_rotina=? WHERE id_treino=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, treino.getNome());
                ps.setString(2, treino.getClasse());
                if (treino.getIdRotina() != null) {
                    ps.setInt(3, treino.getIdRotina());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setInt(4, treino.getIdTreino());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new TreinoException("Treino não encontrado: id=" + treino.getIdTreino());
                }
            }
            conn.commit();
            Logger.info("TreinoDaoNativeImpl.update - sucesso");
            return findById(treino.getIdTreino());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("TreinoDaoNativeImpl.update - erro", e);
            throw new TreinoException("Erro ao atualizar Treino", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Treino WHERE id_treino=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new TreinoException("Treino não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("TreinoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("TreinoDaoNativeImpl.deleteById - erro", e);
            throw new TreinoException("Erro ao deletar Treino", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Treino findById(Integer id) throws TreinoException {
        Logger.info("TreinoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_treino, nome, classe, id_rotina FROM Treino WHERE id_treino=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("TreinoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new TreinoException("Treino não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("TreinoDaoNativeImpl.findById - erro", e);
            throw new TreinoException("Treino não encontrado: id=" + id, e);
        }
    }

    private static final String BASE_SELECT = "SELECT id_treino, nome, classe, id_rotina FROM Treino";

    @Override
    public List<Treino> findAll() {
        Logger.info("TreinoDaoNativeImpl.findAll - inicio");
        List<Treino> list = listBySql(BASE_SELECT);
        Logger.info("TreinoDaoNativeImpl.findAll - sucesso");
        return list;
    }

    @Override
    public List<Treino> findAll(int page, int size) {
        Logger.info("TreinoDaoNativeImpl.findAll(page) - inicio");
        List<Treino> list = listBySql(BASE_SELECT + " LIMIT ? OFFSET ?", size, page * size);
        Logger.info("TreinoDaoNativeImpl.findAll(page) - sucesso");
        return list;
    }

    @Override
    public List<Treino> findByNome(String nome) {
        Logger.info("TreinoDaoNativeImpl.findByNome - inicio");
        List<Treino> list = listBySql(BASE_SELECT + " WHERE nome=?", nome);
        Logger.info("TreinoDaoNativeImpl.findByNome - sucesso");
        return list;
    }

    @Override
    public List<Treino> findByClasse(String classe) {
        Logger.info("TreinoDaoNativeImpl.findByClasse - inicio");
        List<Treino> list = listBySql(BASE_SELECT + " WHERE classe=?", classe);
        Logger.info("TreinoDaoNativeImpl.findByClasse - sucesso");
        return list;
    }

    @Override
    public List<Treino> findByIdRotina(Integer idRotina) {
        Logger.info("TreinoDaoNativeImpl.findByIdRotina - inicio");
        List<Treino> list = listBySql(BASE_SELECT + " WHERE id_rotina=?", idRotina);
        Logger.info("TreinoDaoNativeImpl.findByIdRotina - sucesso");
        return list;
    }

    @Override
    public List<Treino> search(Treino filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Treino> search(Treino filtro, int page, int size) {
        Logger.info("TreinoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getClasse() != null && !filtro.getClasse().isEmpty()) {
            sb.append(" AND classe=?");
            params.add(filtro.getClasse());
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
        List<Treino> list = listBySql(sb.toString(), params.toArray());
        Logger.info("TreinoDaoNativeImpl.search - sucesso");
        return list;
    }
}

