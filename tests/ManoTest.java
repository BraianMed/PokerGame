import ar.edu.unlu.modelo.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class ManoTest {

    @Test
    public void validarTipoDeManoPoker(){
        Carta carta1 = new Carta(CartaValor.Q, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.Q, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.Q, Pinta.DIAMANTE);
        Carta carta4 = new Carta(CartaValor.Q, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.CINCO, Pinta.DIAMANTE);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.POKER);

    }
    @Test
    public void validarTipoDeManoTrio(){
        Carta carta1 = new Carta(CartaValor.Q, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.Q, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.Q, Pinta.DIAMANTE);
        Carta carta4 = new Carta(CartaValor.SIETE, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.SEIS, Pinta.PICA);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.TRIO);
    }
    @Test
    public void validarTipoDeManoPareja(){
        Carta carta1 = new Carta(CartaValor.Q, Pinta.PICA);
        Carta carta2 = new Carta(CartaValor.Q, Pinta.CORAZON);
        Carta carta3 = new Carta(CartaValor.SEIS, Pinta.CORAZON);
        Carta carta4 = new Carta(CartaValor.NUEVE, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.DOS, Pinta.DIAMANTE);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.PAREJA);
    }
    @Test
    public void validarTipoDeManoDoblePareja(){
        Carta carta1 = new Carta(CartaValor.J, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.J, Pinta.TREBOL);
        Carta carta3 = new Carta(CartaValor.NUEVE, Pinta.DIAMANTE);
        Carta carta4 = new Carta(CartaValor.NUEVE, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.DOS, Pinta.DIAMANTE);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.DOBLE_PAREJA);
    }
    @Test
    public void validarTipoDeManoFull(){
        Carta carta1 = new Carta(CartaValor.A, Pinta.DIAMANTE);
        Carta carta2 = new Carta(CartaValor.A, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.A, Pinta.CORAZON);
        Carta carta4 = new Carta(CartaValor.SIETE, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.SIETE, Pinta.DIAMANTE);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.FULL_HOUSE);
    }
    @Test
    public void validarTipoDeManoEscalera(){
        Carta carta1 = new Carta(CartaValor.DIEZ, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.NUEVE, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.OCHO, Pinta.DIAMANTE);
        Carta carta4 = new Carta(CartaValor.SIETE, Pinta.TREBOL);
        Carta carta5 = new Carta(CartaValor.SEIS, Pinta.PICA);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.ESCALERA);
    }

    @Test
    public void validarTipoDeManoEscaleraReal(){
        Carta carta1 = new Carta(CartaValor.A, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.K, Pinta.CORAZON);
        Carta carta3 = new Carta(CartaValor.Q, Pinta.CORAZON);
        Carta carta4 = new Carta(CartaValor.J, Pinta.CORAZON);
        Carta carta5 = new Carta(CartaValor.DIEZ, Pinta.CORAZON);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.ESCALERA_REAL);
    }

    @Test
    public void validarTipoDeManoColor(){
        Carta carta1 = new Carta(CartaValor.A, Pinta.DIAMANTE);
        Carta carta2 = new Carta(CartaValor.J, Pinta.DIAMANTE);
        Carta carta3 = new Carta(CartaValor.OCHO, Pinta.DIAMANTE);
        Carta carta4 = new Carta(CartaValor.CINCO, Pinta.DIAMANTE);
        Carta carta5 = new Carta(CartaValor.SIETE, Pinta.DIAMANTE);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.COLOR);
    }
    @Test
    public void validarTipoDeManoEscaleraColor(){
        Carta carta1 = new Carta(CartaValor.DIEZ, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.NUEVE, Pinta.CORAZON);
        Carta carta3 = new Carta(CartaValor.OCHO, Pinta.CORAZON);
        Carta carta4 = new Carta(CartaValor.SIETE, Pinta.CORAZON);
        Carta carta5 = new Carta(CartaValor.SEIS, Pinta.CORAZON);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.ESCALERA_COLOR);
    }
    @Test
    public void validarTipoDeManoCartaAlta(){
        Carta carta1 = new Carta(CartaValor.A, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.Q, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.SEIS, Pinta.PICA);
        Carta carta4 = new Carta(CartaValor.CINCO, Pinta.DIAMANTE);
        Carta carta5 = new Carta(CartaValor.DIEZ, Pinta.TREBOL);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas);
        mano1.definirMano();

        assertEquals(mano1.getTipoDeMano(), TipoDeMano.CARTA_ALTA);
    }

    @Test
    public void validarEvaluarManoCorrectamente(){
        Carta carta1 = new Carta(CartaValor.DIEZ, Pinta.CORAZON);
        Carta carta2 = new Carta(CartaValor.DIEZ, Pinta.PICA);
        Carta carta3 = new Carta(CartaValor.DIEZ, Pinta.PICA);
        Carta carta4 = new Carta(CartaValor.SIETE, Pinta.DIAMANTE);
        Carta carta5 = new Carta(CartaValor.CINCO, Pinta.TREBOL);
        ArrayList<Carta> cartas1 = new ArrayList<>();
        cartas1.add(carta1);
        cartas1.add(carta2);
        cartas1.add(carta3);
        cartas1.add(carta4);
        cartas1.add(carta5);
        Mano mano1 = new Mano();
        mano1.setCartas(cartas1);
        mano1.definirMano();

        Carta carta6 = new Carta(CartaValor.Q, Pinta.CORAZON);
        Carta carta7 = new Carta(CartaValor.Q, Pinta.PICA);
        Carta carta8 = new Carta(CartaValor.Q, Pinta.DIAMANTE);
        Carta carta9 = new Carta(CartaValor.DIEZ, Pinta.TREBOL);
        Carta carta0 = new Carta(CartaValor.DIEZ, Pinta.PICA);
        ArrayList<Carta> cartas2 = new ArrayList<>();

        cartas2.add(carta6);
        cartas2.add(carta7);
        cartas2.add(carta8);
        cartas2.add(carta9);
        cartas2.add(carta0);
        Mano mano2 = new Mano();
        mano2.setCartas(cartas2);
        mano2.definirMano();

        assertEquals(mano1.evaluarMano(mano2),mano2);

    }
}


