package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsuariOVIDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class UsuariOVIRowMapper implements RowMapper<UsuariOVI> {
        public UsuariOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
            UsuariOVI usuari = new UsuariOVI();

            usuari.setIdUsuari(rs.getInt("id_usuari"));
            usuari.setDni(rs.getString("dni"));
            usuari.setNom(rs.getString("nom"));
            usuari.setCognom1(rs.getString("cognom1"));
            usuari.setCognom2(rs.getString("cognom2"));
            usuari.setTelefon(rs.getString("telefon"));
            usuari.setEmail(rs.getString("email"));
            usuari.setDireccio(rs.getString("direccio"));
            usuari.setContrasenyaInicial(rs.getString("contrasenya"));
            usuari.setConsentimentLOPD(rs.getBoolean("consentiment_LOPD"));
            usuari.setAcceptatPerTecnic(rs.getString("estat_usuari"));

            return usuari;
        }
    }

    public void addUsuariOVI(UsuariOVI usuari) {
        String sql = "INSERT INTO usuariovi (dni, nom, cognom1, cognom2, telefon, email, direccio, contrasenya, consentiment_LOPD, estat_usuari) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?::enum_estat_usuari)";
        jdbcTemplate.update(sql,
                usuari.getDni(),
                usuari.getNom(),
                usuari.getCognom1(),
                usuari.getCognom2(),
                usuari.getTelefon(),
                usuari.getEmail(),
                usuari.getDireccio(),
                usuari.getContrasenyaInicial(),
                usuari.getConsentimentLOPD(),
                usuari.getAcceptatPerTecnic());
    }

    public void updateUsuariOVI(UsuariOVI usuari) {
        String sql = "UPDATE usuariovi SET dni=?, nom=?, cognom1=?, cognom2=?, telefon=?, email=?, direccio=?, contrasenya=?, consentiment_LOPD=?, estat_usuari=?::enum_estat_usuari " +
                "WHERE id_usuari=?";
        jdbcTemplate.update(sql,
                usuari.getDni(),
                usuari.getNom(),
                usuari.getCognom1(),
                usuari.getCognom2(),
                usuari.getTelefon(),
                usuari.getEmail(),
                usuari.getDireccio(),
                usuari.getContrasenyaInicial(),
                usuari.getConsentimentLOPD(),
                usuari.getAcceptatPerTecnic(),
                usuari.getIdUsuari()); 
    }

    public void deleteUsuariOVI(Integer idUsuari) {
        String sql = "DELETE FROM usuariovi WHERE id_usuari=?";
        jdbcTemplate.update(sql, idUsuari);
    }

    public UsuariOVI getUsuariOVI(Integer idUsuari) {
        String sql = "SELECT * FROM usuariovi WHERE id_usuari=?";
        try {
            return jdbcTemplate.queryForObject(sql, new UsuariOVIRowMapper(), idUsuari);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<UsuariOVI> getUsuarisOVI() {
        String sql = "SELECT * FROM usuariovi";
        return jdbcTemplate.query(sql, new UsuariOVIRowMapper());
    }
}