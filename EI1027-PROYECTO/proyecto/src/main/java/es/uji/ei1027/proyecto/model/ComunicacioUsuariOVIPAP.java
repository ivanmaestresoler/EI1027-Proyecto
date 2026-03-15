package es.uji.ei1027.proyecto.model;

import java.time.LocalDateTime;

public class ComunicacioUsuariOVIPAP {
    private int idComunicacio;
    private int idSeleccion;
    private String missatge;
    private String remitent;
    private LocalDateTime dataEnviament;

    public int getIdComunicacio() { return idComunicacio; }
    public void setIdComunicacio(int idComunicacio) { this.idComunicacio = idComunicacio; }

    public int getIdSeleccion() { return idSeleccion; }
    public void setIdSeleccion(int idSeleccion) { this.idSeleccion = idSeleccion; }

    public String getMissatge() { return missatge; }
    public void setMissatge(String missatge) { this.missatge = missatge; }

    public String getRemitent() { return remitent; }
    public void setRemitent(String remitent) { this.remitent = remitent; }

    public LocalDateTime getDataEnviament() { return dataEnviament; }
    public void setDataEnviament(LocalDateTime dataEnviament) { this.dataEnviament = dataEnviament; }
}