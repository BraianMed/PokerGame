package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.List;

public class JuegoPoker {
    private final List<Jugador> jugadores;
    private Bote bote;
    private final Baraja baraja;
    private List<Carta> cartasComunitarias;
    private int etapa; // 0: primeras 3 cartas comunitarias, 1: ultimas dos cartas comunitarias

    public JuegoPoker(List<Jugador> jugadores){
        this.jugadores = jugadores;
        this.baraja = new Baraja();
        this.etapa = 0;
        this.bote = new Bote();
        cartasComunitarias = new ArrayList<>();
    }
    public void ingresarJugadores(){

    }
    public void repartirCartas(){
        for (Jugador jugador : jugadores){
            baraja.repartirCarta(jugador);
        }
    }
    public void repartirFichas(int cantidad){
        for (Jugador jugador : jugadores){
            jugador.recibirFichas(cantidad);
        }
    }
    public void iniciarJuego(int fichasIniciales){
        repartirCartas();
        repartirFichas(fichasIniciales);
    }
    public void reiniciarJuego(int fichasIniciales){
        for (Jugador jugador : jugadores){
            jugador.reiniciame();
        }
        iniciarJuego(fichasIniciales);
    }
    public void repartirComunitarias(){
        if (etapa == 0){
            for (int i = 0; i < 3; i++) {
                baraja.repartirCarta(cartasComunitarias);
            }
        }
        else if (etapa == 1){
            for (int i = 0; i < 2; i++) {
                baraja.repartirCarta(cartasComunitarias);
            }
        }
    }
    public Jugador determinarGanador(){
        return null;
    }
    public void rondaDeApuestas(){

    }
    public Baraja getBaraja() {
        return baraja;
    }

    public int getEtapa() {
        return etapa;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }
}
