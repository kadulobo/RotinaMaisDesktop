// path: src/main/java/dao/impl/ExercicioDaoNativeImpl.java
package dao.impl;

import conexao.ConnectionFactory;
import dao.api.ExercicioDao;
import exception.ExercicioException;
import infra.Logger;
import model.Exercicio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link ExercicioDao} replacing the old EntityManager based version.
 */
public class ExercicioDaoNativeImpl implements ExercicioDao {

    private Exercicio map(ResultSet rs) throws SQLException {
        Exercicio e = new Exercicio();
        e.setIdExercicio(rs.getInt("id_exercicio"));
        e.setNome(rs.getString("nome"));
        e.setCargaLeve((Integer) rs.getObject("carga_leve"));
        e.setCargaMedia((Integer) rs.getObject("carga_media"));
        e.setCargaMaxima((Integer) rs.getObject("carga_maxima"));
        return e;
    }

    @Override
    public void create(Exercicio exercicio) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Exercicio (nome, carga_leve, carga_media, carga_maxima) VALUES (?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, exercicio.getNome());
                if (exercicio.getCargaLeve() != null) {
                    ps.setInt(2, exercicio.getCargaLeve());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (exercicio.getCargaMedia() != null) {
                    ps.setInt(3, exercicio.getCargaMedia());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                if (exercicio.getCargaMaxima() != null) {
                    ps.setInt(4, exercicio.getCargaMaxima());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("ExercicioDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("ExercicioDaoNativeImpl.create - erro", e);
            throw new ExercicioException("Erro ao criar Exercicio", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Exercicio update(Exercicio exercicio) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.update - inicio");
        String sql = "UPDATE Exercicio SET nome=?, carga_leve=?, carga_media=?, carga_maxima=? WHERE id_exercicio=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, exercicio.getNome());
                if (exercicio.getCargaLeve() != null) {
                    ps.setInt(2, exercicio.getCargaLeve());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                if (exercicio.getCargaMedia() != null) {
                    ps.setInt(3, exercicio.getCargaMedia());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                if (exercicio.getCargaMaxima() != null) {
                    ps.setInt(4, exercicio.getCargaMaxima());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setInt(5, exercicio.getIdExercicio());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new ExercicioException("Exercicio não encontrado: id=" + exercicio.getIdExercicio());
                }
            }
            conn.commit();
            Logger.info("ExercicioDaoNativeImpl.update - sucesso");
            return findById(exercicio.getIdExercicio());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("ExercicioDaoNativeImpl.update - erro", e);
            throw new ExercicioException("Erro ao atualizar Exercicio", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Exercicio WHERE id_exercicio=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new ExercicioException("Exercicio não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("ExercicioDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("ExercicioDaoNativeImpl.deleteById - erro", e);
            throw new ExercicioException("Erro ao deletar Exercicio", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Exercicio findById(Integer id) throws ExercicioException {
        Logger.info("ExercicioDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE id_exercicio=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("ExercicioDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new ExercicioException("Exercicio não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findById - erro", e);
            throw new ExercicioException("Exercicio não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Exercicio> findAll() {
        Logger.info("ExercicioDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
            Logger.info("ExercicioDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> findAll(int page, int size) {
        Logger.info("ExercicioDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio LIMIT ? OFFSET ?";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("ExercicioDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> findByNome(String nome) {
        Logger.info("ExercicioDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE nome=?";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("ExercicioDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> findByCargaLeve(Integer cargaLeve) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaLeve - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_leve=?";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cargaLeve != null) {
                ps.setInt(1, cargaLeve);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("ExercicioDaoNativeImpl.findByCargaLeve - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findByCargaLeve - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> findByCargaMedia(Integer cargaMedia) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaMedia - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_media=?";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cargaMedia != null) {
                ps.setInt(1, cargaMedia);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("ExercicioDaoNativeImpl.findByCargaMedia - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findByCargaMedia - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> findByCargaMaxima(Integer cargaMaxima) {
        Logger.info("ExercicioDaoNativeImpl.findByCargaMaxima - inicio");
        String sql = "SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE carga_maxima=?";
        List<Exercicio> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cargaMaxima != null) {
                ps.setInt(1, cargaMaxima);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            Logger.info("ExercicioDaoNativeImpl.findByCargaMaxima - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.findByCargaMaxima - erro", e);
        }
        return list;
    }

    @Override
    public List<Exercicio> search(Exercicio filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Exercicio> search(Exercicio filtro, int page, int size) {
        Logger.info("ExercicioDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_exercicio, nome, carga_leve, carga_media, carga_maxima FROM Exercicio WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getCargaLeve() != null) {
            sb.append(" AND carga_leve=?");
            params.add(filtro.getCargaLeve());
        }
        if (filtro.getCargaMedia() != null) {
            sb.append(" AND carga_media=?");
            params.add(filtro.getCargaMedia());
        }
        if (filtro.getCargaMaxima() != null) {
            sb.append(" AND carga_maxima=?");
            params.add(filtro.getCargaMaxima());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Exercicio> list = new ArrayList<>();
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
            Logger.info("ExercicioDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("ExercicioDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}

