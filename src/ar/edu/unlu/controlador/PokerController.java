package ar.edu.unlu.controlador;

import ar.edu.unlu.modelo.Evento;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.modelo.Jugador;
import ar.edu.unlu.vista.JuegoPokerGui;

import javax.swing.*;
import java.util.ArrayList;

public class PokerController implements IObservador{
    private JuegoPoker modelo;
    private JuegoPokerGui vista;
    private int jugadoresAgregados = 0;
    private Jugador jugadorAsociado;
    private String entrada;

    public PokerController(JuegoPoker modelo,JuegoPokerGui vista){
        this.modelo = modelo;
        this.vista = vista;
        vista.setControlador(this);
        modelo.agregarObservador(this);
    }

    private void solicitarCantidadJugadores() {
        vista.mostrarMensaje("Ingrese la cantidad de jugadores (2-6):");
    }

    private void solicitarNombreJugador() {
        vista.mostrarMensaje("Ingrese el nombre del jugador " + (jugadoresAgregados + 1) + ":");
    }

    public int cantidadJugadores(String jugadores){
        try{
            int cantJugadores = Integer.parseInt(jugadores);
            if(!validarCantJugadores(cantJugadores)){
                vista.mostrarMensaje("Error,ingrese un valor dentro del rango.");
                solicitarCantidadJugadores();
            }
            else{
                return cantJugadores;
            }
        }
        catch (Exception e){
            vista.mostrarMensaje("Error al ingresar un tipo de dato no válido.");
        }
        return 0;
    }

    public boolean validarCantJugadores(int cantidad){
        return cantidad >= 2 && cantidad <= 6;
    }
    public boolean validarCantCartasDescarte(int cantidad){
        if (cantidad > 5 || cantidad < 1){
            return false;
        }
        else{
            return true;
        }
    }

    public void descartarCartasController(ArrayList<Integer> indices){
        modelo.descartarJugador(indices);
    }
    public int cantJugadoresEnJuegoController(){
        return modelo.cantJugadoresEnJuego();
    }

    public String ganadorController(){
        modelo.sumarVictorias();
        modelo.sumarDerrotas();
        return modelo.determinarGanador().getNombre();
    }

    public void inicializarJugadores(ArrayList<String> nombres){
        modelo.agregarJugadores(nombres);
    }

    public String jugadorTurnoController(){
        return modelo.nombreJugadorActual();
    }

    public int turnoController(){
        return modelo.getTurno();
    }
    public void igualarController(){
        modelo.igualarJugador();
    }
    public void apostarJugadorController(int cantidad){
        modelo.apostarJugador(cantidad);
    }
    public int apuestaActualController(){
        return modelo.getApuestaActual();
    }
    public ArrayList<String> cartasTurnoController(){
        return modelo.cartasTurnoActual();
    }

