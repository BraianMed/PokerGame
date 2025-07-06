package ar.edu.unlu.controlador;

import ar.edu.unlu.modelo.Evento;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.modelo.Jugador;
import ar.edu.unlu.vista.JuegoPokerGui;

import javax.swing.*;
import java.util.ArrayList;

public class PokerController implements IObservador{
    private JuegoPoker modelo;
    private JuegoPokerGui vista;
    private int jugadoresAgregados = 0;
    private Jugador jugadorAsociado;
    private String entrada;
    private Evento eventoActual;
    private ArrayList<Integer> indices;
    private int cantDescarte;
    private int cantDescartadas;

    public PokerController(JuegoPoker modelo,JuegoPokerGui vista){
        this.modelo = modelo;
        this.vista = vista;
        vista.setControlador(this);
        modelo.agregarObservador(this);
        this.indices = new ArrayList<>();
        this.cantDescarte = 0;
        this.cantDescartadas = 0;
    }

    private void solicitarCantidadJugadores() {
        vista.mostrarMensaje("Ingrese la cantidad de jugadores (2-6):");
    }

    private void solicitarNombreJugador() {
        vista.mostrarMensaje("Ingrese el nombre del jugador " + (jugadoresAgregados + 1) + ":");
    }

    public int cantidadJugadores(String jugadores){
        try{
            int cantJugadores = Integer.parseInt(jugadores);
            if(!validarCantJugadores(cantJugadores)){
                vista.mostrarMensaje("Error,ingrese un valor dentro del rango.");
                solicitarCantidadJugadores();
            }
            else{
                return cantJugadores;
            }
        }
        catch (Exception e){
            vista.mostrarMensaje("Error al ingresar un tipo de dato no válido.");
        }
        return 0;
    }

    public boolean validarCantJugadores(int cantidad){
        return cantidad >= 2 && cantidad <= 6;
    }

    public boolean validarCantCartasDescarte(int cantidad){
        if (cantidad > 5 || cantidad < 1){
            return false;
        }
        else{
            return true;
        }
    }

    public void descartarCartasController(ArrayList<Integer> indices){
        modelo.descartarJugador(indices);
    }
    public int cantJugadoresEnJuegoController(){
        return modelo.cantJugadoresEnJuego();
    }

    public String ganadorController(){
        modelo.sumarVictorias();
        modelo.sumarDerrotas();
        return modelo.determinarGanador().getNombre();
    }

    public void inicializarJugadores(ArrayList<String> nombres){
        modelo.agregarJugadores(nombres);
    }

    public String jugadorTurnoController(){
        return modelo.nombreJugadorActual();
    }

    public int turnoController(){
        return modelo.getTurno();
    }
    public void igualarController(){
        modelo.igualarJugador();
    }
    public void apostarJugadorController(int cantidad){
        modelo.apostarJugador(cantidad);
    }
    public int apuestaActualController(){
        return modelo.getApuestaActual();
    }
    public ArrayList<String> cartasTurnoController(){
        return modelo.cartasTurnoActual();
    }

    public void fichasInicialesController(String fichasIniciales){
        try{
           int cantFichas = Integer.parseInt(fichasIniciales);
           if (cantFichas <= 0 ){
               vista.mensajeError();
//               this.entrada = vista.pedirCantFichas();
               modelo.configurarJuego();
           }
           else{
               vista.mostrarMensaje("fichas ingresadas con éxito!");
               modelo.setCantFichas(cantFichas);
               modelo.valorFichas();
               System.out.println("numero de fichas exitoso");
           }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.configurarJuego();
        }
    }
    public int valorFichaController(String entrada){
        int valorFicha = -1;
        try {
            valorFicha = Integer.parseInt(entrada);
            if (valorFicha <= 0 ){
                vista.mensajeError();
                modelo.valorFichas();
            }
            else{
                vista.mostrarMensaje("ficha ingresada con éxito!");
                modelo.agregarFicha(valorFicha);
//                System.out.println("numero de fichas exitoso");
            }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.valorFichas();
        }
        return valorFicha;
    }

