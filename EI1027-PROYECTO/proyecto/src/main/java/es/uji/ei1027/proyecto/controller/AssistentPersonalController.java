package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/assistentPersonal")
public class AssistentPersonalController {

    private AssistentPersonalDao assistentPersonalDao;

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @RequestMapping("/list")
    public String listAssistents(Model model) {
        model.addAttribute("assistents", assistentPersonalDao.getAssistentsPersonals());
        return "assistentPersonal/list";
    }

    @RequestMapping(value="/add")
    public String addAssistent(Model model) {
        model.addAttribute("assistent", new AssistentPersonal());
        return "assistentPersonal/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("assistent") AssistentPersonal assistent, BindingResult bindingResult) {
        // 1. Cridem al validador personalitzat
        AssistentPersonalValidator assistentValidator = new AssistentPersonalValidator();
        assistentValidator.validate(assistent, bindingResult);

        // 2. Comprovem que el DNI no existisca ja a la base de dades
        if (assistent.getDni() != null && !assistent.getDni().trim().isEmpty()) {
            if (assistentPersonalDao.getAssistentPersonal(assistent.getDni()) != null) {
                bindingResult.rejectValue("dni", "repetit", "Aquest DNI ja està registrat al sistema");
            }
        }

        // 3. Si hi ha errors, tornem al formulari
        if (bindingResult.hasErrors()) {
            return "assistentPersonal/add";
        }

        assistentPersonalDao.addAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/update/{dni}", method = RequestMethod.GET)
    public String editAssistent(Model model, @PathVariable String dni) {
        model.addAttribute("assistent", assistentPersonalDao.getAssistentPersonal(dni));
        return "assistentPersonal/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("assistent") AssistentPersonal assistent, BindingResult bindingResult) {
        // En l'update validem les dades igualment
        AssistentPersonalValidator assistentValidator = new AssistentPersonalValidator();
        assistentValidator.validate(assistent, bindingResult);

        if (bindingResult.hasErrors()) {
            return "assistentPersonal/update";
        }

        assistentPersonalDao.updateAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        assistentPersonalDao.deleteAssistentPersonal(dni);
        return "redirect:/assistentPersonal/list";
    }
}