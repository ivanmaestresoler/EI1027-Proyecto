package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
@Controller
public class LoginController {

    @Autowired
    private UsuarioDao usuarioDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuarioPost, HttpSession session, Model model) {
        Usuario usuarioBD = usuarioDao.getUsuarioByEmail(usuarioPost.getEmail());
        
        if (usuarioBD == null || !usuarioBD.getContrasenya().equals(usuarioPost.getContrasenya())) {
            model.addAttribute("error", "Email o contrasenya incorrectes");
            return "login";
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