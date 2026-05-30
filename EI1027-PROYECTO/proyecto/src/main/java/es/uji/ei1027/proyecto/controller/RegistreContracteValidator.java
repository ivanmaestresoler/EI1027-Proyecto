package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.RegistreContracte;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistreContracteValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistreContracte.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        RegistreContracte contracte = (RegistreContracte) obj;

        // idRequest obligatori
        if (contracte.getIdRequest() == 0) {
            errors.rejectValue("idRequest", "obligatori",
                    "Cal indicar la sol·licitud associada al contracte.");
        }

        // idAssistent obligatori
        if (contracte.getIdAssistent() == 0) {
            errors.rejectValue("idAssistent", "obligatori",
                    "Cal indicar l'assistent associat al contracte.");
        }

        // dataInici obligatoria
        if (contracte.getDataInici() == null) {
            errors.rejectValue("dataInici", "obligatori",
                    "Cal introduir la data d'inici del contracte.");
        }

        // dataFi no pot ser anterior a dataInici
        if (contracte.getDataInici() != null && contracte.getDataFi() != null) {
            if (contracte.getDataFi().isBefore(contracte.getDataInici())) {
                errors.rejectValue("dataFi", "dataInvalida",
                        "La data de fi no pot ser anterior a la data d'inici.");
            }
        }
    }
}