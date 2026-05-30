package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "index-public";
        if ("UsuariOVI".equals(usuario.getTipusUsuari())) return "index-ovi";
        else if ("AssistentPersonal".equals(usuario.getTipusUsuari())) return "index-assistent";
        else if ("admin".equals(usuario.getTipusUsuari()) || "Tecnic".equals(usuario.getTipusUsuari())) return "index-admin";
        return "index-public";
    }

    @RequestMapping("/registre-completat")
    public String registreCompletat() {
        return "registre-completat";
    }
}