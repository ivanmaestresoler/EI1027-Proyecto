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

            if (rs.getDate("data_solicitud") != null) {
                request.setDataSollicitud(rs.getDate("data_solicitud").toLocalDate());
            }

            request.setPreferencies(rs.getString("preferencies"));
            request.setEstatRequest(rs.getString("estat_request"));
            request.setTipusAssistencia(rs.getString("tipus_assistencia"));

            return request;
        }
    }

    public void addAPRequest(APRequest request) {
        String sql = "INSERT INTO aprequest (id_usuari, data_solicitud, preferencies, estat_request, tipus_assistencia) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, request.getIdUsuari(), request.getDataSollicitud(), request.getPreferencies(), request.getEstatRequest(), request.getTipusAssistencia());
    }

    public void updateAPRequest(APRequest request) {
        String sql = "UPDATE aprequest SET id_usuari=?, data_solicitud=?, preferencies=?, estat_request=?, tipus_assistencia=? " +
                "WHERE id_request=?";
        jdbcTemplate.update(sql, request.getIdUsuari(), request.getDataSollicitud(), request.getPreferencies(), request.getEstatRequest(), request.getTipusAssistencia(), request.getIdRequest());
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
}