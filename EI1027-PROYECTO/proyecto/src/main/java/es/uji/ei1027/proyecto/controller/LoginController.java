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
        
        // 1. Validación manual del Email vacío o con formato incorrecto
        if (usuarioPost.getEmail() == null || usuarioPost.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Cal introduir un correu electrònic.");
            return "login";
        }
        
        // Expresión regular estándar para validar correos electrónicos
        if (!usuarioPost.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            model.addAttribute("error", "El format del correu electrònic no és vàlid (ex: nom@domini.com).");
            return "login";
        }

        // 2. Comprobación de la contraseña vacía
        if (usuarioPost.getContrasenya() == null || usuarioPost.getContrasenya().trim().isEmpty()) {
            model.addAttribute("error", "Cal introduir la contrasenya.");
            return "login";
        }

        // Buscar el usuario en la base de datos
        Usuario usuarioBD = usuarioDao.getUsuarioByEmail(usuarioPost.getEmail());

        if (usuarioBD == null) {
            model.addAttribute("error", "L'usuari o la contrasenya són incorrectes.");
            return "login";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (!passwordEncryptor.checkPassword(usuarioPost.getContrasenya(), usuarioBD.getContrasenya())) {
            model.addAttribute("error", "L'usuari o la contrasenya són incorrectes.");
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