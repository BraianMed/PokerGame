package ar.edu.unlu.modelo;

import java.util.*;

public class Jugador implements ICalcularFichas {
    private String nombre;
    private Mano mano;
    private int numeroJugador;
    private List<Ficha> fichas;
    private boolean enJuego;
    private List<Ficha> apuestaActual;

    public Jugador(List<Ficha> fichasIniciales,String nombre){
        this.nombre = nombre;
        this.fichas = fichasIniciales;
        this.enJuego = true;
        this.apuestaActual = new ArrayList<>();
        this.mano = new Mano();
    }

    public void reiniciame(){
        fichas.clear();          // Elimina todas las fichas
        apuestaActual.clear();   // Elimina todas las apuestas
        enJuego = true;          // vuelve al juego
        mano.vaciarMano();       // vacía la mano del jugador.
    }

    public void recibirCarta(Carta nuevaCarta){
        mano.getCartas().add(nuevaCarta);
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
        if (cantidad > totalFichas()){
            throw new IllegalArgumentException("Tiene cero fichas,el jugador esta fuera!");
        }
        Iterator<Ficha> iterador = fichas.iterator();
        int sumatoria = 0;
        while (iterador.hasNext() && sumatoria != cantidad) {
            Ficha f = iterador.next();
            if (f.getValor() - cantidad <= 0) {
                sumatoria += f.getValor();
                iterador.remove();
            } else {
                f.setValor(cantidad - sumatoria);
                break;
            }
        }
    }
    public void recibirFichas(Ficha ficha){
        fichas.add(ficha);
    }
    public void apostar(int cantidad,Bote bote){
        if (cantidad > totalFichas()){
            throw new IllegalArgumentException("No tienes suficiente fichas");
        }
        int sumatoria = 0;
        Iterator<Ficha> iterador = fichas.iterator();
        while (iterador.hasNext() && sumatoria != cantidad){
            Ficha f = iterador.next();
            if (f.getValor() <= cantidad){
                bote.sumarFichas(f);
                sumatoria += f.getValor();
                apuestaActual.add(f);
                iterador.remove();
            }
            else{
                bote.sumarFichas(new Ficha(cantidad - sumatoria));
                apuestaActual.add(new Ficha(cantidad-sumatoria));
                f.setValor(cantidad-sumatoria);
            }
        }
    }
    public void retirarse(){
        setEnJuego(false);
    }

    public String getNombre() {
        return nombre;
    }


    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEnJuego() {
        return enJuego;
    }

    public void setEnJuego(boolean enJuego) {
        this.enJuego = enJuego;
    }

    public void setNumeroJugador(int numeroJugador) {
        this.numeroJugador = numeroJugador;
    }

    public int getNumeroJugador() {
        return numeroJugador;
    }

    public Mano getMano() {
        return mano;
    }

    public static void main(String[] args) {
        Ficha f1 = new Ficha(100);
        Ficha f2 = new Ficha(100);
        Ficha f3 = new Ficha(100);
        Ficha f4 = new Ficha(100);
        Ficha f5 = new Ficha(100);
        List<Ficha> fichas = new ArrayList<>();
        fichas.add(f1);
        fichas.add(f2);
        fichas.add(f3);
        fichas.add(f4);
        fichas.add(f5);
        Jugador jugador = new Jugador(fichas,"Pedro");
        jugador.reiniciame();
    }
}
