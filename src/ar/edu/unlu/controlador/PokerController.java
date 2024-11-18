package ar.edu.unlu.controlador;

import ar.edu.unlu.modelo.Evento;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.modelo.Jugador;
import ar.edu.unlu.vista.JuegoPokerGui;
import ar.edu.unlu.vista.Ventana;
import ar.edu.unlu.vista.VistaConsola;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class PokerController implements IObservador{
    private JuegoPoker modelo;
    private JuegoPokerGui vista;
    private int jugadoresAgregados = 0;

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

    public int fichasInicialesController(String fichasIniciales){
        int cantFichas = 0;
        try{
           cantFichas = Integer.parseInt(fichasIniciales);

        }
        catch (Exception e){
            vista.mostrarMensaje("Error al ingresar un tipo de dato no válido.");
        }
        return cantFichas;
    }
    public int valorFichaController(String entrada){
        int valorFicha = -1;
        try {
            valorFicha = Integer.parseInt(entrada);
        }
        catch (Exception e){
            vista.mostrarMensaje("Error al ingresar un tipo de dato no válido.");
        }
        return valorFicha;
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
        return modelo.getJugadores().get(turnoController()).totalFichas();
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

    public boolean asignarCiegas(){
        if (modelo.asignarCiegas()){    // si pudo asignar las fichas entonces muevo el repartidor.
            modelo.moverRepartidor();
            return true;
        }
        return false;
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

    @Override
    public void actualizar(Object o) {
        Evento eventoActual = (Evento) o;
        switch (eventoActual){
            case NOMBRE_JUGADOR -> {
                vista.mostrarMensaje("Todos los jugadores han sido registrados.");
                break;
            }
            case FICHAS_INICIALES -> {
                vista.mostrarMensaje("Todas las fichas han sido registradas.");
                break;
            }
            case FICHAS_REPARTIDAS -> {
                vista.mostrarMensaje("Todas las fichas han sido repartidas con éxito!");
            }
            case VALOR_CIEGAS -> {
                vista.mostrarMensaje("Ciegas asignadas con éxito!");
            }
            case REPARTIR_CARTAS -> {
                vista.mostrarMensaje("Cartas repartidas a todos los jugadores.");
            }
//            case MOSTRAR_CARTAS -> {
//                SwingUtilities.invokeLater(() -> {
//                    vista.mostrarMensaje("Jugador " + jugadorTurnoController() + " sus cartas son: " + cartasTurnoController().toString());
//                });
//            }
        }
    }
}
