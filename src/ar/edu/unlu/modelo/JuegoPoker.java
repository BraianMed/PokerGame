package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JuegoPoker {
    private final List<Jugador> jugadores;
    private Bote bote;
    private final Baraja baraja;

    public JuegoPoker(List<Jugador> jugadores){
        this.jugadores = jugadores;
//        for (int i = 0; i < jugadores.size(); i++) {
//            jugadores.get(i).setNumeroJugador(i);
//        }
        this.baraja = new Baraja();
        this.bote = new Bote();
    }

    public void ingresarJugadores(){

    }

    public void repartirCartas(){
        for (Jugador jugador : jugadores){
            baraja.repartirCarta(jugador);
        }
    }

    public void repartirFichas(ArrayList<Ficha> fichasIniciales){
        for (Jugador jugador : jugadores){
            for (Ficha ficha : fichasIniciales){
                jugador.recibirFichas(ficha);
            }
        }
    }

    public void iniciarJuego(ArrayList<Ficha> fichasIniciales){
        if(jugadores.size() >= 2 && jugadores.size() <= 6){
            repartirCartas();
            repartirFichas(fichasIniciales);
        }
    }

    public void reiniciarJuego(ArrayList<Ficha> fichasIniciales){
        for (Jugador jugador : jugadores){
            jugador.reiniciame();
        }
        iniciarJuego(fichasIniciales);
    }

    public Jugador determinarGanador(){
        for (int i = 0; i < jugadores.size(); i++) {

        }
        return null;
    }

    public void rondaDeApuestas(){
        Iterator<Jugador> iterador = jugadores.iterator();
        while(iterador.hasNext()){
            Jugador j = iterador.next();

        }
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}
