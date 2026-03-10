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

            request.setIdRequest(rs.getInt("idRequest"));
            request.setIdUsuari(rs.getInt("idUsuari"));

            if (rs.getDate("dataSolicitud") != null) {
                request.setDataSollicitud(rs.getDate("dataSolicitud").toLocalDate());
            }

            request.setPreferencies(rs.getString("preferencies"));
            request.setEstatRequest(rs.getString("estatRequest"));
            request.setTipusAssistencia(rs.getString("tipusAssistencia"));

            return request;
        }
    }

    public void addAPRequest(APRequest request) {
        String sql = "INSERT INTO apRequest (idRequest, idUsuari, dataSolicitud, preferencies, estatRequest, tipusAssistencia) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, request.getIdRequest(), request.getIdUsuari(), request.getDataSollicitud(), request.getPreferencies(), request.getEstatRequest(), request.getTipusAssistencia());
    }

    public void updateAPRequest(APRequest request) {
        String sql = "UPDATE apRequest SET idUsuari=?, dataSolicitud=?, preferencies=?, estatRequest=?, tipusAssistencia=? " +
                "WHERE idRequest=?";
        jdbcTemplate.update(sql, request.getIdUsuari(), request.getDataSollicitud(), request.getPreferencies(), request.getEstatRequest(), request.getTipusAssistencia(), request.getIdRequest());
    }

    public void deleteAPRequest(Integer idRequest) {
        String sql = "DELETE FROM apRequest WHERE idRequest=?";
        jdbcTemplate.update(sql, idRequest);
    }

    public APRequest getAPRequest(Integer idRequest) {
        String sql = "SELECT * FROM apRequest WHERE idRequest=?";
        try {
            return jdbcTemplate.queryForObject(sql, new APRequestRowMapper(), idRequest);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<APRequest> getAPRequests() {
        String sql = "SELECT * FROM apRequest";
        return jdbcTemplate.query(sql, new APRequestRowMapper());
    }

    public List<APRequest> getAPRequestsPerUsuari(Integer idUsuari) { // Parámetro cambiado a Integer
        String sql = "SELECT * FROM apRequest WHERE idUsuari=?";
        return jdbcTemplate.query(sql, new APRequestRowMapper(), idUsuari);
    }
}