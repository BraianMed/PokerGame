package ar.edu.unlu.vista;

import ar.edu.unlu.controlador.PokerController;

import java.util.ArrayList;

public class VistaGrafica implements IVista{
    @Override
    public void iniciarVentana() {

    }

    @Override
    public void accionBotonEnviar() {

    }

    @Override
    public void menuApuestas(boolean primerApostante, String nombre, int fichas) {

    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }

    @Override
    public void setControlador(PokerController controlador) {

    }

    @Override
    public String pedirNombreJugador() {
        return "";
    }

    @Override
    public String pedirCantFichas() {
        return "";
    }

    @Override
    public String pedirValorFichas() {
        return "";
    }

    @Override
    public String pedirCiegaGrande() {
        return "";
    }

    @Override
    public void mensajeError() {

    }

    @Override
    public void mostrarCartas(ArrayList<String> cartas) {

    }

    @Override
    public int opcionSalir() {
        return 0;
    }

    @Override
    public void mensajeIgualar(String nombre) {

    }

    @Override
    public void mensajeTurnoActual(String nombre) {

    }

    @Override
    public String pedirApuesta() {
        return "";
    }

    @Override
    public void mensajeAposto(String nombre) {

    }

    @Override
    public void mensajePaso(String nombre) {

    }

    @Override
    public void mensajeRetirado(String nombre) {

    }

    @Override
    public void mensajeErrorIgualar(String nombre, int apuesta, int totalFichas) {

    }

    @Override
    public void mensajeDescarte() {

    }

    @Override
    public void mensajeSinDescarte(String nombre) {

    }

    @Override
    public void mensajeIndices() {

    }

    @Override
    public void mensajeCargaExitosa() {

    }

    @Override
    public void mensajeFinal(String ganador) {

    }

    @Override
    public int mensajeReiniciarJuego() {
        return 0;
    }

    @Override
    public void mensajeMostrarApuestaActual(int apuestActual) {

    }

    @Override
    public void salir() {

    }
}
