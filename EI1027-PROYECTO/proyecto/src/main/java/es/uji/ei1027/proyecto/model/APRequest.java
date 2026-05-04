package es.uji.ei1027.proyecto.model;

import java.time.LocalDate;
import java.util.List;

public class APRequest {

    private Integer idRequest;
    private Integer idUsuari;
    private Integer idIdioma;
    private LocalDate dataSollicitud;
    private String experienciaPrevia;
    private String formacioAcademica;
    private String genereAssistent;
    private String localitat;
    private String estatRequest = "En revisió";
    private String tipusAssistencia;
    private List<APRequestDisponibilitat> horariosSolicitados;

    public APRequest() {
    }

    // --- GETTERS Y SETTERS ---

    public Integer getIdRequest() { return idRequest; }
    public void setIdRequest(Integer idRequest) { this.idRequest = idRequest; }

    public Integer getIdUsuari() { return idUsuari; }
    public void setIdUsuari(Integer idUsuari) { this.idUsuari = idUsuari; }

    public Integer getIdIdioma() { return idIdioma; }
    public void setIdIdioma(Integer idIdioma) { this.idIdioma = idIdioma; }

    public LocalDate getDataSollicitud() { return dataSollicitud; }
    public void setDataSollicitud(LocalDate dataSollicitud) { this.dataSollicitud = dataSollicitud; }

    public String getExperienciaPrevia() { return experienciaPrevia; }
    public void setExperienciaPrevia(String experienciaPrevia) { this.experienciaPrevia = experienciaPrevia; }

    public String getFormacioAcademica() { return formacioAcademica; }
    public void setFormacioAcademica(String formacioAcademica) { this.formacioAcademica = formacioAcademica; }

    public String getGenereAssistent() { return genereAssistent; }
    public void setGenereAssistent(String genereAssistent) { this.genereAssistent = genereAssistent; }

    public String getLocalitat() { return localitat; }
    public void setLocalitat(String localitat) { this.localitat = localitat; }

    public String getEstatRequest() { return estatRequest; }
    public void setEstatRequest(String estatRequest) { this.estatRequest = estatRequest; }

    public String getTipusAssistencia() { return tipusAssistencia; }
    public void setTipusAssistencia(String tipusAssistencia) { this.tipusAssistencia = tipusAssistencia; }

    public List<APRequestDisponibilitat> getHorariosSolicitados() { return horariosSolicitados; }
    public void setHorariosSolicitados(List<APRequestDisponibilitat> horariosSolicitados) { this.horariosSolicitados = horariosSolicitados; }

}