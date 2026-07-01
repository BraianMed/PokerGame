package ar.edu.unlu.controlador;

import ar.edu.unlu.modelo.*;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.vista.IVista;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class PokerController implements IControladorRemoto {
    private IModelo modelo;
    private IVista vista;
    private Jugador jugadorAsociado;
    private String entrada;
    private Evento eventoActual;
    private ArrayList<Integer> indices;
    private int cantDescarte;
    private int cantDescartadas;
    private Jugador ganador;

    public PokerController(IVista vista) throws RemoteException {
        this.vista = vista;
//        this.setModeloRemoto(modelo);
        this.indices = new ArrayList<>();
        this.cantDescarte = 0;
        this.cantDescartadas = 0;
    }


    public int apuestaActualController() throws RemoteException {
        return modelo.getApuestaActual();
    }

    public void fichasInicialesController(String fichasIniciales) throws RemoteException {
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
    public int valorFichaController(String entrada) throws RemoteException {
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
    public void validarCiega(String ciegaGrande) throws RemoteException {
        int ciega = 0;
        System.out.println("[CLIENTE] validarCiega: " + ciegaGrande);
        try{
            ciega = Integer.parseInt(ciegaGrande);
            if (ciega <= 0 || !this.validarCiegasController(ciega)){
                vista.mensajeError();
                modelo.errorCiega();
                System.out.println("error en dato negativo o ciega muy grande");
            }
            else{
                vista.mostrarMensaje("Ciega grande ingresada con éxito");
                modelo.inicializarCiegas( (ciega / 2) ,ciega);
                System.out.println("[CLIENTE] validarCiegasController=" + validarCiegasController(ciega));
                try {
                    System.out.println("entra en el try para asignar ciegas");
                    this.asignarCiegas();
                } catch (Exception e) {
                    System.out.println("catch de validarCiega");
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
    public boolean validarSubir(int apuestaActual) throws RemoteException {
        return apuestaActual > modelo.getApuestaActual();
    }

    public boolean validarCiegasController(int ciegaActual) throws RemoteException {
        for (Jugador jugador : modelo.getJugadores()) {
            if (ciegaActual > jugador.totalFichas() || (ciegaActual / 2) > jugador.totalFichas()) {
                return false;
            }
        }
        return true;
    }

    public void asignarCiegas() throws RemoteException {
        System.out.println("[SERVIDOR] entro asignarCiegas");
        if (modelo.asignarCiegas()){    // si pudo asignar las fichas entonces muevo el repartidor.
            modelo.moverRepartidor();
//            modelo.cartasObserver();
            return;
        }
        vista.mensajeError();
        System.out.println("error por lista vacía");
        modelo.verificarJugadoresListos();  // reiniciar juego por falta de jugadores...
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

    private void mostrarMenuApuestasActual() throws RemoteException {
        Jugador jugadorTurno = modelo.manejarTurnos();
        int apuestaActual = modelo.getApuestaActual();
        boolean puedePasar = jugadorTurno.cantApuestaActual() >= apuestaActual;
        vista.actualizarBote(modelo.totalApostadoBote());
        vista.menuApuestas(puedePasar, jugadorTurno.getNombre(), jugadorTurno.totalFichas(), apuestaActual);
    }

    public int validarCantDescarte(String entrada) {
        try {
            int valor = Integer.parseInt(entrada);
            if ((valor >= 1 && valor <= 5) || valor == 0) {
                return valor;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return -1;
    }

    public void reiniciarJuego() throws RemoteException {
        modelo.reiniciarJuego();
    }

    public IModelo getModelo() {
        return modelo;
    }

    public void comunicarEntrada(String input) throws RemoteException {

        int salir = 0;
        if(this.jugadorAsociado == null) {
            if (input != null) {
                Jugador jugadorRegistrado = modelo.agregarJugador(input);
                if (jugadorRegistrado != null) {
                    this.jugadorAsociado = jugadorRegistrado;
                    System.out.println(this.jugadorAsociado.getNombre());
                    this.modelo.verificarJugadoresListos();
                } else {
                    vista.mensajeError();
                    this.modelo.iniciarRegistroJugadores();
                }

            } else {
                salir = vista.opcionSalir();
                if (this.manejarSalir(salir)) {
                    this.modelo.verificarJugadoresListos();
                }
            }
            return;
        }
        if (this.eventoActual == null) {
            vista.mostrarMensaje("Esperá tu turno o la fase del juego.");
            return;
        }

        if (eventoActual.equals(Evento.APUESTA)){
            if (this.soy(modelo.manejarTurnos())){
                switch (input.toLowerCase()){
                    case "igualar" -> {
                        if(!this.manejarIgualar()){
                            return;
                        }
                        this.modelo.gestionVuelta();
                    }
                    case "subir" -> {
                        if (!this.manejarSubir()){
                            return;
                        }
                        modelo.gestionVuelta();
                    }
                    case "pasar" -> {
                        if (!this.manejarPasar()){
                            return;
                        }
                        this.modelo.gestionVuelta();
                    }
                    case "retirarse" ->{
                        this.manejarRetirarse();
                        this.modelo.gestionVuelta();
                    }
                    default -> {
                        vista.mensajeError();
                        mostrarMenuApuestasActual();
                    }
                }
            }
            else{
                vista.mensajeTurnoActual(this.modelo.manejarTurnos().getNombre());
            }
        }
        else if (this.eventoActual.equals(Evento.CANT_DESCARTE)){
            if (this.soy(modelo.manejarTurnos())){
                this.indices.clear();
                cantDescarte = validarCantDescarte(input);
                manejarCantDescarte();
            }
        }
        else if (this.eventoActual.equals(Evento.INDICES_DESCARTE)){
            if (this.soy(modelo.manejarTurnos())){
                manejarIndiceDescarte(input);
            }
        }
//        this.modelo.recibirEntrada(entrada,this.jugadorAsociado);
    }

    private boolean manejarIgualar() throws RemoteException {
        boolean resultado;
        if (modelo.getApuestaActual() <= 0) {
            return manejarPasar();
        }
        if (modelo.manejarTurnos().cantApuestaActual() >= modelo.getApuestaActual()) {
            return manejarPasar();
        }
        try {
            modelo.igualarJugador();
//            this.jugadorAsociado.setPrimerApostante(false);
            vista.actualizarBote(modelo.totalApostadoBote());
            vista.mensajeIgualar(modelo.manejarTurnos().getNombre());
            vista.mostrarMensaje(modelo.manejarTurnos().getNombre() + " ahora tiene " + modelo.manejarTurnos().totalFichas() + " fichas.");
            resultado = true;
        } catch (Exception e) {
            vista.mensajeErrorIgualar(modelo.manejarTurnos().getNombre(),modelo.getApuestaActual(),modelo.manejarTurnos().totalFichas());
            resultado = false;
            // ver como hacer cuando quiere igualar y no tiene saldo...
        }
        return resultado;
    }
    public boolean manejarSubir() throws RemoteException {
        String apuesta = vista.pedirApuesta();
        boolean resultado;
        int apuestaEntera = this.valorFichaApuesta(apuesta);
        if (apuestaEntera != -1 && this.validarSubir(apuestaEntera)){
            try{
                this.modelo.apostarJugador(apuestaEntera);
                vista.actualizarBote(modelo.totalApostadoBote());
                vista.mensajeAposto(modelo.manejarTurnos().getNombre());
                vista.mostrarMensaje(modelo.manejarTurnos().getNombre() + " ahora tiene " + modelo.manejarTurnos().totalFichas() + " fichas.");
//                this.jugadorAsociado.setPrimerApostante(false);
                resultado = true;
            }
            catch (Exception e){
                vista.mensajeError();
                mostrarMenuApuestasActual();
                resultado = false;
            }
        }
        else{
            vista.mensajeError();
            mostrarMenuApuestasActual();
            resultado = false;
        }
        return resultado;
    }

    public boolean manejarPasar() throws RemoteException {
        if (modelo.getApuestaActual() > modelo.manejarTurnos().cantApuestaActual()) {
            vista.mostrarMensaje("No podés pasar: primero tenés que igualar o subir la apuesta actual.");
            mostrarMenuApuestasActual();
            return false;
        }
        vista.mensajePaso(this.jugadorAsociado.getNombre());
        return true;
    }

    public void manejarRetirarse() throws RemoteException {
        modelo.retirarJugador();
        vista.mensajeRetirado(this.jugadorAsociado.getNombre());
    }

    public void manejarCantDescarte() throws RemoteException {
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

    public void manejarIndiceDescarte(String indice) throws RemoteException {
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
    public void manejarDesicion(int opcion) throws RemoteException {

        if (opcion == JOptionPane.YES_OPTION){
            this.modelo.reiniciarJuego();
        } else if (opcion == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        else if (opcion == JOptionPane.CLOSED_OPTION){
            System.exit(0);
        }
    }

    private void pedirDecisionFinal() {
        SwingUtilities.invokeLater(() -> {
            int opcion = vista.mensajeReiniciarJuego();
            Thread decisionThread = new Thread(() -> {
                try {
                    this.manejarDesicion(opcion);
                } catch (RemoteException e) {
                    SwingUtilities.invokeLater(() -> vista.mostrarMensaje("No se pudo aplicar la decisión final."));
                }
            }, "decision-final-poker");
            decisionThread.start();
        });
    }

    private boolean soy(Jugador j) {
        return j != null && this.jugadorAsociado != null
                && j.getNombre().equals(this.jugadorAsociado.getNombre());
    }

    @Override
    public void actualizar(IObservableRemoto observableRemoto,Object o) throws RemoteException {
        Evento eventoActual = (Evento) o;
        int salir = 0;
        switch (eventoActual){
            case NOMBRE_JUGADOR -> {
                if (modelo.isError()){
                    vista.mensajeError();
                    modelo.setError(false);
                }
                if(this.jugadorAsociado == null) {
                    String actual = vista.pedirNombreJugador();
                    if (actual != null) {
                        Jugador jugadorRegistrado = modelo.agregarJugador(actual);
                        if (jugadorRegistrado != null) {
                            this.jugadorAsociado = jugadorRegistrado;
                            System.out.println(this.jugadorAsociado.getNombre());
                            this.modelo.verificarJugadoresListos();
                        } else {
                            vista.mensajeError();
                            this.modelo.iniciarRegistroJugadores();
                        }
//                    if (this.jugadorAsociado == null){
//                        // ver que hacer cuando se llega al limite de jugadores
//                    }
                    } else {
                        salir = vista.opcionSalir();
                        if (this.manejarSalir(salir)) {
                            this.modelo.verificarJugadoresListos();
                        }
                    }
                }
//                else{
//                    vista.mostrarMensaje("Ya se ha ingresado un jugador con el nombre: " + this.jugadorAsociado.getNombre());
//                }
            }
            case FALTAN_JUGADORES -> {
                if (this.soy(this.modelo.getAnfitrion())){
                    if (modelo.isError()){
                        vista.mensajeError();
                        modelo.setError(false);
                        vista.mensajeFaltanJugadores();
                        System.exit(0);
                    }
                }
            }
            case JUGADORES_INGRESADOS -> {
                if (this.soy(this.modelo.getAnfitrion())){
                    vista.mostrarMensaje("Todos los jugadores se han registrado con éxito!");
                    if (this.soy(this.modelo.getAnfitrion())){modelo.configurarJuego();}
                }
            }
            case CANT_FICHAS_INICIALES -> {
                if (this.soy(this.modelo.getAnfitrion())){
                    this.entrada = vista.pedirCantFichas();
                    if (this.entrada != null){
                        this.fichasInicialesController(this.entrada);
                    }
                    else{
                        salir = vista.opcionSalir();
                        if(this.manejarSalir(salir)){modelo.configurarJuego();}
                    }
                }
            }
            case FICHAS_INICIALES -> {
                if (this.soy(this.modelo.getAnfitrion())){
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
                if (this.soy(this.modelo.getAnfitrion())){
                    System.out.println("[CLIENTE] VALOR_CIEGAS, soyAnfitrion=" + soy(modelo.getAnfitrion()));
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
                if (this.soy(this.modelo.manejarTurnos())){
                    vista.mostrarMensaje("repartiendo cartas...");
//                System.out.println("Turno actual: " + modelo.manejarTurnos().getNombre());
                    System.out.println(modelo.getJugadoresRegistrados());
                    System.out.println("Jugador asociado a la vista: " + this.jugadorAsociado.getNombre());

                    modelo.repartirCartas();
                }
                else{
                    System.out.println("error");
                }

            }
            case MOSTRAR_CARTAS -> {
                if (this.soy(this.modelo.manejarTurnos())) {
                    System.out.println("turno actual: "+ modelo.getTurno());
                    vista.mostrarCartas(modelo.cartasTurnoActual());
                    // ver como ahorrar las siguientes 4 líneas de código:
//                    vista.mensajeMostrarApuestaActual(this.modelo.totalApostadoBote());
                    mostrarMenuApuestasActual();
                    this.eventoActual = Evento.APUESTA;
                } else {
                    vista.mostrarMensaje("El jugador con turno actual esta viendo sus cartas.");
                }
            }
            case APUESTA -> {
                if (this.soy(this.modelo.manejarTurnos())){
//                    vista.mensajeMostrarApuestaActual(this.modelo.totalApostadoBote());
                    mostrarMenuApuestasActual();
                    this.eventoActual = Evento.APUESTA;
                }
            }
            case CANT_DESCARTE -> {
                if (this.soy(this.modelo.manejarTurnos())){
                    vista.mensajeDescarte();
                    this.eventoActual = Evento.CANT_DESCARTE;
                }
            }
            case INDICES_DESCARTE -> {
                if (this.soy(this.modelo.manejarTurnos())){
                    vista.mensajeIndices();
                    this.eventoActual = Evento.INDICES_DESCARTE;
                }
            }
            case DEFINIR_GANADORES -> {
                vista.limpiarBarraTexto();
                this.eventoActual = null;
                this.indices.clear();
                this.cantDescarte = 0;
                this.cantDescartadas = 0;
                Jugador ganador = this.modelo.determinarGanador();
                if (ganador == null) {
                    vista.mostrarMensaje("La partida terminó en empate.");
                    vista.mensajeFinal(null);
                }
                else if (this.soy(ganador)){
                    vista.mostrarMensaje("GANASTE!!!");
                    vista.mensajeFinal(ganador.getNombre());
                }
                else{
                    vista.mostrarMensaje("El jugador " + ganador.getNombre() + " ganó la partida.");
                }
            }
            case DECISION -> {
                if (this.soy(this.modelo.getAnfitrion())){
                    pedirDecisionFinal();
                }
            }

        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IModelo) modeloRemoto; // es necesario castear el modelo remoto
    }
}
