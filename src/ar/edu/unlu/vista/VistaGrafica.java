package ar.edu.unlu.vista;

import ar.edu.unlu.controlador.PokerController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VistaGrafica implements IVista{

    private PokerController controlador;    // controlador
    private JFrame frame;   // ventana principal
    private JTextArea chatArea; // texto plano en la zona central
    private JTextField barraTexto;  // barra de texto en la zona inferior
    private JButton apostar;
    private JButton igualar;
    private JButton pasar;
    private JButton retirarse;
    private JLabel img;
    private FondoPanel panelFondo;
    private Image imagenFondo;
    private Image imagenMesa;
    private Image imagenBote;
    private Image imagenMazo;
    private JLabel fichasBote;
    private JLabel lblEstado;

    private JButton btnDescarte0 ,btnDescarte1, btnDescarte2, btnDescarte3, btnDescarte4, btnDescarte5;
    private JPanel panelDescarte; // Panel para contener los botones de descarte

    private Map<String, Image> imagenesCartas; // Mapa para almacenar todas las imágenes de cartas

    // Constantes para las cartas
    private static final String[] PALOS = {"corazones", "diamantes", "treboles", "picas"};
    private static final String[] VALORES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final String RUTA_CARTAS = "resources/cartas/";
    private static final String EXTENSION = ".png";


    public VistaGrafica(){
        cargarRecursosGraficos();
        cargarImagenesCartas();
    }

    public ArrayList<String> crearManoDePrueba() {
        ArrayList<String> manoPrueba = new ArrayList<>();

        // Agregamos algunas cartas de ejemplo
        manoPrueba.add("A de CORAZON");    // As de corazones (AC.png)
        manoPrueba.add("10 de DIAMANTE");  // 10 de diamantes (10D.png)
        manoPrueba.add("Q de PICA");       // Reina de picas (QP.png)
        manoPrueba.add("3 de TREBOL");     // 3 de tréboles (3T.png)
        manoPrueba.add("K de CORAZON");    // Rey de corazones (KC.png)

        return manoPrueba;
    }

    private void cargarRecursosGraficos() {
        // Cargar todas las imágenes necesarias
        this.imagenFondo = cargarImagen("resources/fondo/fondo_poker.jpg");
        this.imagenMesa = cargarImagen("resources/mesa/mesa.png");
        this.imagenBote = cargarImagen("resources/bote/bote.png");
        this.imagenMazo = cargarImagen("resources/mazo/mazo.png");

    }

    private void cargarImagenesCartas() {
        imagenesCartas = new HashMap<>();
        String[] pintas = {"P", "D", "T", "C"}; // Pica, Diamante, Trébol, Corazón
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String pinta : pintas) {
            for (String valor : valores) {
                String nombreArchivo = valor + pinta + ".png";
                String ruta = "resources/cartas/" + nombreArchivo;
                Image imagen = cargarImagen(ruta);

                if (imagen != null) {
                    // Creamos clave como "A de CORAZON" para coincidir con el toString()
                    String clave = convertirANombreCarta(valor, pinta);
                    imagenesCartas.put(clave, imagen);
                } else {
                    System.err.println("No se pudo cargar: " + ruta);
                }
            }
        }
    }

    private String convertirANombreCarta(String valor, String pinta) {
        // Convierte el valor numérico a su nombre en español
        String valorConvertido;
        switch (valor) {
            case "A": valorConvertido = "A"; break;
            case "J": valorConvertido = "J"; break;
            case "Q": valorConvertido = "Q"; break;
            case "K": valorConvertido = "K"; break;
            case "10": valorConvertido = "DIEZ"; break;
            case "9": valorConvertido = "NUEVE"; break;
            case "8": valorConvertido = "OCHO"; break;
            case "7": valorConvertido = "SIETE"; break;
            case "6": valorConvertido = "SEIS"; break;
            case "5": valorConvertido = "CINCO"; break;
            case "4": valorConvertido = "CUATRO"; break;
            case "3": valorConvertido = "TRES"; break;
            case "2": valorConvertido = "DOS"; break;
            default: valorConvertido = valor; // por si acaso
        }

        String pintaConvertida = pinta.equals("P") ? "PICA" :
                pinta.equals("D") ? "DIAMANTE" :
                        pinta.equals("T") ? "TREBOL" : "CORAZON";

        return valorConvertido + " de " + pintaConvertida;
    }

    private Image cargarImagen(String ruta) {
        try {
            // Intenta cargar como recurso del classpath primero
            InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);
            if (is != null) {
                return ImageIO.read(is);
            }
            // Si no está en classpath, intenta cargar como archivo
            Path path = Paths.get(ruta);
            if (Files.exists(path)) {
                return ImageIO.read(path.toFile());
            }
            System.err.println("No se pudo encontrar la imagen en classpath ni en filesystem: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + ruta);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void iniciarVentana() {
        frame = new JFrame("Poker");
        frame.setSize(1000,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // para cerrar el script una vez cerrada la ventana.
        frame.add(panelPrincipal());    // agrego al frame el panel principal
        frame.setLocationRelativeTo(null);  // centrar ventana

//        // Prueba de mostrar cartas
//        ArrayList<String> manoPrueba = crearManoDePrueba();
//        mostrarCartas(manoPrueba);
//        panelFondo.setLayout(null); // Para posicionamiento absoluto si querés colocar fichas/cartas más adelante
        this.accionBotonApuesta();
        this.accionBotonIgualar();
        this.accionBotonPasar();
        this.accionBotonRetirarse();

        this.accionBtnDescarte();
        frame.setVisible(true);
    }

    // Clase interna para pintar fondo escalado
    static class FondoPanel extends JPanel {
        private Image fondo;

        public FondoPanel(String rutaImagen) throws IOException {
            fondo = ImageIO.read(getClass().getResourceAsStream(rutaImagen));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Escala la imagen al tamaño del panel
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JPanel panelPrincipal(){
        // Panel principal con layout personalizado
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar fondo
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
//                    System.out.println("se dibujo fondo");
                }
                // Dibujar mesa en el centro
                if (imagenMesa != null) {
                    // Escalar la mesa como haces con el fondo
                    int mesaWidth = getWidth() * 2 / 3;  // 66% del ancho del panel
                    int mesaHeight = getHeight() * 2 / 3; // 66% del alto del panel
                    int x = (getWidth() - mesaWidth) / 2;
                    int y = (getHeight() - mesaHeight) / 2;
                    g.drawImage(imagenMesa, x, y, mesaWidth, mesaHeight, this);
//                    System.out.println("se dibujo mesa en: " + x + "," + y + " tamaño: " + mesaWidth + "x" + mesaHeight);
                }
                // Dibujar bote y mazo
                if (imagenBote != null && imagenMazo != null) {
                    int centroX = getWidth() / 2;
                    int centroY = getHeight() / 2;
                    g.drawImage(imagenBote, centroX - 50, centroY - 30, this);
                    g.drawImage(imagenMazo, centroX + 100, centroY - 50, this);
//                    System.out.println("se dibujo bote y mazo");
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Panel izquierdo con botones de acción
        JPanel panelAcciones = crearPanelAcciones();
        mainPanel.add(panelAcciones, BorderLayout.WEST);

        // Panel sur (inicialmente vacío) para las cartas del jugador
        JPanel panelCartasInicial = new JPanel();
        panelCartasInicial.setOpaque(false);
        mainPanel.add(panelCartasInicial, BorderLayout.SOUTH);

        // Panel de estado en la parte superior
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEstado.setOpaque(false);
        lblEstado = new JLabel("Bienvenido al Poker");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 14));
        panelEstado.add(lblEstado);

        // Agregar al panel principal
        mainPanel.add(panelEstado, BorderLayout.NORTH);

        // Panel de descarte en la parte derecha
        JPanel panelDerecho = panelDescarte();
        mainPanel.add(panelDerecho,BorderLayout.EAST);
        // Configurar botones inicialmente ocultos
        deshabilitarBotones();

//        // botón que se va a usar en el descarte.
//        this.extra.setEnabled(false);
//        this.extra.setVisible(false);

        return mainPanel;   // retorno el panel principal.
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(150, 40));
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 100, 0)); // Verde oscuro
        boton.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0))); // Borde dorado
        return boton;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(150, 20, 20, 20));

        // Configurar botones de acción
        this.apostar = crearBotonEstilizado("Apostar");
        this.igualar = crearBotonEstilizado("Igualar");
        this.pasar = crearBotonEstilizado("Pasar");
        this.retirarse = crearBotonEstilizado("Retirarse");

        panel.add(apostar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(igualar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(pasar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(retirarse);

        return panel;
    }

    private JPanel panelDescarte() {
        panelDescarte = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelDescarte.setOpaque(false);

        btnDescarte0 = crearBotonDescarte("0");
        btnDescarte1 = crearBotonDescarte("1");
        btnDescarte2 = crearBotonDescarte("2");
        btnDescarte3 = crearBotonDescarte("3");
        btnDescarte4 = crearBotonDescarte("4");
        btnDescarte5 = crearBotonDescarte("5");

        panelDescarte.add(btnDescarte0);
        panelDescarte.add(btnDescarte1);
        panelDescarte.add(btnDescarte2);
        panelDescarte.add(btnDescarte3);
        panelDescarte.add(btnDescarte4);
        panelDescarte.add(btnDescarte5);

        // Inicialmente ocultos
        panelDescarte.setVisible(false);

        return panelDescarte;
    }

    private JButton crearBotonDescarte(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(70, 130, 180)); // Azul acero
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        btn.setFocusPainted(false);
        return btn;
    }

    public void accionBotonApuesta(){
        deshabilitarBotones();
        this.apostar.addActionListener(e -> controlador.comunicarEntrada("subir"));
    }
    public void accionBotonIgualar(){
        deshabilitarBotones();
        this.igualar.addActionListener(e -> controlador.comunicarEntrada("igualar"));
    }
    public void accionBotonPasar(){
        deshabilitarBotones();
        this.pasar.addActionListener(e -> controlador.comunicarEntrada("pasar"));
    }
    public void accionBotonRetirarse(){
        deshabilitarBotones();
        this.retirarse.addActionListener(e -> controlador.comunicarEntrada("retirarse"));
    }
    private void deshabilitarBotones() {
        this.apostar.setEnabled(false);
        this.igualar.setEnabled(false);
        this.pasar.setEnabled(false);
        this.retirarse.setEnabled(false);
    }

    @Override
    public void menuApuestas(boolean primerApostante, String nombre, int fichas) {
        // 1. Configurar el área de estado/mensajes
        actualizarEstado(nombre + " - Fichas: " + fichas + " - Elija acción:");

        // 2. Mostrar botones según el contexto
        if (primerApostante) {
            apostar.setEnabled(true);
            igualar.setEnabled(true);
            retirarse.setEnabled(true);
        } else {
            apostar.setEnabled(true);
            igualar.setEnabled(true);
            retirarse.setEnabled(true);
        }

        // 3. Actualizar interfaz
        frame.revalidate();
        frame.repaint();
    }

    private void actualizarEstado(String mensaje) {
        mostrarMensaje(mensaje);

//        //mostrar temporalmente con fade
//        Timer timer = new Timer(3000, e -> lblEstado.setText(" "));
//        timer.setRepeats(false);
//        timer.start();
    }

    // Método para ocultar los botones cuando no es el turno
    public void ocultarOpcionesApuestas() {
        apostar.setEnabled(false);
        igualar.setEnabled(false);
        pasar.setEnabled(false);
        retirarse.setEnabled(false);

        // Opcional: hacerlos semi-transparentes
        for (JButton btn : new JButton[]{apostar, igualar, pasar, retirarse}) {
            btn.setForeground(new Color(255, 255, 255, 80));
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        if (lblEstado == null) {
            lblEstado = new JLabel(" ");
            lblEstado.setForeground(Color.WHITE);
            lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
            // Agregar al panel adecuado en tu inicialización
        }
        lblEstado.setText(mensaje);
    }

    @Override
    public void limpiarBarraTexto() {

    }

    @Override
    public void limpiarTextoPlano() {
        mostrarMensaje("");
    }

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
    public void mostrarCartas(ArrayList<String> cartas) {
        // Obtener el panel principal
        JPanel mainPanel = (JPanel) frame.getContentPane().getComponent(0);

        // Buscar y eliminar el panel de cartas anterior
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            if (layout.getConstraints(comp) == BorderLayout.SOUTH) {
                mainPanel.remove(comp);
                break;
            }
        }

        // Crear nuevo panel para las cartas
        JPanel panelCartasJugador = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCartasJugador.setOpaque(false);

        for (String cartaStr : cartas) {
            Image imagenCarta = imagenesCartas.get(cartaStr);
            if (imagenCarta != null) {
                ImageIcon icono = new ImageIcon(imagenCarta.getScaledInstance(80, 120, Image.SCALE_SMOOTH));
                JLabel labelCarta = new JLabel(icono);
                panelCartasJugador.add(labelCarta);
            } else {
                System.err.println("No se encontró imagen para: " + cartaStr);
                JLabel labelTexto = new JLabel(cartaStr);
                labelTexto.setForeground(Color.WHITE);
                panelCartasJugador.add(labelTexto);
            }
        }

        // Agregar el nuevo panel y actualizar
        mainPanel.add(panelCartasJugador, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
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
    public String pedirApuesta(){
        return JOptionPane.showInputDialog("Ingrese la cantidad a apostar: [tiene que ser mayor que la apuesta actual = " + controlador.apuestaActualController() +"]");
    }

    @Override
    public void mensajeAposto(String nombre){
        this.mostrarMensaje("El jugador " + nombre + " apostó exitosamente.");
    }

    @Override
    public void mensajePaso(String nombre){
        mostrarMensaje(controlador.jugadorTurnoController() + "El jugador: " + nombre + " decidió pasar.");
    }

    @Override
    public void mensajeRetirado(String nombre){
        mostrarMensaje(controlador.jugadorTurnoController() + "El jugador: " + nombre + " se retiro de la partida.");
    }

    @Override
    public void mensajeErrorIgualar(String nombre, int apuesta, int totalFichas){
        this.mostrarMensaje(nombre + " tu saldo es de: " + totalFichas + " y pretendes igualar una apuesta de: " + apuesta);
        this.mostrarMensaje("no tiene saldo suficiente para dicha acción");
    }

    @Override
    public void mensajeDescarte() {
        deshabilitarBotones();
        mostrarMensaje("Proceso de descarte decida cuantas cartas va a descartar:");
        habilitarDescarte(true);

    }
    private void habilitarDescarte(boolean mostrar){
        panelDescarte.setVisible(mostrar);

        btnDescarte0.setVisible(mostrar);
        btnDescarte0.setEnabled(mostrar);

        btnDescarte1.setVisible(mostrar);
        btnDescarte1.setEnabled(mostrar);

        btnDescarte2.setVisible(mostrar);
        btnDescarte2.setEnabled(mostrar);

        btnDescarte3.setVisible(mostrar);
        btnDescarte3.setEnabled(mostrar);

        btnDescarte4.setVisible(mostrar);
        btnDescarte4.setEnabled(mostrar);

        btnDescarte5.setVisible(mostrar);
        btnDescarte5.setEnabled(mostrar);
    }

    private void accionBtnDescarte(){
        this.btnDescarte0.addActionListener(e -> controlador.comunicarEntrada("0"));
        this.btnDescarte1.addActionListener(e -> controlador.comunicarEntrada("1"));
        this.btnDescarte2.addActionListener(e -> controlador.comunicarEntrada("2"));
        this.btnDescarte3.addActionListener(e -> controlador.comunicarEntrada("3"));
        this.btnDescarte4.addActionListener(e -> controlador.comunicarEntrada("4"));
        this.btnDescarte5.addActionListener(e -> controlador.comunicarEntrada("5"));
    }

    @Override
    public void mensajeSinDescarte(String nombre){
        mostrarMensaje("El jugador " + nombre + " decidió no descartar!");
        habilitarDescarte(false);
    }

    @Override
    public void mensajeIndices() {
        mostrarMensaje("Indique cual carta descartará: :");
        habilitarDescarte(true);
        this.btnDescarte0.setVisible(false);
        this.btnDescarte0.setEnabled(false);
    }

    @Override
    public void mensajeCargaExitosa(){
        mostrarMensaje("Los indices han sido cargados con éxito!");
        habilitarDescarte(false);
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
    public void salir() {

    }
}
