package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.ComunicacioUsuariOVIPAPDao;
import es.uji.ei1027.proyecto.model.ComunicacioUsuariOVIPAP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comunicacio")
public class ComunicacioController {

    private ComunicacioUsuariOVIPAPDao comunicacioDao;

    @Autowired
    public void setComunicacioDao(ComunicacioUsuariOVIPAPDao comunicacioDao) {
        this.comunicacioDao = comunicacioDao;
    }

    @GetMapping("/list")
    public String listComunicacions(Model model) {
        model.addAttribute("comunicacions", comunicacioDao.getComunicacions());
        return "comunicacio/list";
    }

    @GetMapping("/add")
    public String addComunicacio(Model model) {
        model.addAttribute("comunicacio", new ComunicacioUsuariOVIPAP());
        return "comunicacio/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(@ModelAttribute("comunicacio") ComunicacioUsuariOVIPAP comunicacio) {
        comunicacioDao.addComunicacio(comunicacio);
        return "redirect:list";
    }

    @GetMapping("/update/{id}")
    public String updateComunicacio(Model model, @PathVariable int id) {
        model.addAttribute("comunicacio", comunicacioDao.getComunicacio(id));
        return "comunicacio/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("comunicacio") ComunicacioUsuariOVIPAP comunicacio) {
        comunicacioDao.updateComunicacio(comunicacio);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        comunicacioDao.deleteComunicacio(id);
        return "redirect:../list";
    }
}