package es.uji.ei1027.proyecto.model;

public class AssistenciaFormacio {

    private Integer idAssistencia;
    private Integer idActivitat;
    private Integer idUsuari;
    private Integer idAssistent;
    private Boolean inscripcioPrevia;
    private Boolean haAssistit;
    private Boolean certificatEmes;

    public AssistenciaFormacio() {
    }

    public Integer getIdAssistencia() { return idAssistencia; }
    public void setIdAssistencia(Integer idAssistencia) { this.idAssistencia = idAssistencia; }

    public Integer getIdActivitat() { return idActivitat; }
    public void setIdActivitat(Integer idActivitat) { this.idActivitat = idActivitat; }

    public Integer getIdUsuari() { return idUsuari; }
    public void setIdUsuari(Integer idUsuari) { this.idUsuari = idUsuari; }

    public Integer getIdAssistent() { return idAssistent; }
    public void setIdAssistent(Integer idAssistent) { this.idAssistent = idAssistent; }

    public Boolean getInscripcioPrevia() { return inscripcioPrevia; }
    public void setInscripcioPrevia(Boolean inscripcioPrevia) { this.inscripcioPrevia = inscripcioPrevia; }

    public Boolean getHaAssistit() { return haAssistit; }
    public void setHaAssistit(Boolean haAssistit) { this.haAssistit = haAssistit; }

    public Boolean getCertificatEmes() { return certificatEmes; }
    public void setCertificatEmes(Boolean certificatEmes) { this.certificatEmes = certificatEmes; }
}