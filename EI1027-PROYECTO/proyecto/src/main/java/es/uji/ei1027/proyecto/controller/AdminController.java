package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import es.uji.ei1027.proyecto.model.APRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UsuariOVIDAO usuariOVIDAO;
    private AssistentPersonalDao assistentPersonalDao;
    private APRequestDAO apRequestDao;

    @Autowired
    public void setUsuariOVIDAO(UsuariOVIDAO usuariOVIDAO) {
        this.usuariOVIDAO = usuariOVIDAO;
    }

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) {
        this.apRequestDao = apRequestDao;
    }

    @GetMapping("/solicituds-ovi")
    public String llistarSolicitudsOVI(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5; 
        int offset = (page - 1) * pageSize;
        int totalRecords = usuariOVIDAO.getTotalUsuarisPendents();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("usuarisPendents", usuariOVIDAO.getUsuarisPendentsPaginats(pageSize, offset));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
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
    public String llistarSolicitudsAssistent(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5; 
        int offset = (page - 1) * pageSize;
        int totalRecords = assistentPersonalDao.getTotalCandidats();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("assistentsPendents", assistentPersonalDao.getCandidatsPaginats(pageSize, offset));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
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

    @GetMapping("/peticions-pendents")
    public String llistarPeticionsPendents(Model model) {
        model.addAttribute("requests", apRequestDao.getAPRequestsEnRevisio());
        return "admin/peticions-pendents";
    }

    @GetMapping("/generar-proposta/{idRequest}")
    public String generarPropostaMatch(Model model, @PathVariable int idRequest) {
        APRequest request = apRequestDao.getAPRequest(idRequest);
        List<AssistentPersonal> totsAssistents = assistentPersonalDao.getAssistentsAcceptats();

        List<AssistentPersonal> candidatsAdients = totsAssistents.stream()
                .filter(a -> request.getLocalitat() == null || request.getLocalitat().isEmpty() || request.getLocalitat().equals(a.getNombrePueblo()))
                .filter(a -> request.getGenereAssistent() == null || request.getGenereAssistent().equals("Prefereixc no dir-ho") || request.getGenereAssistent().equals(a.getGenere()))
                .filter(a -> request.getTipusAssistencia() == null || request.getTipusAssistencia().isEmpty() || request.getTipusAssistencia().equals(a.getTipus()))
                .collect(Collectors.toList());

        model.addAttribute("aprequest", request);
        model.addAttribute("candidatsAdients", candidatsAdients);

        return "admin/proposta-candidats";
    }

    @GetMapping("/aprovar-petici/{idRequest}")
    public String aprovarPetici(Model model, @PathVariable int idRequest) {
        apRequestDao.aprovarRequest(idRequest);
        return "redirect:/admin/peticions-pendents";
    }
}