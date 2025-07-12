package ar.edu.unlu.vista;

import ar.edu.unlu.controlador.PokerController;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public interface IVista {
    void iniciarVentana();

    void accionBotonEnviar();

    void menuApuestas(boolean primerApostante, String nombre, int fichas);

    void mostrarMensaje(String mensaje);

    // setter de controlador para que el controller pueda autoasignarse a sí mismo en su constructor.
    void setControlador(PokerController controlador);

    String pedirNombreJugador();

    String pedirCantFichas();

    String pedirValorFichas();

    String pedirCiegaGrande();

    void mensajeError();

    void mostrarCartas(ArrayList<String> cartas);

    int opcionSalir();

    void mensajeIgualar(String nombre);

    void mensajeTurnoActual(String nombre);

    String pedirApuesta();

    void mensajeAposto(String nombre);

    void mensajePaso(String nombre);

    void mensajeRetirado(String nombre);

    void mensajeErrorIgualar(String nombre, int apuesta, int totalFichas);

    void mensajeDescarte();

    void mensajeSinDescarte(String nombre);

    void mensajeIndices();

    void mensajeCargaExitosa();

    void mensajeFinal(String ganador);

    int mensajeReiniciarJuego();

    void mensajeMostrarApuestaActual(int apuestActual);

    void salir();
}
