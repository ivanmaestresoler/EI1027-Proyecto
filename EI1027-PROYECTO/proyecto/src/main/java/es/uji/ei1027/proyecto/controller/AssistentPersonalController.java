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

    @RequestMapping("/solicituds")
    public String listSolicituds(Model model) {
        model.addAttribute("candidats", assistentPersonalDao.getCandidats());
        return "assistentPersonal/solicituds";
    }

    @RequestMapping(value="/add")
    public String addAssistent(Model model) {
        model.addAttribute("assistent", new AssistentPersonal());
        return "assistentPersonal/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("assistent") AssistentPersonal assistent, BindingResult bindingResult) {
        AssistentPersonalValidator assistentValidator = new AssistentPersonalValidator();
        assistentValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) return "assistentPersonal/add";
        assistentPersonalDao.addAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editAssistent(Model model, @PathVariable int id) {
        model.addAttribute("assistent", assistentPersonalDao.getAssistentPersonal(id));
        return "assistentPersonal/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("assistent") AssistentPersonal assistent, BindingResult bindingResult) {
        AssistentPersonalValidator assistentValidator = new AssistentPersonalValidator();
        assistentValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) return "assistentPersonal/update";
        assistentPersonalDao.updateAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        assistentPersonalDao.deleteAssistentPersonal(id);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping("/approve/{id}")
    public String approve(@PathVariable int id) {
        assistentPersonalDao.approveAssistent(id);
        return "redirect:/assistentPersonal/solicituds";
    }

    @RequestMapping("/reject/{id}")
    public String reject(@PathVariable int id) {
        assistentPersonalDao.rejectAssistent(id);
        return "redirect:/assistentPersonal/solicituds";
    }
}