package es.uji.ei1027.proyecto.model;

import java.time.LocalDateTime;

public class ComunicacioUsuariOVIPAP {
    private int idComunicacio;
    private int idSeleccion;
    private String missatge;
    private int idFrom;
    private int idTo;
    private LocalDateTime dataEnviament;

    public int getIdComunicacio() { return idComunicacio; }
    public void setIdComunicacio(int idComunicacio) { this.idComunicacio = idComunicacio; }

    public int getIdSeleccion() { return idSeleccion; }
    public void setIdSeleccion(int idSeleccion) { this.idSeleccion = idSeleccion; }

    public String getMissatge() { return missatge; }
    public void setMissatge(String missatge) { this.missatge = missatge; }

    public int getIdFrom() { return idFrom; }
    public void setIdFrom(int idFrom) { this.idFrom = idFrom; }

    public int getIdTo() { return idTo; }
    public void setIdTo(int idTo) { this.idTo = idTo; }

    public LocalDateTime getDataEnviament() { return dataEnviament; }
    public void setDataEnviament(LocalDateTime dataEnviament) { this.dataEnviament = dataEnviament; }
}