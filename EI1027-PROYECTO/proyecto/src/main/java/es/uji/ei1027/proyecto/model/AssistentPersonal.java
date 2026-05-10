package es.uji.ei1027.proyecto.model;

import java.util.List;

public class AssistentPersonal extends Usuario {
    private int idUsuario;
    private String formacioAcademica;
    private String disponibilitat;
    private String cv;
    private String tipus;
    private String estatAcceptat;
    private boolean actiu;
    private List<String> tipusAssistenciaSeleccionats;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

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

    public boolean isActiu() { return actiu; }
    public void setActiu(boolean actiu) { this.actiu = actiu; }

    public List<String> getTipusAssistenciaSeleccionats() { return tipusAssistenciaSeleccionats; }
    public void setTipusAssistenciaSeleccionats(List<String> tipusAssistenciaSeleccionats) { this.tipusAssistenciaSeleccionats = tipusAssistenciaSeleccionats; }
}