package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.ComunicacioUsuariOVIPAP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ComunicacioUsuariOVIPAPDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class ComunicacioRowMapper implements RowMapper<ComunicacioUsuariOVIPAP> {
        public ComunicacioUsuariOVIPAP mapRow(ResultSet rs, int rowNum) throws SQLException {
            ComunicacioUsuariOVIPAP com = new ComunicacioUsuariOVIPAP();
            com.setIdComunicacio(rs.getInt("id_comunicacio"));
            com.setIdSeleccion(rs.getInt("id_seleccion"));
            com.setMissatge(rs.getString("missatge"));
            com.setRemitent(rs.getString("remitent"));

            if (rs.getTimestamp("data_enviament") != null) {
                com.setDataEnviament(rs.getTimestamp("data_enviament").toLocalDateTime());
            }
            return com;
        }
    }

    public void addComunicacio(ComunicacioUsuariOVIPAP com) {
        String sql = "INSERT INTO comunicaciousuariovipap (id_seleccion, missatge, remitent, data_enviament) VALUES (?, ?, ?::enum_remitent, ?)";
        jdbcTemplate.update(sql, com.getIdSeleccion(), com.getMissatge(), com.getRemitent(), com.getDataEnviament());
    }

    public void updateComunicacio(ComunicacioUsuariOVIPAP com) {
        String sql = "UPDATE comunicaciousuariovipap SET id_seleccion=?, missatge=?, remitent=?::enum_remitent, data_enviament=? WHERE id_comunicacio=?";
        jdbcTemplate.update(sql, com.getIdSeleccion(), com.getMissatge(), com.getRemitent(), com.getDataEnviament(), com.getIdComunicacio());
    }

    public void deleteComunicacio(Integer idComunicacio) {
        String sql = "DELETE FROM comunicaciousuariovipap WHERE id_comunicacio=?";
        jdbcTemplate.update(sql, idComunicacio);
    }

    public ComunicacioUsuariOVIPAP getComunicacio(Integer idComunicacio) {
        String sql = "SELECT * FROM comunicaciousuariovipap WHERE id_comunicacio=?";
        try {
            return jdbcTemplate.queryForObject(sql, new ComunicacioRowMapper(), idComunicacio);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ComunicacioUsuariOVIPAP> getComunicacions() {
        String sql = "SELECT * FROM comunicaciousuariovipap";
        return jdbcTemplate.query(sql, new ComunicacioRowMapper());
    }
}