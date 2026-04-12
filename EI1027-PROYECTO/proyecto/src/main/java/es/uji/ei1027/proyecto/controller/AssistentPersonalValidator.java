package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.AssistentPersonal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AssistentPersonalValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return AssistentPersonal.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        AssistentPersonal assistent = (AssistentPersonal) obj;

        // Validació del DNI
        if (assistent.getDni() == null || assistent.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatori", "Cal introduir el DNI");
        } else if (assistent.getDni().length() < 9 || assistent.getDni().length() > 10) {
            errors.rejectValue("dni", "longitud", "El DNI ha de tindre 9 caràcters");
        }

        // Validació del Nom
        if (assistent.getNom() == null || assistent.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "obligatori", "Cal introduir el nom");
        } else if (assistent.getNom().length() < 2 || assistent.getNom().length() > 50) {
            errors.rejectValue("nom", "longitud", "El nom ha de tindre entre 2 i 50 caràcters");
        }
        // Validació del Primer Cognom
        if (assistent.getCognom1() == null || assistent.getCognom1().trim().isEmpty()) {
            errors.rejectValue("cognom1", "obligatori", "Cal introduir el primer cognom");
        } else if (assistent.getCognom1().length() < 2 || assistent.getCognom1().length() > 50) {
            errors.rejectValue("cognom1", "longitud", "El primer cognom ha de tindre entre 2 i 50 caràcters");
        }

        // Validació de l'Email
        if (assistent.getEmail() == null || assistent.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "Cal introduir un email de contacte");
        } else if (!assistent.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.rejectValue("email", "format", "El format de l'email no és correcte (ex: nom@domini.com)");
        } else if (assistent.getEmail().length() > 100) {
            errors.rejectValue("email", "longitud", "L'email no pot superar els 100 caràcters");
        }

        // Validació del Tipus
        if (assistent.getTipus() == null || assistent.getTipus().trim().isEmpty()) {
            errors.rejectValue("tipus", "obligatori", "Cal seleccionar el tipus d'assistent");
        } else if (!assistent.getTipus().equals("PAP") && !assistent.getTipus().equals("PATI")) {
            // Si el texto no es ni PAP ni PATI, salta este error
            errors.rejectValue("tipus", "invalid", "El tipus només pot ser 'PAP' o 'PATI'");
        }
        // Validació de la Contrasenya
        if (assistent.getContrasenya() == null || assistent.getContrasenya().trim().isEmpty()) {
            errors.rejectValue("contrasenya", "obligatori", "Cal introduir una contrasenya");
        } else if (assistent.getContrasenya().length() < 6) {
            errors.rejectValue("contrasenya", "longitud", "La contrasenya ha de tindre almenys 6 caràcters");
        }
    }
}