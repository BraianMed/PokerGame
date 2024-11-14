package ar.edu.unlu.modelo;


import ar.edu.unlu.controlador.IObservador;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JuegoPoker implements IObservable{
    private List<Jugador> jugadores;
    private Bote bote;
    private final Baraja baraja;
    private Ficha ciegaChica;
    private Ficha ciegaGrande;
    private int posRepartidor;
    private ArrayList<Ficha> fichasIniciales;
    private ArrayList<IObservador> observadores;
    private int apuestaActual;
    private int turno;

    public JuegoPoker(){
        this.jugadores = new ArrayList<>();
        this.baraja = new Baraja();
        this.bote = new Bote();
        this.posRepartidor = 0;
        this.fichasIniciales = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.turno = 0;
        this.apuestaActual = 0;
    }
    public void moverRepartidor(){
        // hacemos módulo por el tamaño de la lista de jugadores para que cuando llegue al final vuelva al inicio de la lista como una cola circular
        // el resto de la división dara como resultado un número entre 0 y la cantidad de jugadores (perfecto para el índice)
        this.posRepartidor = (this.posRepartidor + 1) % jugadores.size();
    }

    public boolean asignarCiegas(){
        if (!jugadores.isEmpty()){
            // el primero a la izquierda del repartidor es el que inicia con la ciega chica
            int posCiegaChica = (this.posRepartidor + 1) % jugadores.size();
            // el segundo a la izquierda del repartidor termina con la ciega grande
            int posCiegaGrande = (this.posRepartidor + 2) % jugadores.size();

            // obtengo los jugadores de las respectivas ciegas
            Jugador jugadorCiegaChica = jugadores.get(posCiegaChica);
            Jugador jugadorCiegaGrande = jugadores.get(posCiegaGrande);

            // cada jugador apuesta la ciega correspondiente.
            jugadorCiegaChica.apostar(ciegaChica.getValor(),bote);
            jugadorCiegaGrande.apostar(ciegaGrande.getValor(),bote);

            // con la información que tengo ya puedo definir el turno del jugador y la apuesta actual.
            this.turno = (posRepartidor + 3) % jugadores.size();
            this.apuestaActual = ciegaGrande.getValor();

            notificar(Evento.VALOR_CIEGAS);

            return true;
        }
        return false;
    }

    public boolean jugadoresIsEmpty(){
        return jugadores.isEmpty();
    }

    public boolean repartirCartas(){
        if (!jugadores.isEmpty()){
            for (Jugador jugador : jugadores){
                baraja.repartirCarta(jugador);
            }
            notificar(Evento.REPARTIR_CARTAS);
            return true;
        }
        return false;
    }

    public String nombreJugadorActual(){
        return manejarTurnos().getNombre();
    }
    public ArrayList<String> cartasTurnoActual(){
        return manejarTurnos().devolverCartas();
    }
    public void repartirFichas(){
        for (Jugador jugador : jugadores){
            jugador.setFichas(this.fichasIniciales);
        }
        notificar(Evento.FICHAS_REPARTIDAS);
    }

//    public void iniciarJuego(){
//        if (!jugadores.isEmpty()){
//            if(jugadores.size() >= 2 && jugadores.size() <= 6){
//                repartirFichas();
//                asignarCiegas();
//                moverRepartidor();
//                repartirCartas();
//            }
//        }
//    }

    public void descartarJugador(ArrayList<Integer> indices){
        jugadores.get(getTurno()).descartar(indices);
    }

    public int cantJugadoresEnJuego(){
        int contador = 0;
        for (Jugador jugador : jugadores){
            if (jugador.isEnJuego()){
                contador++;
            }
        }
        return contador;
    }
    public void reiniciarJuego(){
        for (Jugador jugador : jugadores){
            jugador.reiniciame();
        }
    }

    public Jugador determinarGanador(){
        Mano ganadorMano = manoGanadora();
        for (Jugador j : jugadores){
            if (ganadorMano.equals(j.getMano())){
                return j;
            }
        }
        return null;
    }
    public void sumarVictorias(){
        Jugador ganador = determinarGanador();
        for (Jugador jugador : jugadores){
            if (jugador.equals(ganador)){
                jugador.sumarVictorias();
            }
        }
    }
    public void sumarDerrotas(){
        Jugador ganador = determinarGanador();
        for (Jugador jugador : jugadores){
            if (!jugador.equals(ganador)){
                jugador.sumarDerrotas();
            }
        }
    }
    public Mano manoGanadora(){
        Mano resultado = new Mano();
        Iterator<Jugador> iterator = jugadores.iterator();
        Jugador anterior = iterator.next();
        while (iterator.hasNext()){
            Jugador j = iterator.next();
            resultado = j.getMano().evaluarMano(anterior.getMano());
            anterior = j;
        }
        return resultado;
    }


    public void igualarJugador(){
        jugadores.get(getTurno()).apostar(apuestaActual,bote);
    }
    public Jugador manejarTurnos(){
        return jugadores.get(getTurno());
    }
    public void retirarJugador(){
        jugadores.get(getTurno()).retirarse();
    }

    public void apostarJugador(int cantidad){
        jugadores.get(getTurno()).apostar(cantidad,bote);
        this.apuestaActual = cantidad;
    }

    public void agregarJugador(String nombre){
        jugadores.add(new Jugador(nombre));
    }

    public void agregarJugadores(ArrayList<String> nombres){
        for (String nombre : nombres){
            agregarJugador(nombre);
        }
        notificar(Evento.NOMBRE_JUGADOR);
    }

    @Override
    public void agregarObservador(IObservador observador) {
        observadores.add(observador);
    }

    @Override
    public void eliminarObservador(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(Object o) {
        for (IObservador observador: observadores){
            observador.actualizar(o);
        }
    }

    public Baraja getBaraja() {
        return baraja;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void agregarFicha(int valor){
        this.fichasIniciales.add(new Ficha(valor));
    }
    public void agregarFichas(ArrayList<Integer> valores){
        for (Integer valorFicha : valores){
            agregarFicha(valorFicha);
        }
        notificar(Evento.FICHAS_INICIALES);
    }
    public void inicializarCiegas(int ciegaChica,int ciegaGrande){
        this.ciegaChica = new Ficha(ciegaChica);
        this.ciegaGrande = new Ficha(ciegaGrande);
    }



    public int siguienteTurno(){
        this.turno = (this.turno + 1) % jugadores.size();
        return this.turno;
    }

    public void setCiegaChica(int valor) {
        this.ciegaChica = new Ficha(valor);
    }
    public void setCiegaGrande(int valor){
        this.ciegaGrande = new Ficha(valor);
    }

    public int getCiegaGrande() {
        return ciegaGrande.getValor();
    }

    public int getPosRepartidor() {
        return posRepartidor;
    }

    public int getTurno() {
        if (jugadores.isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la lista.");
        }

        Jugador actual;
        do {
            actual = jugadores.get(turno);
            if (!actual.isEnJuego()) {
                siguienteTurno();
            }
        } while (!actual.isEnJuego());

        return turno;
    }


    public int getApuestaActual() {
        return apuestaActual;
    }

    public Jugador getJugadorTurno() {
        return jugadores.get(turno);
    }
}
