package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.RegistreContracteDao;
import es.uji.ei1027.proyecto.model.APRequest;
import es.uji.ei1027.proyecto.model.RegistreContracte;
import es.uji.ei1027.proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/registreContracte")
public class RegistreContracteController {

    private RegistreContracteDao registreContracteDao;
    private APRequestDAO apRequestDao;
    private RegistreContracteValidator validator;

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

        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("contractes", contractes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "registreContracte/list";
    }

    @GetMapping("/add")
    public String addContracte(Model model,
                               @RequestParam(required = false) Integer idRequest,
                               @RequestParam(required = false) Integer idAssistent) {
        RegistreContracte rc = new RegistreContracte();
        if (idRequest != null) rc.setIdRequest(idRequest);
        if (idAssistent != null) rc.setIdAssistent(idAssistent);
        model.addAttribute("registreContracte", rc);
        return "registreContracte/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(
            @ModelAttribute("registreContracte") RegistreContracte registreContracte,
            BindingResult bindingResult, Model model) {

        validator.validate(registreContracte, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registreContracte/add";
        }

        registreContracteDao.addContracte(registreContracte);

        APRequest request = apRequestDao.getAPRequest(registreContracte.getIdRequest());
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
        if (usuario.getTipusUsuari().equals("admin")) return "redirect:/registreContracte/list";
        model.addAttribute("registreContracte", registreContracteDao.getContracte(id));
        return "registreContracte/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(
            @ModelAttribute("registreContracte") RegistreContracte registreContracte,
            BindingResult bindingResult, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";
        if (usuario.getTipusUsuari().equals("admin")) return "redirect:/registreContracte/list";
        validator.validate(registreContracte, bindingResult);
        if (bindingResult.hasErrors()) return "registreContracte/update";
        registreContracteDao.updateContracte(registreContracte);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id, HttpSession session) {
        return "redirect:/registreContracte/list";
    }
}