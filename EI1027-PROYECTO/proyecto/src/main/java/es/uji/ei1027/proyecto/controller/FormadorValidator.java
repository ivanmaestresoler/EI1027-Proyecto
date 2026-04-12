package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.Formador;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormadorValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Formador.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Formador formador = (Formador) obj;

        if (formador.getNom() == null || formador.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "obligatori", "Cal introduir el nom");
        } else if (formador.getNom().length() < 2 || formador.getNom().length() > 50) {
            errors.rejectValue("nom", "longitud", "El nom ha de tindre entre 2 i 50 caràcters");
        }

        if (formador.getCognom1() == null || formador.getCognom1().trim().isEmpty()) {
            errors.rejectValue("cognom1", "obligatori", "Cal introduir el primer cognom");
        } else if (formador.getCognom1().length() < 2 || formador.getCognom1().length() > 50) {
            errors.rejectValue("cognom1", "longitud", "El primer cognom ha de tindre entre 2 i 50 caràcters");
        }

        if (formador.getCognom2() != null && !formador.getCognom2().trim().isEmpty()) {
            if (formador.getCognom2().length() > 50) {
                errors.rejectValue("cognom2", "longitud", "El segon cognom no pot superar els 50 caràcters");
            }
        }

        if (formador.getEmailContacte() == null || formador.getEmailContacte().trim().isEmpty()) {
            errors.rejectValue("emailContacte", "obligatori", "Cal introduir un email de contacte");
        } else if (!formador.getEmailContacte().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errors.rejectValue("emailContacte", "format", "El format de l'email no és correcte (ex: nom@domini.com)");
        } else if (formador.getEmailContacte().length() > 100) {
            errors.rejectValue("emailContacte", "longitud", "L'email no pot superar els 100 caràcters");
        }

        if (formador.getEspecialitat() == null || formador.getEspecialitat().trim().isEmpty()) {
            errors.rejectValue("especialitat", "obligatori", "Cal introduir una especialitat");
        } else if (formador.getEspecialitat().length() < 3 || formador.getEspecialitat().length() > 100) {
            errors.rejectValue("especialitat", "longitud", "L'especialitat ha de tindre entre 3 i 100 caràcters");
        }
    }
}