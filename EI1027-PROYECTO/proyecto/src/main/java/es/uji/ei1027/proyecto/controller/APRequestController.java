package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.PuebloDAO;
import es.uji.ei1027.proyecto.dao.IdiomaDAO;
import es.uji.ei1027.proyecto.dao.SeleccionDao;
import es.uji.ei1027.proyecto.model.APRequest;
import es.uji.ei1027.proyecto.model.Seleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/aprequest")
public class APRequestController {

    private APRequestDAO apRequestDao;
    private APRequestValidator apRequestValidator;
    private PuebloDAO puebloDao;
    private IdiomaDAO idiomaDao;
    private SeleccionDao seleccionDao;

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) { this.apRequestDao = apRequestDao; }

    @Autowired
    public void setApRequestValidator(APRequestValidator apRequestValidator) { this.apRequestValidator = apRequestValidator; }

    @Autowired
    public void setPuebloDao(PuebloDAO puebloDao) { this.puebloDao = puebloDao; }

    @Autowired
    public void setIdiomaDao(IdiomaDAO idiomaDao) { this.idiomaDao = idiomaDao; }

    @Autowired
    public void setSeleccionDao(SeleccionDao seleccionDao) { this.seleccionDao = seleccionDao; }

    private void cargaAtributosFormulario(Model model) {
        model.addAttribute("tiposAssistencia", Arrays.asList(
                "Higiene personal", "Mobilitat", "Suport emocional",
                "Acompanyament mèdic", "Tasques de la llar"));
        model.addAttribute("generos", Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho"));
        model.addAttribute("pueblos", puebloDao.getPueblos());
        model.addAttribute("idiomas", idiomaDao.getIdiomas());
    }

    @RequestMapping("/list")
    public String listRequests(Model model, jakarta.servlet.http.HttpSession session,
                               @RequestParam(value = "estado", required = false) String estado,
                               @RequestParam(value = "page", defaultValue = "1") int page) {

        es.uji.ei1027.proyecto.model.Usuario usuario =
                (es.uji.ei1027.proyecto.model.Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        int totalRecords;
        List<APRequest> requests;

        if (usuario.getTipusUsuari().equals("UsuariOVI")) {
            requests = apRequestDao.getAPRequestsPerUsuariFiltradesPaginadas(
                    usuario.getIdUsuario(), estado, pageSize, offset);
            totalRecords = apRequestDao.countAPRequestsPerUsuariFiltrades(
                    usuario.getIdUsuario(), estado);
        } else {
            requests = apRequestDao.getAPRequestsFiltradesPaginadas(estado, pageSize, offset);
            totalRecords = apRequestDao.countAPRequestsFiltrades(estado);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;

        model.addAttribute("requests", requests);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "aprequest/list";
    }

    @RequestMapping(value="/add")
    public String addRequest(Model model) {
        model.addAttribute("aprequest", new APRequest());
        cargaAtributosFormulario(model);
        return "aprequest/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("aprequest") APRequest aprequest,
                                   BindingResult bindingResult, Model model,
                                   jakarta.servlet.http.HttpSession session) {
        es.uji.ei1027.proyecto.model.Usuario usuario =
                (es.uji.ei1027.proyecto.model.Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            aprequest.setIdUsuari(usuario.getIdUsuario());
        }
        apRequestValidator.validate(aprequest, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "aprequest/add";
        }
        apRequestDao.addAPRequest(aprequest);
        model.addAttribute("tipus", "acceptat");
        model.addAttribute("destinatari", "Tècnic OVI");
        model.addAttribute("assumpte", "Nova sol·licitud d'assistència rebuda");
        model.addAttribute("cos", "S'ha rebut una nova sol·licitud d'assistència personal. " +
                "Accedeix al panell per revisar-la i generar una proposta de candidats.");
        return "admin/confirmacion-aprovada";
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        model.addAttribute("aprequest", apRequestDao.getAPRequest(id));
        cargaAtributosFormulario(model);
        return "aprequest/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("aprequest") APRequest aprequest,
                                      BindingResult bindingResult, Model model) {
        apRequestValidator.validate(aprequest, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "aprequest/update";
        }
        apRequestDao.updateAPRequest(aprequest);
        return "redirect:/aprequest/list";
    }

    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        apRequestDao.deleteAPRequest(id);
        return "redirect:/aprequest/list";
    }

    // DETALLE — carga los candidatos de Seleccion
    @RequestMapping(value="/detalle/{id}", method = RequestMethod.GET)
    public String veureDetalle(Model model, @PathVariable int id,
                               jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuario") == null) return "redirect:/login";

        APRequest peticion = apRequestDao.getAPRequest(id);
        if (peticion == null) return "redirect:/aprequest/list";

        List<Seleccion> candidats = seleccionDao.getSeleccionsByRequest(id);

        model.addAttribute("aprequest", peticion);
        model.addAttribute("candidats", candidats);
        return "aprequest/detalle";
    }
}