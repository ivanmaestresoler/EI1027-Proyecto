package es.uji.ei1027.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistreController {

    @GetMapping("/registre")
    public String mostraSeleccioRegistre() {
        return "seleccio-registre";
    }
    @GetMapping("/registre-completat")
    public String mostraRegistreCompletat() {
        return "registre-completat";
    }
}