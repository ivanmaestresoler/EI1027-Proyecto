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
            assistent.setTipusUsuari(rs.getString("tipus_usuari"));
            assistent.setContrasenya(rs.getString("contrasenya"));
            
            assistent.setDataNaixement(rs.getDate("data_naixement") != null ? rs.getDate("data_naixement").toLocalDate() : null);
            assistent.setGenere(rs.getString("genere"));
            assistent.setFormacioAcademica(rs.getString("formacio_academica"));
            assistent.setTipus(rs.getString("tipus"));
            assistent.setEstatAcceptat(rs.getString("estat_acceptat"));
            assistent.setDireccio(rs.getString("direccio"));
            assistent.setTelefon(rs.getString("telefon"));
            return assistent;
        }
    }

    public void addAssistentPersonal(AssistentPersonal assistent) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Usuario (nom, cognom1, cognom2, dni, email, tipus_usuari, contrasenya) VALUES (?, ?, ?, ?, ?, 'AssistentPersonal'::enum_tipus_usuari, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, assistent.getNom());
            ps.setString(2, assistent.getCognom1());
            ps.setString(3, assistent.getCognom2());
            ps.setString(4, assistent.getDni());
            ps.setString(5, assistent.getEmail());
            ps.setString(6, assistent.getContrasenya());
            return ps;
        }, keyHolder);

        int idUsuario = (int) keyHolder.getKeys().get("id_usuario");

        jdbcTemplate.update(
                "INSERT INTO AssistentPersonal (id_assistent, data_naixement, genere, formacio_academica, tipus, estat_acceptat, direccio, telefon) VALUES (?, ?, ?::enum_genere, ?::enum_formacio, ?::enum_tipus_assistent, 'Candidat'::enum_estat_assistent, ?, ?)",
                idUsuario, assistent.getDataNaixement(), assistent.getGenere(), assistent.getFormacioAcademica(), assistent.getTipus(), assistent.getDireccio(), assistent.getTelefon()
        );

        if (assistent.getTipusAssistenciaSeleccionats() != null) {
            for (String tipus : assistent.getTipusAssistenciaSeleccionats()) {
                jdbcTemplate.update("INSERT INTO TipusAssistenciaAssistent (id_assistent, tipus_assistencia) VALUES (?, ?::enum_tipus_assistencia)", idUsuario, tipus);
            }
        }
    }

    public void deleteAssistentPersonal(int idUsuario) {
        jdbcTemplate.update("DELETE FROM Usuario WHERE id_usuario = ?", idUsuario);
    }

    public void updateAssistentPersonal(AssistentPersonal assistent) {
        jdbcTemplate.update(
                "UPDATE Usuario SET nom=?, cognom1=?, cognom2=?, dni=?, email=?, contrasenya=? WHERE id_usuario=?",
                assistent.getNom(), assistent.getCognom1(), assistent.getCognom2(), assistent.getDni(), assistent.getEmail(), assistent.getContrasenya(), assistent.getIdUsuario()
        );

        jdbcTemplate.update(
                "UPDATE AssistentPersonal SET data_naixement=?, genere=?::enum_genere, formacio_academica=?::enum_formacio, tipus=?::enum_tipus_assistent, estat_acceptat=?::enum_estat_assistent, direccio=?, telefon=? WHERE id_assistent=?",
                assistent.getDataNaixement(), assistent.getGenere(), assistent.getFormacioAcademica(), assistent.getTipus(), assistent.getEstatAcceptat(), assistent.getDireccio(), assistent.getTelefon(), assistent.getIdUsuario()
        );

        jdbcTemplate.update("DELETE FROM TipusAssistenciaAssistent WHERE id_assistent = ?", assistent.getIdUsuario());

        if (assistent.getTipusAssistenciaSeleccionats() != null) {
            for (String tipus : assistent.getTipusAssistenciaSeleccionats()) {
                jdbcTemplate.update("INSERT INTO TipusAssistenciaAssistent (id_assistent, tipus_assistencia) VALUES (?, ?::enum_tipus_assistencia)", assistent.getIdUsuario(), tipus);
            }
        }
    }

    public AssistentPersonal getAssistentPersonal(int idUsuario) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE u.id_usuario = ?",
                    new AssistentPersonalRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<AssistentPersonal> getAssistentsPersonals() {
        return jdbcTemplate.query(
                "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent",
                new AssistentPersonalRowMapper());
    }

    public int getTotalAssistentsPersonals() {
        String sql = "SELECT COUNT(*) FROM AssistentPersonal";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public List<AssistentPersonal> getAssistentsPersonalsPaginats(int limit, int offset) {
        String sql = "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new AssistentPersonalRowMapper(), limit, offset);
    }

    public List<AssistentPersonal> getCandidats() {
        return jdbcTemplate.query("SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE a.estat_acceptat = 'Candidat'::enum_estat_assistent", new AssistentPersonalRowMapper());
    }

    public List<AssistentPersonal> getAssistentsAcceptats() {
        return jdbcTemplate.query("SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE a.estat_acceptat = 'Acceptat'::enum_estat_assistent", new AssistentPersonalRowMapper());
    }

    public int getTotalCandidats() {
        String sql = "SELECT COUNT(*) FROM AssistentPersonal WHERE estat_acceptat = 'Candidat'::enum_estat_assistent";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public List<AssistentPersonal> getCandidatsPaginats(int limit, int offset) {
        String sql = "SELECT * FROM Usuario u JOIN AssistentPersonal a ON u.id_usuario = a.id_assistent WHERE a.estat_acceptat = 'Candidat'::enum_estat_assistent LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new AssistentPersonalRowMapper(), limit, offset);
    }

    public List<String> getTipusAssistenciaPerAssistent(int idAssistent) {
        String sql = "SELECT tipus_assistencia FROM TipusAssistenciaAssistent WHERE id_assistent = ?";
        return jdbcTemplate.queryForList(sql, String.class, idAssistent);
    }
    
    public void approveAssistent(int idUsuario) {
        jdbcTemplate.update(
            "UPDATE AssistentPersonal SET estat_acceptat = 'Acceptat'::enum_estat_assistent WHERE id_assistent = ?",
            idUsuario
        );
    }

    public void rejectAssistent(int idUsuario) {
        jdbcTemplate.update(
            "UPDATE AssistentPersonal SET estat_acceptat = 'Rebutjat'::enum_estat_assistent WHERE id_assistent = ?",
            idUsuario
        );
    }
}