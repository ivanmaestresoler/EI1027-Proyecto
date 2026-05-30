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
    public String listSeleccions(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        int totalRecords = seleccionDao.getTotalSeleccions();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("seleccions", seleccionDao.getSeleccionsPaginades(pageSize, offset));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

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
