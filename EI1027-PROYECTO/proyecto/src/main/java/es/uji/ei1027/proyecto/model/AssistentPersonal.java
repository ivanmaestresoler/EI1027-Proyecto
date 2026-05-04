package es.uji.ei1027.proyecto.model;

import java.util.List;

public class AssistentPersonal extends Usuario {

    private String formacioAcademica;
    private String disponibilitat;
    private String cv;
    private String tipus;
    private String estatAcceptat = "Candidat";
    private Boolean actiu = true;
    private List<Idioma> idiomas;
    private List<AssistentDisponibilitat> disponibilitats;

    public AssistentPersonal() {
    }

    public String getFormacioAcademica() { return formacioAcademica; }
    public void setFormacioAcademica(String formacioAcademica) { this.formacioAcademica = formacioAcademica; }

    public String getDisponibilitat() { return disponibilitat; }
    public void setDisponibilitat(String disponibilitat) { this.disponibilitat = disponibilitat; }

    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public String getEstatAcceptat() { return estatAcceptat; }
    public void setEstatAcceptat(String estatAcceptat) { this.estatAcceptat = estatAcceptat; }

    public Boolean getActiu() { return actiu; }
    public void setActiu(Boolean actiu) { this.actiu = actiu; }

    public List<Idioma> getIdiomas() { return idiomas; }
    public void setIdiomas(List<Idioma> idiomas) { this.idiomas = idiomas; }

    public List<AssistentDisponibilitat> getDisponibilitats() { return disponibilitats; }
    public void setDisponibilitats(List<AssistentDisponibilitat> disponibilitats) { this.disponibilitats = disponibilitats; }

    @Override
    public String toString() {
        return "AssistentPersonal{idUsuario=" + getIdUsuario() + ", nom='" + getNom() + "'}";
    }
}