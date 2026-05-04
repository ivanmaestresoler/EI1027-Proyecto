package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.APRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class APRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return APRequest.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        APRequest req = (APRequest) obj;

        if (req.getIdUsuari() == null ) {
            errors.rejectValue("idUsuari", "obligatori", "Cal introduir l'ID de l'usuari que fa la petició");
        }

        if (req.getDataSollicitud() == null || req.getDataSollicitud() > get) {
            errors.rejectValue("nom", "obligatori", "Cal introduir el nom");
        }

        if (usuari.getCognom1() == null || usuari.getCognom1().trim().isEmpty()) {
            errors.rejectValue("cognom1", "obligatori", "Cal introduir el primer cognom");
        }

        if (usuari.getTelefon() == null || usuari.getTelefon().trim().isEmpty()) {
            errors.rejectValue("telefon", "obligatori", "Cal introduir un telèfon de contacte");
        } else if (!usuari.getTelefon().matches("^[6789][0-9]{8}$")) {
            errors.rejectValue("telefon", "format", "El telèfon ha de tindre 9 dígits i començar per 6, 7, 8 o 9");
        }

        if (usuari.getEmail() == null || usuari.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "Cal introduir un email");
        } else if (!usuari.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.rejectValue("email", "format", "El format de l'email no és correcte (ex: nom@domini.com)");
        }

        if (usuari.getDireccio() == null || usuari.getDireccio().trim().isEmpty()) {
            errors.rejectValue("direccio", "obligatori", "Cal introduir una direcció");
        }

        if (usuari.getContrasenyaInicial() == null || usuari.getContrasenyaInicial().trim().isEmpty()) {
            errors.rejectValue("contrasenyaInicial", "obligatori", "Cal introduir una contrasenya");
        } else if (usuari.getContrasenyaInicial().length() < 6) {
            errors.rejectValue("contrasenyaInicial", "curta", "La contrasenya ha de tindre almenys 6 caràcters");
        }

        if (usuari.getConsentimentLOPD() == null || !usuari.getConsentimentLOPD()) {
            errors.rejectValue("consentimentLOPD", "obligatori", "Has d'acceptar el consentiment de la LOPD per registrar-te");
        }
    }
}