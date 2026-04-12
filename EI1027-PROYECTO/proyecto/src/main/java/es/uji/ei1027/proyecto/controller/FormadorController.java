package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.FormadorDao;
import es.uji.ei1027.proyecto.model.Formador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value="/add") 
    public String addFormador(Model model) {
        model.addAttribute("formador", new Formador());
        return "formador/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST) 
    public String processAddSubmit(@ModelAttribute("formador") Formador formador, org.springframework.ui.Model model) { 
        if (formadorDao.getFormadorByEmail(formador.getEmailContacte()) != null) {
            model.addAttribute("errorEmail", "Aquest correu electrònic ja està registrat.");
            return "formador/add"; 
        }
        formadorDao.addFormador(formador); 
        return "redirect:/formador/list"; 
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editFormador(Model model, @PathVariable int id) {
        model.addAttribute("formador", formadorDao.getFormador(id));
        return "formador/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("formador") Formador formador, org.springframework.ui.Model model) {
        Formador existent = formadorDao.getFormadorByEmail(formador.getEmailContacte());
        
        if (existent != null && !existent.getIdFormador().equals(formador.getIdFormador())) {
            model.addAttribute("errorEmail", "Aquest correu electrònic ja el té un altre formador.");
            return "formador/update";
        }
    
        formadorDao.updateFormador(formador);
        return "redirect:/formador/list";
    }
    
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        formadorDao.deleteFormador(id);
        return "redirect:/formador/list";
    }
}