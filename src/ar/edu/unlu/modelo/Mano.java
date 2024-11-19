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
        // defino las manos de los dos jugadores para evitar un tipoDeMano null
        this.definirMano();
        mano.definirMano();
        Mano ganador = new Mano();
        if (this.compareTo(mano) < 0){
            ganador = mano;
        }
        else if (this.compareTo(mano) > 0){
            ganador = this;
        }
        return ganador; // si las manos son iguales y la mano "ganador" es null, entonces se notificara que es empate.
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
                resultado.put(valor,resultado.getOrDefault(valor,0) + 1);   // inserta en valor, el valor existente +1 o 0 por default

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
        if (cartas == null || cartas.isEmpty()) {
            return false;
        }
        Iterator<Carta> it = cartas.iterator();
        if (!it.hasNext()) {
            return false;
        }
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
    public boolean equals(Object o) {
        // Si son dos objetos con el mismo objectID entonces retorna true. (es el mismo objeto)
        if (this == o) return true;
        // si la mano "O" es null o el tipo estático de la instancia actual es distinto que el de "o" entonces será falso.
        if (o == null || getClass() != o.getClass()) return false;
        Mano mano = (Mano) o;
        // si no, comparo que las cartas de ambas manos sean iguales al mismo tiempo que los tipoDeMano.
        return cartas.equals(mano.cartas) && tipoDeMano.equals(mano.tipoDeMano);
    }

    @Override
    public int compareTo(Mano o) {
        // comparo por el tipoDeMano y si no son iguales entonces retorno el resultado de la comparación
        int tipoComparacion = this.tipoDeMano.compareTo(o.getTipoDeMano());
        if (tipoComparacion != 0) return tipoComparacion;
        // si no, comparo los valores ordinales sumados en el map, ya que los valores son los ordinales de los enum CartaValor.
        return Integer.compare(this.valorTotalCartas(),o.valorTotalCartas());
    }
    @Override
    public int hashCode() {
        return Objects.hash(cartas, tipoDeMano);
    }

    private int valorTotalCartas() {
        if (this.tipoDeMano == TipoDeMano.PAREJA || this.tipoDeMano == TipoDeMano.DOBLE_PAREJA || this.tipoDeMano == TipoDeMano.TRIO || this.tipoDeMano == TipoDeMano.POKER) {
            // Si es un tipoDeMano con cartas repetidas entonces se obtiene el valor ordinal de una de las cartas repetidas.
            return obtenerValorDeMano();
        } else {
            // Para el resto de manos (como Escalera, Escalera Color, etc.), suma los valores totales.
            int suma = 0;
            for (Carta c : cartas) {
                suma += c.getValor().ordinal();
            }
            return suma;
        }
    }
