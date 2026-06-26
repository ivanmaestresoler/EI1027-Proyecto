package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.model.RegistreContracte;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.time.LocalDate;

@Component
public class RegistreContracteValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistreContracte.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        RegistreContracte contracte = (RegistreContracte) obj;

        if (contracte.getIdRequest() == 0) {
            errors.rejectValue("idRequest", "obligatori",
                    "Cal indicar la sol·licitud associada al contracte.");
        }

        if (contracte.getIdAssistent() == 0) {
            errors.rejectValue("idAssistent", "obligatori",
                    "Cal indicar l'assistent associat al contracte.");
        }

        if (contracte.getDataInici() == null) {
            errors.rejectValue("dataInici", "obligatori",
                    "Cal introduir la data d'inici del contracte.");
        } else if (contracte.getDataInici().isBefore(LocalDate.now())) {
            errors.rejectValue("dataInici", "dataInvalida",
                    "La data d'inici no pot ser anterior a la data d'avui.");
        }

        if (contracte.getDataInici() != null && contracte.getDataFi() != null) {
            if (contracte.getDataFi().isBefore(contracte.getDataInici())) {
                errors.rejectValue("dataFi", "dataInvalida",
                        "La data de fi no pot ser anterior a la data d'inici.");
            }
        }
    }
}