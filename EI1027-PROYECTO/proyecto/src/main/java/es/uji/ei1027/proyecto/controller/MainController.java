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

        // 1. Si no hay usuario logueado, mostramos el inicio público
        if (usuario == null) {
            return "index-public";
        }

        // 2. Si es un Usuario OVI
        if ("UsuariOVI".equals(usuario.getTipusUsuari())) {
            return "index-ovi";
        }
        // 3. Si es un Asistente Personal (PAP/PATI)
        else if ("AssistentPersonal".equals(usuario.getTipusUsuari())) {
            return "index-assistent";
        }
        // 4. Si es un Administrador / Técnico OVI
        else if ("admin".equals(usuario.getTipusUsuari()) || "Tecnic".equals(usuario.getTipusUsuari())) {
            return "index-admin";
        }

        // Por defecto, si hay algún error de rol, lo mandamos al inicio público
        return "index-public";
    }
}