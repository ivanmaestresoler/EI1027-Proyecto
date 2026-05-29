package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.APRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class APRequestDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class APRequestRowMapper implements RowMapper<APRequest> {
        public APRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            APRequest request = new APRequest();
            request.setIdRequest(rs.getInt("id_request"));
            request.setIdUsuari(rs.getInt("id_usuari"));
            if (rs.getObject("id_idioma") != null) {
                request.setIdIdioma(rs.getInt("id_idioma"));
            }
            if (rs.getDate("data_solicitud") != null) {
                request.setDataSollicitud(rs.getDate("data_solicitud").toLocalDate());
            }
            request.setExperienciaPrevia(rs.getString("experiencia_previa"));
            request.setFormacioAcademica(rs.getString("formacio_academica"));
            request.setGenereAssistent(rs.getString("genere_assistent"));
            request.setLocalitat(rs.getString("localitat"));
            request.setEstatRequest(rs.getString("estat_request"));
            request.setTipusAssistencia(rs.getString("tipus_assistencia"));
            return request;
        }
    }

    public void addAPRequest(APRequest request) {
        String sql = "INSERT INTO aprequest (id_usuari, id_idioma, data_solicitud, experiencia_previa, formacio_academica, genere_assistent, localitat, estat_request, tipus_assistencia) VALUES (?, ?, ?, ?, ?, ?::enum_genere, ?, ?::enum_estat_request, ?::enum_tipus_assistencia)";
        jdbcTemplate.update(sql, request.getIdUsuari(), request.getIdIdioma(), request.getDataSollicitud(), request.getExperienciaPrevia(), request.getFormacioAcademica(), request.getGenereAssistent(), request.getLocalitat(), request.getEstatRequest(), request.getTipusAssistencia());
    }

    public void updateAPRequest(APRequest request) {
        String sql = "UPDATE aprequest SET id_usuari=?, id_idioma=?, data_solicitud=?, experiencia_previa=?, formacio_academica=?, genere_assistent=?::enum_genere, localitat=?, estat_request=?::enum_estat_request, tipus_assistencia=?::enum_tipus_assistencia WHERE id_request=?";
        jdbcTemplate.update(sql, request.getIdUsuari(), request.getIdIdioma(), request.getDataSollicitud(), request.getExperienciaPrevia(), request.getFormacioAcademica(), request.getGenereAssistent(), request.getLocalitat(), request.getEstatRequest(), request.getTipusAssistencia(), request.getIdRequest());
    }

    public void deleteAPRequest(Integer idRequest) {
        String sql = "DELETE FROM aprequest WHERE id_request=?";
        jdbcTemplate.update(sql, idRequest);
    }

    public APRequest getAPRequest(Integer idRequest) {
        String sql = "SELECT * FROM aprequest WHERE id_request=?";
        try {
            return jdbcTemplate.queryForObject(sql, new APRequestRowMapper(), idRequest);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<APRequest> getAPRequests() {
        String sql = "SELECT * FROM aprequest";
        return jdbcTemplate.query(sql, new APRequestRowMapper());
    }

    public List<APRequest> getAPRequestsPerUsuari(Integer idUsuari) {
        String sql = "SELECT * FROM aprequest WHERE id_usuari=?";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), idUsuari);
    }

    public List<APRequest> getAPRequestsEnRevisio() {
        String sql = "SELECT * FROM aprequest WHERE estat_request='En revisió'::enum_estat_request";
        return jdbcTemplate.query(sql, new APRequestRowMapper());
    }

    public void aprovarRequest(Integer idRequest) {
        String sql = "UPDATE aprequest SET estat_request='Aprovada'::enum_estat_request WHERE id_request=?";
        jdbcTemplate.update(sql, idRequest);
    }

    public void rebutjarRequest(Integer idRequest) {
        String sql = "UPDATE aprequest SET estat_request='Rebutjada'::enum_estat_request WHERE id_request=?";
        jdbcTemplate.update(sql, idRequest);
    }

    public List<APRequest> getAPRequestsPerUsuariFiltrades(Integer idUsuari, String estat) {
        if (estat == null || estat.isEmpty()) {
            return getAPRequestsPerUsuari(idUsuari);
        }
        String sql = "SELECT * FROM aprequest WHERE id_usuari=? AND estat_request=?::enum_estat_request";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), idUsuari, estat);
    }

    public List<APRequest> getAPRequestsFiltrades(String estat) {
        if (estat == null || estat.isEmpty()) {
            return getAPRequests();
        }
        String sql = "SELECT * FROM aprequest WHERE estat_request=?::enum_estat_request";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), estat);
    }

    public List<APRequest> getAPRequestsPerUsuariFiltradesPaginadas(Integer idUsuari, String estat, int limit, int offset) {
        if (estat == null || estat.isEmpty()) {
            String sql = "SELECT * FROM aprequest WHERE id_usuari=? LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, new APRequestRowMapper(), idUsuari, limit, offset);
        }
        String sql = "SELECT * FROM aprequest WHERE id_usuari=? AND estat_request=?::enum_estat_request LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), idUsuari, estat, limit, offset);
    }

    public int countAPRequestsPerUsuariFiltrades(Integer idUsuari, String estat) {
        if (estat == null || estat.isEmpty()) {
            String sql = "SELECT COUNT(*) FROM aprequest WHERE id_usuari=?";
            return jdbcTemplate.queryForObject(sql, Integer.class, idUsuari);
        }
        String sql = "SELECT COUNT(*) FROM aprequest WHERE id_usuari=? AND estat_request=?::enum_estat_request";
        return jdbcTemplate.queryForObject(sql, Integer.class, idUsuari, estat);
    }

    public List<APRequest> getAPRequestsFiltradesPaginadas(String estat, int limit, int offset) {
        if (estat == null || estat.isEmpty()) {
            String sql = "SELECT * FROM aprequest LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, new APRequestRowMapper(), limit, offset);
        }
        String sql = "SELECT * FROM aprequest WHERE estat_request=?::enum_estat_request LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), estat, limit, offset);
    }

    public int countAPRequestsFiltrades(String estat) {
        if (estat == null || estat.isEmpty()) {
            String sql = "SELECT COUNT(*) FROM aprequest";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        }
        String sql = "SELECT COUNT(*) FROM aprequest WHERE estat_request=?::enum_estat_request";
        return jdbcTemplate.queryForObject(sql, Integer.class, estat);
    }
}