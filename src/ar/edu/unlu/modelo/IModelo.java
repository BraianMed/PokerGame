package ar.edu.unlu.modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IModelo extends IObservableRemoto {
    void iniciarJuego() throws RemoteException;

    void moverRepartidor() throws RemoteException;

    boolean asignarCiegas() throws RemoteException;

    void errorCiega() throws RemoteException;

    boolean jugadoresIsEmpty() throws RemoteException;

    void repartirCartas() throws RemoteException;

    String nombreJugadorActual() throws RemoteException;

    ArrayList<String> cartasTurnoActual() throws RemoteException;

    void repartirFichas() throws RemoteException;

    void descartarJugador(ArrayList<Integer> indices) throws RemoteException;

    void gestionVuelta() throws RemoteException;

    int cantJugadoresEnJuego() throws RemoteException;

    void reiniciarJuego() throws RemoteException;

    Jugador determinarGanador() throws RemoteException;

    void resultados() throws RemoteException;

    void sumarVictorias() throws RemoteException;

    void sumarDerrotas() throws RemoteException;

    Mano manoGanadora() throws RemoteException;

    void igualarJugador() throws RemoteException;

    Jugador manejarTurnos() throws RemoteException;

    void retirarJugador() throws RemoteException;

    void apostarJugador(int cantidad) throws IllegalArgumentException, RemoteException;

    Jugador agregarJugador(String nombre) throws RemoteException;

    void agregarJugadores(ArrayList<String> nombres) throws RemoteException;

    void configurarJuego() throws RemoteException;

    List<Jugador> getJugadores() throws RemoteException;

    void agregarFicha(int valor) throws RemoteException;

    void valorFichas() throws RemoteException;

    void agregarFichas(ArrayList<Integer> valores) throws RemoteException;

    void inicializarCiegas(int ciegaChica, int ciegaGrande) throws RemoteException;

    int siguienteTurno() throws RemoteException;

    int getCiegaGrande() throws RemoteException;

    int getTurno() throws RemoteException;

    void verificarJugadoresListos() throws RemoteException;

    int getApuestaActual() throws RemoteException;

    Jugador getJugadorTurno() throws RemoteException;

    void setCantFichas(int cantFichas) throws RemoteException;

    Jugador getAnfitrion() throws RemoteException;

    void setCantidadJugadores(int cantidadJugadores) throws RemoteException;

    int getJugadoresRegistrados() throws RemoteException;

    int totalApostadoBote() throws RemoteException;

    boolean isError() throws RemoteException;

    void setError(boolean error) throws RemoteException;
}