    public int valorFichaApuesta(String entrada){
        int valorFicha;
        try {
            valorFicha = Integer.parseInt(entrada);
            if (valorFicha <= 0 ){
                return -1;
            }
        }
        catch (Exception e){
            return -1;
        }
        return valorFicha;
    }
    public void validarCiega(String ciegaGrande){
        int ciega = 0;
        try{
            ciega = Integer.parseInt(ciegaGrande);
            if (ciega <= 0 && !this.validarCiegasController(ciega)){
                vista.mensajeError();
                modelo.errorCiega();
                System.out.println("error en dato negativo o ciega muy grande");
            }
            else{
                vista.mostrarMensaje("Ciega grande ingresada con éxito");
                modelo.inicializarCiegas( (ciega / 2) ,ciega);
                try {
                    this.asignarCiegas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            vista.mensajeError();
            modelo.errorCiega();
            System.out.println("error de tipo de dato");
        }
    }
    public boolean validarSubir(int apuestaActual){
        if (apuestaActual > modelo.getCiegaGrande()){
            return true;
        }
        else{
            return false;
        }
    }

    public void retirarJugadorController(){
        modelo.retirarJugador();
    }

    public int fichasJugadorActual(){
        return modelo.manejarTurnos().totalFichas();
    }

    public void asignarFichasIniciales(ArrayList<Integer> fichasIniciales){
        modelo.agregarFichas(fichasIniciales);
        modelo.repartirFichas();
    }

    public int moverTurnoController(){
        return modelo.siguienteTurno();
    }

    public void valorCiegasController(int ciegaGrande){
        modelo.inicializarCiegas( (ciegaGrande / 2) ,ciegaGrande);
    }

    public boolean validarCiegasController(int ciegaActual){
        if (ciegaActual > modelo.getJugadores().get(0).totalFichas() && (ciegaActual / 2) > modelo.getJugadores().get(0).totalFichas() ){
            return false;
        }
        else{
            return true;
        }
    }

    public void asignarCiegas(){
        if (modelo.asignarCiegas()){    // si pudo asignar las fichas entonces muevo el repartidor.
            modelo.moverRepartidor();
//            modelo.cartasObserver();
            return;
        }
        vista.mensajeError();
        System.out.println("error por lista vacía");
        modelo.iniciarJuego();  // reiniciar juego por falta de jugadores...
    }
    public boolean repartirCartas(){
        if (!modelo.jugadoresIsEmpty()){
            modelo.repartirCartas();
            return true;
        }
        else{
            return false;
        }

    }

    public boolean validarIndices(ArrayList<Integer> indices){
        boolean resultado = true;
        for (Integer i : indices){
            if (i < 0 || i > 4) {
                resultado = false;
                break;
            }
        }
        return resultado;
    }
    public int validarCantDescarte(String entrada) {
        try {
            int valor = Integer.parseInt(entrada);
            if (valor >= 1 && valor <= 5) {
                return valor;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return -1;
    }

    public void reiniciarJuego(){
        modelo.reiniciarJuego();
    }

    public JuegoPoker getModelo() {
        return modelo;
    }

    public void comunicarEntrada(String input) {
        if (eventoActual.equals(Evento.APUESTA)){
            if (this.modelo.manejarTurnos().equals(this.jugadorAsociado)){
                switch (input.toLowerCase()){
                    case "igualar" -> {
                        this.manejarIgualar();
                        this.modelo.gestionVuelta();
                    }
                    case "subir" -> {
                        this.manejarSubir();
                        this.modelo.gestionVuelta();
                    }
                    case "pasar" -> {
                        this.manejarPasar();
                        this.modelo.gestionVuelta();
                    }
                    case "retirarse" ->{
                        this.manejarRetirarse();
                        this.modelo.gestionVuelta();
                    }
                    default -> {
                        vista.mensajeError();
                        vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
                    }
                }
            }
            else{
                vista.mensajeTurnoActual(this.modelo.manejarTurnos().getNombre());
            }
        }
        else if (this.eventoActual.equals(Evento.CANT_DESCARTE)){
            if (this.modelo.manejarTurnos().equals(this.jugadorAsociado)){
                this.indices.clear();
                cantDescarte = validarCantDescarte(input);
                manejarCantDescarte();
            }
        }
        else if (this.eventoActual.equals(Evento.INDICES_DESCARTE)){
            if (this.modelo.manejarTurnos().equals(this.jugadorAsociado)){
                manejarIndiceDescarte(input);
            }
        }
//        this.modelo.recibirEntrada(entrada,this.jugadorAsociado);
    }

    private void manejarIgualar(){
        try {
            modelo.igualarJugador();
            this.jugadorAsociado.setPrimerApostante(false);
        } catch (Exception e) {
            vista.mensajeErrorIgualar(this.jugadorAsociado.getNombre(),modelo.getApuestaActual(),this.jugadorAsociado.totalFichas());
            // ver como hacer cuando quiere igualar y no tiene saldo...
        }
        vista.mensajeIgualar(this.jugadorAsociado.getNombre());
    }
    public void manejarSubir(){
        String apuesta = vista.pedirApuesta();
        int apuestaEntera = this.valorFichaApuesta(apuesta);
        if (apuestaEntera != -1 && this.validarSubir(apuestaEntera)){
            this.modelo.apostarJugador(apuestaEntera);
            vista.mensajeAposto(this.jugadorAsociado.getNombre());
            this.jugadorAsociado.setPrimerApostante(false);
        }
        else{
            vista.mensajeError();
            vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
        }
    }

    public void manejarPasar(){
        vista.mensajePaso(this.jugadorAsociado.getNombre());
    }

    public void manejarRetirarse(){
        modelo.retirarJugador();
        vista.mensajeRetirado(this.jugadorAsociado.getNombre());
    }

    public void manejarCantDescarte(){
        if (this.cantDescarte != -1){
            if (this.cantDescarte == 0){
                vista.mensajeSinDescarte(this.jugadorAsociado.getNombre());
                this.modelo.gestionVuelta();
            }
            else{
                vista.mensajeIndices();
                eventoActual = Evento.INDICES_DESCARTE;
            }
        }
        else{
            vista.mensajeError();
            vista.mensajeDescarte();
        }
    }

    public void manejarIndiceDescarte(String indice){
        int indiceDescarte;
        try {
            indiceDescarte = Integer.parseInt(indice) - 1;
        } catch (NumberFormatException e) {
            vista.mensajeError();
            vista.mensajeIndices();
            return;
        }
        // ver si funciona la validación, si no usar el método this.validarIndices
        if (indiceDescarte >= 0 && indiceDescarte <= 4 && !indices.contains(indiceDescarte)) {
            this.indices.add(indiceDescarte);
            cantDescartadas++;
            if (cantDescartadas == this.cantDescarte) {
                this.cantDescartadas = 0;
                this.cantDescarte = 0;
                vista.mensajeCargaExitosa();
                this.modelo.descartarJugador(new ArrayList<>(indices));
                this.indices.clear(); // Limpiar la lista después de usarla
                vista.mostrarCartas(modelo.cartasTurnoActual());
                this.modelo.gestionVuelta();
            }
            else{
                vista.mensajeCargaExitosa();
                vista.mensajeIndices();
            }
        } else {
            vista.mensajeError();
            vista.mensajeIndices();
        }
    }

    public String getEntrada() {
        return entrada;
    }
    public boolean manejarSalir(int opcion){
        boolean resultado = false;
        if (opcion == JOptionPane.YES_OPTION){
            System.exit(0);
        } else if (opcion == JOptionPane.NO_OPTION) {
            resultado = true;
        }
        else if (opcion == JOptionPane.CLOSED_OPTION){
            System.exit(0);
        }
        return resultado;
    }
    public boolean manejarDesicion(int opcion){
        boolean resultado = false;
        if (opcion == JOptionPane.YES_OPTION){
            this.modelo.reiniciarJuego();
        } else if (opcion == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        else if (opcion == JOptionPane.CLOSED_OPTION){
            System.exit(0);
        }
        return resultado;
    }

    @Override
    public void actualizar(Object o) {
        Evento eventoActual = (Evento) o;
        int salir = 0;
        switch (eventoActual){
            case NOMBRE_JUGADOR -> {
                //vista.iniciarVentana();
                if (modelo.isError()){
                    vista.mensajeError();
                    modelo.setError(false);
                }
                if(this.jugadorAsociado == null) {
                    String actual = vista.pedirNombreJugador();
                    if (actual != null) {
                        this.jugadorAsociado = modelo.agregarJugador(actual);
                        System.out.println(this.jugadorAsociado.getNombre());
                        if (modelo.isReady()){
                            modelo.notificar(Evento.JUGADORES_INGRESADOS);
                        }
//                    if (this.jugadorAsociado == null){
//                        // ver que hacer cuando se llega al limite de jugadores
//                    }
                    } else {
                        salir = vista.opcionSalir();
                        if (this.manejarSalir(salir)) {
                            modelo.iniciarJuego();
                        }
                    }
                }
//                else{
//                    vista.mostrarMensaje("Ya se ha ingresado un jugador con el nombre: " + this.jugadorAsociado.getNombre());
//                }
            }
            case JUGADORES_INGRESADOS -> {
                vista.mostrarMensaje("Todos los jugadores se han registrado con éxito!");
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){modelo.configurarJuego();}
            }
            case CANT_FICHAS_INICIALES -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirCantFichas();
                    if (this.entrada != null){
                        this.fichasInicialesController(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.configurarJuego();}
                    }
                }
                else{
                    vista.mostrarMensaje("El anfitrión esta configurando las reglas de la partida...");
                }
            }
            case FICHAS_INICIALES -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirValorFichas();
                    if (this.entrada != null){
                        valorFichaController(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.valorFichas();} // ver el tema de cuando ingresan pocas fichas y la ciega es más grande
                    }
                }
            }
            case VALOR_CIEGAS -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    this.entrada = vista.pedirCiegaGrande();
                    if (this.entrada != null){
                        this.validarCiega(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.errorCiega();}
                    }
                }
            }
            case REPARTIR_CARTAS -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mostrarMensaje("repartiendo cartas...");
//                System.out.println("Turno actual: " + modelo.manejarTurnos().getNombre());
                    System.out.println(modelo.getJugadoresRegistrados());
                    System.out.println("Jugador asociado a la vista: " + this.jugadorAsociado.getNombre());

