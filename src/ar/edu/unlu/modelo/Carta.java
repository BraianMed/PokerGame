package ar.edu.unlu.modelo;

public class Carta {
    private CartaValor valor;
    private Pinta pinta;

    public Carta(CartaValor valor,Pinta pinta){
        this.valor = valor;
        this.pinta = pinta;
    }
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

    @Override
    public String toString() {
        return valor + " de " + pinta;
    }
}
