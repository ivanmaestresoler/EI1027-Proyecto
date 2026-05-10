package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import es.uji.ei1027.proyecto.dao.PuebloDAO;
import es.uji.ei1027.proyecto.dao.IdiomaDAO;
import es.uji.ei1027.proyecto.model.APRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/aprequest")
public class APRequestController {

    private APRequestDAO apRequestDao;
    private APRequestValidator apRequestValidator;
    private PuebloDAO puebloDao;
    private IdiomaDAO idiomaDao;

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) {
        this.apRequestDao = apRequestDao;
    }

    @Autowired
    public void setApRequestValidator(APRequestValidator apRequestValidator) {
        this.apRequestValidator = apRequestValidator;
    }

    @Autowired
    public void setPuebloDao(PuebloDAO puebloDao) {
        this.puebloDao = puebloDao;
    }

    @Autowired
    public void setIdiomaDao(IdiomaDAO idiomaDao) {
        this.idiomaDao = idiomaDao;
    }

    private void cargaAtributosFormulario(Model model) {
        List<String> tiposAssistencia = Arrays.asList("Higiene personal", "Mobilitat", "Suport emocional", "Acompanyament mèdic", "Tasques de la llar");
        List<String> generos = Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho");

        model.addAttribute("tiposAssistencia", tiposAssistencia);
        model.addAttribute("generos", generos);
        model.addAttribute("pueblos", puebloDao.getPueblos());
        model.addAttribute("idiomas", idiomaDao.getIdiomas());
    }

    @RequestMapping("/list")
    public String listRequests(Model model) {
        model.addAttribute("requests", apRequestDao.getAPRequests());
        return "aprequest/list";
    }

    @RequestMapping(value="/add")
    public String addRequest(Model model) {
        model.addAttribute("aprequest", new APRequest());
        cargaAtributosFormulario(model);
        return "aprequest/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("aprequest") APRequest aprequest, BindingResult bindingResult, Model model, jakarta.servlet.http.HttpSession session) {
        es.uji.ei1027.proyecto.model.Usuario usuario = (es.uji.ei1027.proyecto.model.Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            aprequest.setIdUsuari(usuario.getIdUsuario());
        }

        apRequestValidator.validate(aprequest, bindingResult);

        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "aprequest/add";
        }
        apRequestDao.addAPRequest(aprequest);
        return "redirect:/aprequest/list";
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editRequest(Model model, @PathVariable int id) {
        model.addAttribute("aprequest", apRequestDao.getAPRequest(id));
        cargaAtributosFormulario(model);
        return "aprequest/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("aprequest") APRequest aprequest,BindingResult bindingResult, Model model) {
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
}