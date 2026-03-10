package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class AssistentPersonalDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AssistentPersonal> getAssistentsPersonals() {
        return jdbcTemplate.query("SELECT * FROM assistentpersonal", new AssistentPersonalRowMapper());
    }
}