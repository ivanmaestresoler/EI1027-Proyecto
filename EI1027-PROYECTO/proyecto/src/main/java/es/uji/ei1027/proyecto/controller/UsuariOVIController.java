package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuari")
public class UsuariOVIController {

    private UsuariOVIDAO usuariOviDao;

    @Autowired
    public void setUsuariOviDao(UsuariOVIDAO usuariOviDao) {
        this.usuariOviDao = usuariOviDao;
    }

    @RequestMapping("/list")
    public String listUsuaris(Model model) {
        model.addAttribute("usuaris", usuariOviDao.getUsuarisOVI());

        return "usuari/list";
    }
}