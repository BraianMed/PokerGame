package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.List;

public interface IJugador extends ICalcularFichas {
    void reiniciame();

    void recibirCarta(Carta nuevaCarta);

    int totalFichas();

    int cantApuestaActual();

    void restarFichas(int cantidad);

    void recibirFichas(Ficha ficha);

    void apostar(int cantidad, Bote bote);

    void descartar(ArrayList<Integer> indices);

    void retirarse();

    ArrayList<String> devolverCartas();

    void definirManoJugador();

    List<Ficha> getFichas();

    void setFichas(List<Ficha> fichas);

    boolean isEnJuego();

    void setEnJuego(boolean enJuego);

    Mano getMano();

    void setPrimerApostante(boolean primerApostante);

    boolean isPrimerApostante();
}
