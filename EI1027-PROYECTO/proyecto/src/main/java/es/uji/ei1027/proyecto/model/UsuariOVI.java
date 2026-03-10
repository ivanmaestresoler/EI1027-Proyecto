package es.uji.ei1027.proyecto.model;

public class UsuariOVI {

    private Integer idUsuari;
    private String dni;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String telefon;
    private String email;
    private String direccio;
    private String contrasenyaInicial;

    private Boolean consentimentLOPD = false;
    private String acceptatPerTecnic = "Pendent"; // Utiliza el enumerado estatUsuari ('Pendent', 'Acceptat', 'Rebutjat')

    public UsuariOVI() {
    }

    // GETTERS Y SETTERS

    public Integer getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(Integer idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getContrasenyaInicial() {
        return contrasenyaInicial;
    }

    public void setContrasenyaInicial(String contrasenyaInicial) {
        this.contrasenyaInicial = contrasenyaInicial;
    }

    public Boolean getConsentimentLOPD() {
        return consentimentLOPD;
    }

    public void setConsentimentLOPD(Boolean consentimentLOPD) {
        this.consentimentLOPD = consentimentLOPD;
    }

    public String getAcceptatPerTecnic() {
        return acceptatPerTecnic;
    }

    public void setAcceptatPerTecnic(String acceptatPerTecnic) {
        this.acceptatPerTecnic = acceptatPerTecnic;
    }

}
