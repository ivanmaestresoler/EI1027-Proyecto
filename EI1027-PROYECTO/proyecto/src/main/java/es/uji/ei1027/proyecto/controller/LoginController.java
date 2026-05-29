package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.model.Usuario;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private UsuarioDao usuarioDao;
    private UsuariOVIDAO usuariOVIDAO;
    private AssistentPersonalDao assistentPersonalDao;

    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Autowired
    public void setUsuariOVIDAO(UsuariOVIDAO usuariOVIDAO) {
        this.usuariOVIDAO = usuariOVIDAO;
    }

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuarioPost, HttpSession session, Model model) {
        if (usuarioPost.getEmail() == null || usuarioPost.getEmail().trim().isEmpty() ||
            usuarioPost.getContrasenya() == null || usuarioPost.getContrasenya().trim().isEmpty()) {
            model.addAttribute("error", "Per favor, introduïsca l'email i la contrasenya.");
            return "login";
        }

        if (!usuarioPost.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "El format de l'email no és vàlid.");
            return "login";
        }

        Usuario usuarioBD = usuarioDao.getUsuarioByEmail(usuarioPost.getEmail());
        
        if (usuarioBD == null || !usuarioBD.getContrasenya().equals(usuarioPost.getContrasenya())) {
            model.addAttribute("error", "Email o contrasenya incorrectes");
            return "login";
        }

        if (usuarioBD.getTipusUsuari().equals("UsuariOVI")) {
            UsuariOVI ovi = usuariOVIDAO.getUsuariOVI(usuarioBD.getIdUsuario());
            if (ovi != null && !ovi.getEstatUsuari().equals("Acceptat")) {
                model.addAttribute("error", "La teua conta encara no ha sigut acceptada per l'administrador.");
                return "login";
            }
        } else if (usuarioBD.getTipusUsuari().equals("AssistentPersonal")) {
            AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(usuarioBD.getIdUsuario());
            if (assistent != null && !assistent.getEstatAcceptat().equals("Acceptat")) {
                model.addAttribute("error", "La teua sol·licitud d'assistent encara està pendent de revisió.");
                return "login";
            }
        }

        session.setAttribute("usuario", usuarioBD);
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}