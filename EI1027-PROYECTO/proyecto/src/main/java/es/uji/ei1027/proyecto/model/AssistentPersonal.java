package es.uji.ei1027.proyecto.model;

public class AssistentPersonal {
    private String dni;
    private String nom;
    private String cognom1;
    private String email;
    private String tipus;
    private String estat_acceptat;

    public AssistentPersonal() {}

    // Getters y Setters (obligatorios para que Thymeleaf lea los datos)
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognom1() { return cognom1; }
    public void setCognom1(String cognom1) { this.cognom1 = cognom1; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }
    public String getEstat_acceptat() { return estat_acceptat; }
    public void setEstat_acceptat(String estat_acceptat) { this.estat_acceptat = estat_acceptat; }

    @Override
    public String toString() {
        return "AssistentPersonal{dni='" + dni + "', nom='" + nom + "'}";
    }
}