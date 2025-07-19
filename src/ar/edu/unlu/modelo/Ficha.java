package ar.edu.unlu.modelo;

import java.io.Serializable;

public class Ficha implements Serializable {
    private int valor;
    private static final long serialVersionUID = 5603087402016698850L;

    public Ficha(int valor){
        this.valor = valor;
    }

    public void repartirFicha(Jugador jugador){
        jugador.getFichas().add(this);
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
