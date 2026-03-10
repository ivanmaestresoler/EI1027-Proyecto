package es.uji.ei1027.proyecto.model;

import java.time.LocalDate;

public class APRequest {

    private Integer idRequest;
    private Integer idUsuari; // Cambiado a Integer
    private LocalDate dataSollicitud;
    private String preferencies;

    private String estatRequest = "En Revisió";
    private String tipusAssistencia;

    public APRequest() {
    }

    public Integer getIdRequest() {
        return idRequest;
    }
    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public Integer getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Integer idUsuari) {
        this.idUsuari = idUsuari;
    }

    public LocalDate getDataSollicitud() {
        return dataSollicitud;
    }

    public void setDataSollicitud(LocalDate dataSollicitud) {
        this.dataSollicitud = dataSollicitud;
    }

    public String getPreferencies() {
        return preferencies;
    }

    public void setPreferencies(String preferencies) {
        this.preferencies = preferencies;
    }

    public String getEstatRequest() {
        return estatRequest;
    }

    public void setEstatRequest(String estatRequest) {
        this.estatRequest = estatRequest;
    }

    public String getTipusAssistencia() {
        return tipusAssistencia;
    }

    public void setTipusAssistencia(String tipusAssistencia) {
        this.tipusAssistencia = tipusAssistencia;
    }
}