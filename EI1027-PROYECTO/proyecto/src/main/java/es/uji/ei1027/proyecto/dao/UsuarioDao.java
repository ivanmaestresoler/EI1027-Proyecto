package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.Usuario;
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
public class UsuarioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class UsuarioRowMapper implements RowMapper<Usuario> {
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNom(rs.getString("nom"));
            usuario.setCognom1(rs.getString("cognom1"));
            usuario.setCognom2(rs.getString("cognom2"));
            usuario.setDni(rs.getString("dni"));
            usuario.setEmail(rs.getString("email"));
            usuario.setContrasenya(rs.getString("contrasenya"));
            usuario.setGenere(rs.getString("genere"));
            if (rs.getDate("data_naixement") != null) {
                usuario.setDataNaixement(rs.getDate("data_naixement").toLocalDate());
            }
            usuario.setTipusUsuari(rs.getString("tipus_usuari"));
            usuario.setTelefon(rs.getString("telefon"));
            usuario.setNombrePueblo(rs.getString("nombre_pueblo"));
            usuario.setDireccio(rs.getString("direccio"));
            return usuario;
        }
    }

    public List<Usuario> getUsuarios() {
        return jdbcTemplate.query("SELECT * FROM Usuario", new UsuarioRowMapper());
    }

    public Usuario getUsuario(int idUsuario) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Usuario WHERE id_usuario=?",
                    new UsuarioRowMapper(), idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Usuario getUsuarioByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Usuario WHERE email=?",
                    new UsuarioRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Usuario getUsuarioByDni(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Usuario WHERE dni=?",
                    new UsuarioRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateUsuario(Usuario usuario) {
        String sql = "UPDATE Usuario SET nom=?, cognom1=?, cognom2=?, dni=?, email=?, contrasenya=?, genere=?::enum_genere, data_naixement=?, tipus_usuari=?::enum_tipus_usuari, telefon=?, nombre_pueblo=?, direccio=? WHERE id_usuario=?";
        jdbcTemplate.update(sql,
                usuario.getNom(), usuario.getCognom1(), usuario.getCognom2(), usuario.getDni(),
                usuario.getEmail(), usuario.getContrasenya(), usuario.getGenere(),
                usuario.getDataNaixement(), usuario.getTipusUsuari(), usuario.getTelefon(),
                usuario.getNombrePueblo(), usuario.getDireccio(), usuario.getIdUsuario());
    }

    public void deleteUsuario(int idUsuario) {
        jdbcTemplate.update("DELETE FROM Usuario WHERE id_usuario=?", idUsuario);
    }
}