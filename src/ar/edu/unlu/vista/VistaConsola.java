package ar.edu.unlu.vista;

import ar.edu.unlu.modelo.Carta;
import ar.edu.unlu.modelo.Jugador;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaConsola {
    private Scanner sc;

    public VistaConsola() {
        sc = new Scanner(System.in);
    }

    public void menuJuego() {
        System.out.println("__________________________________");
        System.out.println("POKER GAME!");
        System.out.println("__________________________________");
    }

    public int menuFichasIniciales() {
        menuJuego();
        System.out.println("ingrese las fichas iniciales para todos los jugadores de la partida [no su valor sino cantidad]: ");
        return sc.nextInt();
    }

    public int menuCantidadJugadores() {
        menuJuego();
        System.out.println("Ingresar la cantidad de jugadores para la partida: ");
        return sc.nextInt();
    }

    public void errorCantJugadores(){
        System.out.println("ERROR,debe ingresar una cantidad de jugadores de entre el 2 al 6");
    }

    public String menuNombreUsuario(int i) {
        menuJuego();
        System.out.println("Ingresar su nombre de usuario para el jugador " + i + " : ");
        return sc.nextLine();
    }

    public int menuAsignarFicha(int i) {
        menuJuego();
        System.out.println("Ingresar el valor de la ficha " + i + " : ");
        return sc.nextInt();
    }
    public void errorIngresoIndices(){
        System.out.println("Error, ingrese un valor del 1 al 5 por favor.");
    }
    public ArrayList<Integer> menuDescarte(Jugador jugador) {
        menuJuego();
        System.out.println("Jugador : " + jugador.getNombre() + " indique del 1 al 5 que cartas quiere descartar: ");
        int opcion = 0;
        ArrayList<Integer> resultado = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            opcion = sc.nextInt();
            resultado.add(opcion - 1);
        }
        return resultado;
    }

    public int obtenerAccionJugador(Jugador jugador) {
        menuJuego();
        System.out.println("Elija su acción" + jugador.getNombre() + " : ");
        System.out.println("1. PASAR");
        System.out.println("2. IGUALAR");
        System.out.println("3. SUBIR");
        System.out.println("4. RETIRARSE");
        System.out.println("5. APOSTAR");

        return sc.nextInt();
    }
    public int obtenerApuestaJugador(int apuestaActual,Jugador jugador){
        menuJuego();
        System.out.println("Ingrese la cantidad a apostar: ");
        return sc.nextInt();
    }
    public void errorAlIgualar(){
        System.out.println("no puede igualar ya que no hay apuestas aún.");
    }
    public void cerrarScanner(){
        sc.close();
    }

}
