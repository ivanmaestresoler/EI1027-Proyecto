package es.uji.ei1027.proyecto.model;

import java.time.LocalDateTime;

public class Seleccion {
    private int idSeleccion;
    private int idRequest;
    private int idAssistent;
    private LocalDateTime dataProposta;

    public int getIdSeleccion() { return idSeleccion; }
    public void setIdSeleccion(int idSeleccion) { this.idSeleccion = idSeleccion; }

    public int getIdRequest() { return idRequest; }
    public void setIdRequest(int idRequest) { this.idRequest = idRequest; }

    public int getIdAssistent() { return idAssistent; }
    public void setIdAssistent(int idAssistent) { this.idAssistent = idAssistent; }

    public LocalDateTime getDataProposta() { return dataProposta; }
    public void setDataProposta(LocalDateTime dataProposta) { this.dataProposta = dataProposta; }
}
