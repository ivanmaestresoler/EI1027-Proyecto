package es.uji.ei1027.proyecto.model;

import java.time.LocalTime;

public class AssistentDisponibilitat {
    private int idDisponibilitat;
    private int idAssistent;
    private String diaSetmana; // Enum: Dilluns, Dimarts...
    private LocalTime horaInici;
    private LocalTime horaFi;

    public AssistentDisponibilitat() {}

    public int getIdDisponibilitat() { return idDisponibilitat; }
    public void setIdDisponibilitat(int idDisponibilitat) { this.idDisponibilitat = idDisponibilitat; }

    public int getIdAssistent() { return idAssistent; }
    public void setIdAssistent(int idAssistent) { this.idAssistent = idAssistent; }

    public String getDiaSetmana() { return diaSetmana; }
    public void setDiaSetmana(String diaSetmana) { this.diaSetmana = diaSetmana; }

    public LocalTime getHoraInici() { return horaInici; }
    public void setHoraInici(LocalTime horaInici) { this.horaInici = horaInici; }

    public LocalTime getHoraFi() { return horaFi; }
    public void setHoraFi(LocalTime horaFi) { this.horaFi = horaFi; }
}