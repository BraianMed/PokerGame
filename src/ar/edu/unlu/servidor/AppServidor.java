package ar.edu.unlu.servidor;

import ar.edu.unlu.modelo.IModelo;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static java.lang.System.exit;

public class AppServidor {

    public static void main(String[] args) throws RemoteException {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        String input;
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
        IModelo modelo = JuegoPoker.getInstancia();
        modelo.setCantidadJugadores(cantidad);
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            servidor.iniciar(modelo);
//            modelo.iniciarJuego();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}