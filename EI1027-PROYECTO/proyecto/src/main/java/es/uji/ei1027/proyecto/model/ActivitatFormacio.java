package es.uji.ei1027.proyecto.model;

import java.time.LocalDateTime;

public class ActivitatFormacio {

    private Integer idActivitat;
    private Integer idFormador;
    private String titol;
    private String descripcio;
    private LocalDateTime dataHora;
    private String tipusActivitat;
    private Integer aforamentMaxim;

    public ActivitatFormacio() {
    }

    public Integer getIdActivitat() { return idActivitat; }
    public void setIdActivitat(Integer idActivitat) { this.idActivitat = idActivitat; }

    public Integer getIdFormador() { return idFormador; }
    public void setIdFormador(Integer idFormador) { this.idFormador = idFormador; }

    public String getTitol() { return titol; }
    public void setTitol(String titol) { this.titol = titol; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getTipusActivitat() { return tipusActivitat; }
    public void setTipusActivitat(String tipusActivitat) { this.tipusActivitat = tipusActivitat; }

    public Integer getAforamentMaxim() { return aforamentMaxim; }
    public void setAforamentMaxim(Integer aforamentMaxim) { this.aforamentMaxim = aforamentMaxim; }
}