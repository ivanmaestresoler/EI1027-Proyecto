package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.model.UsuariOVI;
// import es.uji.ei1027.proyecto.dao.PuebloDao; // Habilítalo cuando tengas el DAO de Pueblos

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

    // --- CARGA DE DESPLEGABLES PARA FORMULARIOS ---
    private void cargaAtributosFormulario(Model model) {
        // Valores extraídos exactamente de los ENUMs de tu BBDD
        List<String> generos = Arrays.asList("Masculí", "Femení", "Prefereixc no dir-ho");
        List<String> tiposAssistencia = Arrays.asList("Higiene personal", "Mobilitat", "Suport emocional", "Acompanyament mèdic", "Tasques de la llar", "Altres");

        model.addAttribute("generos", generos);
        model.addAttribute("tiposAssistencia", tiposAssistencia);
        // model.addAttribute("pueblos", puebloDao.getPueblos());
    }

    // --- LISTAR USUARIOS ---
    @RequestMapping("/list")
    public String listUsuarios(Model model) {
        model.addAttribute("usuaris", usuariOVIDAO.getUsuariosOVI());
        return "usuari/list";
    }

    // --- AÑADIR USUARIO (GET) ---
    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addUsuario(Model model) {
        model.addAttribute("usuariOVI", new UsuariOVI());
        cargaAtributosFormulario(model);
        return "usuari/add";
    }

    // --- AÑADIR USUARIO (POST) ---
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI, BindingResult bindingResult, Model model) {
        // 1. Validamos los datos introducidos
        usuariOVIValidator.validate(usuariOVI, bindingResult);

        // 2. Si hay errores, recargamos el formulario manteniendo los datos
        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/add";
        }

        // 3. Guardamos en Base de Datos
        usuariOVIDAO.addUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    // --- ACTUALIZAR USUARIO (GET) ---
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editUsuario(Model model, @PathVariable int id) {
        model.addAttribute("usuariOVI", usuariOVIDAO.getUsuariOVI(id));
        cargaAtributosFormulario(model);
        return "usuari/update";
    }

    // --- ACTUALIZAR USUARIO (POST) ---
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("usuariOVI") UsuariOVI usuariOVI, BindingResult bindingResult, Model model) {
        usuariOVIValidator.validate(usuariOVI, bindingResult);

        if (bindingResult.hasErrors()) {
            cargaAtributosFormulario(model);
            return "usuari/update";
        }

        usuariOVIDAO.updateUsuariOVI(usuariOVI);
        return "redirect:list";
    }

    // --- BORRAR USUARIO ---
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        usuariOVIDAO.deleteUsuariOVI(id);
        return "redirect:../list";
    }
}