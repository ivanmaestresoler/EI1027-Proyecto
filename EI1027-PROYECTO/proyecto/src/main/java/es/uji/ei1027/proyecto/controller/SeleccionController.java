package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.SeleccionDao;
import es.uji.ei1027.proyecto.model.Seleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seleccion")
public class SeleccionController {

    private SeleccionDao seleccionDao;

    @Autowired
    public void setSeleccionDao(SeleccionDao seleccionDao) {
        this.seleccionDao = seleccionDao;
    }

    @GetMapping("/list")
    public String listSeleccions(Model model) {
        model.addAttribute("seleccions", seleccionDao.getSeleccions());
        return "seleccion/list";
    }

    @GetMapping("/add")
    public String addSeleccion(Model model) {
        model.addAttribute("seleccion", new Seleccion());
        return "seleccion/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(@ModelAttribute("seleccion") Seleccion seleccion) {
        seleccionDao.addSeleccion(seleccion);
        return "redirect:list";
    }

    @GetMapping("/update/{id}")
    public String updateSeleccion(Model model, @PathVariable int id) {
        model.addAttribute("seleccion", seleccionDao.getSeleccion(id));
        return "seleccion/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("seleccion") Seleccion seleccion) {
        seleccionDao.updateSeleccion(seleccion);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        seleccionDao.deleteSeleccion(id);
        return "redirect:../list";
    }
}
