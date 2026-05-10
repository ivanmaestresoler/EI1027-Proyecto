package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.Pueblo;
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
public class PuebloDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class PuebloRowMapper implements RowMapper<Pueblo> {
        public Pueblo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pueblo pueblo = new Pueblo();
            pueblo.setNombre(rs.getString("nombre"));
            pueblo.setCodpos(rs.getString("codpos"));
            return pueblo;
        }
    }

    public List<Pueblo> getPueblos() {
        // Añadida la 's' a Pueblos
        return jdbcTemplate.query("SELECT * FROM Pueblos", new PuebloRowMapper());
    }

    public Pueblo getPueblo(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Pueblos WHERE nombre = ?",
                    new PuebloRowMapper(), nombre);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addPueblo(Pueblo pueblo) {
        jdbcTemplate.update(
                "INSERT INTO Pueblos (nombre, codpos) VALUES (?, ?)",
                pueblo.getNombre(), pueblo.getCodpos()
        );
    }

    public void updatePueblo(Pueblo pueblo) {
        jdbcTemplate.update(
                "UPDATE Pueblos SET codpos=? WHERE nombre=?",
                pueblo.getCodpos(), pueblo.getNombre()
        );
    }

    public void deletePueblo(String nombre) {
        jdbcTemplate.update("DELETE FROM Pueblos WHERE nombre = ?", nombre);
    }
}