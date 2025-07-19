package ar.edu.unlu.modelo;


import ar.edu.unlu.controlador.IObservador;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class JuegoPoker extends ObservableRemoto implements IModelo {
    private List<Jugador> jugadores;
    private int cantidadJugadores;
    private int jugadoresRegistrados;
    private boolean primeraVuelta;
    private int turnoActual;
    private Bote bote;
    private Baraja baraja;
    private Ficha ciegaChica;
    private Ficha ciegaGrande;
    private int posRepartidor;
    private ArrayList<Ficha> fichasIniciales;
//    private ArrayList<IObservador> observadores;
    private int apuestaActual;
    private int turno;
    private Jugador anfitrion;
    private int cantFichas;
    private boolean error;
    private ArrayList<String> cartasTurnoActual;
    private int turnoAnterior;
    private int accionesContadas;

    public JuegoPoker(){
        this.jugadores = new ArrayList<>();
        this.cantidadJugadores = 0;
        this.jugadoresRegistrados = 0;
        this.primeraVuelta = true;
        this.baraja = new Baraja();
        this.bote = new Bote();
        this.posRepartidor = 0;
        this.fichasIniciales = new ArrayList<>();
//        this.observadores = new ArrayList<>();
        this.turno = 0;
        this.apuestaActual = 0;
        this.error = false;
        this.cartasTurnoActual = new ArrayList<>();
        this.turnoActual = 0;
        this.turnoAnterior = 0;
        this.accionesContadas = 0;
    }

    @Override
    public void iniciarJuego() throws RemoteException {
        notificarObservadores(Evento.NOMBRE_JUGADOR);
    }

    @Override
    public void moverRepartidor(){
        // hacemos módulo por el tamaño de la lista de jugadores para que cuando llegue al final vuelva al inicio de la lista como una cola circular
        // el resto de la división dara como resultado un número entre 0 y la cantidad de jugadores (perfecto para el índice)
        this.posRepartidor = (this.posRepartidor + 1) % jugadores.size();
    }

    @Override
    public boolean asignarCiegas() throws RemoteException {
        if (!jugadores.isEmpty()){
            int posCiegaChica = 0;
            int posCiegaGrande = 0;

            if (jugadores.size() == 2) {
                posCiegaChica = posRepartidor;
                posCiegaGrande = (posRepartidor + 1) % jugadores.size();
            }
            else{
                // el primero a la izquierda del repartidor es el que inicia con la ciega chica
                posCiegaChica = (this.posRepartidor + 1) % jugadores.size();
                // el segundo a la izquierda del repartidor termina con la ciega grande
                posCiegaGrande = (this.posRepartidor + 2) % jugadores.size();
            }
            // obtengo los jugadores de las respectivas ciegas
            Jugador jugadorCiegaChica = jugadores.get(posCiegaChica);
            Jugador jugadorCiegaGrande = jugadores.get(posCiegaGrande);

            // cada jugador apuesta la ciega correspondiente.
            jugadorCiegaChica.apostar(ciegaChica.getValor(),bote);
            jugadorCiegaGrande.apostar(ciegaGrande.getValor(),bote);

            // con la información que tengo ya puedo definir el turno del jugador y la apuesta actual.
            this.turno = (posRepartidor + 3) % jugadores.size();
            jugadores.get(this.turno).setPrimerApostante(true);

            this.apuestaActual = ciegaGrande.getValor();

            notificarObservadores(Evento.REPARTIR_CARTAS);
            return true;
        }
        return false;
    }

    @Override
    public void errorCiega() throws RemoteException {
        notificarObservadores(Evento.VALOR_CIEGAS);
    }

    @Override
    public boolean jugadoresIsEmpty(){
        return jugadores.isEmpty();
    }

    @Override
    public void repartirCartas() throws RemoteException {
        if (!jugadores.isEmpty()){
            for (Jugador jugador : jugadores){
                baraja.repartirCarta(jugador);
                jugador.definirManoJugador();
            }
            notificarObservadores(Evento.MOSTRAR_CARTAS);
            System.out.println("cartas repartidas, y luego notifica a mostrar cartas");
        }
        else{
            this.error = true;
            notificarObservadores(Evento.NOMBRE_JUGADOR);
        }
    }

    @Override
    public String nombreJugadorActual(){
        return manejarTurnos().getNombre();
    }

    @Override
    public ArrayList<String> cartasTurnoActual(){
//        notificar(Evento.MOSTRAR_CARTAS);
        this.cartasTurnoActual = manejarTurnos().devolverCartas();
        return manejarTurnos().devolverCartas();
    }
    @Override
    public void repartirFichas() {
        for (Jugador jugador : jugadores) {
            // Clonar la lista
            List<Ficha> copia = new ArrayList<>();
            for (Ficha f : this.fichasIniciales) {
                copia.add(new Ficha(f.getValor()));
            }
            jugador.setFichas(copia);
        }
//        notificar(Evento.FICHAS_REPARTIDAS);
    }

    @Override
    public void descartarJugador(ArrayList<Integer> indices){
        jugadores.get(getTurno()).descartar(indices);
    }

    @Override
    public void gestionVuelta() throws RemoteException {
        int activos = cantJugadoresEnJuego();
        if (activos == 0) return;

        accionesContadas++;

        if (primeraVuelta) {
            // Primera vuelta ⇒ cada jugador tiene 2 acciones
            int totalAcciones1 = 2 * activos;
            if (accionesContadas < totalAcciones1) {
                // Si la acción recién contada es impar → acabamos de apostar → pedimos descarte
                if (accionesContadas % 2 == 1) {
                    notificarObservadores(Evento.CANT_DESCARTE);
                }
                // Si es par → acabamos de descartar → avanzamos al siguiente jugador para apostar
                else {
                    siguienteTurno();
                    notificarObservadores(Evento.MOSTRAR_CARTAS);
                }
            } else {
                // Se completaron todas las apuestas+descartes de la primera vuelta
                primeraVuelta = false;
                accionesContadas = 0;            // reiniciar contador para fase 2
                siguienteTurno();                // pasa al primer jugador de la 2ª ronda
                notificarObservadores(Evento.APUESTA);       // fase 2: todos apuestan
            }
        }
        else {
            // Segunda vuelta ⇒ cada jugador sólo 1 acción (apostar)
            int totalAcciones2 = activos;
            if (accionesContadas < totalAcciones2) {
                siguienteTurno();
                notificarObservadores(Evento.APUESTA);
            } else {
                // Fin de la segunda vuelta
                notificarObservadores(Evento.DEFINIR_GANADORES);
            }
        }
    }

    @Override
    public int cantJugadoresEnJuego(){
        int contador = 0;
        for (Jugador jugador : jugadores){
            if (jugador.isEnJuego()){
                contador++;
            }
        }
        return contador;
    }
    @Override
    public void reiniciarJuego() throws RemoteException {
        this.primeraVuelta = true;
        this.baraja = new Baraja();
        this.bote = new Bote();
        this.posRepartidor = 0;
        this.fichasIniciales = new ArrayList<>();
        this.turno = 0;
        this.apuestaActual = 0;
        this.error = false;
        this.cartasTurnoActual = new ArrayList<>();
        this.turnoActual = 0;
        this.turnoAnterior = 0;
        this.accionesContadas = 0;
        for (Jugador jugador : jugadores){
            jugador.reiniciame();
        }
        this.configurarJuego();
    }

    @Override
    public Jugador determinarGanador(){
        Mano ganadorMano = manoGanadora();
        for (Jugador j : jugadores){
            if (ganadorMano.equals(j.getMano())){
                return j;
            }
        }
        return null;
    }

    @Override
    public void resultados() throws RemoteException {
        this.sumarVictorias();
        this.sumarDerrotas();
        notificarObservadores(Evento.DECISION);
    }

    @Override
    public void sumarVictorias(){
        Jugador ganador = determinarGanador();
        for (Jugador jugador : jugadores){
            if (jugador.equals(ganador)){
                jugador.sumarVictorias();
            }
        }
    }
    @Override
    public void sumarDerrotas(){
        Jugador ganador = determinarGanador();
        for (Jugador jugador : jugadores){
            if (!jugador.equals(ganador)){
                jugador.sumarDerrotas();
            }
        }
    }
    @Override
    public Mano manoGanadora() {
        Mano mejorMano = jugadores.get(0).getMano();
        for (int i = 1; i < jugadores.size(); i++) {
            Mano manoComparada = jugadores.get(i).getMano();
            mejorMano = mejorMano.evaluarMano(manoComparada); // evaluamos la mejor mano entre la actual y la nueva
        }
        return mejorMano;
    }

    @Override
    public void igualarJugador() throws RemoteException {
        jugadores.get(getTurno()).apostar(apuestaActual,bote);
        this.apuestaActual = this.apuestaActual + apuestaActual;
    }
    @Override
    public Jugador manejarTurnos(){
        return jugadores.get(getTurno());
    }
    @Override
    public void retirarJugador(){
        jugadores.get(getTurno()).retirarse();
    }

    @Override
    public void apostarJugador(int cantidad) throws IllegalArgumentException, RemoteException {
        jugadores.get(getTurno()).apostar(cantidad,bote);
        this.apuestaActual = cantidad;
//        notificar(Evento.APUESTA);
    }

    @Override
    public Jugador agregarJugador(String nombre) {
        int maxJugadores = 6; // Límite superior

        if (jugadores.size() >= maxJugadores) {
            System.out.println("No se pueden agregar más jugadores.");
            return null;
        }

        Jugador actual = new Jugador(nombre);
        if (this.anfitrion == null) {
            this.anfitrion = actual;
        }

        jugadores.add(actual);

        this.jugadoresRegistrados++;
//        this.incrementoJugadoresRegistrados();

        return actual;
    }

    @Override
    public void agregarJugadores(ArrayList<String> nombres) throws RemoteException {
        for (String nombre : nombres){
            agregarJugador(nombre);
        }
        notificarObservadores(Evento.NOMBRE_JUGADOR);
    }

//    @Override
//    public void agregarObservador(IObservador observador) {
//        observadores.add(observador);
//    }
//
//    @Override
//    public void eliminarObservador(IObservador observador) {
//        observadores.remove(observador);
//    }
//
//    @Override
//    public void notificar(Object o) {
//        for (IObservador observador: observadores){
//            observador.actualizar(o);
//        }
//    }

    @Override
    public void configurarJuego() throws RemoteException {
//        System.out.println("debug");
        notificarObservadores(Evento.CANT_FICHAS_INICIALES);
    }

    @Override
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public void agregarFicha(int valor) throws RemoteException {
        if (fichasIniciales.size() != this.cantFichas){
            this.fichasIniciales.add(new Ficha(valor));
            if (fichasIniciales.size() != this.cantFichas){
                notificarObservadores(Evento.FICHAS_INICIALES);
            }
            else{
                this.repartirFichas();
                notificarObservadores(Evento.VALOR_CIEGAS);
            }
        }
        else{
            System.out.println("debug");
            notificarObservadores(Evento.VALOR_CIEGAS);
        }
    }

    @Override
    public void valorFichas() throws RemoteException {
        notificarObservadores(Evento.FICHAS_INICIALES);
    }

    @Override
    public void agregarFichas(ArrayList<Integer> valores) throws RemoteException {
        for (Integer valorFicha : valores){
            agregarFicha(valorFicha);
        }
        notificarObservadores(Evento.FICHAS_INICIALES);
    }
    @Override
    public void inicializarCiegas(int ciegaChica, int ciegaGrande){
        this.ciegaChica = new Ficha(ciegaChica);
        this.ciegaGrande = new Ficha(ciegaGrande);
        System.out.println(this.ciegaChica.getValor());
        System.out.println(this.ciegaGrande.getValor());
    }

    @Override
    public int siguienteTurno() {
        if (jugadores.isEmpty()){
            throw new IllegalStateException("No hay jugadores en la lista.");
        }
        this.turnoAnterior = turno;

        int totalJugadores = jugadores.size();
        int intentos = 0;

        do {
            turno = (turno + 1) % totalJugadores;
            intentos++;
            if (intentos > totalJugadores){
                throw new IllegalStateException("Todos los jugadores están retirados");
            }

        } while (!jugadores.get(turno).isEnJuego());

        return turno;
    }

    @Override
    public int getCiegaGrande() {
        return ciegaGrande.getValor();
    }

    @Override
    public int getTurno() {
        return turno;
    }

    @Override
    public void verificarJugadoresListos() throws RemoteException {
        if (jugadoresRegistrados == cantidadJugadores) {
            notificarObservadores(Evento.JUGADORES_INGRESADOS);
        }
        else{
            notificarObservadores(Evento.NOMBRE_JUGADOR);
        }
    }
    @Override
    public int getApuestaActual(){
        return apuestaActual;
    }

    @Override
    public Jugador getJugadorTurno() {
        return jugadores.get(getTurno());
    }

    @Override
    public void setCantFichas(int cantFichas) {
        this.cantFichas = cantFichas;
    }

    @Override
    public Jugador getAnfitrion() {
        return anfitrion;
    }

    @Override
    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    @Override
    public int getJugadoresRegistrados() {
        return jugadoresRegistrados;
    }

    @Override
    public int totalApostadoBote(){
        return this.bote.totalFichas();
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void setError(boolean error) {
        this.error = error;
    }
}
