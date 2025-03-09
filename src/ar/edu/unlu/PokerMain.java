package ar.edu.unlu;

import ar.edu.unlu.controlador.PokerController;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.vista.JuegoPokerGui;

public class PokerMain {
    public static void main(String[] args) {
        JuegoPokerGui vista = new JuegoPokerGui();
        JuegoPoker modelo = new JuegoPoker();

        PokerController controlador = new PokerController(modelo,vista);

        JuegoPokerGui vista2 = new JuegoPokerGui();
        PokerController controlador2 = new PokerController(modelo,vista2);

        modelo.iniciarJuego();
//        vista.iniciarVentana();

    }
}
