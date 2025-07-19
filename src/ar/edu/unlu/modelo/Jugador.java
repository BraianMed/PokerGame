package ar.edu.unlu.modelo;

import java.rmi.RemoteException;
import java.util.*;

public class Jugador extends Usuario implements IJugador {
    private Mano mano;
    private List<Ficha> fichas;
    private boolean enJuego;
    private List<Ficha> apuestaActual;
    private boolean primerApostante;

    public Jugador(String nombre){
        super(nombre);
        this.enJuego = true;
        this.apuestaActual = new ArrayList<>();
        this.fichas = new ArrayList<>();
        this.mano = new Mano();
        this.primerApostante = false;
    }

    @Override
    public void reiniciame(){
        fichas.clear();          // Elimina todas las fichas
        apuestaActual.clear();   // Elimina todas las apuestas
        enJuego = true;          // vuelve al juego
        mano.vaciarMano();       // vacía la mano del jugador.
        primerApostante = false;
    }

    @Override
    public void recibirCarta(Carta nuevaCarta){
        mano.agregarCarta(nuevaCarta);
    }

    @Override
    public int totalFichas(){
        int sumatoria = 0;
        for(Ficha ficha : fichas){
            sumatoria += ficha.getValor();
        }
        System.out.println(sumatoria);
        return sumatoria;
    }
    @Override
    public int cantApuestaActual(){
        int sumatoria = 0;
        for(Ficha ficha : apuestaActual){
            sumatoria += ficha.getValor();
        }
        return sumatoria;
    }
    @Override
    public void restarFichas(int cantidad) {
        // usar un iterador para que me permita eliminar un elemento en medio del ciclo
        if (cantidad > totalFichas()){
            throw new IllegalArgumentException("Tiene cero fichas,el jugador esta fuera!");
        }
        Iterator<Ficha> iterador = fichas.iterator();
        int sumatoria = 0;
        // mientras tenga siguiente en fichas y la sumatoria no llegue a igualar la cantidad a restar
        while (iterador.hasNext() && sumatoria != cantidad) {
            Ficha f = iterador.next();
            // si el valor de la ficha actual menos la cantidad requerida es menor o igual a cero entonces la borro
            // e incremento sumatoria para ir guardando cuanto voy restando.
            if (f.getValor() - cantidad <= 0) {
                sumatoria += f.getValor();
                iterador.remove();
                // si la resta da mayor a cero entonces obtengo la diferencia que me falta para completar la cantidad total
                // actualizo sumatoria sumándole lo que me faltaba para completar la cantidad requerida y me quite del while
                // luego actualizo el valor de la ficha que era mayor que la cantidad para que quede la cantidad restada por la diferencia.
            } else {
                int diferencia = cantidad - sumatoria;
                sumatoria += f.getValor() - sumatoria;
                f.setValor((f.getValor()) - (diferencia) );
            }
        }
    }
    @Override
    public void recibirFichas(Ficha ficha){
        fichas.add(ficha);
    }

    @Override
    public void apostar(int cantidad, Bote bote) throws RemoteException {
        if (cantidad > totalFichas()){
            throw new IllegalArgumentException("No tienes suficiente fichas");
        }
        int sumatoria = 0;
        Iterator<Ficha> iterador = fichas.iterator();
        // mientras fichas tenga siguiente y sumatoria se sume hasta que sea igual que cantidad
        // si sumatoria es igual que cantidad entonces quiere decir que ya aposto la cantidad necesaria.
        while (iterador.hasNext() && sumatoria != cantidad){
            Ficha f = iterador.next();
            // si el valor de la ficha del jugador es menor o igual a la cantidad se suma al bote la ficha actual y se eliminan del jugador.
            if (f.getValor() <= cantidad){
                bote.sumarFichas(f);
                sumatoria += f.getValor();
                apuestaActual.add(f);
                iterador.remove();
            }
            // si diera el caso que el valor de la ficha es mayor a la apuesta entonces se le suma al bote una ficha con el valor de:
            // la diferencia entre la cantidad a apostar y lo que lleva apostando para poder completar la cantidad requerida.
            else{
                int diferencia = cantidad - sumatoria;
                bote.sumarFichas(new Ficha(cantidad - sumatoria));  // se suma al bote la diferencia
                apuestaActual.add(new Ficha(cantidad - sumatoria)); // también se suma a la apuestaActual
                sumatoria += cantidad - sumatoria;  // actualizo sumatoria para que me quite del while
                f.setValor(f.getValor() - (diferencia) ); // se actualiza el valor a lo que ya tenía, menos lo que faltaba agregar a la apuesta
            }
        }
    }

    @Override
    public void descartar(ArrayList<Integer> indices){
        // creo dos arreglos de valores y pintas para poder sacar uno de forma aleatoria y asígnasela al jugador que descarto.
        CartaValor[] valores = CartaValor.values();
        Pinta[] pintas = Pinta.values();
        indices.sort(Collections.reverseOrder()); // Ordena en orden descendente

        for (Integer i : indices){  // recorro los índices.
            mano.getCartas().remove(i.intValue());  // remuevo las cartas del índice actual
            // obtengo los índices aleatorios con random con un rango hasta el tamaño del arreglo (todos los posibles resultados)
            int indiceAleatorio = new Random().nextInt(valores.length);
            int indiceAleatorioPinta = new Random().nextInt(pintas.length);

            // agrego la nueva carta aleatoria a su mano.
            mano.getCartas().add(new Carta(valores[indiceAleatorio],pintas[indiceAleatorioPinta]) );
        }

    }

    @Override
    public void retirarse(){
        setEnJuego(false);
    }

    @Override
    public ArrayList<String> devolverCartas(){
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < mano.getCartas().size(); i++) {
            resultado.add(mano.getCartas().get(i).toString());
        }
        return resultado;
    }

    @Override
    public void definirManoJugador(){
        if (!mano.getCartas().isEmpty()){
            mano.definirMano();
        }
    }

    @Override
    public List<Ficha> getFichas() {
        return fichas;
    }

    @Override
    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    @Override
    public boolean isEnJuego() {
        return enJuego;
    }

    @Override
    public void setEnJuego(boolean enJuego) {
        this.enJuego = enJuego;
    }

    @Override
    public Mano getMano() {
        return mano;
    }

    @Override
    public void setPrimerApostante(boolean primerApostante) {
        this.primerApostante = primerApostante;
    }

    @Override
    public boolean isPrimerApostante() {
        return primerApostante;
    }
}
