package es.uji.ei1027.proyecto.model;

public class Pueblo {

    private String nombre; // Clave primaria en la BD
    private String codpos;

    public Pueblo() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodpos() {
        return codpos;
    }

    public void setCodpos(String codpos) {
        this.codpos = codpos;
    }
}