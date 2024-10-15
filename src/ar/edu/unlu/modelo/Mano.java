package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class Mano implements Comparable<Mano>{
    private TipoDeMano tipoDeMano;
    private ArrayList<Carta> cartas;
    private int valorMano;

    public Mano evaluarMano(Mano mano){
        Mano ganador = new Mano();
        if (this.compareTo(mano) < 0){
            ganador = mano;
        }
        else if (this.compareTo(mano) > 0){
            ganador = this;
        }
        return ganador;
    }

    public void definirMano(){
        esEscaleraReal();
        esColor();

    }

    public void esEscaleraReal(){
        int contador = 0;
        for (Carta c : cartas){
            if (c.getValor() == CartaValor.A){
                contador++;
            }
            if (c.getValor() == CartaValor.K){
                contador++;
            }
            if (c.getValor() == CartaValor.Q){
                contador++;
            }
            if (c.getValor() == CartaValor.J){
                contador++;
            }
            if (c.getValor() == CartaValor.DIEZ){
                contador++;
            }
        }
        if (contador == 5){
            this.tipoDeMano = TipoDeMano.ESCALERA_REAL;
        }
    }

    public void esColor(){
        TipoDeMano mano = TipoDeMano.COLOR;
        Iterator<Carta> iterator = cartas.iterator();
        Pinta pintaInicial = iterator.next().getPinta();    // obtengo la pinta del primer elemento
        while (iterator.hasNext()){ // mientras tenga cartas
            Carta i = iterator.next();
            // comparo la primera pinta con las demás, si diera el caso que alguna es distinta entonces no es color
            if (pintaInicial != i.getPinta()){
                mano = null;
            }
        }
        // caso contrario asignó a la instancia actual de mano el tipoDeMano
        if (mano == TipoDeMano.COLOR){
            tipoDeMano = TipoDeMano.COLOR;
        }
    }

    public void vaciarMano(){
        cartas.clear();
        valorMano = 0;
        tipoDeMano = null;

    }

    @Override
    public int compareTo(Mano o) {
        // comparar el tipo de mano + cuanto vale su mano;
        int comp = 0;
        int j1 = 0;
        int j2 = 0;
        // comparo el valor de las cartas de ambos jugadores y guardo un puntaje para ambos
        for (Carta c : cartas){
            for (Carta c2 : o.getCartas()){
                comp = c.getValor().compareTo(c2.getValor());
                if (comp > 0){
                    j1++;
                }
                else if (comp < 0){
                    j2++;
                }
                else{
                    j1++;
                    j2++;
                }
            }
        }
        // si el tipo de mano de la instancia actual es mayor que la Mano o, y el valor de las cartas de la instancia actual supera al
        // jugador 2 entonces la instancia actual es mejor mano
        if (this.tipoDeMano.compareTo(o.getTipoDeMano()) > 0 && j1 > j2){
            return 1;
        }
        // todo lo contrario al primer caso ganaría la mano del jugador 2
        else if (this.tipoDeMano.compareTo(o.getTipoDeMano()) < 0 && j1 < j2){
            return -1;
        }
        // si no quiere decir que las manos son exactamente iguales
        return 0;
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public int getValorMano() {
        return valorMano;
    }

    public TipoDeMano getTipoDeMano() {
        return tipoDeMano;
    }
}
