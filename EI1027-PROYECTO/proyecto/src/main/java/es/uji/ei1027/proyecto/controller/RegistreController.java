package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.AssistentPersonalDao;
import es.uji.ei1027.proyecto.dao.UsuariOVIDAO;
import es.uji.ei1027.proyecto.model.AssistentPersonal;
import es.uji.ei1027.proyecto.model.UsuariOVI;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/registre")
public class RegistreController {

    @Autowired
    private UsuariOVIDAO usuariOviDao;

    @Autowired
    private AssistentPersonalDao assistentPersonalDao;

    // Menú de selección de tipo de registro
    @RequestMapping(value="", method=RequestMethod.GET)
    public String tipusRegistre() {
        return "seleccio-registre"; // Pantalla con dos botones: OVI o Asistente
    }

    // ==========================================================
    // REGISTRO DE USUARIO OVI
    // ==========================================================
    @RequestMapping(value="/usuariovi", method=RequestMethod.GET)
    public String addUsuariOVI(Model model) {
        model.addAttribute("usuari", new UsuariOVI());
        return "usuari/add";
    }

    @RequestMapping(value="/usuariovi", method=RequestMethod.POST)
    public String processAddUsuariOVI(@ModelAttribute("usuari") UsuariOVI usuari, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "usuari/add";
        }

        // 1. Encriptar la contraseña de forma segura
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String pwdEncriptada = passwordEncryptor.encryptPassword(usuari.getContrasenya());
        usuari.setContrasenya(pwdEncriptada);

        // 2. Establecer estado inicial y guardar
        usuari.setEstatUsuari("Pendent");
        usuariOviDao.addUsuariOVI(usuari);

        return "registre-completat"; // O tu vista de confirmación
    }

    // ==========================================================
    // REGISTRO DE ASISTENTE PERSONAL
    // ==========================================================
    @RequestMapping(value="/assistent", method=RequestMethod.GET)
    public String addAssistent(Model model) {
        model.addAttribute("assistent", new AssistentPersonal());
        return "assistentPersonal/add";
    }

    @RequestMapping(value="/assistent", method=RequestMethod.POST)
    public String processAddAssistent(@ModelAttribute("assistent") AssistentPersonal assistent, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "assistentPersonal/add";
        }
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String pwdEncriptada = passwordEncryptor.encryptPassword(assistent.getContrasenya());
        assistent.setContrasenya(pwdEncriptada);

        assistent.setEstatAcceptat("Candidat");
        assistentPersonalDao.addAssistentPersonal(assistent);

        return "registre-completat"; // O tu vista de confirmación
    }
}