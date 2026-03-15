package es.uji.ei1027.proyecto.model;

import java.time.LocalDate;

public class RegistreContracte {
    private int idContracte;
    private int idRequest;
    private int idAssistent;
    private LocalDate dataInici;
    private LocalDate dataFi;
    private String rutaPdf;

    public int getIdContracte() { return idContracte; }
    public void setIdContracte(int idContracte) { this.idContracte = idContracte; }

    public int getIdRequest() { return idRequest; }
    public void setIdRequest(int idRequest) { this.idRequest = idRequest; }

    public int getIdAssistent() { return idAssistent; }
    public void setIdAssistent(int idAssistent) { this.idAssistent = idAssistent; }

    public LocalDate getDataInici() { return dataInici; }
    public void setDataInici(LocalDate dataInici) { this.dataInici = dataInici; }

    public LocalDate getDataFi() { return dataFi; }
    public void setDataFi(LocalDate dataFi) { this.dataFi = dataFi; }

    public String getRutaPdf() { return rutaPdf; }
    public void setRutaPdf(String rutaPdf) { this.rutaPdf = rutaPdf; }
}