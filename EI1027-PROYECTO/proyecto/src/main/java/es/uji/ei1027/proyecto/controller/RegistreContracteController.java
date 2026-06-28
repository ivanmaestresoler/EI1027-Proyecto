package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.RegistreContracteDao;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.model.APRequest;
import es.uji.ei1027.proyecto.model.RegistreContracte;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import es.uji.ei1027.proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/registreContracte")
public class RegistreContracteController {

    private RegistreContracteDao registreContracteDao;
    private APRequestDAO apRequestDao;
    private RegistreContracteValidator validator;
    private AssistentPersonalDao assistentPersonalDao;
    private UsuarioDao usuarioDao;

    @Autowired
    public void setRegistreContracteDao(RegistreContracteDao registreContracteDao) {
        this.registreContracteDao = registreContracteDao;
    }

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) {
        this.apRequestDao = apRequestDao;
    }

    @Autowired
    public void setValidator(RegistreContracteValidator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @Autowired(required = false)
    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @GetMapping("/list")
    public String listContractes(Model model, HttpSession session,
                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        List<RegistreContracte> contractes;
        int totalRecords;

        if (usuario.getTipusUsuari().equals("UsuariOVI")) {
            contractes = registreContracteDao.getContractesByUsuariPaginats(usuario.getIdUsuario(), pageSize, offset);
            totalRecords = registreContracteDao.getTotalContractesByUsuari(usuario.getIdUsuario());
        } else if (usuario.getTipusUsuari().equals("AssistentPersonal")) {
            contractes = registreContracteDao.getContractesByAssistentPaginats(usuario.getIdUsuario(), pageSize, offset);
            totalRecords = registreContracteDao.getTotalContractesByAssistent(usuario.getIdUsuario());
        } else {
            contractes = registreContracteDao.getContractesPaginats(pageSize, offset);
            totalRecords = registreContracteDao.getTotalContractes();
        }

        for (RegistreContracte rc : contractes) {
            APRequest req = apRequestDao.getAPRequest(rc.getIdRequest());
            if (req != null) {
                rc.setTipusAssistencia(req.getTipusAssistencia());
                if (usuarioDao != null) {
                    try {
                        Usuario u = usuarioDao.getUsuario(req.getIdUsuari());
                        if (u != null) {
                            rc.setNomUsuari(u.getNom());
                        } else {
                            rc.setNomUsuari(String.valueOf(req.getIdUsuari()));
                        }
                    } catch (Exception e) {
                        rc.setNomUsuari(String.valueOf(req.getIdUsuari()));
                    }
                } else {
                    rc.setNomUsuari(String.valueOf(req.getIdUsuari()));
                }
            }
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(rc.getIdAssistent());
            if (ap != null) {
                rc.setNomAssistent(ap.getNom() + (ap.getCognom1() != null ? " " + ap.getCognom1() : ""));
            }
        }

        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("contractes", contractes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "registreContracte/list";
    }

    @GetMapping("/add")
    public String addContracte(Model model,
                               @RequestParam(required = false) Integer idRequest,
                               @RequestParam(required = false) Integer idAssistent,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) { // <-- Requiere RedirectAttributes

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (!usuario.getTipusUsuari().equals("UsuariOVI")) return "redirect:/registreContracte/list";

        // VALIDACIÓ: Si ja té contracte, el retornem al DETALL de la petició amb un error
        if (idRequest != null) {
            APRequest request = apRequestDao.getAPRequest(idRequest);
            if (request != null && (request.getEstatRequest().equals("Tancada amb contracte") || request.getEstatRequest().equals("Tancada amb contracte finalitzat"))) {
                redirectAttributes.addFlashAttribute("error", "Acció denegada: Aquesta petició d'assistència ja té un contracte actiu.");
                return "redirect:/aprequest/detalle/" + idRequest;
            }
        }

        RegistreContracte rc = new RegistreContracte();
        if (idRequest != null) rc.setIdRequest(idRequest);
        if (idAssistent != null) rc.setIdAssistent(idAssistent);
        model.addAttribute("registreContracte", rc);
        model.addAttribute("dataAvui", LocalDate.now());
        if (idAssistent != null) {
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(idAssistent);
            if (ap != null) {
                model.addAttribute("nomAssistent", ap.getNom() + " " + ap.getCognom1());
            }
        }
        return "registreContracte/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(
            @ModelAttribute("registreContracte") RegistreContracte registreContracte,
            BindingResult bindingResult, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) { // <-- Requiere RedirectAttributes

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (!usuario.getTipusUsuari().equals("UsuariOVI")) return "redirect:/registreContracte/list";

        // VALIDACIÓ EXTRA AL POST: Per si intenten forçar-ho
        APRequest request = apRequestDao.getAPRequest(registreContracte.getIdRequest());
        if (request != null && (request.getEstatRequest().equals("Tancada amb contracte") || request.getEstatRequest().equals("Tancada amb contracte finalitzat"))) {
            redirectAttributes.addFlashAttribute("error", "No es pot crear el contracte perquè ja n'hi ha un d'actiu per a aquesta petició.");
            return "redirect:/aprequest/detalle/" + registreContracte.getIdRequest();
        }

        validator.validate(registreContracte, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("dataAvui", LocalDate.now());
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(registreContracte.getIdAssistent());
            if (ap != null) {
                model.addAttribute("nomAssistent", ap.getNom() + " " + ap.getCognom1());
            }
            return "registreContracte/add";
        }

        registreContracteDao.addContracte(registreContracte);

        if (request != null) {
            request.setEstatRequest("Tancada amb contracte");
            apRequestDao.updateAPRequest(request);
        }

        model.addAttribute("tipus", "acceptat");
        model.addAttribute("destinatari", "Usuari OVI");
        model.addAttribute("assumpte", "Contracte registrat correctament");
        model.addAttribute("cos", "El contracte per a la sol·licitud #" +
                registreContracte.getIdRequest() +
                " ha sigut registrat correctament. Ja pots consultar-lo des del teu panell.");
        return "admin/confirmacion-aprovada";
    }

    @GetMapping("/update/{id}")
    public String updateContracte(Model model, @PathVariable int id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (!usuario.getTipusUsuari().equals("UsuariOVI")) return "redirect:/registreContracte/list";
        RegistreContracte rc = registreContracteDao.getContracte(id);
        model.addAttribute("registreContracte", rc);
        model.addAttribute("dataAvui", LocalDate.now());
        if (rc != null) {
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(rc.getIdAssistent());
            if (ap != null) {
                model.addAttribute("nomAssistent", ap.getNom() + " " + ap.getCognom1());
            }
        }
        return "registreContracte/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(
            @ModelAttribute("registreContracte") RegistreContracte registreContracte,
            BindingResult bindingResult, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (!usuario.getTipusUsuari().equals("UsuariOVI")) return "redirect:/registreContracte/list";
        validator.validate(registreContracte, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("dataAvui", LocalDate.now());
            AssistentPersonal ap = assistentPersonalDao.getAssistentPersonal(registreContracte.getIdAssistent());
            if (ap != null) {
                model.addAttribute("nomAssistent", ap.getNom() + " " + ap.getCognom1());
            }
            return "registreContracte/update";
        }
        registreContracteDao.updateContracte(registreContracte);
        return "redirect:/registreContracte/list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id, HttpSession session) {
        return "redirect:/registreContracte/list";
    }
}