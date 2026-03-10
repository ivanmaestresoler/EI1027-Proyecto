package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistenciaFormacio;
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
public class AssistenciaFormacioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class AssistenciaFormacioRowMapper implements RowMapper<AssistenciaFormacio> {
        public AssistenciaFormacio mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssistenciaFormacio assistencia = new AssistenciaFormacio();
            assistencia.setIdAssistencia(rs.getInt("id_assistencia"));
            assistencia.setIdActivitat(rs.getInt("id_activitat"));
            
            if (rs.getObject("id_usuari") != null) {
                assistencia.setIdUsuari(rs.getInt("id_usuari"));
            }
            
            if (rs.getObject("id_assistent") != null) {
                assistencia.setIdAssistent(rs.getInt("id_assistent"));
            }
            
            assistencia.setInscripcioPrevia(rs.getBoolean("inscripcio_previa"));
            assistencia.setHaAssistit(rs.getBoolean("ha_assistit"));
            assistencia.setCertificatEmes(rs.getBoolean("certificat_emes"));
            return assistencia;
        }
    }

    public List<AssistenciaFormacio> getAssistencies() {
        return jdbcTemplate.query("SELECT * FROM AssistenciaFormacio", new AssistenciaFormacioRowMapper());
    }

    public AssistenciaFormacio getAssistencia(Integer idAssistencia) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM AssistenciaFormacio WHERE id_assistencia = ?",
                    new AssistenciaFormacioRowMapper(), idAssistencia);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addAssistencia(AssistenciaFormacio assistencia) {
        jdbcTemplate.update(
                "INSERT INTO AssistenciaFormacio (id_activitat, id_usuari, id_assistent, inscripcio_previa, ha_assistit, certificat_emes) VALUES (?, ?, ?, ?, ?, ?)",
                assistencia.getIdActivitat(), assistencia.getIdUsuari(), assistencia.getIdAssistent(),
                assistencia.getInscripcioPrevia(), assistencia.getHaAssistit(), assistencia.getCertificatEmes()
        );
    }

    public void updateAssistencia(AssistenciaFormacio assistencia) {
        jdbcTemplate.update(
                "UPDATE AssistenciaFormacio SET id_activitat=?, id_usuari=?, id_assistent=?, inscripcio_previa=?, ha_assistit=?, certificat_emes=? WHERE id_assistencia=?",
                assistencia.getIdActivitat(), assistencia.getIdUsuari(), assistencia.getIdAssistent(),
                assistencia.getInscripcioPrevia(), assistencia.getHaAssistit(), assistencia.getCertificatEmes(),
                assistencia.getIdAssistencia()
        );
    }

    public void deleteAssistencia(Integer idAssistencia) {
        jdbcTemplate.update("DELETE FROM AssistenciaFormacio WHERE id_assistencia = ?", idAssistencia);
    }
}