                    modelo.repartirCartas();
                }

            }
            case MOSTRAR_CARTAS -> {
                if(eventoActual.equals(Evento.APUESTA)){break;}

                if (modelo.manejarTurnos().equals(this.jugadorAsociado)) {
                    System.out.println("turno actual: "+ modelo.getTurno());
                    vista.mostrarCartas(modelo.cartasTurnoActual());
                    // ver como ahorrar las siguientes 3 líneas de código:
                    vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(), this.jugadorAsociado.getNombre(), this.jugadorAsociado.totalFichas());
                    this.eventoActual = Evento.APUESTA;
//                    vista.accionBotonEnviar();
                } else {
                    vista.mostrarMensaje("El jugador con turno actual esta viendo sus cartas.");
                }
            }
            case APUESTA -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
                    this.eventoActual = Evento.APUESTA;
//                    vista.accionBotonEnviar();
                }
            }
            case CANT_DESCARTE -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mensajeDescarte();
                    this.eventoActual = Evento.CANT_DESCARTE;
//                    vista.accionBotonEnviar();
                }
            }
            case INDICES_DESCARTE -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mensajeIndices();
                    this.eventoActual = Evento.INDICES_DESCARTE;
//                    vista.accionBotonEnviar();
                }
            }
            case DEFINIR_GANADORES -> {
                vista.mensajeFinal(this.ganadorController());
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    int opcion = vista.mensajeReiniciarJuego();
                    this.manejarDesicion(opcion);
                }
            }

        }
    }
}
