package ar.edu.unlu.vista;

import ar.edu.unlu.controlador.PokerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class JuegoPokerGui implements IVista {

    private PokerController controlador;    // controlador
    private JFrame frame;   // ventana principal
    private JTextArea chatArea; // texto plano en la zona central
    private JTextField barraTexto;  // barra de texto en la zona inferior
    private JButton botonEnviar;    // botón de enviar en la zona inferior

    @Override
    public void iniciarVentana(){
        frame = new JFrame("Poker");
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // para cerrar el script una vez cerrada la ventana.
        frame.add(panelPrincipal());    // agrego al frame el panel principal con el panel inferior y sus respectivos componentes.
        frame.getRootPane().setDefaultButton(botonEnviar);  // permito que se pueda acceder al botón apretando enter.
        frame.setLocationRelativeTo(null);  // centrar ventana
//        mostrarMensaje("Ingrese la cantidad de jugadores (2-6):");
//        accionBotonEnviar();
        this.accionBotonEnviar();
        frame.setVisible(true); // pongo la visibilidad del frame en true.
    }

    public JPanel panelPrincipal(){
        JPanel panel = new JPanel();    // panel principal a retornar
        panel.setLayout(new BorderLayout());    // le asigno un layout Boder
        JPanel panelInferior = panelInferior(); // le asigno a panel inferior un layout Flow
        componentesPanelInferior(panelInferior);    // le agrego el botón de enviar y la barra de texto al panel inferior
        panel.add(panelInferior,BorderLayout.SOUTH);    // como es inferior lo agrego al panel principal en la zona sur
        componentesPanelCentral(panel); // agrego los componentes al panel principal, básicamente el texto plano que iría en el centro.

        return panel;   // retorno el panel principal.
    }

    public JPanel panelInferior(){
        return new JPanel(new FlowLayout());    // panel inferior con un layout Flow que sera agregada al sur del panel principal.
    }

    public void componentesPanelInferior(JPanel panel){
        botonEnviar = new JButton("Enviar");    // botón del panel inferior
        barraTexto = new JTextField(20);    // barra de texto del panel inferior

        panel.add(barraTexto);  // agrego los componentes al panel que recibe (panel inferior)
        panel.add(botonEnviar);


    }

    public void componentesPanelCentral(JPanel panel){
        // Crear un JTextArea para mostrar los mensajes del chat (texto plano)
        chatArea = new JTextArea();
        chatArea.setEditable(false); // Deshabilitar la edición para que sea de solo lectura
        chatArea.setLineWrap(true);  // Ajustar el texto a la línea
        chatArea.setWrapStyleWord(true); // Ajustar el texto por palabras

        // Colocar el JTextArea dentro de un JScrollPane para permitir desplazamiento
        JScrollPane scrollPane = new JScrollPane(chatArea);

        panel.add(scrollPane,BorderLayout.CENTER);  // se agrega al panel pasado por parámetro (panel principal en la zona central)
    }

    public void accionBotonEnviar() {
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controlador.comunicarEntrada(barraTexto.getText().trim());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("capturado");
//                entrada = barraTexto.getText().trim();
                barraTexto.setText(""); // Limpiar el texto después de obtener el valor
//                barraTexto.requestFocusInWindow();
            }
        });
    }

    public void setEnviarListener(ActionListener listener) {
        botonEnviar.addActionListener(listener);
    }

    @Override
    public void menuApuestas(boolean primerApostante, String nombre, int fichas){
        mostrarMensaje(nombre + " usted tiene " + fichas + " fichas");
        mostrarMensaje("Elija una acción: ");
        if (primerApostante){
            mostrarMensaje("IGUALAR");
            mostrarMensaje("SUBIR");
            mostrarMensaje("RETIRARSE");
        }
        else{
            mostrarMensaje("IGUALAR");
            mostrarMensaje("SUBIR");
            mostrarMensaje("RETIRARSE");
            mostrarMensaje("PASAR");
        }
    }

    public String obtenerTextoIngresado() {
        String texto = barraTexto.getText().trim();
        barraTexto.setText(""); // Limpiar después de obtener
        return texto;
    }

    @Override
    public void mostrarMensaje(String mensaje){
        chatArea.append(mensaje + "\n");
    }

    @Override
    public void limpiarBarraTexto(){
        barraTexto.setText("");
    }

    @Override
    public void limpiarTextoPlano(){
        chatArea.setText("");
    }

    // setter de controlador para que el controller pueda autoasignarse a sí mismo en su constructor.
    @Override
    public void setControlador(PokerController controlador) {
        this.controlador = controlador;
    }

    @Override
    public String pedirNombreJugador(){
        return JOptionPane.showInputDialog(null,"Ingrese su nombre como jugador: ","NickName");
    }
    @Override
    public String pedirCantFichas(){
        return JOptionPane.showInputDialog(null,"Ingrese la cantidad(no su valor) de fichas iniciales para la partida");
    }
    @Override
    public String pedirValorFichas(){
        return JOptionPane.showInputDialog(null,"Ingrese el valor de la ficha");
    }
    @Override
    public String pedirCiegaGrande(){
        return JOptionPane.showInputDialog(null,"Ingrese el valor de la ciega grande");
    }
    @Override
    public void mensajeError(){
        JOptionPane.showMessageDialog(null,"ERROR AL INGRESAR EL DATO -> REINTENTAR NUEVAMENTE");
    }
    @Override
    public void mostrarCartas(ArrayList<String> cartas){
        this.mostrarMensaje("Tus cartas son: " + cartas.toString());
    }
    @Override
    public int opcionSalir(){
        return JOptionPane.showConfirmDialog(null,"Quiere salir del juego?","Exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    }
    @Override
    public void mensajeIgualar(String nombre){
        this.mostrarMensaje("El jugador " + nombre + " igualó correctamente.");
    }
    @Override
    public void mensajeTurnoActual(String nombre){
        this.mostrarMensaje("Es el turno del jugador " + nombre + ", espere su turno...");
    }
    @Override
    public String pedirApuesta() throws RemoteException {
        return JOptionPane.showInputDialog("Ingrese la cantidad a apostar: [tiene que ser mayor que la apuesta actual = " + controlador.apuestaActualController() +"]");
    }
    @Override
    public void mensajeAposto(String nombre){
        this.mostrarMensaje("El jugador " + nombre + " apostó exitosamente.");
    }
    @Override
    public void mensajePaso(String nombre) throws RemoteException {
        mostrarMensaje(controlador.jugadorTurnoController() + "El jugador: " + nombre + " decidió pasar.");
    }
    @Override
    public void mensajeRetirado(String nombre) throws RemoteException {
        mostrarMensaje(controlador.jugadorTurnoController() + "El jugador: " + nombre + " se retiro de la partida.");
    }
    @Override
    public void mensajeErrorIgualar(String nombre, int apuesta, int totalFichas){
        this.mostrarMensaje(nombre + " tu saldo es de: " + totalFichas + " y pretendes igualar una apuesta de: " + apuesta);
        this.mostrarMensaje("no tiene saldo suficiente para dicha acción");
    }
    @Override
    public void mensajeDescarte(){
        mostrarMensaje("proceso de descarte, decida cuantas cartas va a descartar: [0 para no descartar ninguna]");
    }
    @Override
    public void mensajeSinDescarte(String nombre){
        mostrarMensaje("El jugador " + nombre + " decidió no descartar!");
    }
    @Override
    public void mensajeIndices(){
        mostrarMensaje("Ingrese el numero de cartas a descartar: ej. carta 1, carta 2 etc...");
    }
    @Override
    public void mensajeCargaExitosa(){
        mostrarMensaje("Los indices han sido cargados con éxito!");
    }
    @Override
    public void mensajeFinal(String ganador){
        if (ganador == null) {
            JOptionPane.showMessageDialog(null, "La partida concluyo en empate.");
        } else {
            JOptionPane.showMessageDialog(null, "El ganador indiscutido es: " + ganador);
        }
    }
    @Override
    public int mensajeReiniciarJuego(){
        return JOptionPane.showConfirmDialog(null,"Quiere reiniciar el juego?","Decisión",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    }
    @Override
    public void mensajeMostrarApuestaActual(int apuestActual){
        this.mostrarMensaje("La apuesta actual vigente es de: " + apuestActual);
    }
    @Override
    public void salir(){

    }
}


