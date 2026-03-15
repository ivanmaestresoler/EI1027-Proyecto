package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AssistentPersonalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
        jdbcTemplate.update(
                "INSERT INTO assistentpersonal (dni, nom, cognom1, email, tipus, estat_acceptat) VALUES (?, ?, ?, ?, ?, ?)",
                assistent.getDni(),
                assistent.getNom(),
                assistent.getCognom1(),
                assistent.getEmail(),
                assistent.getTipus(),
                assistent.getEstat_acceptat()
        );
    }

    public void updateAssistentPersonal(AssistentPersonal assistent) {
        jdbcTemplate.update(
                "UPDATE assistentpersonal SET nom=?, cognom1=?, email=?, tipus=?, estat_acceptat=? WHERE dni=?",
                assistent.getNom(),
                assistent.getCognom1(),
                assistent.getEmail(),
                assistent.getTipus(),
                assistent.getEstat_acceptat(),
                assistent.getDni() // El DNI va al final perquè és la condició del WHERE
        );
    }

    public void deleteAssistentPersonal(String dni) {
        jdbcTemplate.update("DELETE FROM assistentpersonal WHERE dni = ?", dni);
    }
}