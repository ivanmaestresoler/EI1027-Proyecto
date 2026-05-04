package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.UsuariOVI;
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
public class UsuariOVIDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* ---------------------------------------------------------
     * ROW MAPPER: Mapea el JOIN de la BBDD a tu clase Java
     * --------------------------------------------------------- */
    private static final class UsuariOVIMapper implements RowMapper<UsuariOVI> {
        public UsuariOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
            UsuariOVI usuari = new UsuariOVI();
            // Campos heredados de Usuario
            usuari.setIdUsuario(rs.getInt("id_usuario"));
            usuari.setNom(rs.getString("nom"));
            usuari.setCognom1(rs.getString("cognom1"));
            usuari.setCognom2(rs.getString("cognom2"));
            usuari.setDni(rs.getString("dni"));
            usuari.setEmail(rs.getString("email"));
            usuari.setContrasenya(rs.getString("contrasenya"));
            usuari.setGenere(rs.getString("genere"));
            usuari.setDataNaixement(rs.getDate("data_naixement") != null ? rs.getDate("data_naixement").toLocalDate() : null);
            usuari.setTipusUsuari(rs.getString("tipus_usuari"));
            usuari.setTelefon(rs.getString("telefon"));
            usuari.setNombrePueblo(rs.getString("nombre_pueblo"));
            usuari.setDireccio(rs.getString("direccio"));

            // Campos específicos de UsuariOVI
            usuari.setPlaVida(rs.getString("pla_vida"));
            usuari.setTipusAssistencia(rs.getString("tipus_assistencia"));
            usuari.setConsentimentLOPD(rs.getBoolean("consentiment_LOPD"));
            usuari.setEstatUsuari(rs.getString("estat_usuari"));

            return usuari;
        }
    }

    /* ---------------------------------------------------------
     * ADD: Inserción doble usando KeyHolder
     * --------------------------------------------------------- */
    public void addUsuariOVI(UsuariOVI usuari) {
        // 1. Insertamos en Usuario y pedimos que nos devuelva las claves generadas
        String sqlUsuario = "INSERT INTO Usuario (nom, cognom1, cognom2, dni, email, contrasenya, genere, data_naixement, tipus_usuari, telefon, nombre_pueblo, direccio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?::enum_genere, ?, 'UsuariOVI'::enum_tipus_usuari, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuari.getNom());
            ps.setString(2, usuari.getCognom1());
            ps.setString(3, usuari.getCognom2());
            ps.setString(4, usuari.getDni());
            ps.setString(5, usuari.getEmail());
            ps.setString(6, usuari.getContrasenya());
            ps.setString(7, usuari.getGenere());
            ps.setObject(8, usuari.getDataNaixement());
            ps.setString(9, usuari.getTelefon());
            ps.setString(10, usuari.getNombrePueblo());
            ps.setString(11, usuari.getDireccio());
            return ps;
        }, keyHolder);

        // Obtenemos el ID generado por el tipo SERIAL
        int idGenerado = (int) keyHolder.getKeys().get("id_usuario");

        // 2. Usamos el ID generado para insertar en la tabla hija
        String sqlUsuariOVI = "INSERT INTO UsuariOVI (id_usuari, pla_vida, tipus_assistencia, consentiment_LOPD, estat_usuari) " +
                "VALUES (?, ?, ?::enum_tipus_assistencia, ?, 'Pendent'::enum_estat_usuari)";

        jdbcTemplate.update(sqlUsuariOVI,
                idGenerado,
                usuari.getPlaVida(),
                usuari.getTipusAssistencia(),
                usuari.getConsentimentLOPD()
        );
    }

    /* ---------------------------------------------------------
     * GET Y LIST: Consultas con JOIN
     * --------------------------------------------------------- */
    public List<UsuariOVI> getUsuariosOVI() {
        String sql = "SELECT * FROM Usuario u INNER JOIN UsuariOVI o ON u.id_usuario = o.id_usuari";
        return jdbcTemplate.query(sql, new UsuariOVIMapper());
    }

    public UsuariOVI getUsuariOVI(int idUsuari) {
        try {
            String sql = "SELECT * FROM Usuario u INNER JOIN UsuariOVI o ON u.id_usuario = o.id_usuari WHERE o.id_usuari = ?";
            return jdbcTemplate.queryForObject(sql, new UsuariOVIMapper(), idUsuari);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* ---------------------------------------------------------
     * UPDATE: Actualización doble (Padre e Hijo)
     * --------------------------------------------------------- */
    public void updateUsuariOVI(UsuariOVI usuari) {
        // 1. Actualizamos los datos personales en la tabla padre (Usuario)
        String sqlUsuario = "UPDATE Usuario SET nom=?, cognom1=?, cognom2=?, dni=?, email=?, contrasenya=?, genere=?::enum_genere, data_naixement=?, telefon=?, nombre_pueblo=?, direccio=? WHERE id_usuario=?";

        jdbcTemplate.update(sqlUsuario,
                usuari.getNom(), usuari.getCognom1(), usuari.getCognom2(), usuari.getDni(),
                usuari.getEmail(), usuari.getContrasenya(), usuari.getGenere(),
                usuari.getDataNaixement(), usuari.getTelefon(), usuari.getNombrePueblo(),
                usuari.getDireccio(), usuari.getIdUsuario());

        // 2. Actualizamos los datos específicos en la tabla hija (UsuariOVI)
        String sqlUsuariOVI = "UPDATE UsuariOVI SET pla_vida=?, tipus_assistencia=?::enum_tipus_assistencia, consentiment_LOPD=?, estat_usuari=?::enum_estat_usuari WHERE id_usuari=?";

        jdbcTemplate.update(sqlUsuariOVI,
                usuari.getPlaVida(), usuari.getTipusAssistencia(),
                usuari.getConsentimentLOPD(), usuari.getEstatUsuari(),
                usuari.getIdUsuario());
    }

    /* ---------------------------------------------------------
     * DELETE: Borrado en cascada
     * --------------------------------------------------------- */
    public void deleteUsuariOVI(int idUsuario) {
        // Gracias al ON DELETE CASCADE de la FK en PostgreSQL,
        // al borrar el registro de Usuario se borra automáticamente el registro en UsuariOVI.
        String sql = "DELETE FROM Usuario WHERE id_usuario=?";
        jdbcTemplate.update(sql, idUsuario);
    }
}