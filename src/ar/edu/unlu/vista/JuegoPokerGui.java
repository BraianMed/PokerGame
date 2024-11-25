package ar.edu.unlu.vista;

import ar.edu.unlu.controlador.PokerController;
import ar.edu.unlu.modelo.Evento;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static javax.swing.SwingUtilities.getRootPane;

public class JuegoPokerGui{

    private PokerController controlador;    // controlador
    private JFrame frame;   // ventana principal
    private JTextArea chatArea; // texto plano en la zona central
    private JTextField barraTexto;  // barra de texto en la zona inferior
    private JButton botonEnviar;    // botón de enviar en la zona inferior

    private String entrada;   // input
    private int cantJugadores;
    private ArrayList<String> nomJugadores = new ArrayList<>();
    private int jugadoresAgregados;
    private Evento eventoActual;
    private int cantFichasIniciales;
    private ArrayList<Integer> fichasIniciales = new ArrayList<>();
    private int fichasAgregadas;
    private int ciegaGrande;
    private int turnoActual;
    private boolean primerApostante;
    private int contadorDescarte;
    private String jugadorActual;
    private ArrayList<Integer> indices = new ArrayList<>();
    private int turnosTotales;
    private int vueltas;
    private int cartasDescartadas;
    private int cantCartasDescartar;
    private boolean vieneDeDescartar;
    private boolean vieneDeApostar;
    private int eventosPrincipales;
    private boolean noDebenDescartar;

