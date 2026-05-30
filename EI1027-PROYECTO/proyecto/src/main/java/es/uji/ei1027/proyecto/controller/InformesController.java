package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.RegistreContracteDao;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/informes")
public class InformesController {

    @Autowired private APRequestDAO apRequestDao;
    @Autowired private AssistentPersonalDao assistentPersonalDao;
    @Autowired private RegistreContracteDao registreContracteDao;
    @Autowired private UsuariOVIDAO usuariOVIDAO;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !usuario.getTipusUsuari().equals("admin")) {
            return "redirect:/login";
        }

        // ── USUARIS OVI ─────────────────────────────────────────────────────
        long usuarisPendents = usuariOVIDAO.getUsuarisOVI().stream()
                .filter(u -> "Pendent".equals(u.getEstatUsuari())).count();
        long usuarisAcceptats = usuariOVIDAO.getUsuarisOVI().stream()
                .filter(u -> "Acceptat".equals(u.getEstatUsuari())).count();
        long usuarisRebutjats = usuariOVIDAO.getUsuarisOVI().stream()
                .filter(u -> "Rebutjat".equals(u.getEstatUsuari())).count();
        long totalUsuaris = usuariOVIDAO.getTotalUsuarisOVI();

        // ── SOL·LICITUDS AP ──────────────────────────────────────────────────
        long requestEnRevisio = apRequestDao.getAPRequestsFiltrades("En revisió").size();
        long requestAprovada = apRequestDao.getAPRequestsFiltrades("Aprovada").size();
        long requestRebutjada = apRequestDao.getAPRequestsFiltrades("Rebutjada").size();
        long requestTancada = apRequestDao.getAPRequestsFiltrades("Tancada amb contracte").size();
        long requestTancadaFinalitzat = apRequestDao.getAPRequestsFiltrades("Tancada amb contracte finalitzat").size();
        long totalRequests = apRequestDao.getAPRequests().size();

        // ── ASSISTENTS ───────────────────────────────────────────────────────
        long assistentsCandidats = assistentPersonalDao.getCandidats().size();
        long assistentsAcceptats = assistentPersonalDao.getAssistentsAcceptats().size();
        long assistentsRebutjats = assistentPersonalDao.getAssistentsPersonals().stream()
                .filter(a -> "Rebutjat".equals(a.getEstatAcceptat())).count();
        long totalAssistents = assistentPersonalDao.getTotalAssistentsPersonals();

        // ── CONTRACTES ───────────────────────────────────────────────────────
        List<es.uji.ei1027.proyecto.model.RegistreContracte> contractes =
                registreContracteDao.getContractes();
        long contractesActius = contractes.stream()
                .filter(c -> c.getDataFi() == null || c.getDataFi().isAfter(LocalDate.now()))
                .count();
        long contractesFinalitzats = contractes.stream()
                .filter(c -> c.getDataFi() != null && !c.getDataFi().isAfter(LocalDate.now()))
                .count();
        long totalContractes = contractes.size();

        // ── PASSAR AL MODEL ──────────────────────────────────────────────────
        model.addAttribute("totalUsuaris", totalUsuaris);
        model.addAttribute("usuarisPendents", usuarisPendents);
        model.addAttribute("usuarisAcceptats", usuarisAcceptats);
        model.addAttribute("usuarisRebutjats", usuarisRebutjats);

        model.addAttribute("totalRequests", totalRequests);
        model.addAttribute("requestEnRevisio", requestEnRevisio);
        model.addAttribute("requestAprovada", requestAprovada);
        model.addAttribute("requestRebutjada", requestRebutjada);
        model.addAttribute("requestTancada", requestTancada);
        model.addAttribute("requestTancadaFinalitzat", requestTancadaFinalitzat);

        model.addAttribute("totalAssistents", totalAssistents);
        model.addAttribute("assistentsCandidats", assistentsCandidats);
        model.addAttribute("assistentsAcceptats", assistentsAcceptats);
        model.addAttribute("assistentsRebutjats", assistentsRebutjats);

        model.addAttribute("totalContractes", totalContractes);
        model.addAttribute("contractesActius", contractesActius);
        model.addAttribute("contractesFinalitzats", contractesFinalitzats);

        return "informes/dashboard";
    }
}