package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> cartas;


    public Baraja() {
        cartas = new ArrayList<>();

    }

    public void barajar(){
        Collections.shuffle(cartas);
    }
    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}
