package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/usuari")
public class UsuariOVIController {

    private UsuariOVIDAO usuariOVIDAO;
    private UsuariOVIValidator usuariOVIValidator;

    @Autowired
    public void setUsuariOVIDAO(UsuariOVIDAO usuariOVIDAO) {
        this.usuariOVIDAO = usuariOVIDAO;
    }

    @Autowired
    public void setUsuariOVIValidator(UsuariOVIValidator usuariOVIValidator) {
        this.usuariOVIValidator = usuariOVIValidator;
    }

    private void cargaAtributosFormulario(Model model) {
        List<String> generos = Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho");
        List<String> tiposAsistencia = Arrays.asList(
            "Higiene personal", 
            "Mobilitat", 
            "Suport emocional", 
            "Acompanyament mèdic", 
            "Tasques de la llar", 
            "Altres"
        );
        model.addAttribute("generos", generos);
        model.addAttribute("tiposAsistencia", tiposAsistencia);
    }

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
    public String processAddSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI, BindingResult bindingResult, Model model) {
        usuariOVIValidator.validate(usuariOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/add";
        }

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
    public String processUpdateSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI, BindingResult bindingResult, Model model) {
        usuariOVIValidator.validate(usuariOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/update";
        }

        usuariOVIDAO.updateUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        usuariOVIDAO.deleteUsuariOVI(id);
        return "redirect:../list";
    }
}