package es.uji.ei1027.proyecto.model;

import java.time.LocalDate;

public class Usuario {

    private int idUsuario;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String dni;
    private String email;
    private String contrasenya;
    private String genere;
    private LocalDate dataNaixement;
    private String tipusUsuari;
    private String telefon;
    private String nombrePueblo;
    private String direccio;

    public Usuario() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom1() { return cognom1; }
    public void setCognom1(String cognom1) { this.cognom1 = cognom1; }

    public String getCognom2() { return cognom2; }
    public void setCognom2(String cognom2) { this.cognom2 = cognom2; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }

    public LocalDate getDataNaixement() { return dataNaixement; }
    public void setDataNaixement(LocalDate dataNaixement) { this.dataNaixement = dataNaixement; }

    public String getTipusUsuari() { return tipusUsuari; }
    public void setTipusUsuari(String tipusUsuari) { this.tipusUsuari = tipusUsuari; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getNombrePueblo() { return nombrePueblo; }
    public void setNombrePueblo(String nombrePueblo) { this.nombrePueblo = nombrePueblo; }

    public String getDireccio() { return direccio; }
    public void setDireccio(String direccio) { this.direccio = direccio; }
}