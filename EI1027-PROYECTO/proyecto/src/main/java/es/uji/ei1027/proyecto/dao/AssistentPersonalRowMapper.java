package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class AssistentPersonalRowMapper implements RowMapper<AssistentPersonal> {
    public AssistentPersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssistentPersonal assistent = new AssistentPersonal();
        assistent.setDni(rs.getString("dni"));
        assistent.setNom(rs.getString("nom"));
        assistent.setCognom1(rs.getString("cognom1"));
        assistent.setEmail(rs.getString("email"));
        assistent.setTipus(rs.getString("tipus"));
        assistent.setEstat_acceptat(rs.getString("estat_acceptat"));
        return assistent;
    }
}
