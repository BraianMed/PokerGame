package ar.edu.unlu.modelo;

public class Usuario {
    private String nombre;
    private int victorias;
    private int derrotas;

    public Usuario(String nombre,int victorias,int derrotas){
        this.nombre = nombre;
        this.victorias = victorias;
        this.derrotas = derrotas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
