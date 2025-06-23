package ar.edu.unlu;

import javax.swing.JOptionPane;
import ar.edu.unlu.controlador.PokerController;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.vista.JuegoPokerGui;
import java.util.ArrayList;

import static java.lang.System.exit;

public class PokerApp {
    public ArrayList<PokerController> controladores;
    public String input;

    public void iniciar() {
        int cantidad = 0;
        while (cantidad < 2) { // Mínimo 2 jugadores
            input = JOptionPane.showInputDialog(null, "Ingrese la cantidad de jugadores: [2 a 6]");
            if (input == null) {exit(0);} // Cancelar
            try {
                cantidad = Integer.parseInt(input);
                if (cantidad > 6){
                    JOptionPane.showMessageDialog(null,"ERROR AL INGRESAR EL DATO -> REINTENTAR NUEVAMENTE");
                    cantidad = 0;
                }

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