import ar.edu.unlu.modelo.Bote;
import ar.edu.unlu.modelo.Ficha;
import ar.edu.unlu.modelo.Jugador;
import static org.junit.Assert.*;
import org.junit.Test;

public class JugadorTest {

    @Test
    public void validarRestarFichas(){
        Jugador jugador = new Jugador("Pedro");
        Ficha f1 = new Ficha(100);
        Ficha f2 = new Ficha(200);
        Ficha f3 = new Ficha(800);
        jugador.recibirFichas(f1);
        jugador.recibirFichas(f2);
        jugador.recibirFichas(f3);
        jugador.restarFichas(600);

        assertEquals(jugador.totalFichas(),500);
    }
    @Test
    public void validarApostar(){
        Jugador jugador = new Jugador("Pedro");
        Bote bote = new Bote();
        Ficha f1 = new Ficha(100);
        Ficha f2 = new Ficha(200);
        Ficha f3 = new Ficha(800);
        jugador.recibirFichas(f1);
        jugador.recibirFichas(f2);
        jugador.recibirFichas(f3);
        jugador.apostar(600,bote);

        assertEquals(jugador.totalFichas(),500);
        assertEquals(bote.totalFichas(),600);
    }
}
