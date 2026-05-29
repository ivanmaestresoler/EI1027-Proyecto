package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private APRequestDAO apRequestDao;

    @Autowired
    private UsuariOVIDAO usuariOVIDAO;

    @Autowired
    private AssistentPersonalDao assistentPersonalDao;


    private boolean isNotAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario == null || !usuario.getTipusUsuari().equals("admin");
    }

    @GetMapping("/solicituds-ovi")
    public String validarUsuarisOVI(Model model, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("usuaris", usuariOVIDAO.getUsuarisPendents());
        return "admin/solicituds-ovi";
    }

    @GetMapping("/solicituds-assistent")
    public String validarAssistents(Model model, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("assistents", assistentPersonalDao.getCandidats());
        return "admin/solicituds-assistent";
    }
    @GetMapping("/peticions-pendents")
    public String peticionsPendents(Model model, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login"; // Protegemos la ruta

        model.addAttribute("requests", apRequestDao.getAPRequestsFiltrades("En revisió"));
        return "aprequest/list";
    }

    @GetMapping("/aprovar-petici/{idRequest}")
    public String aprovarPetici(Model model, @PathVariable int idRequest, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        apRequestDao.aprovarRequest(idRequest);
        model.addAttribute("mensaje", "S'ha aprovat la petició i s'ha simulat l'enviament d'un correu amb la proposta de candidats a l'Usuari OVI.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-petici/{idRequest}")
    public String rebutjarPetici(Model model, @PathVariable int idRequest, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        apRequestDao.rebutjarRequest(idRequest);
        model.addAttribute("mensaje", "La petició ha sigut rebutjada. S'ha enviat un correu explicatiu a l'usuari per informar-lo.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/aprovar-usuari/{idUsuario}")
    public String aprovarUsuari(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        usuariOVIDAO.aprovarUsuari(idUsuario);
        model.addAttribute("mensaje", "L'Usuari OVI ha sigut validat correctament. S'ha simulat l'enviament d'un correu de benvinguda indicant que ja pot fer peticions.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-usuari/{idUsuario}")
    public String rebutjarUsuari(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        usuariOVIDAO.rebutjarUsuari(idUsuario);
        model.addAttribute("mensaje", "La sol·licitud de registre de l'Usuari OVI ha sigut rebutjada. S'ha enviat un correu informant-lo dels motius.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/aprovar-assistent/{idUsuario}")
    public String aprovarAssistent(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        assistentPersonalDao.aprovarAssistent(idUsuario);
        model.addAttribute("mensaje", "L'Assistent Personal ha sigut validat i acceptat correctament. S'ha enviat un correu de benvinguda per a informar-lo.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-assistent/{idUsuario}")
    public String rebutjarAssistent(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        assistentPersonalDao.rebutjarAssistent(idUsuario);
        model.addAttribute("mensaje", "La sol·licitud de registre de l'Assistent Personal ha sigut rebutjada. S'ha enviat un correu informant-lo dels motius.");
        return "admin/confirmacion-aprovada";
    }
}