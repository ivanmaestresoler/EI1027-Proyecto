package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.APRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class APRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return APRequest.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        APRequest req = (APRequest) obj;

        if (req.getIdUsuari() == null) {
            errors.rejectValue("idUsuari", "obligatori", "Cal introduir l'ID de l'usuari que fa la petició");
        }

        if (req.getDataSollicitud() == null) {
            errors.rejectValue("dataSollicitud", "obligatori", "Cal introduir la data de la sol·licitud");
        }

        if (req.getTipusAssistencia() == null || req.getTipusAssistencia().trim().isEmpty()) {
            errors.rejectValue("tipusAssistencia", "obligatori", "Has d'escollir un tipus d'assistència");
        }
    }
}