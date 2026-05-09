package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class AssistentPersonalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class AssistentPersonalRowMapper implements RowMapper<AssistentPersonal> {
        public AssistentPersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssistentPersonal assistent = new AssistentPersonal();
            
            assistent.setIdUsuario(rs.getInt("id_usuario"));
            assistent.setNom(rs.getString("nom"));
            assistent.setCognom1(rs.getString("cognom1"));
            assistent.setCognom2(rs.getString("cognom2"));
            assistent.setDni(rs.getString("dni"));
            assistent.setEmail(rs.getString("email"));
            assistent.setContrasenya(rs.getString("contrasenya"));
            assistent.setGenere(rs.getString("genere"));
            
            if (rs.getDate("data_naixement") != null) {
                assistent.setDataNaixement(rs.getDate("data_naixement").toLocalDate());
            }
            
            assistent.setTipusUsuari(rs.getString("tipus_usuari"));
            assistent.setTelefon(rs.getString("telefon"));
            assistent.setNombrePueblo(rs.getString("nombre_pueblo"));
            assistent.setDireccio(rs.getString("direccio"));

            assistent.setFormacioAcademica(rs.getString("formacio_academica"));
            assistent.setTipus(rs.getString("tipus"));
            assistent.setEstatAcceptat(rs.getString("estat_acceptat"));

            return assistent;
        }
    }

    public void addAssistentPersonal(AssistentPersonal assistent) {
        String sqlUsuario = "INSERT INTO Usuario (nom, cognom1, cognom2, dni, email, contrasenya, genere, data_naixement, tipus_usuari, telefon, nombre_pueblo, direccio) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?::enum_genere, ?, 'AssistentPersonal'::enum_tipus_usuari, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, assistent.getNom());
            ps.setString(2, assistent.getCognom1());
            ps.setString(3, assistent.getCognom2());
            ps.setString(4, assistent.getDni());
            ps.setString(5, assistent.getEmail());
            ps.setString(6, assistent.getContrasenya());
            ps.setString(7, assistent.getGenere());
            ps.setObject(8, assistent.getDataNaixement());
            ps.setString(9, assistent.getTelefon());
            ps.setString(10, assistent.getNombrePueblo());
            ps.setString(11, assistent.getDireccio());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            int idGenerado = keyHolder.getKey().intValue();
            assistent.setIdUsuario(idGenerado);
            
            String sqlAssistent = "INSERT INTO AssistentPersonal (id_assistent, formacio_academica, tipus, estat_acceptat) " +
                                  "VALUES (?, ?, ?::enum_tipus_assistent, ?::enum_estat_assistent)";
            
            jdbcTemplate.update(sqlAssistent, idGenerado, assistent.getFormacioAcademica(), assistent.getTipus(), assistent.getEstatAcceptat());
        }
    }

    public void updateAssistentPersonal(AssistentPersonal assistent) {
        String sqlUsuario = "UPDATE Usuario SET nom=?, cognom1=?, cognom2=?, dni=?, email=?, contrasenya=?, genere=?::enum_genere, data_naixement=?, telefon=?, nombre_pueblo=?, direccio=? WHERE id_usuario=?";
        
        jdbcTemplate.update(sqlUsuario,
                assistent.getNom(), assistent.getCognom1(), assistent.getCognom2(), assistent.getDni(),
                assistent.getEmail(), assistent.getContrasenya(), assistent.getGenere(),
                assistent.getDataNaixement(), assistent.getTelefon(), assistent.getNombrePueblo(),
                assistent.getDireccio(), assistent.getIdUsuario());

        String sqlAssistent = "UPDATE AssistentPersonal SET formacio_academica=?, tipus=?::enum_tipus_assistent, estat_acceptat=?::enum_estat_assistent WHERE id_assistent=?";
        
        jdbcTemplate.update(sqlAssistent,
                assistent.getFormacioAcademica(), assistent.getTipus(), assistent.getEstatAcceptat(), assistent.getIdUsuario());
    }

    public void deleteAssistentPersonal(int idUsuario) {
        String sql = "DELETE FROM Usuario WHERE id_usuario=?";
        jdbcTemplate.update(sql, idUsuario);
    }

    public AssistentPersonal getAssistentPersonal(int idUsuario) {
        String sql = "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE u.id_usuario=?";
        try {
            return jdbcTemplate.queryForObject(sql, new AssistentPersonalRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AssistentPersonal> getAssistentsPersonals() {
        String sql = "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent";
        return jdbcTemplate.query(sql, new AssistentPersonalRowMapper());
    }

    public void approveAssistent(int idAssistent) {
        jdbcTemplate.update("UPDATE AssistentPersonal SET estat_acceptat = 'Acceptat'::enum_estat_assistent WHERE id_assistent = ?", idAssistent);
    }

    public void rejectAssistent(int idAssistent) {
        jdbcTemplate.update("UPDATE AssistentPersonal SET estat_acceptat = 'Rebutjat'::enum_estat_assistent WHERE id_assistent = ?", idAssistent);
    }

    public List<AssistentPersonal> getCandidats() {
        String sql = "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE a.estat_acceptat = 'Candidat'";
        return jdbcTemplate.query(sql, new AssistentPersonalRowMapper());
    }
}