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

    // Listado: técnico ve todos, usuario OVI solo los suyos
    @GetMapping("/list")
    public String listContractes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        if (usuario.getTipusUsuari().equals("UsuariOVI")) {
            model.addAttribute("contractes",
                    registreContracteDao.getContractesByUsuari(usuario.getIdUsuario()));
        } else {
            model.addAttribute("contractes", registreContracteDao.getContractes());
        }
        return "registreContracte/list";
    }

    // GET add: recibe idRequest e idAssistent opcionales desde el detalle
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

    // POST add: valida, guarda, actualitza estat request i mostra confirmació
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
    public String updateContracte(Model model, @PathVariable int id) {
        model.addAttribute("registreContracte", registreContracteDao.getContracte(id));
        return "registreContracte/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(
            @ModelAttribute("registreContracte") RegistreContracte registreContracte,
            BindingResult bindingResult, Model model) {

        validator.validate(registreContracte, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registreContracte/update";
        }

        registreContracteDao.updateContracte(registreContracte);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        registreContracteDao.deleteContracte(id);
        return "redirect:../list";
    }
}