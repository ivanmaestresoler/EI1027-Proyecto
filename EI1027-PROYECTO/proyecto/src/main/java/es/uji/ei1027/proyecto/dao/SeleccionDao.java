package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.Seleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SeleccionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SeleccionRowMapper implements RowMapper<Seleccion> {
        public Seleccion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Seleccion seleccion = new Seleccion();
            seleccion.setIdSeleccion(rs.getInt("id_seleccion"));
            seleccion.setIdRequest(rs.getInt("id_request"));
            seleccion.setIdAssistent(rs.getInt("id_assistent"));

            if (rs.getTimestamp("data_proposta") != null) {
                seleccion.setDataProposta(rs.getTimestamp("data_proposta").toLocalDateTime());
            }
            return seleccion;
        }
    }

    public void addSeleccion(Seleccion seleccion) {
        String sql = "INSERT INTO seleccion (id_request, id_assistent, data_proposta) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, seleccion.getIdRequest(), seleccion.getIdAssistent(), seleccion.getDataProposta());
    }

    public void updateSeleccion(Seleccion seleccion) {
        String sql = "UPDATE seleccion SET id_request=?, id_assistent=?, data_proposta=? WHERE id_seleccion=?";
        jdbcTemplate.update(sql, seleccion.getIdRequest(), seleccion.getIdAssistent(), seleccion.getDataProposta(), seleccion.getIdSeleccion());
    }

    public void deleteSeleccion(Integer idSeleccion) {
        String sql = "DELETE FROM seleccion WHERE id_seleccion=?";
        jdbcTemplate.update(sql, idSeleccion);
    }

    public Seleccion getSeleccion(Integer idSeleccion) {
        String sql = "SELECT * FROM seleccion WHERE id_seleccion=?";
        try {
            return jdbcTemplate.queryForObject(sql, new SeleccionRowMapper(), idSeleccion);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Seleccion> getSeleccions() {
        String sql = "SELECT * FROM seleccion";
        return jdbcTemplate.query(sql, new SeleccionRowMapper());
    }
}