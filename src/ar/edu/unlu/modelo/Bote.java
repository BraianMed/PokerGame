package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.List;

public class Bote implements ICalcularFichas{
    private List<Ficha> fichas;

    public Bote(){
        this.fichas = new ArrayList<>();
    }

    public void sumarFichas(Ficha ficha){
        fichas.add(ficha);
    }

    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    @Override
    public int totalFichas(){
        int sumatoria = 0;
        for(Ficha ficha : fichas){
            sumatoria += ficha.getValor();
        }
        return sumatoria;
    }
}
