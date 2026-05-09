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

        if (req.getLocalitat() == null || req.getLocalitat().trim().isEmpty()) {
            errors.rejectValue("localitat", "obligatori", "Cal introduir la localitat");
        }

        if (req.getTipusAssistencia() == null || req.getTipusAssistencia().trim().isEmpty()) {
            errors.rejectValue("tipusAssistencia", "obligatori", "Cal introduir el tipus d'assistència");
        }
    }
}