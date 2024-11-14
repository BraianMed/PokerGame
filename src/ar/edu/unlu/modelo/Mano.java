package ar.edu.unlu.modelo;

import java.util.*;

public class Mano implements Comparable<Mano>{
    private TipoDeMano tipoDeMano;
    private ArrayList<Carta> cartas;
    private int valorMano;

    public Mano(){
        this.tipoDeMano = null;
        this.cartas = new ArrayList<>();
        this.valorMano = 0;
    }

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

    public void definirMano() {
        // comienzo verificando desde la mano más alta hacia la más baja
        // en caso de que se diera un tipo de mano entonces se asigna al tipo de mano de la instancia actual y retorna.
        if (esEscaleraReal()) {
            return;
        }
        if (esEscaleraColor()) {
            return;
        }
        if (esPoker()) {
            return;
        }
        if (esFull()) {
            return;
        }
        if (esTrio()) {
            return;
        }
        if (esDoblePareja()) {
            return;
        }
        if (esPareja()) {
            return;
        }
        if (esEscalera()) {
            return;
        }
        if (esColor()) {
            return;
        }
        // si no es ninguna de las anteriores entonces el tipo de mano es carta alta.
        if (this.tipoDeMano == null) {
            tipoDeMano = TipoDeMano.CARTA_ALTA;
        }
    }

    public boolean esPoker(){
        HashMap<CartaValor,Integer> map = cartasRepetidas();
        for (Integer conteo : map.values()){    // map.values() devuelve una colección de los valores del map
            // si tiene algún valor (valor del map) estrictamente en 4 entonces tiene poker. Ejemplo: {Q,4}
            if (conteo == 4){
                this.tipoDeMano = TipoDeMano.POKER;
                return true;
            }
        }
        return false;
    }
    public boolean esTrio(){
        HashMap<CartaValor,Integer> map = cartasRepetidas();
        for (Integer conteo : map.values()){    // map.values() devuelve una colección de los valores del map
            // si tiene algún valor (valor del map) estrictamente en 3 entonces tiene trio. Ejemplo: {10,3}
            if (conteo == 3){
                this.tipoDeMano = TipoDeMano.TRIO;
                return true;
            }
        }
        return false;
    }
    public boolean esPareja(){
        HashMap<CartaValor,Integer> map = cartasRepetidas();
        for (Integer conteo : map.values()){    // map.values() devuelve una colección de los valores del map
            // si tiene algún valor (valor del map) estrictamente en 2 entonces tiene pareja. Ejemplo: {A,2}
            if (conteo == 2){
                this.tipoDeMano = TipoDeMano.PAREJA;
                return true;
            }
        }
        return false;
    }
    public boolean esDoblePareja(){
        HashMap<CartaValor,Integer> map = cartasRepetidas();
        int contador = 0;
        for (Integer conteo : map.values()){    // map.values() devuelve una colección de los valores del map
            // si tiene algún valor en 2 entonces tiene pareja.
            if (conteo == 2){
                contador++;
            }
            // Si contador es mayor que 2 quiere decir que tiene más de una pareja por ende no será doble. (cosa que sería ilógica además, ya que solo tenemos 5 cartas)
            if (contador > 2){
                return false;
            }
        }
        // si contador es igual a 2 quiere decir que tenemos dos parejas.
        if (contador == 2){
            this.tipoDeMano = TipoDeMano.DOBLE_PAREJA;
            return true;
        }
        return false;
    }
    public boolean esFull(){
        boolean tienePareja = esPareja();
        boolean tieneTrio = esTrio();
        // si se cumple que tiene dentro del map una clave con valor estrictamente en 2 y otra estrictamente en 3 entonces es full house.
        if (tienePareja && tieneTrio){
            this.tipoDeMano = TipoDeMano.FULL_HOUSE;
            return true;
        }
        return false;
    }