    public void iniciarVentana(){
        frame = new JFrame("Poker");
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // para cerrar el script una vez cerrada la ventana.
        frame.add(panelPrincipal());    // agrego al frame el panel principal con el panel inferior y sus respectivos componentes.
        frame.getRootPane().setDefaultButton(botonEnviar);  // permito que se pueda acceder al botón apretando enter.

        this.cantJugadores = 0;
        this.jugadoresAgregados = 0;
        this.eventoActual = Evento.CANT_JUGADORES;
        this.cantFichasIniciales = 0;
        this.fichasAgregadas = 0;
        this.ciegaGrande = 0;
        this.contadorDescarte = 0;
        this.turnoActual = 0;
        this.primerApostante = true;
        this.turnosTotales = 0;
        this.vueltas = 0;
        this.cartasDescartadas = 0;
        this.cantCartasDescartar = 0;
        this.vieneDeDescartar = false;
        this.vieneDeApostar = false;
        this.eventosPrincipales = 0;
        this.noDebenDescartar = false;
        mostrarMensaje("Ingrese la cantidad de jugadores (2-6):");
        accionBotonEnviar();

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
                entrada = barraTexto.getText().trim();
                barraTexto.setText(""); // Limpiar el texto después de obtener el valor

                if (eventoActual == Evento.CANT_JUGADORES) {
                    mostrarMensaje("Cantidad ingresada: " + entrada);
                    cantJugadores = controlador.cantidadJugadores(entrada);

                    if (cantJugadores != 0) {
                        mostrarMensaje("Cantidad de jugadores aceptada!");
                        eventoActual = Evento.NOMBRE_JUGADOR;  // Cambiamos al siguiente evento
                        jugadoresAgregados = 0;  // Reiniciar el contador de jugadores
                        // muestro el mensaje para comenzar la asignación de nombres enviando el mensaje inicial desde la terminación de este proceso
                        // para poder continuarlo en el siguiente evento.
                        mostrarMensaje("Ingrese el nombre del jugador " + (jugadoresAgregados + 1) + ":");
                    }
                    else {
                        mostrarMensaje("Cantidad inválida. Por favor ingrese un número entre 2 y 6.");
                    }

                }
                else if (eventoActual == Evento.NOMBRE_JUGADOR) {
                    if (jugadoresAgregados < cantJugadores) {
                        mostrarMensaje("Jugador " + (jugadoresAgregados + 1) + ": " + entrada + " registrado.");
                        nomJugadores.add(entrada);
                        jugadoresAgregados++;

                        if (jugadoresAgregados < cantJugadores) {
                            mostrarMensaje("Ingrese el nombre del jugador " + (jugadoresAgregados + 1) + ":");
                        }
                        else {
                            controlador.inicializarJugadores(nomJugadores); // aca se activa observer.
                            eventoActual = Evento.CANT_FICHAS_INICIALES;
                            mostrarMensaje("Ingrese las fichas iniciales para todos los jugadores de la partida [no su valor sino cantidad]: ");
                        }
                    }
                }
                else if (eventoActual == Evento.CANT_FICHAS_INICIALES){
                    cantFichasIniciales = controlador.fichasInicialesController(entrada);
                    if (cantFichasIniciales != 0){
                        mostrarMensaje("Cantidad de fichas aceptada!");
                        eventoActual = Evento.FICHAS_INICIALES;
                        fichasAgregadas = 0;
                        mostrarMensaje("Ingrese el valor de la ficha " + (fichasAgregadas + 1) + ":");
                    }
                    else{
                        mostrarMensaje("Cantidad de fichas inválida.");
                    }
                }
                else if (eventoActual == Evento.FICHAS_INICIALES){
                    if (fichasAgregadas < cantFichasIniciales){
                        int valorActual = controlador.valorFichaController(entrada);
                        if (valorActual != -1){
                            mostrarMensaje("Ficha " + (fichasAgregadas + 1) + " con valor: " + entrada + " registrada.");
                            fichasIniciales.add(valorActual);
                            ++fichasAgregadas;

                            if (fichasAgregadas < cantFichasIniciales){
                                mostrarMensaje("Ingrese el valor de la ficha " + (fichasAgregadas + 1) + ":");
                            }
                            else {
                                controlador.asignarFichasIniciales(fichasIniciales);    // aca se activa observer.
                                mostrarMensaje("Ingrese el valor para la ciega grande: ");
                                ciegaGrande = 0;
                                eventoActual = Evento.VALOR_CIEGAS;
                            }
                        }
                    }
                }
                else if(eventoActual == Evento.VALOR_CIEGAS){

                    int valorCiegaActual = controlador.valorFichaController(entrada);
                    if (valorCiegaActual != -1 && controlador.validarCiegasController(valorCiegaActual)){
                        mostrarMensaje("Valor de la ciega grande ingresado: " + valorCiegaActual);
                        mostrarMensaje("Valor de la ciega pequeña calculado para la partida: " + valorCiegaActual / 2);
                        ciegaGrande = valorCiegaActual;

                        controlador.valorCiegasController(valorCiegaActual);
                        mostrarMensaje("Las ciegas han sido inicializadas!");
                        mostrarMensaje("Asignando ciegas...");
                        if (controlador.asignarCiegas()){   // aca se activa observer
                            mostrarMensaje("Ingrese la tecla [ y ] para repartir cartas a los jugadores: ");
                            eventoActual = Evento.REPARTIR_CARTAS;
                        }
                        else{
                            mostrarMensaje("Error por falta de jugadores...");
                            mostrarMensaje("Reiniciando proceso...");
                            eventoActual = Evento.CANT_JUGADORES;
                        }
                    }
                    else {
                        mostrarMensaje("Error al ingresar una ciega con valor mayor al total de fichas de los jugadores...");
                        mostrarMensaje("Intente de nuevo: ");
                    }
                }
                else if (eventoActual == Evento.REPARTIR_CARTAS) {
                    String entradaLower = entrada.toLowerCase();
                    if (entradaLower.equals("y")){
                        if (controlador.repartirCartas()){  // aca se activa observer
                            eventoActual = Evento.MOSTRAR_CARTAS;
                            mostrarMensaje("Ingrese la tecla [ y ] para mostrar sus cartas: ");
                        }
                        else{
                            mostrarMensaje("Error por falta de jugadores...");
                            mostrarMensaje("Reiniciando proceso...");
                            eventoActual = Evento.CANT_JUGADORES;
                        }
                    }
                    else{
                        mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                    }
                }
                else if (eventoActual == Evento.MOSTRAR_CARTAS){

                    if (turnosTotales == controlador.cantJugadoresEnJuegoController()){
                        vueltas++;
                        turnosTotales = 0;
                        noDebenDescartar = true;
                    }
                    if (vueltas == 2){
                        mostrarMensaje("Definiendo ganadores...");
                        eventoActual = Evento.DEFINIR_GANADORES;
                    }
                    else{
                        String entradaLower = entrada.toLowerCase();
                        if (entradaLower.equals("y")){
                            turnoActual = controlador.turnoController();

                            ArrayList<String> cartasJugadorActual = controlador.cartasTurnoController();
                            mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " sus cartas son: " + cartasJugadorActual.toString());
                            if (vieneDeDescartar){
                                vieneDeDescartar = false;

                                controlador.moverTurnoController();
                                mostrarMensaje("Siguiente turno...");
                                eventoActual = Evento.MOSTRAR_CARTAS;
                                mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                            }
                            else{
                                eventoActual = Evento.APUESTA;
                                mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " tiene " + controlador.fichasJugadorActual() + " fichas, ingrese su acción: ");
                                menuApuestas(primerApostante);
                            }
                        }
                        else{
                            mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                        }
                    }

                }
                else if (eventoActual == Evento.APUESTA) {
                    primerApostante = false;
                    String entradaLower = entrada.toLowerCase();
                    if (entradaLower.equals("igualar")){
                        controlador.igualarController();
                        mostrarMensaje(controlador.jugadorTurnoController() + " igualo exitosamente.");
                        vieneDeApostar = true;

                        if (noDebenDescartar){
                            turnosTotales++;
                            if (turnosTotales == controlador.cantJugadoresEnJuegoController()){
                                eventoActual = Evento.DEFINIR_GANADORES;
                                mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                            }
                            else{
                                controlador.moverTurnoController();
                                eventoActual = Evento.APUESTA;
                                mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " tiene " + controlador.fichasJugadorActual() + " fichas, ingrese su acción: ");
                                menuApuestas(primerApostante);
                            }
                        }
                        else{
                            eventoActual = Evento.CANT_DESCARTE;
                            cantCartasDescartar = 0;
                            cartasDescartadas = 0;
                            mostrarMensaje("proceso de descarte, decida cuantas cartas va a descartar: ");
                        }

                    }
                    else if (entradaLower.equals("subir")){
                        String apuesta = JOptionPane.showInputDialog("Ingrese la cantidad a apostar: [tiene que ser mayor que la apuesta actual = " + controlador.apuestaActualController() +"]");
                        int apuestaEntera = controlador.valorFichaController(apuesta);
                        if (apuestaEntera != -1 && controlador.validarSubir(apuestaEntera)){
                            controlador.apostarJugadorController(apuestaEntera);
                            mostrarMensaje(controlador.jugadorTurnoController() + " decidió apostar exitosamente.");
                            vieneDeApostar = true;

                            if (noDebenDescartar){
                                turnosTotales++;
                                if (turnosTotales == controlador.cantJugadoresEnJuegoController()){
                                    eventoActual = Evento.DEFINIR_GANADORES;
                                    mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                                }
                                else{
                                    controlador.moverTurnoController();
                                    eventoActual = Evento.APUESTA;
                                    mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " tiene " + controlador.fichasJugadorActual() + " fichas, ingrese su acción: ");
                                    menuApuestas(primerApostante);

                                }
                            }
                            else{
                                eventoActual = Evento.CANT_DESCARTE;

                                cantCartasDescartar = 0;
                                cartasDescartadas = 0;
                                mostrarMensaje("proceso de descarte, decida cuantas cartas va a descartar: ");
                            }
                        }
                        else{
                            primerApostante = true;
                            mostrarMensaje("Ingrese una apuesta mayor que la ciega grande por favor: [tipee la opción nuevamente]");
                        }
                    }
                    else if (entradaLower.equals("pasar")) {
                        mostrarMensaje(controlador.jugadorTurnoController() + " decidió pasar.");
                        vieneDeApostar = true;

                        if (noDebenDescartar){
                            turnosTotales++;
                            if (turnosTotales == controlador.cantJugadoresEnJuegoController()){
                                eventoActual = Evento.DEFINIR_GANADORES;
                                mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                            }
                            else{
                                controlador.moverTurnoController();
                                eventoActual = Evento.APUESTA;
                                mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " tiene " + controlador.fichasJugadorActual() + " fichas, ingrese su acción: ");
                                menuApuestas(primerApostante);
                            }
                        }
                        else{
                            eventoActual = Evento.CANT_DESCARTE;
                            cantCartasDescartar = 0;
                            cartasDescartadas = 0;
                            mostrarMensaje("proceso de descarte, decida cuantas cartas va a descartar: ");
                        }
                    }
                    else if (entradaLower.equals("retirarse")){
                        mostrarMensaje("El jugador: " + controlador.jugadorTurnoController() + " se ha retirado del juego!");
                        controlador.retirarJugadorController();
                        // como el jugador se retiró no se le pedirán más acciones y se seguirá con el juego.
                        if (controlador.cantJugadoresEnJuegoController() == 1){
                            JOptionPane.showMessageDialog(null,"El ganador indiscutido es: " + controlador.jugadorTurnoController());
                        }
                        else{
                            if (noDebenDescartar){
                                turnosTotales++;
                                if (turnosTotales == controlador.cantJugadoresEnJuegoController()){
                                    eventoActual = Evento.DEFINIR_GANADORES;
                                    mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                                }
                                else{
                                    controlador.moverTurnoController();
                                    eventoActual = Evento.APUESTA;
                                    mostrarMensaje("Jugador: " + controlador.jugadorTurnoController() + " tiene " + controlador.fichasJugadorActual() + " fichas, ingrese su acción: ");
                                    menuApuestas(primerApostante);
                                }
                            }
                            else{
                                controlador.moverTurnoController();
                                eventoActual = Evento.MOSTRAR_CARTAS;
                                cantCartasDescartar = 0;
                                cartasDescartadas = 0;
                            }

                        }
                    }
                    else{
                        mostrarMensaje("Seleccione una opción válida por favor: ");
                    }
                }
                else if (eventoActual == Evento.CANT_DESCARTE){
                    indices.clear(); // lo vacío para que otro jugador lo use.
                    int cantidadCartas = controlador.valorFichaController(entrada);
                    if (cantidadCartas != -1){
                        if (cantidadCartas == 0){
                            mostrarMensaje("Decide no descartar ninguna carta.");
                            vieneDeDescartar = true;
                            eventoActual = Evento.MOSTRAR_CARTAS;
                            turnosTotales++;
                            mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                        }
                        else if (controlador.validarCantCartasDescarte(cantidadCartas)){
                            cantCartasDescartar = cantidadCartas;
                            eventoActual = Evento.INDICES_DESCARTE;
                            mostrarMensaje("Ingrese el numero de cartas a descartar: ej. carta 1, carta 2 etc...");
                        }
                    }
                    else{
                        mostrarMensaje("Error al ingresar el número de carta, intente nuevamente: ");
                    }
                }
                else if (eventoActual == Evento.INDICES_DESCARTE) {

                    if (cartasDescartadas < cantCartasDescartar){
                        int indiceDescarte = controlador.valorFichaController(entrada);
                        if (indiceDescarte != -1 && controlador.validarCantCartasDescarte(indiceDescarte)){
                            indices.add(indiceDescarte - 1);
                            ++cartasDescartadas;
                            if (cartasDescartadas < cantCartasDescartar){
                                mostrarMensaje("Ingrese el numero de cartas a descartar: ej. carta 1, carta 2 etc...");
                            }
                            else{
                                mostrarMensaje("Todos las cartas a descartar fueron ingresadas con éxito.");
                                controlador.descartarCartasController(indices);
                                vieneDeDescartar = true;
                                eventoActual = Evento.MOSTRAR_CARTAS;
                                turnosTotales++;
                                mostrarMensaje("Ingrese la tecla [ y ] para poder continuar con el juego por favor...");
                            }
                        }
                        else{
                            mostrarMensaje("Error al ingresar el número de carta, intente nuevamente: ");
                        }
                    }

                }
                else if (eventoActual == Evento.DEFINIR_GANADORES){
                    String ganador = controlador.ganadorController();
                    if (ganador == null){
                        JOptionPane.showMessageDialog(null,"La partida concluyo en empate.");
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"El ganador indiscutido es: " + ganador);
                    }
                }
            }
        });
    }


    public void setEnviarListener(ActionListener listener) {
        botonEnviar.addActionListener(listener);
    }

    public void menuApuestas(boolean primerApostante){
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

    public void mostrarMensaje(String mensaje){
        chatArea.append(mensaje + "\n");
    }

    public void limpiarBarraTexto(){
        barraTexto.setText("");
    }

    // setter de controlador para que el controller pueda autoasignarse a sí mismo en su constructor.
    public void setControlador(PokerController controlador) {
        this.controlador = controlador;
    }
}


