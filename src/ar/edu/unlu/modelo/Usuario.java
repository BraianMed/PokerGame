package ar.edu.unlu.modelo;

import java.io.Serial;
import java.io.Serializable;

public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = -8541698973154465927L;
    private String nombre;
    private int victorias;
    private int derrotas;


    public Usuario(String nombre){
        this.nombre = nombre;
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
    public void sumarVictorias(){
        this.victorias++;
    }
    public void sumarDerrotas(){
        this.derrotas++;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
