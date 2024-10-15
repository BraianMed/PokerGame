package ar.edu.unlu.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();
        for(Pinta pinta : Pinta.values()){  //Enum.values() devuelve una lista de Pinta en este caso, en el orden en el que fueron declarados.
            for (CartaValor valor : CartaValor.values()){
                cartas.add(new Carta(valor,pinta)); // agrego cartas a la baraja del primer pinta que saco e itero n veces (valores de carta)
            }
        }
        this.barajar();
    }

    public void barajar(){
        Collections.shuffle(cartas);
    }

    public void repartirCarta(Jugador jugador){
        while(jugador.getMano().getCartas().size() < 5){
            jugador.recibirCarta(cartas.get(0));
            cartas.remove(cartas.get(0));
        }
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

}
