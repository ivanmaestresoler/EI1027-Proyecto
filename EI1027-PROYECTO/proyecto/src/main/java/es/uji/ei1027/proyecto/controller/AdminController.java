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
import org.springframework.web.bind.annotation.RequestParam;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import es.uji.ei1027.proyecto.model.AssistentPersonal;

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
    public String peticionsPendents(Model model, HttpSession session,
                                    @RequestParam(required = false) String estat) {
        if (isNotAdmin(session)) return "redirect:/login";
        model.addAttribute("requests", apRequestDao.getAPRequestsFiltrades(estat));
        model.addAttribute("estatActual", estat);
        return "admin/peticions-pendents";
    }

    @GetMapping("/aprovar-petici/{idRequest}")
    public String aprovarPetici(Model model, @PathVariable int idRequest, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        apRequestDao.aprovarRequest(idRequest);
        model.addAttribute("tipus", "acceptat");
        model.addAttribute("destinatari", "Usuari OVI");
        model.addAttribute("assumpte", "La teua petició d'assistència ha sigut aprovada");
        model.addAttribute("cos", "La teua sol·licitud d'assistència personal ha sigut revisada i aprovada pel tècnic OVI. En breu rebràs una proposta de candidats adients al teu perfil.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-petici/{idRequest}")
    public String rebutjarPetici(Model model, @PathVariable int idRequest, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        apRequestDao.rebutjarRequest(idRequest);
        model.addAttribute("tipus", "rebutjat");
        model.addAttribute("destinatari", "Usuari OVI");
        model.addAttribute("assumpte", "La teua petició d'assistència ha sigut rebutjada");
        model.addAttribute("cos", "Lamentem informar-te que la teua sol·licitud d'assistència personal no ha pogut ser aprovada. Si tens dubtes, posa't en contacte amb l'OVI.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/aprovar-usuari/{idUsuario}")
    public String aprovarUsuari(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        String email = "";
        try {
            UsuariOVI u = usuariOVIDAO.getUsuariOVI(idUsuario);
            if (u != null) {
                email = u.getEmail();
            }
        } catch (Exception e) {
        }
        model.addAttribute("emailUsuario", email);
        usuariOVIDAO.aprovarUsuari(idUsuario);
        model.addAttribute("tipus", "acceptat");
        model.addAttribute("destinatari", "Usuari OVI");
        model.addAttribute("assumpte", "El teu compte ha sigut activat");
        model.addAttribute("cos", "El teu registre com a Usuari OVI ha sigut validat correctament. Ja pots iniciar sessió a la plataforma i fer les teues sol·licituds d'assistència personal.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-usuari/{idUsuario}")
    public String rebutjarUsuari(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        String email = "";
        try {
            UsuariOVI u = usuariOVIDAO.getUsuariOVI(idUsuario);
            if (u != null) {
                email = u.getEmail();
            }
        } catch (Exception e) {
        }
        model.addAttribute("emailUsuario", email);
        usuariOVIDAO.rebutjarUsuari(idUsuario);
        model.addAttribute("tipus", "rebutjat");
        model.addAttribute("destinatari", "Usuari OVI");
        model.addAttribute("assumpte", "La teua sol·licitud de registre ha sigut rebutjada");
        model.addAttribute("cos", "Lamentem informar-te que la teua sol·licitud de registre com a Usuari OVI no ha pogut ser acceptada. Si creus que és un error, posa't en contacte amb l'administració de l'OVI.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/aprovar-assistent/{idUsuario}")
    public String aprovarAssistent(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        String email = "";
        try {
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(idUsuario);
            if (ap != null) {
                email = ap.getEmail();
            }
        } catch (Exception e) {
        }
        model.addAttribute("emailUsuario", email);
        assistentPersonalDao.aprovarAssistent(idUsuario);
        model.addAttribute("tipus", "acceptat");
        model.addAttribute("destinatari", "Assistent Personal");
        model.addAttribute("assumpte", "El teu perfil d'assistent ha sigut acceptat");
        model.addAttribute("cos", "El teu perfil com a Assistent Personal ha sigut validat i acceptat. Ja pots iniciar sessió i apareixeràs com a candidat en les sol·licituds que s'adeqüen al teu perfil.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/rebutjar-assistent/{idUsuario}")
    public String rebutjarAssistent(Model model, @PathVariable int idUsuario, HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";

        String email = "";
        try {
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(idUsuario);
            if (ap != null) {
                email = ap.getEmail();
            }
        } catch (Exception e) {
        }
        model.addAttribute("emailUsuario", email);
        assistentPersonalDao.rebutjarAssistent(idUsuario);
        model.addAttribute("tipus", "rebutjat");
        model.addAttribute("destinatari", "Assistent Personal");
        model.addAttribute("assumpte", "La teua sol·licitud com a assistent ha sigut rebutjada");
        model.addAttribute("cos", "Lamentem informar-te que la teua sol·licitud per a ser Assistent Personal no ha pogut ser acceptada. Si tens dubtes, posa't en contacte amb l'OVI.");
        return "admin/confirmacion-aprovada";
    }
}