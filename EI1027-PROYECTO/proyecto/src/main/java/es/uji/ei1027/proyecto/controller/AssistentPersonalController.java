package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}