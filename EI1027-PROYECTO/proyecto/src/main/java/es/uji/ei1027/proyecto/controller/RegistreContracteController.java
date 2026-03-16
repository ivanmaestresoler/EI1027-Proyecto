package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.RegistreContracteDao;
import es.uji.ei1027.proyecto.model.RegistreContracte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registreContracte")
public class RegistreContracteController {

    private RegistreContracteDao registreContracteDao;

    @Autowired
    public void setRegistreContracteDao(RegistreContracteDao registreContracteDao) {
        this.registreContracteDao = registreContracteDao;
    }

    @GetMapping("/list")
    public String listContractes(Model model) {
        model.addAttribute("contractes", registreContracteDao.getContractes());
        return "registreContracte/list";
    }

    @GetMapping("/add")
    public String addContracte(Model model) {
        model.addAttribute("registreContracte", new RegistreContracte());
        return "registreContracte/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(@ModelAttribute("registreContracte") RegistreContracte registreContracte) {
        registreContracteDao.addContracte(registreContracte);
        return "redirect:list";
    }

    @GetMapping("/update/{id}")
    public String updateContracte(Model model, @PathVariable int id) {
        model.addAttribute("registreContracte", registreContracteDao.getContracte(id));
        return "registreContracte/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("registreContracte") RegistreContracte registreContracte) {
        registreContracteDao.updateContracte(registreContracte);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        registreContracteDao.deleteContracte(id);
        return "redirect:../list";
    }
}