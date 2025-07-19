package ar.edu.unlu;

import javax.swing.JOptionPane;
import ar.edu.unlu.controlador.PokerController;
import ar.edu.unlu.modelo.IModelo;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.vista.IVista;
import ar.edu.unlu.vista.JuegoPokerGui;
import ar.edu.unlu.vista.VistaGrafica;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.System.exit;

public class PokerApp {
    public ArrayList<PokerController> controladores;
    public String input;
    public int opcion;

    public void elegirTipoVentana(){
        String opciones[] = {"Vista de consola","Vista Gráfica"};
        this.opcion = JOptionPane.showOptionDialog(null,"Seleccione una vista: ","VISTA",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
        if (opcion == JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(null, "Elegiste la vista de consola!");
        } else if (opcion == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Elegiste la vista gráfica!");
        }
        else if (opcion == JOptionPane.CLOSED_OPTION){
            System.exit(0);
        }
    }

    public void iniciar() throws RemoteException {
        this.elegirTipoVentana();

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

        IModelo modelo = new JuegoPoker();
        this.controladores = new ArrayList<>();
        modelo.setCantidadJugadores(cantidad);
        for (int i = 0; i < cantidad; i++) {
            IVista vista = (opcion == 0) ? new JuegoPokerGui() : new VistaGrafica();
            PokerController controlador = new PokerController(vista);
            controlador.setModeloRemoto(modelo);
            this.controladores.add(controlador);
            vista.iniciarVentana();

        }
        modelo.iniciarJuego();
    }
}