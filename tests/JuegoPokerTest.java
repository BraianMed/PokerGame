import org.junit.Test;
import static org.junit.Assert.*;
import ar.edu.unlu.modelo.Ficha;
import ar.edu.unlu.modelo.JuegoPoker;
import ar.edu.unlu.modelo.Jugador;

import java.rmi.RemoteException;

public class JuegoPokerTest {

    @Test
    public void validarRondaApuestas(){

    }

    @Test
    public void igualarEnSegundaFaseDebeRestarFichasSinResetearApuestaActual() throws RemoteException {
        JuegoPoker juego = new JuegoPoker();
        juego.setCantidadJugadores(2);
        Jugador braian = juego.agregarJugador("braian");
        Jugador juan = juego.agregarJugador("juan");
        braian.recibirFichas(new Ficha(1000));
        juan.recibirFichas(new Ficha(1000));
        juego.inicializarCiegas(10, 20);
        juego.asignarCiegas();

        juego.igualarJugador();
        juego.gestionVuelta();
        juego.gestionVuelta();
        juego.igualarJugador();
        juego.gestionVuelta();
        juego.gestionVuelta();

        assertEquals(20, juego.getApuestaActual());
        int fichasAntesDeIgualar = juego.manejarTurnos().totalFichas();
        juego.igualarJugador();

        assertEquals(fichasAntesDeIgualar - 20, juego.manejarTurnos().totalFichas());
    }

    @Test
    public void igualarEnTodasLasFasesDebeLlegarAlResultado() throws RemoteException {
        JuegoPoker juego = new JuegoPoker();
        juego.setCantidadJugadores(2);
        Jugador braian = juego.agregarJugador("braian");
        Jugador juan = juego.agregarJugador("juan");
        braian.recibirFichas(new Ficha(1000));
        juan.recibirFichas(new Ficha(1000));
        juego.inicializarCiegas(10, 20);
        juego.asignarCiegas();

        juego.igualarJugador();
        juego.gestionVuelta();
        juego.gestionVuelta();
        juego.igualarJugador();
        juego.gestionVuelta();
        juego.gestionVuelta();
        juego.igualarJugador();
        juego.gestionVuelta();
        juego.igualarJugador();
        juego.gestionVuelta();

        assertEquals(2, juego.getJugadores().size());
    }
}