    public void fichasInicialesController(String fichasIniciales){
        try{
           int cantFichas = Integer.parseInt(fichasIniciales);
           if (cantFichas <= 0 ){
               vista.mensajeError();
               modelo.configurarJuego();
           }
           else{
               vista.mostrarMensaje("fichas ingresadas con éxito!");
               modelo.setCantFichas(cantFichas);
               modelo.valorFichas();
               System.out.println("numero de fichas exitoso");
           }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.configurarJuego();
        }
    }
    public int valorFichaController(String entrada){
        int valorFicha = -1;
        try {
            valorFicha = Integer.parseInt(entrada);
            if (valorFicha <= 0 ){
                vista.mensajeError();
                modelo.valorFichas();
            }
            else{
                vista.mostrarMensaje("ficha ingresada con éxito!");
                modelo.agregarFicha(valorFicha);
//                System.out.println("numero de fichas exitoso");
            }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.valorFichas();
        }
        return valorFicha;
    }
    public void validarCiega(String ciegaGrande){
        int ciega = 0;
        try{
            ciega = Integer.parseInt(ciegaGrande);
            if (ciega <= 0 && !this.validarCiegasController(ciega)){
                vista.mensajeError();
                modelo.errorCiega();
                System.out.println("error en dato negativo o ciega muy grande");
            }
            else{
                vista.mostrarMensaje("Ciega grande ingresada con éxito");
                modelo.inicializarCiegas( (ciega / 2) ,ciega);
                try {
                    this.asignarCiegas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.errorCiega();
            System.out.println("error de tipo de dato");
        }
    }
    public boolean validarSubir(int apuestaActual){
        if (apuestaActual > modelo.getCiegaGrande()){
            return true;
        }
        else{
            return false;
        }
    }
    public void retirarJugadorController(){
        modelo.retirarJugador();
    }
    public int fichasJugadorActual(){
        return modelo.manejarTurnos().totalFichas();
    }

    public void asignarFichasIniciales(ArrayList<Integer> fichasIniciales){
        modelo.agregarFichas(fichasIniciales);
        modelo.repartirFichas();
    }

    public int moverTurnoController(){
        return modelo.siguienteTurno();
    }

    public void valorCiegasController(int ciegaGrande){
        modelo.inicializarCiegas( (ciegaGrande / 2) ,ciegaGrande);
    }
    public boolean validarCiegasController(int ciegaActual){
        if (ciegaActual > modelo.getJugadores().get(0).totalFichas() && (ciegaActual / 2) > modelo.getJugadores().get(0).totalFichas() ){
            return false;
        }
        else{
            return true;
        }
    }

    public void asignarCiegas(){
        if (modelo.asignarCiegas()){    // si pudo asignar las fichas entonces muevo el repartidor.
            modelo.moverRepartidor();
//            modelo.cartasObserver();
            return;
        }
        vista.mensajeError();
        System.out.println("error por lista vacía");
        modelo.iniciarJuego();  // reiniciar juego por falta de jugadores...
    }
    public boolean repartirCartas(){
        if (!modelo.jugadoresIsEmpty()){
            modelo.repartirCartas();
            return true;
        }
        else{
            return false;
        }

    }

    public boolean validarIndices(ArrayList<Integer> indices){
        boolean resultado = true;
        for (Integer i : indices){
            if (i < 0 || i > 4) {
                resultado = false;
                break;
            }
        }
        return resultado;
    }

    public void reiniciarJuego(){
        modelo.reiniciarJuego();
    }

    public JuegoPoker getModelo() {
        return modelo;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getEntrada() {
        return entrada;
    }
    public boolean manejarSalir(int opcion){
        boolean resultado = false;
        if (opcion == JOptionPane.YES_OPTION){
            System.exit(0);
        } else if (opcion == JOptionPane.NO_OPTION) {
            resultado = true;
        }
        else if (opcion == JOptionPane.CLOSED_OPTION){
            System.exit(0);
        }
        return resultado;
    }

    @Override
    public void actualizar(Object o) {
        Evento eventoActual = (Evento) o;
        int salir = 0;
        switch (eventoActual){
            case NOMBRE_JUGADOR -> {
                vista.iniciarVentana();
                String actual = vista.pedirNombreJugador();
                if (actual != null){
                    this.jugadorAsociado = modelo.agregarJugador(actual);
                    System.out.println(this.jugadorAsociado.getNombre());
//                    if (this.jugadorAsociado == null){
//                        // ver que hacer cuando se llega al limite de jugadores
//                    }
                }
                else{
                    salir = vista.opcionSalir();
                    if(this.manejarSalir(salir)){modelo.iniciarJuego();}
                }

            }
            case JUGADORES_INGRESADOS -> {
                vista.mostrarMensaje("Todos los jugadores se han registrado con éxito!");
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){modelo.configurarJuego();}
            }
            case CANT_FICHAS_INICIALES -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirCantFichas();
                    if (this.entrada != null){
                        this.fichasInicialesController(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.configurarJuego();}
                    }
                }
                else{
                    vista.mostrarMensaje("El anfitrión esta configurando las reglas de la partida...");
                }
            }
            case FICHAS_INICIALES -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirValorFichas();
                    if (this.entrada != null){
                        valorFichaController(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.valorFichas();}
                    }
                }
            }
            case VALOR_CIEGAS -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirCiegaGrande();
                    if (this.entrada != null){
                        this.validarCiega(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.errorCiega();}
                    }
                }
            }
            case REPARTIR_CARTAS -> {
                vista.mostrarMensaje("repartiendo cartas...");
//                System.out.println("Turno actual: " + modelo.manejarTurnos().getNombre());
                System.out.println("Jugador asociado a la vista: " + this.jugadorAsociado.getNombre());

                if (this.repartirCartas()){
                    if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                        vista.mostrarCartas(modelo.cartasTurnoActual());
                        vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
                    }
                    else{
                        vista.mostrarMensaje("El jugador con turno actual esta viendo sus cartas.");
                    }
                }
                else{
                    vista.mensajeError();
                    modelo.iniciarJuego();
                }
            }

        }
    }
}
