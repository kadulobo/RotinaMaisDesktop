package dao.impl;

import conexao.ConnectionFactory;
import dao.api.SiteObjetoDao;
import exception.SiteObjetoException;
import infra.Logger;
import model.SiteObjeto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SiteObjetoDaoNativeImpl implements SiteObjetoDao {
    private SiteObjeto map(ResultSet rs) throws SQLException {
        SiteObjeto so = new SiteObjeto();
        so.setIdSiteObjeto(rs.getInt("id_site_objeto"));
        so.setIdSite(rs.getInt("id_site"));
        so.setIdObjeto(rs.getInt("id_objeto"));
        return so;
    }

    @Override
    public void create(SiteObjeto e) {
        Logger.info("SiteObjetoDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Site_Objeto (id_site, id_objeto) VALUES (?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getIdSite());
                ps.setInt(2, e.getIdObjeto());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("SiteObjetoDaoNativeImpl.create - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback create", e1); }
            }
            Logger.error("SiteObjetoDaoNativeImpl.create - erro", ex);
            throw new SiteObjetoException("Erro ao criar SiteObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void update(SiteObjeto e) {
        Logger.info("SiteObjetoDaoNativeImpl.update - inicio");
        String sql = "UPDATE Site_Objeto SET id_site=?, id_objeto=? WHERE id_site_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, e.getIdSite());
                ps.setInt(2, e.getIdObjeto());
                ps.setInt(3, e.getIdSiteObjeto());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new SiteObjetoException("SiteObjeto não encontrado: id=" + e.getIdSiteObjeto());
                }
            }
            conn.commit();
            Logger.info("SiteObjetoDaoNativeImpl.update - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback update", e1); }
            }
            Logger.error("SiteObjetoDaoNativeImpl.update - erro", ex);
            throw new SiteObjetoException("Erro ao atualizar SiteObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        Logger.info("SiteObjetoDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Site_Objeto WHERE id_site_objeto=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new SiteObjetoException("SiteObjeto não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("SiteObjetoDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e1) { Logger.error("Rollback delete", e1); }
            }
            Logger.error("SiteObjetoDaoNativeImpl.deleteById - erro", ex);
            throw new SiteObjetoException("Erro ao deletar SiteObjeto", ex);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public SiteObjeto findById(Integer id) {
        Logger.info("SiteObjetoDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_site_objeto=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("SiteObjetoDaoNativeImpl.findById - sucesso");
                    return map(rs);
                }
            }
            throw new SiteObjetoException("SiteObjeto não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("SiteObjetoDaoNativeImpl.findById - erro", e);
            throw new SiteObjetoException("SiteObjeto não encontrado: id=" + id, e);
        }
    }

    @Override
    public List<SiteObjeto> findAll() {
        Logger.info("SiteObjetoDaoNativeImpl.findAll - inicio");
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<SiteObjeto> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(map(rs));
            }
            Logger.info("SiteObjetoDaoNativeImpl.findAll - sucesso");
            return lista;
        } catch (SQLException e) {
            Logger.error("SiteObjetoDaoNativeImpl.findAll - erro", e);
            throw new SiteObjetoException("Erro ao listar SiteObjeto", e);
        }
    }

    @Override
    public List<SiteObjeto> findAll(int page, int size) {
        Logger.info("SiteObjetoDaoNativeImpl.findAll(paginated) - inicio");
        String sql = "SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto LIMIT ? OFFSET ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, page * size);
            try (ResultSet rs = ps.executeQuery()) {
                List<SiteObjeto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(map(rs));
                }
                Logger.info("SiteObjetoDaoNativeImpl.findAll(paginated) - sucesso");
                return lista;
            }
        } catch (SQLException e) {
            Logger.error("SiteObjetoDaoNativeImpl.findAll(paginated) - erro", e);
            throw new SiteObjetoException("Erro ao listar SiteObjeto", e);
        }
    }

    private List<SiteObjeto> findByField(String sql, Integer value) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                List<SiteObjeto> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("SiteObjetoDaoNativeImpl.findByField - erro", e);
            throw new SiteObjetoException("Erro na consulta de SiteObjeto", e);
        }
    }

    @Override
    public List<SiteObjeto> findByIdSite(Integer idSite) {
        return findByField("SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_site = ?", idSite);
    }

    @Override
    public List<SiteObjeto> findByIdObjeto(Integer idObjeto) {
        return findByField("SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE id_objeto = ?", idObjeto);
    }

    @Override
    public List<SiteObjeto> search(SiteObjeto f) {
        return search(f, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<SiteObjeto> search(SiteObjeto f, int page, int size) {
        StringBuilder sb = new StringBuilder("SELECT id_site_objeto, id_site, id_objeto FROM Site_Objeto WHERE 1=1");
        List<Integer> params = new ArrayList<>();
        if (f.getIdSite() != null) {
            sb.append(" AND id_site = ?");
            params.add(f.getIdSite());
        }
        if (f.getIdObjeto() != null) {
            sb.append(" AND id_objeto = ?");
            params.add(f.getIdObjeto());
        }
        sb.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setInt(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<SiteObjeto> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }
        } catch (SQLException e) {
            Logger.error("SiteObjetoDaoNativeImpl.search - erro", e);
            throw new SiteObjetoException("Erro na busca de SiteObjeto", e);
        }
    }
}
