package dao.impl;

import conexao.ConnectionFactory;
import dao.api.EventoDao;
import exception.EventoException;
import infra.Logger;
import model.Categoria;
import model.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDaoNativeImpl implements EventoDao {
    private Evento map(ResultSet rs, boolean withFoto) throws SQLException {
        Evento e = new Evento();
        e.setIdEvento(rs.getInt("id_evento"));
        e.setVantagem((Boolean) rs.getObject("vantagem"));
        e.setStatus((Integer) rs.getObject("status"));
        if (withFoto) {
            e.setFoto(rs.getBytes("foto"));
        }
        e.setNome(rs.getString("nome"));
        e.setDescricao(rs.getString("descricao"));
        Date dt = rs.getDate("data_criacao");
        if (dt != null) {
            e.setDataCriacao(dt.toLocalDate());
        }
        Integer idCategoria = (Integer) rs.getObject("id_categoria");
        if (idCategoria != null) {
            Categoria c = new Categoria();
            c.setIdCategoria(idCategoria);
            e.setCategoria(c);
        }
        return e;
    }

    @Override
    public void create(Evento evento) throws EventoException {
        Logger.info("EventoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Evento (status, vantagem, foto, nome, descricao, data_criacao, id_categoria) VALUES (?,?,?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (evento.getStatus() != null) {
                    ps.setInt(1, evento.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                if (evento.getVantagem() != null) {
                    ps.setBoolean(2, evento.getVantagem());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                ps.setBytes(3, evento.getFoto());
                ps.setString(4, evento.getNome());
                ps.setString(5, evento.getDescricao());
                if (evento.getDataCriacao() != null) {
                    ps.setDate(6, Date.valueOf(evento.getDataCriacao()));
                } else {
                    ps.setNull(6, Types.DATE);
                }
                if (evento.getCategoria() != null && evento.getCategoria().getIdCategoria() != null) {
                    ps.setInt(7, evento.getCategoria().getIdCategoria());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("EventoDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("EventoDaoNativeImpl.create - erro", e);
            throw new EventoException("Erro ao criar Evento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Evento update(Evento evento) throws EventoException {
        Logger.info("EventoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Evento SET status=?, vantagem=?, foto=?, nome=?, descricao=?, data_criacao=?, id_categoria=? WHERE id_evento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (evento.getStatus() != null) {
                    ps.setInt(1, evento.getStatus());
                } else {
                    ps.setNull(1, Types.INTEGER);
                }
                if (evento.getVantagem() != null) {
                    ps.setBoolean(2, evento.getVantagem());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                ps.setBytes(3, evento.getFoto());
                ps.setString(4, evento.getNome());
                ps.setString(5, evento.getDescricao());
                if (evento.getDataCriacao() != null) {
                    ps.setDate(6, Date.valueOf(evento.getDataCriacao()));
                } else {
                    ps.setNull(6, Types.DATE);
                }
                if (evento.getCategoria() != null && evento.getCategoria().getIdCategoria() != null) {
                    ps.setInt(7, evento.getCategoria().getIdCategoria());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.setInt(8, evento.getIdEvento());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new EventoException("Evento não encontrado: id=" + evento.getIdEvento());
                }
            }
            conn.commit();
            Logger.info("EventoDaoNativeImpl.update - sucesso");
            return findById(evento.getIdEvento());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("EventoDaoNativeImpl.update - erro", e);
            throw new EventoException("Erro ao atualizar Evento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Evento WHERE id_evento=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new EventoException("Evento não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("EventoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("EventoDaoNativeImpl.deleteById - erro", e);
            throw new EventoException("Erro ao deletar Evento", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Evento findById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_evento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("EventoDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new EventoException("Evento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findById - erro", e);
            throw new EventoException("Evento não encontrado: id=" + id, e);
        }
    }

    @Override
    public Evento findWithBlobsById(Integer id) throws EventoException {
        Logger.info("EventoDaoNativeImpl.findWithBlobsById - inicio");
        String sql = "SELECT id_evento, status, vantagem, foto, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_evento=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("EventoDaoNativeImpl.findWithBlobsById - sucesso");
                    return map(rs, true);
                }
            }
            throw new EventoException("Evento não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findWithBlobsById - erro", e);
            throw new EventoException("Evento não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<Evento> findAll() {
        Logger.info("EventoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs, false));
            }
            Logger.info("EventoDaoNativeImpl.findAll - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findAll - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findAll(int page, int size) {
        Logger.info("EventoDaoNativeImpl.findAll(page) - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento LIMIT ? OFFSET ?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findAll(page) - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findAll(page) - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByVantagem(Boolean vantagem) {
        Logger.info("EventoDaoNativeImpl.findByVantagem - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE vantagem=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (vantagem != null) {
                ps.setBoolean(1, vantagem);
            } else {
                ps.setNull(1, Types.BOOLEAN);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByVantagem - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByVantagem - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByFoto(byte[] foto) {
        Logger.info("EventoDaoNativeImpl.findByFoto - inicio");
        String sql = "SELECT id_evento, status, vantagem, foto, nome, descricao, data_criacao, id_categoria FROM Evento WHERE foto=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, foto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, true));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByFoto - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByFoto - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByNome(String nome) {
        Logger.info("EventoDaoNativeImpl.findByNome - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE nome=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByNome - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByNome - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByDescricao(String descricao) {
        Logger.info("EventoDaoNativeImpl.findByDescricao - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE descricao=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByDescricao - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByDescricao - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByDataCriacao(java.time.LocalDate dataCriacao) {
        Logger.info("EventoDaoNativeImpl.findByDataCriacao - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE data_criacao=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dataCriacao));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByDataCriacao - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByDataCriacao - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> findByIdCategoria(Integer idCategoria) {
        Logger.info("EventoDaoNativeImpl.findByIdCategoria - inicio");
        String sql = "SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE id_categoria=?";
        List<Evento> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, false));
                }
            }
            Logger.info("EventoDaoNativeImpl.findByIdCategoria - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.findByIdCategoria - erro", e);
        }
        return list;
    }

    @Override
    public List<Evento> search(Evento filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Evento> search(Evento filtro, int page, int size) {
        Logger.info("EventoDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder("SELECT id_evento, status, vantagem, nome, descricao, data_criacao, id_categoria FROM Evento WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getVantagem() != null) {
            sb.append(" AND vantagem=?");
            params.add(filtro.getVantagem());
        }
        if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
            sb.append(" AND nome=?");
            params.add(filtro.getNome());
        }
        if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty()) {
            sb.append(" AND descricao=?");
            params.add(filtro.getDescricao());
        }
        if (filtro.getStatus() != null) {
            sb.append(" AND status=?");
            params.add(filtro.getStatus());
        }
        if (filtro.getDataCriacao() != null) {
            sb.append(" AND data_criacao=?");
            params.add(Date.valueOf(filtro.getDataCriacao()));
        }
        if (filtro.getCategoria() != null && filtro.getCategoria().getIdCategoria() != null) {
            sb.append(" AND id_categoria=?");
            params.add(filtro.getCategoria().getIdCategoria());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Evento> list = new ArrayList<>();
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
            Logger.info("EventoDaoNativeImpl.search - sucesso");
        } catch (SQLException e) {
            Logger.error("EventoDaoNativeImpl.search - erro", e);
        }
        return list;
    }
}
