package es.uji.ei1027.proyecto.model;

public class UsuariOVI extends Usuario {

    private String plaVida;
    private String tipusAssistencia;
    private Boolean consentimentLOPD = false;
    private String estatUsuari = "Pendent";

    public UsuariOVI() {
    }

    public String getPlaVida() { return plaVida; }
    public void setPlaVida(String plaVida) { this.plaVida = plaVida; }

    public String getTipusAssistencia() { return tipusAssistencia; }
    public void setTipusAssistencia(String tipusAssistencia) { this.tipusAssistencia = tipusAssistencia; }

    public Boolean getConsentimentLOPD() { return consentimentLOPD; }
    public void setConsentimentLOPD(Boolean consentimentLOPD) { this.consentimentLOPD = consentimentLOPD; }

    public String getEstatUsuari() { return estatUsuari; }
    public void setEstatUsuari(String estatUsuari) { this.estatUsuari = estatUsuari; }
}