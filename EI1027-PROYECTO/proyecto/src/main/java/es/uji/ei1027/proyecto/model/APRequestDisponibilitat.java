package es.uji.ei1027.proyecto.model;

import java.time.LocalTime;

public class APRequestDisponibilitat {
    private int idRequest;
    private String diaSetmana;
    private LocalTime horaInici;
    private LocalTime horaFi;

    public APRequestDisponibilitat() {}

    public int getIdRequest() { return idRequest; }
    public void setIdRequest(int idRequest) { this.idRequest = idRequest; }

    public String getDiaSetmana() { return diaSetmana; }
    public void setDiaSetmana(String diaSetmana) { this.diaSetmana = diaSetmana; }

    public LocalTime getHoraInici() { return horaInici; }
    public void setHoraInici(LocalTime horaInici) { this.horaInici = horaInici; }

    public LocalTime getHoraFi() { return horaFi; }
    public void setHoraFi(LocalTime horaFi) { this.horaFi = horaFi; }
}