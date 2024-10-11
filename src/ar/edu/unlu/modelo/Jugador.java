package ar.edu.unlu.modelo;

import com.sun.nio.sctp.IllegalReceiveException;

import java.util.*;

public class Jugador{
    private String nombre;
    private List<Ficha> fichas;
    private List<Carta> mano;

    public Jugador(List<Ficha> fichasIniciales,String nombre){
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.fichas = fichasIniciales;
    }
    public void recibirCarta(Carta nuevaCarta){
            this.mano.add(nuevaCarta);
    }
    public int totalFichas(){
        int sumatoria = 0;
        for(Ficha ficha : fichas){
            sumatoria += ficha.getValor();
        }
        return sumatoria;
    }
    public void restarFichas(int cantidad) {
        // usar un iterador para que me permita eliminar un elemento en medio del ciclo
        Iterator<Ficha> iterador = fichas.iterator();
        while (iterador.hasNext()) {
            Ficha f = iterador.next();
            if (f.getValor() - cantidad <= 0) {
                iterador.remove();
            } else {
                f.setValor(f.getValor() - cantidad);
                break;
            }
        }
    }
    public void recibirFichas(int cantidad){
        fichas.getFirst().setValor(fichas.getFirst().getValor() + cantidad);
    }
    public void apostar(int cantidad,Bote bote){
        if (cantidad > totalFichas()){
            throw new IllegalArgumentException("No tienes suficiente fichas");
        }
        Iterator<Ficha> iterador = fichas.iterator();
        while (iterador.hasNext()){
            Ficha f = iterador.next();
            if (f.getValor() <= cantidad){
                bote.sumarFichas(f);
                iterador.remove();
            }
            else{
                bote.sumarFichas(f);
                f.setValor(f.getValor()-cantidad);
            }
        }
    }
    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
