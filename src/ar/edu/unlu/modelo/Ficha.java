package ar.edu.unlu.modelo;

public class Ficha {
    private int valor;

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
