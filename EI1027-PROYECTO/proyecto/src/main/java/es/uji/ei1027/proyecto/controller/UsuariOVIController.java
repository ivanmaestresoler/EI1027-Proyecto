package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.PuebloDAO;
import es.uji.ei1027.proyecto.dao.RegistreContracteDao;
import es.uji.ei1027.proyecto.dao.SeleccionDao;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.model.APRequest;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import es.uji.ei1027.proyecto.model.RegistreContracte;
import es.uji.ei1027.proyecto.model.Seleccion;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import es.uji.ei1027.proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/usuari")
public class UsuariOVIController {

    private UsuariOVIDAO usuariOVIDAO;
    private UsuariOVIValidator usuariOVIValidator;
    private APRequestDAO apRequestDao;
    private SeleccionDao seleccionDao;
    private AssistentPersonalDao assistentPersonalDao;
    private RegistreContracteDao registreContracteDao;
    private UsuarioDao usuarioDao;
    private PuebloDAO puebloDao;

    @Autowired
    public void setUsuariOVIDAO(UsuariOVIDAO usuariOVIDAO) { this.usuariOVIDAO = usuariOVIDAO; }

    @Autowired
    public void setUsuariOVIValidator(UsuariOVIValidator usuariOVIValidator) { this.usuariOVIValidator = usuariOVIValidator; }

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) { this.apRequestDao = apRequestDao; }

    @Autowired
    public void setSeleccionDao(SeleccionDao seleccionDao) { this.seleccionDao = seleccionDao; }

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) { this.assistentPersonalDao = assistentPersonalDao; }

    @Autowired
    public void setRegistreContracteDao(RegistreContracteDao registreContracteDao) { this.registreContracteDao = registreContracteDao; }

    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) { this.usuarioDao = usuarioDao; }

    @Autowired
    public void setPuebloDao(PuebloDAO puebloDao) { this.puebloDao = puebloDao; }

    private void cargaAtributosFormulario(Model model) {
        model.addAttribute("generos", Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho"));
        model.addAttribute("tiposAsistencia", Arrays.asList(
                "Higiene personal", "Mobilitat", "Suport emocional",
                "Acompanyament mèdic", "Tasques de la llar", "Altres"));
        model.addAttribute("pueblos", puebloDao.getPueblos());
    }

    // ── CRUD ────────────────────────────────────────────────────────────────

    @GetMapping("/list")
    public String listUsuaris(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        int totalRecords = usuariOVIDAO.getTotalUsuarisOVI();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);
        model.addAttribute("usuaris", usuariOVIDAO.getUsuarisOVIPaginats(pageSize, offset));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "usuari/list";
    }

    @GetMapping("/add")
    public String addUsuario(Model model) {
        UsuariOVI usuariOVI = new UsuariOVI();
        usuariOVI.setGenere("Prefereixc no dir-ho");
        model.addAttribute("usuariOVI", usuariOVI);
        cargaAtributosFormulario(model);
        return "usuari/add";
    }

    @PostMapping("/add")
    public String processAddSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
                                   BindingResult bindingResult, Model model) {

        usuariOVIValidator.validate(usuariOVI, bindingResult);

        // Comprovar email duplicat
        if (usuariOVI.getEmail() != null && !usuariOVI.getEmail().isEmpty()) {
            if (usuarioDao.getUsuarioByEmail(usuariOVI.getEmail()) != null) {
                bindingResult.rejectValue("email", "duplicat",
                        "Aquest email ja està registrat. Utilitza un altre.");
            }
        }

        // Comprovar DNI duplicat
        if (usuariOVI.getDni() != null && !usuariOVI.getDni().isEmpty()) {
            if (usuarioDao.getUsuarioByDni(usuariOVI.getDni()) != null) {
                bindingResult.rejectValue("dni", "duplicat",
                        "Aquest DNI ja està registrat.");
            }
        }

        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/add";
        }

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuariOVI.setContrasenya(passwordEncryptor.encryptPassword(usuariOVI.getContrasenya()));
        usuariOVI.setEstatUsuari("Pendent");
        usuariOVIDAO.addUsuariOVI(usuariOVI);
        return "redirect:/registre-completat";
    }

    @GetMapping("/update/{id}")
    public String editUsuario(Model model, @PathVariable int id) {
        model.addAttribute("usuariOVI", usuariOVIDAO.getUsuariOVI(id));
        cargaAtributosFormulario(model);
        return "usuari/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI,
                                      BindingResult bindingResult, Model model,
                                      HttpSession session) {
        usuariOVIValidator.validate(usuariOVI, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/update";
        }
        usuariOVIDAO.updateUsuariOVI(usuariOVI);

        // Redirigir según rol
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null && usuario.getTipusUsuari().equals("UsuariOVI")) {
            return "redirect:/";
        }
        return "redirect:/usuari/list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        usuariOVIDAO.deleteUsuariOVI(id);
        return "redirect:../list";
    }

    // ── CANDIDATS I FINALITZAR CONTRACTE ───────────────────────────────────

    @GetMapping("/candidats/{idRequest}")
    public String veureCandidats(@PathVariable int idRequest, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        APRequest request = apRequestDao.getAPRequest(idRequest);
        List<Seleccion> seleccions = seleccionDao.getSeleccionsByRequest(idRequest);

        List<AssistentPersonal> candidats = new ArrayList<>();
        for (Seleccion s : seleccions) {
            AssistentPersonal a = assistentPersonalDao.getAssistentPersonal(s.getIdAssistent());
            if (a != null) candidats.add(a);
        }

        model.addAttribute("request", request);
        model.addAttribute("candidats", candidats);
        return "usuari/candidats";
    }

    @GetMapping("/finalitzar-contracte/{idRequest}/{idAssistent}")
    public String finalitzarContracte(@PathVariable int idRequest,
                                      @PathVariable int idAssistent,
                                      Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        RegistreContracte contracte = new RegistreContracte();
        contracte.setIdRequest(idRequest);
        contracte.setIdAssistent(idAssistent);
        contracte.setDataInici(LocalDate.now());
        registreContracteDao.addContracte(contracte);

        APRequest request = apRequestDao.getAPRequest(idRequest);
        request.setEstatRequest("Tancada amb contracte");
        apRequestDao.updateAPRequest(request);

        AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(idAssistent);
        model.addAttribute("request", apRequestDao.getAPRequest(idRequest));
        model.addAttribute("assistent", assistent);
        return "usuari/confirmacio-contracte";
    }
}