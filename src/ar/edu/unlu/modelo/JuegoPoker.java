package ar.edu.unlu.modelo;


import ar.edu.unlu.controlador.IObservador;

import java.util.ArrayList;
import java.util.List;

public class JuegoPoker implements IObservable{
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
    private ArrayList<IObservador> observadores;
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
        this.observadores = new ArrayList<>();
        this.turno = 0;
        this.apuestaActual = 0;
        this.error = false;
        this.cartasTurnoActual = new ArrayList<>();
        this.turnoActual = 0;
        this.turnoAnterior = 0;
        this.accionesContadas = 0;
    }

    public void iniciarJuego(){
        notificar(Evento.NOMBRE_JUGADOR);
    }

    public void moverRepartidor(){
        // hacemos módulo por el tamaño de la lista de jugadores para que cuando llegue al final vuelva al inicio de la lista como una cola circular
        // el resto de la división dara como resultado un número entre 0 y la cantidad de jugadores (perfecto para el índice)
        this.posRepartidor = (this.posRepartidor + 1) % jugadores.size();
    }

    public boolean asignarCiegas(){
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

            notificar(Evento.REPARTIR_CARTAS);
            return true;
        }
        return false;
    }

    public void errorCiega(){
        notificar(Evento.VALOR_CIEGAS);
    }

    public boolean jugadoresIsEmpty(){
        return jugadores.isEmpty();
    }

    public void repartirCartas(){
        if (!jugadores.isEmpty()){
            for (Jugador jugador : jugadores){
                baraja.repartirCarta(jugador);
                jugador.definirManoJugador();
            }
            notificar(Evento.MOSTRAR_CARTAS);
            System.out.println("cartas repartidas, y luego notifica a mostrar cartas");
        }
        else{
            this.error = true;
            notificar(Evento.NOMBRE_JUGADOR);
        }
    }

    public String nombreJugadorActual(){
        return manejarTurnos().getNombre();
    }

    public ArrayList<String> cartasTurnoActual(){
//        notificar(Evento.MOSTRAR_CARTAS);
        this.cartasTurnoActual = manejarTurnos().devolverCartas();
        return manejarTurnos().devolverCartas();
    }
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

    public void descartarJugador(ArrayList<Integer> indices){
        jugadores.get(getTurno()).descartar(indices);
    }

    public void gestionVuelta() {
        int activos = cantJugadoresEnJuego();
        if (activos == 0) return;

        accionesContadas++;

        if (primeraVuelta) {
            // Primera vuelta ⇒ cada jugador tiene 2 acciones
            int totalAcciones1 = 2 * activos;
            if (accionesContadas < totalAcciones1) {
                // Si la acción recién contada es impar → acabamos de apostar → pedimos descarte
                if (accionesContadas % 2 == 1) {
                    notificar(Evento.CANT_DESCARTE);
                }
                // Si es par → acabamos de descartar → avanzamos al siguiente jugador para apostar
                else {
                    siguienteTurno();
                    notificar(Evento.MOSTRAR_CARTAS);
                }
            } else {
                // Se completaron todas las apuestas+descartes de la primera vuelta
                primeraVuelta = false;
                accionesContadas = 0;            // reiniciar contador para fase 2
                siguienteTurno();                // pasa al primer jugador de la 2ª ronda
                notificar(Evento.APUESTA);       // fase 2: todos apuestan
            }
        }
        else {
            // Segunda vuelta ⇒ cada jugador sólo 1 acción (apostar)
            int totalAcciones2 = activos;
            if (accionesContadas < totalAcciones2) {
                siguienteTurno();
                notificar(Evento.APUESTA);
            } else {
                // Fin de la segunda vuelta
                notificar(Evento.DEFINIR_GANADORES);
            }
        }
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
    public Mano manoGanadora() {
        Mano mejorMano = jugadores.get(0).getMano();
        for (int i = 1; i < jugadores.size(); i++) {
            Mano manoComparada = jugadores.get(i).getMano();
            mejorMano = mejorMano.evaluarMano(manoComparada); // evaluamos la mejor mano entre la actual y la nueva
        }
        return mejorMano;
    }

    public void igualarJugador(){
        jugadores.get(getTurno()).apostar(apuestaActual,bote);
        this.apuestaActual = this.apuestaActual + apuestaActual;
    }
    public Jugador manejarTurnos(){
        return jugadores.get(getTurno());
    }
    public void retirarJugador(){
        jugadores.get(getTurno()).retirarse();
    }

    public void apostarJugador(int cantidad) throws IllegalArgumentException{
        jugadores.get(getTurno()).apostar(cantidad,bote);
        this.apuestaActual = cantidad;
//        notificar(Evento.APUESTA);
    }

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

    public void configurarJuego(){
//        System.out.println("debug");
        notificar(Evento.CANT_FICHAS_INICIALES);
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void agregarFicha(int valor){
        if (fichasIniciales.size() != this.cantFichas){
            this.fichasIniciales.add(new Ficha(valor));
            if (fichasIniciales.size() != this.cantFichas){
                notificar(Evento.FICHAS_INICIALES);
            }
            else{
                this.repartirFichas();
                notificar(Evento.VALOR_CIEGAS);
            }
        }
        else{
            System.out.println("debug");
            notificar(Evento.VALOR_CIEGAS);
        }
    }

    public void valorFichas(){
        notificar(Evento.FICHAS_INICIALES);
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
        System.out.println(this.ciegaChica.getValor());
        System.out.println(this.ciegaGrande.getValor());
    }

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
    private void avanzarTurno() {
        do {
            turnoActual = (turnoActual + 1) % jugadores.size();
        } while (!jugadores.get(turnoActual).isEnJuego());
    }

    public int getCiegaGrande() {
        return ciegaGrande.getValor();
    }

    public int getTurno() {
        return turno;
    }

    public void verificarJugadoresListos(){
        if (jugadoresRegistrados == cantidadJugadores) {
            notificar(Evento.JUGADORES_INGRESADOS);
        }
        else{
            notificar(Evento.NOMBRE_JUGADOR);
        }
    }
    public int getApuestaActual() {
        return apuestaActual;
    }

    public Jugador getJugadorTurno() {
        return jugadores.get(getTurno());
    }

    public void setCantFichas(int cantFichas) {
        this.cantFichas = cantFichas;
    }

    public Jugador getAnfitrion() {
        return anfitrion;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getJugadoresRegistrados() {
        return jugadoresRegistrados;
    }

    public int totalApostadoBote(){
        return this.bote.totalFichas();
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
