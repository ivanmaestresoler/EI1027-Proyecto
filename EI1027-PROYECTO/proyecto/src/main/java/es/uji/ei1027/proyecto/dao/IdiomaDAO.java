package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.Idioma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class IdiomaDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class IdiomaRowMapper implements RowMapper<Idioma> {
        public Idioma mapRow(ResultSet rs, int rowNum) throws SQLException {
            Idioma idioma = new Idioma();
            idioma.setIdIdioma(rs.getInt("id_idioma"));
            idioma.setNombre(rs.getString("nombre"));
            return idioma;
        }
    }

    public List<Idioma> getIdiomas() {
        return jdbcTemplate.query("SELECT * FROM Idiomas", new IdiomaRowMapper());
    }
}