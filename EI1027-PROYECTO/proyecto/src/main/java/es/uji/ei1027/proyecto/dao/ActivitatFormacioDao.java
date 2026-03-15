package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.ActivitatFormacio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ActivitatFormacioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class ActivitatFormacioRowMapper implements RowMapper<ActivitatFormacio> {
        public ActivitatFormacio mapRow(ResultSet rs, int rowNum) throws SQLException {
            ActivitatFormacio activitat = new ActivitatFormacio();
            activitat.setIdActivitat(rs.getInt("id_activitat"));
            activitat.setIdFormador(rs.getInt("id_formador"));
            activitat.setTitol(rs.getString("titol"));
            activitat.setDescripcio(rs.getString("descripcio"));
            
            if (rs.getTimestamp("data_hora") != null) {
                activitat.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
            }
            
            activitat.setTipusActivitat(rs.getString("tipus_activitat"));
            
            if (rs.getObject("aforament_maxim") != null) {
                activitat.setAforamentMaxim(rs.getInt("aforament_maxim"));
            }
            
            return activitat;
        }
    }

    public List<ActivitatFormacio> getActivitats() {
        return jdbcTemplate.query("SELECT * FROM ActivitatFormacio", new ActivitatFormacioRowMapper());
    }

    public ActivitatFormacio getActivitat(Integer idActivitat) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM ActivitatFormacio WHERE id_activitat = ?",
                    new ActivitatFormacioRowMapper(), idActivitat);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addActivitat(ActivitatFormacio activitat) {
        jdbcTemplate.update(
                "INSERT INTO ActivitatFormacio (id_formador, titol, descripcio, data_hora, tipus_activitat, aforament_maxim) VALUES (?, ?, ?, ?, ?::enum_tipus_activitat, ?)",
                activitat.getIdFormador(), activitat.getTitol(), activitat.getDescripcio(),
                activitat.getDataHora(), activitat.getTipusActivitat(), activitat.getAforamentMaxim()
        );
    }

    public void updateActivitat(ActivitatFormacio activitat) {
        jdbcTemplate.update(
                "UPDATE ActivitatFormacio SET id_formador=?, titol=?, descripcio=?, data_hora=?, tipus_activitat=?::enum_tipus_activitat, aforament_maxim=? WHERE id_activitat=?",
                activitat.getIdFormador(), activitat.getTitol(), activitat.getDescripcio(),
                activitat.getDataHora(), activitat.getTipusActivitat(), activitat.getAforamentMaxim(),
                activitat.getIdActivitat()
        );
    }

    public void deleteActivitat(Integer idActivitat) {
        jdbcTemplate.update("DELETE FROM ActivitatFormacio WHERE id_activitat = ?", idActivitat);
    }
}