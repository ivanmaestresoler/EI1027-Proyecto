package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.Formador;
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
public class FormadorDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class FormadorRowMapper implements RowMapper<Formador> {
        public Formador mapRow(ResultSet rs, int rowNum) throws SQLException {
            Formador formador = new Formador();
            formador.setIdFormador(rs.getInt("id_formador"));
            formador.setNom(rs.getString("nom"));
            formador.setCognom1(rs.getString("cognom1"));
            formador.setCognom2(rs.getString("cognom2"));
            formador.setEmailContacte(rs.getString("email_contacte"));
            formador.setEspecialitat(rs.getString("especialitat"));
            return formador;
        }
    }

    public List<Formador> getFormadors() {
        return jdbcTemplate.query("SELECT * FROM Formador", new FormadorRowMapper());
    }

    public Formador getFormador(Integer idFormador) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Formador WHERE id_formador = ?",
                    new FormadorRowMapper(), idFormador);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addFormador(Formador formador) {
        jdbcTemplate.update(
                "INSERT INTO Formador (nom, cognom1, cognom2, email_contacte, especialitat) VALUES (?, ?, ?, ?, ?)",
                formador.getNom(), formador.getCognom1(), formador.getCognom2(), 
                formador.getEmailContacte(), formador.getEspecialitat()
        );
    }

    public void updateFormador(Formador formador) {
        jdbcTemplate.update(
                "UPDATE Formador SET nom=?, cognom1=?, cognom2=?, email_contacte=?, especialitat=? WHERE id_formador=?",
                formador.getNom(), formador.getCognom1(), formador.getCognom2(), 
                formador.getEmailContacte(), formador.getEspecialitat(), formador.getIdFormador()
        );
    }

    public void deleteFormador(Integer idFormador) {
        jdbcTemplate.update("DELETE FROM Formador WHERE id_formador = ?", idFormador);
    }
}