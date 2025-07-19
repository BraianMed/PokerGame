package ar.edu.unlu;

import ar.edu.unlu.vista.IVista;
import ar.edu.unlu.vista.VistaGrafica;

import java.rmi.RemoteException;

public class PokerMain {
    public static void main(String[] args) throws RemoteException {
        PokerApp app = new PokerApp();
        app.iniciar();
//        IVista vista = new VistaGrafica();
//        vista.iniciarVentana();
    }
}
