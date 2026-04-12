package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
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
public class AssistentPersonalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* ========================================================
     * ROW MAPPER INTERNO (Oculto para el resto de la aplicación)
     * ======================================================== */
    private static final class AssistentPersonalRowMapper implements RowMapper<AssistentPersonal> {
        public AssistentPersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssistentPersonal assistent = new AssistentPersonal();

            // Leemos el ID real de la base de datos
            assistent.setIdAssistent(rs.getInt("id_assistent"));

            assistent.setDni(rs.getString("dni"));
            assistent.setNom(rs.getString("nom"));
            assistent.setCognom1(rs.getString("cognom1"));
            assistent.setEmail(rs.getString("email"));
            assistent.setTipus(rs.getString("tipus"));

            // ¡CORREGIDO! Usamos getString porque es un texto ("Candidat"), no un boolean
            assistent.setEstatAcceptat(rs.getString("estat_acceptat"));

            assistent.setContrasenya(rs.getString("contrasenya"));

            return assistent;
        }
    }

    /* ========================================================
     * MÉTODOS DEL DAO
     * ======================================================== */
    public List<AssistentPersonal> getAssistentsPersonals() {
        return jdbcTemplate.query("SELECT * FROM assistentpersonal", new AssistentPersonalRowMapper());
    }

    public AssistentPersonal getAssistentPersonal(String dni) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM assistentpersonal WHERE dni = ?",
                    new AssistentPersonalRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addAssistentPersonal(AssistentPersonal assistent) {
        // En PostgreSQL, como tipus y estat_acceptat son ENUMs, hay que hacerles el cast ::enum_...
        jdbcTemplate.update(
                "INSERT INTO assistentpersonal (dni, nom, cognom1, email, tipus, estat_acceptat, contrasenya) VALUES (?, ?, ?, ?, ?::enum_tipus_assistent, ?::enum_estat_assistent, ?)",
                assistent.getDni(),
                assistent.getNom(),
                assistent.getCognom1(),
                assistent.getEmail(),
                assistent.getTipus(),
                assistent.getEstatAcceptat() != null ? assistent.getEstatAcceptat() : "Candidat", // Valor por defecto si viene vacío
                assistent.getContrasenya()
        );
    }

    public void updateAssistentPersonal(AssistentPersonal assistent) {
        jdbcTemplate.update(
                "UPDATE assistentpersonal SET nom=?, cognom1=?, email=?, tipus=?::enum_tipus_assistent, estat_acceptat=?::enum_estat_assistent, contrasenya=? WHERE dni=?",
                assistent.getNom(),
                assistent.getCognom1(),
                assistent.getEmail(),
                assistent.getTipus(),
                assistent.getEstatAcceptat(),
                assistent.getContrasenya(),
                assistent.getDni()
        );
    }

    public void deleteAssistentPersonal(String dni) {
        jdbcTemplate.update("DELETE FROM assistentpersonal WHERE dni = ?", dni);
    }
}