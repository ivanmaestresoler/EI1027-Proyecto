package es.uji.ei1027.proyecto.model;

public class Formador {
    
    private Integer idFormador;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String emailContacte;
    private String especialitat;

    public Formador() {
    }

    public Integer getIdFormador() { return idFormador; }
    public void setIdFormador(Integer idFormador) { this.idFormador = idFormador; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom1() { return cognom1; }
    public void setCognom1(String cognom1) { this.cognom1 = cognom1; }

    public String getCognom2() { return cognom2; }
    public void setCognom2(String cognom2) { this.cognom2 = cognom2; }

    public String getEmailContacte() { return emailContacte; }
    public void setEmailContacte(String emailContacte) { this.emailContacte = emailContacte; }

    public String getEspecialitat() { return especialitat; }
    public void setEspecialitat(String especialitat) { this.especialitat = especialitat; }
}