package es.uji.ei1027.proyecto.model;

public class AssistentPersonal {

    // Clave Primaria Real
    private int idAssistent;

    // Campos obligatorios (NOT NULL en BD)
    private String dni;
    private String nom;
    private String cognom1;
    private String email;
    private String contrasenya;
    private String tipus;
    private String estatAcceptat;

    // Campos opcionales / con valores por defecto
    private String cognom2;
    private String telefon;
    private String formacioAcademica;
    private String experienciaPrevia;
    private String disponibilitat;
    private String proximitatGeografica;

    private boolean actiu;

    public AssistentPersonal() {}

    // --- GETTERS Y SETTERS ---

    public int getIdAssistent() { return idAssistent; }
    public void setIdAssistent(int idAssistent) { this.idAssistent = idAssistent; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom1() { return cognom1; }
    public void setCognom1(String cognom1) { this.cognom1 = cognom1; }

    public String getCognom2() { return cognom2; }
    public void setCognom2(String cognom2) { this.cognom2 = cognom2; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getFormacioAcademica() { return formacioAcademica; }
    public void setFormacioAcademica(String formacioAcademica) { this.formacioAcademica = formacioAcademica; }

    public String getExperienciaPrevia() { return experienciaPrevia; }
    public void setExperienciaPrevia(String experienciaPrevia) { this.experienciaPrevia = experienciaPrevia; }

    public String getDisponibilitat() { return disponibilitat; }
    public void setDisponibilitat(String disponibilitat) { this.disponibilitat = disponibilitat; }

    public String getProximitatGeografica() { return proximitatGeografica; }
    public void setProximitatGeografica(String proximitatGeografica) { this.proximitatGeografica = proximitatGeografica; }

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }


    public boolean isActiu() { return actiu; }
    public void setActiu(boolean actiu) { this.actiu = actiu; }

    @Override
    public String toString() {
        return "AssistentPersonal{id=" + idAssistent + ", dni='" + dni + "', nom='" + nom + "'}";
    }
    public String getEstatAcceptat() {
        return estatAcceptat;
    }

    public void setEstatAcceptat(String estatAcceptat) {
        this.estatAcceptat = estatAcceptat;
    }
}