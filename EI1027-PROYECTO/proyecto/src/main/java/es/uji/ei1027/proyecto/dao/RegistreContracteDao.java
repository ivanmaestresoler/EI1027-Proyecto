package es.uji.ei1027.proyecto.dao;

import es.uji.ei1027.proyecto.model.RegistreContracte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RegistreContracteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class RegistreContracteRowMapper implements RowMapper<RegistreContracte> {
        public RegistreContracte mapRow(ResultSet rs, int rowNum) throws SQLException {
            RegistreContracte contracte = new RegistreContracte();
            contracte.setIdContracte(rs.getInt("id_contracte"));
            contracte.setIdRequest(rs.getInt("id_request"));
            contracte.setIdAssistent(rs.getInt("id_assistent"));

            if (rs.getDate("data_inici") != null) {
                contracte.setDataInici(rs.getDate("data_inici").toLocalDate());
            }
            if (rs.getDate("data_fi") != null) {
                contracte.setDataFi(rs.getDate("data_fi").toLocalDate());
            }

            contracte.setRutaPdf(rs.getString("ruta_pdf"));

            return contracte;
        }
    }

    public void addContracte(RegistreContracte contracte) {
        String sql = "INSERT INTO registrecontracte (id_request, id_assistent, data_inici, data_fi, ruta_pdf) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contracte.getIdRequest(), contracte.getIdAssistent(), contracte.getDataInici(), contracte.getDataFi(), contracte.getRutaPdf());
    }

    public void updateContracte(RegistreContracte contracte) {
        String sql = "UPDATE registrecontracte SET id_request=?, id_assistent=?, data_inici=?, data_fi=?, ruta_pdf=? WHERE id_contracte=?";
        jdbcTemplate.update(sql, contracte.getIdRequest(), contracte.getIdAssistent(), contracte.getDataInici(), contracte.getDataFi(), contracte.getRutaPdf(), contracte.getIdContracte());
    }

    public void deleteContracte(Integer idContracte) {
        String sql = "DELETE FROM registrecontracte WHERE id_contracte=?";
        jdbcTemplate.update(sql, idContracte);
    }

    public RegistreContracte getContracte(Integer idContracte) {
        String sql = "SELECT * FROM registrecontracte WHERE id_contracte=?";
        try {
            return jdbcTemplate.queryForObject(sql, new RegistreContracteRowMapper(), idContracte);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<RegistreContracte> getContractesByUsuari(int idUsuari) {
        String sql = "SELECT rc.* FROM registrecontracte rc " +
                "JOIN aprequest ar ON rc.id_request = ar.id_request " +
                "WHERE ar.id_usuari = ?";
        return jdbcTemplate.query(sql, new RegistreContracteRowMapper(), idUsuari);
    }

    public List<RegistreContracte> getContractes() {
        String sql = "SELECT * FROM registrecontracte";
        return jdbcTemplate.query(sql, new RegistreContracteRowMapper());
    }
    public List<RegistreContracte> getContractesByAssistent(int idAssistent) {
        String sql = "SELECT * FROM registrecontracte WHERE id_assistent=?";
        return jdbcTemplate.query(sql, new RegistreContracteRowMapper(), idAssistent);
    }
    // Para Admins (Todos los contratos)
    public int getTotalContractes() {
        try { return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM registrecontracte", Integer.class); }
        catch (EmptyResultDataAccessException e) { return 0; }
    }
    public List<RegistreContracte> getContractesPaginats(int limit, int offset) {
        return jdbcTemplate.query("SELECT * FROM registrecontracte LIMIT ? OFFSET ?", new RegistreContracteRowMapper(), limit, offset);
    }

    // Para Usuarios OVI
    public int getTotalContractesByUsuari(int idUsuari) {
        try { return jdbcTemplate.queryForObject("SELECT COUNT(rc.*) FROM registrecontracte rc JOIN aprequest ar ON rc.id_request = ar.id_request WHERE ar.id_usuari = ?", Integer.class, idUsuari); }
        catch (EmptyResultDataAccessException e) { return 0; }
    }
    public List<RegistreContracte> getContractesByUsuariPaginats(int idUsuari, int limit, int offset) {
        return jdbcTemplate.query("SELECT rc.* FROM registrecontracte rc JOIN aprequest ar ON rc.id_request = ar.id_request WHERE ar.id_usuari = ? LIMIT ? OFFSET ?", new RegistreContracteRowMapper(), idUsuari, limit, offset);
    }

    // Para Asistentes
    public int getTotalContractesByAssistent(int idAssistent) {
        try { return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM registrecontracte WHERE id_assistent=?", Integer.class, idAssistent); }
        catch (EmptyResultDataAccessException e) { return 0; }
    }
    public List<RegistreContracte> getContractesByAssistentPaginats(int idAssistent, int limit, int offset) {
        return jdbcTemplate.query("SELECT * FROM registrecontracte WHERE id_assistent=? LIMIT ? OFFSET ?", new RegistreContracteRowMapper(), idAssistent, limit, offset);
    }
}