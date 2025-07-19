package ar.edu.unlu.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bote implements ICalcularFichas, Serializable {
    private List<Ficha> fichas;
    private static final long serialVersionUID = -7934591226417571440L;

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
