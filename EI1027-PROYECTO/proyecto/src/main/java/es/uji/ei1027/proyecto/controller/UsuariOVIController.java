package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/add")
    public String addUsuari(Model model) {
        model.addAttribute("usuari", new UsuariOVI());
        return "usuari/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("usuari") UsuariOVI usuari) {
        usuariOviDao.addUsuariOVI(usuari);
        return "redirect:/usuari/list";
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editUsuari(Model model, @PathVariable int id) {
        model.addAttribute("usuari", usuariOviDao.getUsuariOVI(id));
        return "usuari/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("usuari") UsuariOVI usuari) {
        usuariOviDao.updateUsuariOVI(usuari);
        return "redirect:/usuari/list";
    }

    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        usuariOviDao.deleteUsuariOVI(id);
        return "redirect:/usuari/list";
    }
}