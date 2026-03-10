package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.FormadorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/formador")
public class FormadorController {

    private FormadorDao formadorDao;

    @Autowired
    public void setFormadorDao(FormadorDao formadorDao) {
        this.formadorDao = formadorDao;
    }

    @RequestMapping("/list")
    public String listFormadors(Model model) {
        model.addAttribute("formadors", formadorDao.getFormadors());
        return "formador/list";
    }
}