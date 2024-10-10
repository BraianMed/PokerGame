package ar.edu.unlu.modelo;

public class Carta {
    private CartaValor valor;
    private Pinta pinta;


    public CartaValor getValor() {
        return valor;
    }

    public Pinta getPinta() {
        return pinta;
    }

    public void setPinta(Pinta pinta) {
        this.pinta = pinta;
    }

    public void setValor(CartaValor valor) {
        this.valor = valor;
    }
}