    public HashMap<CartaValor,Integer> cartasRepetidas() {
        // creo una tabla hash donde la clave será el valor de la carta y el valor serán las veces que se repita
        HashMap<CartaValor,Integer> resultado = new HashMap<>();
        for (Carta c : cartas){
                // obtengo el valor actual de la carta en mano
                CartaValor valor = c.getValor();
                // en resultado uso "put" para agregar la clave a la tabla, y de valor va a asignarle lo que obtenga
                // de usar getOrDefault que obtiene el valor asociado a la clave o un valor por default, se le suma 1 si fuera el caso que ya se repitió
                // y si no estuviera repetido entonces se le suma en 1 para determinar que al menos está una vez.
                resultado.put(valor,resultado.getOrDefault(valor,0) + 1);

        }
        return resultado;
    }

    public boolean esEscalera(){
        HashSet<Integer> valores = new HashSet<>();
        // creo un set que me permite almacenar valores irrepetibles

        // recorro las cartas de la mano y voy agregando los ordinales de enum de sus valores
        for (Carta c : cartas){
            valores.add(c.getValor().ordinal());
        }
        // si el set tiene menos valores quiere decir que había alguna repetición por lo que no puede ser escalera.
        if(valores.size() != 5){
            return false;
        }
        // Obtener el valor mínimo y máximo
        int min = Collections.min(valores);
        int max = Collections.max(valores);

        // Si la diferencia entre el valor mínimo y máximo es de 4 será escalera indistintamente del orden que tengan.
        // 5, 2, 4, 8, 9 si tuviera estas cartas con saber que 9 - 2 no es igual a 4 quita que sea una escalera sin necesidad de ordenarlo
        if(max - min == 4){
            this.tipoDeMano = TipoDeMano.ESCALERA;
            return true;
        }
        return false;
    }

    public boolean esEscaleraReal(){
        boolean esA = false;
        boolean esK = false;
        boolean esQ = false;
        boolean esJ = false;
        boolean esDiez = false;

        // primeramente, verifico que sea color, ya que una escalera real tienen todos el mismo palo
        if (esColor()){
            for (Carta c : cartas){
                if (c.getValor() == CartaValor.A){
                    esA = true;
                }
                else if (c.getValor() == CartaValor.K){
                    esK = true;
                }
                else if(c.getValor() == CartaValor.Q){
                    esQ = true;
                }
                else if (c.getValor() == CartaValor.J){
                    esJ= true;
                }
                else if (c.getValor() == CartaValor.DIEZ){
                    esDiez = true;
                }
            }
        }
        // si al terminar de recorrer todas las cartas de la mano tengo absolutamente todo en true quiere decir que es escalera real
        if (esA && esK && esQ && esJ && esDiez){
            this.tipoDeMano = TipoDeMano.ESCALERA_REAL;
            return true;
        }
        return false;
    }

    public boolean esColor(){
        Iterator<Carta> iterator = cartas.iterator();
        Pinta pintaInicial = iterator.next().getPinta();    // obtengo la pinta del primer elemento
        while (iterator.hasNext()){ // mientras tenga cartas
            Carta i = iterator.next();
            // comparo la primera pinta con las demás, si diera el caso que alguna es distinta entonces no es color
            if (pintaInicial != i.getPinta()){
                return false;
            }
        }
        // caso contrario asignó a la instancia actual de mano el tipoDeMano
        tipoDeMano = TipoDeMano.COLOR;
        return true;
    }

    public boolean esEscaleraColor(){
        if(esColor() && esEscalera()){
            this.tipoDeMano = TipoDeMano.ESCALERA_COLOR;
            return true;
        }
        return false;
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
        // si los tipos de manos son iguales entonces se evalúan los valores de sus manos.
        else if (this.tipoDeMano == o.getTipoDeMano() && j1 > j2){
            return 1;
        }
        else if (this.tipoDeMano == o.getTipoDeMano() && j1 < j2){
            return -1;
        }
        // si no quiere decir que las manos son exactamente iguales
        return 0;
    }
    public void agregarCarta(Carta carta){
        cartas.add(carta);
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

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }
}
