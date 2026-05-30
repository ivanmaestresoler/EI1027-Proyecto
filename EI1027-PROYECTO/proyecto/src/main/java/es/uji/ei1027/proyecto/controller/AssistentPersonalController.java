package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.PuebloDAO;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.dao.DuplicateKeyException;

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

    @RequestMapping("/list")
    public String listAssistents(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;
        int totalRecords = assistentPersonalDao.getTotalAssistentsPersonals();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        List<AssistentPersonal> assistents = assistentPersonalDao.getAssistentsPersonalsPaginats(pageSize, offset);
        for (AssistentPersonal a : assistents) {
            a.setTipusAssistenciaSeleccionats(
                    assistentPersonalDao.getTipusAssistenciaPerAssistent(a.getIdUsuario()));
        }
        model.addAttribute("assistents", assistents);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "assistentPersonal/list";
    }

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addAssistent(Model model) {
        AssistentPersonal assistent = new AssistentPersonal();
        assistent.setGenere("Prefereixc no dir-ho");
        model.addAttribute("assistent", assistent);
        cargaAtributos(model);
        return "assistentPersonal/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("assistent") AssistentPersonal assistent,
                                   BindingResult bindingResult, Model model) {
        assistentPersonalValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributos(model);
            return "assistentPersonal/add";
        }
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        assistent.setContrasenya(passwordEncryptor.encryptPassword(assistent.getContrasenya()));
        try {
            assistentPersonalDao.addAssistentPersonal(assistent); 
        } catch (DuplicateKeyException e) {
            bindingResult.rejectValue("email", "duplicat", "Aquest correu electrònic ja està registrat al sistema.");
            cargaAtributos(model);
            return "assistentPersonal/add";
        }
        return "redirect:/registre-completat";
    }

    @RequestMapping(value="/update/{idUsuario}", method = RequestMethod.GET)
    public String editAssistent(Model model, @PathVariable int idUsuario) {
        AssistentPersonal assistent = assistentPersonalDao.getAssistentPersonal(idUsuario);
        if (assistent != null) {
            assistent.setTipusAssistenciaSeleccionats(assistentPersonalDao.getTipusAssistenciaPerAssistent(idUsuario));
        }
        model.addAttribute("assistent", assistent);
        cargaAtributos(model);
        return "assistentPersonal/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("assistent") AssistentPersonal assistent,
                                      BindingResult bindingResult, Model model,
                                      jakarta.servlet.http.HttpSession session) {
        assistentPersonalValidator.validate(assistent, bindingResult);
        if (bindingResult.hasErrors()) {
            cargaAtributos(model);
            return "assistentPersonal/update";
        }
        assistentPersonalDao.updateAssistentPersonal(assistent);

        // Redirigir según rol
        es.uji.ei1027.proyecto.model.Usuario usuario =
                (es.uji.ei1027.proyecto.model.Usuario) session.getAttribute("usuario");
        if (usuario != null && usuario.getTipusUsuari().equals("AssistentPersonal")) {
            return "redirect:/";
        }
        return "redirect:/assistentPersonal/list";
    }

    @RequestMapping(value="/delete/{idUsuario}")
    public String processDelete(@PathVariable int idUsuario) {
        assistentPersonalDao.deleteAssistentPersonal(idUsuario);
        return "redirect:/assistentPersonal/list";
    }

    private void cargaAtributos(Model model) {
        model.addAttribute("pueblos", puebloDao.getPueblos());
        model.addAttribute("generos", Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho"));
        model.addAttribute("formaciones", Arrays.asList("ESO", "BATXILLERAT", "FPGM", "FPGS", "GRAU UNIVERSITARI"));
        model.addAttribute("tipos", Arrays.asList("PAP", "PATI"));
        model.addAttribute("tiposAssistencia", Arrays.asList("Higiene personal", "Mobilitat", "Suport emocional", "Acompanyament mèdic", "Tasques de la llar", "Altres"));
    }
}