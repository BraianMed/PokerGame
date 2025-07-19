package ar.edu.unlu.controlador;

import ar.edu.unlu.modelo.Evento;
import ar.edu.unlu.modelo.IModelo;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.modelo.Jugador;
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
        vista.setControlador(this);
        this.setModeloRemoto(modelo);
        this.indices = new ArrayList<>();
        this.cantDescarte = 0;
        this.cantDescartadas = 0;
    }

    public String ganadorController() throws RemoteException {
        modelo.sumarVictorias();
        modelo.sumarDerrotas();
        return modelo.determinarGanador().getNombre();
    }

    public String jugadorTurnoController() throws RemoteException {
        return modelo.nombreJugadorActual();
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
    public boolean validarSubir(int apuestaActual) throws RemoteException {
        if (apuestaActual > modelo.getCiegaGrande()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean validarCiegasController(int ciegaActual) throws RemoteException {
        if (ciegaActual > modelo.getJugadores().get(0).totalFichas() && (ciegaActual / 2) > modelo.getJugadores().get(0).totalFichas() ){
            return false;
        }
        else{
            return true;
        }
    }

    public void asignarCiegas() throws RemoteException {
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
        System.out.println("comunicarEntrada: jugador=" + jugadorAsociado.getNombre()
                + " turnoModelo=" + modelo.manejarTurnos().getNombre());

        if (eventoActual.equals(Evento.APUESTA)){
            if (this.modelo.manejarTurnos().equals(this.jugadorAsociado)){
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

    private boolean manejarIgualar() throws RemoteException {
        boolean resultado;
        try {
            modelo.igualarJugador();
            this.jugadorAsociado.setPrimerApostante(false);
            resultado = true;
        } catch (Exception e) {
            vista.mensajeErrorIgualar(this.jugadorAsociado.getNombre(),modelo.getApuestaActual(),this.jugadorAsociado.totalFichas());
            resultado = false;
            // ver como hacer cuando quiere igualar y no tiene saldo...
        }
        vista.mensajeIgualar(this.jugadorAsociado.getNombre());
        return resultado;
    }
    public boolean manejarSubir() throws RemoteException {
        String apuesta = vista.pedirApuesta();
        boolean resultado;
        int apuestaEntera = this.valorFichaApuesta(apuesta);
        if (apuestaEntera != -1 && this.validarSubir(apuestaEntera)){
            try{
                this.modelo.apostarJugador(apuestaEntera);
                vista.mensajeAposto(this.jugadorAsociado.getNombre());
                this.jugadorAsociado.setPrimerApostante(false);
                resultado = true;
            }
            catch (Exception e){
                vista.mensajeError();
                vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
                resultado = false;
            }
        }
        else{
            vista.mensajeError();
            vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
            resultado = false;
        }
        return resultado;
    }

    public void manejarPasar() throws RemoteException {
        vista.mensajePaso(this.jugadorAsociado.getNombre());
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
                        this.jugadorAsociado = modelo.agregarJugador(actual);
                        System.out.println(this.jugadorAsociado.getNombre());
                        this.modelo.verificarJugadoresListos();
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
            case JUGADORES_INGRESADOS -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    vista.mostrarMensaje("Todos los jugadores se han registrado con éxito!");
                    if (modelo.getAnfitrion().equals(this.jugadorAsociado)){modelo.configurarJuego();}
                }
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
                    // ver como ahorrar las siguientes 4 líneas de código:
                    vista.mensajeMostrarApuestaActual(this.modelo.totalApostadoBote());
                    vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(), this.jugadorAsociado.getNombre(), this.jugadorAsociado.totalFichas());
                    this.eventoActual = Evento.APUESTA;
                } else {
                    vista.mostrarMensaje("El jugador con turno actual esta viendo sus cartas.");
                }
            }
            case APUESTA -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mensajeMostrarApuestaActual(this.modelo.totalApostadoBote());
                    vista.menuApuestas(this.jugadorAsociado.isPrimerApostante(),this.jugadorAsociado.getNombre(),this.jugadorAsociado.totalFichas());
                    this.eventoActual = Evento.APUESTA;
                }
            }
            case CANT_DESCARTE -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mensajeDescarte();
                    this.eventoActual = Evento.CANT_DESCARTE;
                }
            }
            case INDICES_DESCARTE -> {
                if (modelo.manejarTurnos().equals(this.jugadorAsociado)){
                    vista.mensajeIndices();
                    this.eventoActual = Evento.INDICES_DESCARTE;
                }
            }
            case DEFINIR_GANADORES -> {
                vista.limpiarBarraTexto();
                vista.limpiarTextoPlano();
                if (modelo.determinarGanador().equals(this.jugadorAsociado)){
                    vista.mostrarMensaje("GANASTE!!!");
                    vista.mensajeFinal(modelo.determinarGanador().getNombre());
                    modelo.resultados();
                }
                else{
                    vista.mostrarMensaje("El jugador " + modelo.determinarGanador().getNombre() + " gano la partida.");
                }
            }
            case DECISION -> {
                if (modelo.getAnfitrion().equals(this.jugadorAsociado)){
                    int opcion = vista.mensajeReiniciarJuego();
                    this.manejarDesicion(opcion);
                }
            }

        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IModelo) modeloRemoto; // es necesario castear el modelo remoto
    }
}
