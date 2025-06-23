package ar.edu.unlu;

import javax.swing.JOptionPane;
import ar.edu.unlu.controlador.PokerController;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.vista.JuegoPokerGui;
import java.util.ArrayList;

import static java.lang.System.exit;

public class PokerApp {
    public ArrayList<PokerController> controladores;

    public void iniciar() {
        int cantidad = 0;
        while (cantidad < 2) { // Mínimo 2 jugadores
            String input = JOptionPane.showInputDialog(null, "Ingrese la cantidad de jugadores:");
            if (input == null) {exit(0);} // Cancelar
            try {
                cantidad = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                cantidad = 0;
            }
        }

        JuegoPoker modelo = new JuegoPoker();
        this.controladores = new ArrayList<>();
        modelo.setCantidadJugadores(cantidad);
        for (int i = 0; i < cantidad; i++) {
            JuegoPokerGui vista = new JuegoPokerGui();
            PokerController controlador = new PokerController(modelo, vista);
            this.controladores.add(controlador);
            vista.iniciarVentana();
        }

        modelo.iniciarJuego();
    }
}