//    private int desempate(){
//        int suma = 0;
//        for (Carta c : cartas) {
//            suma += c.getValor().ordinal();
//        }
//        return suma;
//    }

    private int obtenerValorDeMano() {
        // Dependiendo el tipo de mano, voy a devolver el valor más relevante
        switch (this.tipoDeMano) {
            case PAREJA:
                // Devuelve el valor de la carta que forma la pareja
                return obtenerValorDeManoPareja();

            case DOBLE_PAREJA:
                // Devuelve el valor de la carta más alta de las parejas
                List<Carta> parejas = valorRepetidas(2); // Cartas con el mismo valor
                return Math.max(parejas.get(0).getValor().ordinal(), parejas.get(1).getValor().ordinal());

            case TRIO:
                // Devuelve el valor de la carta que forma el trío
                return obtenerValorDeManoTrio();

            case POKER:
                // Devuelve el valor de la carta que forma el poker
                return obtenerValorDeManoPoker();

            // incognita (?)
            case ESCALERA:
            case ESCALERA_COLOR:
            case FULL_HOUSE:
            case COLOR:
                // Si es una mano con varias cartas en orden, devuelve la carta más alta
                // Para una escalera o escalera color, será la carta más alta
                return valorCartaOrdinal(0); // Devuelve la carta más alta en estos casos

            default:
                // Para otras manos, simplemente devolvemos el valor de la carta más alta
                return valorCartaOrdinal(0); // Carta más alta si no es una pareja, trío, etc.
        }
    }

    private int obtenerValorDeManoPareja() {
        // Obtengo las cartas que tienen el mismo valor
        List<Carta> pareja = valorRepetidas(2); // 2 cartas con el mismo valor

        // Si encuentra una pareja entonces devuelvo el valor de una de las cartas.
        if (!pareja.isEmpty()) {
            return pareja.get(0).getValor().ordinal(); // Devuelve el valor de la carta de la pareja
        }

        // en caso de no encontrar nada retornara -1
        return -1;
    }

    private int obtenerValorDeManoPoker() {
        // Obtengo las cartas que tienen el mismo valor
        List<Carta> poker = valorRepetidas(4); // 4 cartas con el mismo valor

        // Si encuentra poker entonces devuelvo el valor de una de las cartas.
        if (!poker.isEmpty()) {
            return poker.get(0).getValor().ordinal(); // Devuelve el valor de la carta que forma poker
        }

        // en caso de no encontrar nada retornara -1
        return -1;
    }

    private int obtenerValorDeManoTrio() {
        // Obtengo las cartas que tienen el mismo valor
        List<Carta> trio = valorRepetidas(3); // 3 cartas con el mismo valor

        // Si encuentra un trio entonces devuelvo el valor de una de las cartas.
        if (!trio.isEmpty()) {
            return trio.get(0).getValor().ordinal(); // Devuelve el valor de la carta del trio
        }

        // en caso de no encontrar nada retornara -1
        return -1;
    }

    private int valorCartaOrdinal(int indice) {
        // Ordena las cartas y devuelve la carta de valor más alto según la cantidad que esté buscando
        List<Carta> cartasOrdenadas = new ArrayList<>(this.cartas);
        // para ordenar las cartas creo el comparador como clase anónima (como en el caso del ActionListener con ActionPerformed)
        // lo ordeno de forma descendente.
        Collections.sort(cartasOrdenadas, new Comparator<Carta>() {
            @Override
            public int compare(Carta c1, Carta c2) {
                return c2.getValor().ordinal() - c1.getValor().ordinal();
            }
        });

        // Si la mano tiene varias cartas iguales, seleccionamos la que corresponde a la cantidad
        return cartasOrdenadas.get(indice).getValor().ordinal();
    }


    private List<Carta> valorRepetidas(int cantidad) {
        // Busca y devuelve las cartas que tienen el mismo valor (parejas, tríos, etc.)
        Map<CartaValor, List<Carta>> valorCartas = new HashMap<>();

        // si no existe una lista asociada a la clave CartaValor entonces se le asocia una nueva lista con esas cartas.
        for (Carta carta : this.cartas) {
            // Verificamos si la clave (carta.getValor()) ya existe en el mapa
            if (!valorCartas.containsKey(carta.getValor())) {
                // Si no existe, agregamos una nueva entrada con un ArrayList vacío
                valorCartas.put(carta.getValor(), new ArrayList<>());
            }
            // Agregamos la carta al ArrayList correspondiente a la clave
            valorCartas.get(carta.getValor()).add(carta);
        }

        // Filtramos para obtener las cartas que tienen la cantidad que necesitamos (por ejemplo, 2 para una pareja)
        // entrySet() devuelve un conjunto del tipo Map.Entry que representa una entrada del mapa, una relación clave valor.
        // Map.Entry representa una entrada del mapa, por lo que se puede acceder al valor con getValue(), o a la clave con getKey()
        for (Map.Entry<CartaValor, List<Carta>> entry : valorCartas.entrySet()) {
            if (entry.getValue().size() == cantidad) {  // si las cartas repetidas son igual a la cantidad
                return entry.getValue();    // entonces retorno la lista que está en valor.
            }
        }
        return Collections.emptyList(); // Si no se encuentra la cantidad esperada de cartas devuelve una lista vacía inmutable.
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
