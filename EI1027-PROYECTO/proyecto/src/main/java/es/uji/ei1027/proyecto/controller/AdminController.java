package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UsuariOVIDAO usuariOVIDAO;
    private AssistentPersonalDao assistentPersonalDao;

    @Autowired
    public void setUsuariOVIDAO(UsuariOVIDAO usuariOVIDAO) {
        this.usuariOVIDAO = usuariOVIDAO;
    }

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @GetMapping("/solicituds-ovi")
    public String llistarSolicitudsOVI(Model model) {
        model.addAttribute("usuarisPendents", usuariOVIDAO.getUsuarisPendents());
        return "admin/solicituds-ovi";
    }

    @GetMapping("/acceptar-ovi/{id}")
    public String acceptarOVI(@PathVariable int id) {
        usuariOVIDAO.actualitzarEstat(id, "Acceptat");
        return "redirect:/admin/solicituds-ovi";
    }

    @GetMapping("/rebutjar-ovi/{id}")
    public String rebutjarOVI(@PathVariable int id) {
        usuariOVIDAO.actualitzarEstat(id, "Rebutjat");
        return "redirect:/admin/solicituds-ovi";
    }

    @GetMapping("/solicituds-assistent")
    public String llistarSolicitudsAssistent(Model model) {
        model.addAttribute("assistentsPendents", assistentPersonalDao.getCandidats());
        return "admin/solicituds-assistent";
    }

    @GetMapping("/acceptar-assistent/{id}")
    public String acceptarAssistent(@PathVariable int id) {
        assistentPersonalDao.approveAssistent(id);
        return "redirect:/admin/solicituds-assistent";
    }

    @GetMapping("/rebutjar-assistent/{id}")
    public String rebutjarAssistent(@PathVariable int id) {
        assistentPersonalDao.rejectAssistent(id);
        return "redirect:/admin/solicituds-assistent";
    }
}