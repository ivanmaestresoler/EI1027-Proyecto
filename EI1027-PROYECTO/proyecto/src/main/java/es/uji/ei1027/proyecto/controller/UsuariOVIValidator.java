package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.UsuariOVI;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UsuariOVIValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return UsuariOVI.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UsuariOVI usuari = (UsuariOVI) obj;

        if (usuari.getDni() == null || usuari.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatori", "Cal introduir el DNI/NIE");
        } else if (!usuari.getDni().matches("^([0-9]{8}[A-Za-z])|([XYZxyz][0-9]{7}[A-Za-z])$")) {
            errors.rejectValue("dni", "format", "Format invàlid: Ha de ser 8 números i 1 lletra (DNI) o X/Y/Z + 7 números i lletra (NIE)");
        }

        if (usuari.getNom() == null || usuari.getNom().trim().isEmpty()) {
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

        if (usuari.getGenere() == null || usuari.getGenere().trim().isEmpty()) {
            errors.rejectValue("genere", "obligatori", "Cal seleccionar un gènere");
        }

        if (usuari.getDataNaixement() == null) {
            errors.rejectValue("dataNaixement", "obligatori", "Cal introduir la data de naixement");
        }

        if (usuari.getNombrePueblo() == null || usuari.getNombrePueblo().trim().isEmpty()) {
            errors.rejectValue("nombrePueblo", "obligatori", "Cal seleccionar una localitat/poble");
        }

        if (usuari.getContrasenya() == null || usuari.getContrasenya().trim().isEmpty()) {
            errors.rejectValue("contrasenya", "obligatori", "Cal introduir una contrasenya");
        } else if (usuari.getContrasenya().length() < 6) {
            errors.rejectValue("contrasenya", "curta", "La contrasenya ha de tindre almenys 6 caràcters");
        }

        if (usuari.getTipusAssistencia() == null || usuari.getTipusAssistencia().trim().isEmpty()) {
            errors.rejectValue("tipusAssistencia", "obligatori", "Cal seleccionar el tipus d'assistència necessària");
        }

        if (usuari.getConsentimentLOPD() == null || !usuari.getConsentimentLOPD()) {
            errors.rejectValue("consentimentLOPD", "obligatori", "Has d'acceptar el consentiment de la LOPD per registrar-te");
        }
    }
}