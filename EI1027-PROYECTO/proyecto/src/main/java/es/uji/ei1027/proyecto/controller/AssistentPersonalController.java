package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.PuebloDAO;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
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
@RequestMapping("/assistentPersonal")
public class AssistentPersonalController {

    private AssistentPersonalDao assistentPersonalDao;
    private AssistentPersonalValidator assistentPersonalValidator;
    private PuebloDAO puebloDao;

    @Autowired
    public void setAssistentPersonalDao(AssistentPersonalDao assistentPersonalDao) {
        this.assistentPersonalDao = assistentPersonalDao;
    }

    @Autowired
    public void setAssistentPersonalValidator(AssistentPersonalValidator assistentPersonalValidator) {
        this.assistentPersonalValidator = assistentPersonalValidator;
    }

    @Autowired
    public void setPuebloDao(PuebloDAO puebloDao) {
        this.puebloDao = puebloDao;
    }

    private void cargaAtributos(Model model) {
        model.addAttribute("formaciones", Arrays.asList("ESO", "BATXILLERAT", "FPGM", "FPGS", "GRAU UNIVERSITARI"));
        model.addAttribute("tipos", Arrays.asList("PAP", "PATI"));
        model.addAttribute("generos", Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho"));
        model.addAttribute("pueblos", puebloDao.getPueblos());
        // AÑADIDO: Los tipos de asistencia para los checkboxes
        model.addAttribute("tiposAssistencia", Arrays.asList("Higiene personal", "Mobilitat", "Suport emocional", "Acompanyament mèdic", "Tasques de la llar", "Altres"));
    }

    @RequestMapping("/list")
    public String listAssistents(Model model) {
        model.addAttribute("assistents", assistentPersonalDao.getAssistentsPersonals());
        return "assistentPersonal/list";
    }

    @RequestMapping(value="/add")
    public String addAssistent(Model model) {
        model.addAttribute("assistent", new AssistentPersonal());
        cargaAtributos(model);
        return "assistentPersonal/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("assistent") AssistentPersonal assistent,
                                   BindingResult bindingResult, Model model) {
        assistentPersonalValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributos(model);
            return "assistentPersonal/add";
        }
        assistentPersonalDao.addAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/update/{idUsuario}", method = RequestMethod.GET)
    public String editAssistent(Model model, @PathVariable int idUsuario) {
        AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(idUsuario);
        if (assistent != null) {
            // AÑADIDO: Cargar los checkboxes marcados previamente en la base de datos
            assistent.setTipusAssistenciaSeleccionats(assistentPersonalDao.getTipusAssistenciaPerAssistent(idUsuario));
        }
        model.addAttribute("assistent", assistent);
        cargaAtributos(model);
        return "assistentPersonal/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("assistent") AssistentPersonal assistent,
                                      BindingResult bindingResult, Model model) {
        assistentPersonalValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributos(model);
            return "assistentPersonal/update";
        }
        assistentPersonalDao.updateAssistentPersonal(assistent);
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/delete/{idUsuario}")
    public String processDelete(@PathVariable int idUsuario) {
        assistentPersonalDao.deleteAssistentPersonal(idUsuario);
        return "redirect:/assistentPersonal/list";
    }
}