package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import es.uji.ei1027.proyecto.model.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private UsuariOVIDAO usuariOVIDAO;

    @Autowired
    private AssistentPersonalDao assistentPersonalDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuarioPost, BindingResult bindingResult, HttpSession session, Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        Usuario usuarioBD = usuarioDao.getUsuarioByEmail(usuarioPost.getEmail());
        if (usuarioBD == null) {
            model.addAttribute("error", "Email o contrasenya incorrectes.");
            return "login";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        try {
            if (!passwordEncryptor.checkPassword(usuarioPost.getContrasenya(), usuarioBD.getContrasenya())) {
                model.addAttribute("error", "Email o contrasenya incorrectes.");
                return "login";
            }
        } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException e) {
            model.addAttribute("error", "Aquest usuari antic no té la contrasenya encriptada. Esborra'l de la base de dades i torna a registrar-lo.");
            return "login";
        }

        if (usuarioBD.getTipusUsuari().equals("UsuariOVI")) {
            UsuariOVI ovi = usuariOVIDAO.getUsuariOVI(usuarioBD.getIdUsuario());
            if (ovi != null && !ovi.getEstatUsuari().equals("Acceptat")) {
                model.addAttribute("error", "El teu compte encara està pendent de validació per l'administrador o ha sigut rebutjat.");
                return "login";
            }
        } else if (usuarioBD.getTipusUsuari().equals("AssistentPersonal")) {
            AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(usuarioBD.getIdUsuario());
            if (assistent != null && !assistent.getEstatAcceptat().equals("Acceptat")) {
                model.addAttribute("error", "La teua sol·licitud d'Assistent Personal encara no ha sigut validada per l'administrador.");
                return "login";
            }
        }

        session.setAttribute("usuario", usuarioBD);

        String rol = usuarioBD.getTipusUsuari();
        if ("admin".equals(rol) || "Tecnic".equals(rol)) {
            return "index-admin";

        } else if ("UsuariOVI".equals(rol)) {
            return "index-ovi";

        } else if ("AssistentPersonal".equals(rol)) {
            return "index-assistent";
        }
        return "index-public";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}