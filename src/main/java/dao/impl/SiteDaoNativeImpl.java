package dao.impl;

import conexao.ConnectionFactory;
import dao.api.SiteDao;
import exception.SiteException;
import infra.Logger;
import model.Site;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiteDaoNativeImpl implements SiteDao {

    private Site map(ResultSet rs, boolean withLogo) throws SQLException {
        Site s = new Site();
        s.setIdSite(rs.getInt("id_site"));
        s.setUrl(rs.getString("url"));
        s.setAtivo((Boolean) rs.getObject("ativo"));
        if (withLogo) {
            s.setLogo(rs.getBytes("logo"));
        }
        return s;
    }

    private List<Site> listBySql(String sql, boolean withLogo, Object... params) {
        List<Site> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs, withLogo));
                }
            }
        } catch (SQLException e) {
            Logger.error("SiteDaoNativeImpl.listBySql - erro", e);
        }
        return list;
    }

    @Override
    public void create(Site site) throws SiteException {
        Logger.info("SiteDaoNativeImpl.create - inicio");
        String sql = "INSERT INTO Site (url, ativo, logo) VALUES (?,?,?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, site.getUrl());
                if (site.getAtivo() != null) {
                    ps.setBoolean(2, site.getAtivo());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                ps.setBytes(3, site.getLogo());
                ps.executeUpdate();
            }
            conn.commit();
            Logger.info("SiteDaoNativeImpl.create - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback create", ex); }
            }
            Logger.error("SiteDaoNativeImpl.create - erro", e);
            throw new SiteException("Erro ao criar Site", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Site update(Site site) throws SiteException {
        Logger.info("SiteDaoNativeImpl.update - inicio");
        String sql = "UPDATE Site SET url=?, ativo=?, logo=? WHERE id_site=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, site.getUrl());
                if (site.getAtivo() != null) {
                    ps.setBoolean(2, site.getAtivo());
                } else {
                    ps.setNull(2, Types.BOOLEAN);
                }
                ps.setBytes(3, site.getLogo());
                ps.setInt(4, site.getIdSite());
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    throw new SiteException("Site não encontrado: id=" + site.getIdSite());
                }
            }
            conn.commit();
            Logger.info("SiteDaoNativeImpl.update - sucesso");
            return findById(site.getIdSite());
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback update", ex); }
            }
            Logger.error("SiteDaoNativeImpl.update - erro", e);
            throw new SiteException("Erro ao atualizar Site", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.deleteById - inicio");
        String sql = "DELETE FROM Site WHERE id_site=?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int deleted = ps.executeUpdate();
                if (deleted == 0) {
                    throw new SiteException("Site não encontrado: id=" + id);
                }
            }
            conn.commit();
            Logger.info("SiteDaoNativeImpl.deleteById - sucesso");
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { Logger.error("Rollback delete", ex); }
            }
            Logger.error("SiteDaoNativeImpl.deleteById - erro", e);
            throw new SiteException("Erro ao deletar Site", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) { }
            }
        }
    }

    @Override
    public Site findById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.findById - inicio");
        String sql = "SELECT id_site, url, ativo FROM Site WHERE id_site=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("SiteDaoNativeImpl.findById - sucesso");
                    return map(rs, false);
                }
            }
            throw new SiteException("Site não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("SiteDaoNativeImpl.findById - erro", e);
            throw new SiteException("Site não encontrado: id=" + id, e);
        }
    }

    @Override
    public Site findWithLogoById(Integer id) throws SiteException {
        Logger.info("SiteDaoNativeImpl.findWithLogoById - inicio");
        String sql = "SELECT id_site, url, ativo, logo FROM Site WHERE id_site=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Logger.info("SiteDaoNativeImpl.findWithLogoById - sucesso");
                    return map(rs, true);
                }
            }
            throw new SiteException("Site não encontrado: id=" + id);
        } catch (SQLException e) {
            Logger.error("SiteDaoNativeImpl.findWithLogoById - erro", e);
            throw new SiteException("Site não encontrado: id=" + id, e);
        }
    }

    private static final String BASE_SELECT = "SELECT id_site, url, ativo FROM Site";

    @Override
    public List<Site> findAll() {
        Logger.info("SiteDaoNativeImpl.findAll - inicio");
        List<Site> list = listBySql(BASE_SELECT, false);
        Logger.info("SiteDaoNativeImpl.findAll - sucesso");
        return list;
    }

    @Override
    public List<Site> findAll(int page, int size) {
        Logger.info("SiteDaoNativeImpl.findAll(page) - inicio");
        List<Site> list = listBySql(BASE_SELECT + " LIMIT ? OFFSET ?", false, size, page * size);
        Logger.info("SiteDaoNativeImpl.findAll(page) - sucesso");
        return list;
    }

    @Override
    public List<Site> findByUrl(String url) {
        Logger.info("SiteDaoNativeImpl.findByUrl - inicio");
        List<Site> list = listBySql(BASE_SELECT + " WHERE url=?", false, url);
        Logger.info("SiteDaoNativeImpl.findByUrl - sucesso");
        return list;
    }

    @Override
    public List<Site> findByAtivo(Boolean ativo) {
        Logger.info("SiteDaoNativeImpl.findByAtivo - inicio");
        List<Site> list = listBySql(BASE_SELECT + " WHERE ativo=?", false, ativo);
        Logger.info("SiteDaoNativeImpl.findByAtivo - sucesso");
        return list;
    }

    @Override
    public List<Site> findByLogo(byte[] logo) {
        Logger.info("SiteDaoNativeImpl.findByLogo - inicio");
        List<Site> list = listBySql("SELECT id_site, url, ativo, logo FROM Site WHERE logo=?", true, logo);
        Logger.info("SiteDaoNativeImpl.findByLogo - sucesso");
        return list;
    }

    @Override
    public List<Site> search(Site filtro) {
        return search(filtro, -1, -1);
    }

    @Override
    public List<Site> search(Site filtro, int page, int size) {
        Logger.info("SiteDaoNativeImpl.search - inicio");
        StringBuilder sb = new StringBuilder(BASE_SELECT + " WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filtro.getUrl() != null && !filtro.getUrl().isEmpty()) {
            sb.append(" AND url=?");
            params.add(filtro.getUrl());
        }
        if (filtro.getAtivo() != null) {
            sb.append(" AND ativo=?");
            params.add(filtro.getAtivo());
        }
        if (filtro.getLogo() != null) {
            sb.append(" AND logo=?");
            params.add(filtro.getLogo());
        }
        if (page >= 0 && size > 0) {
            sb.append(" LIMIT ? OFFSET ?");
            params.add(size);
            params.add(page * size);
        }
        List<Site> list = listBySql(sb.toString(), false, params.toArray());
        Logger.info("SiteDaoNativeImpl.search - sucesso");
        return list;
    }
}

