package ar.edu.unlu.modelo;

import ar.edu.unlu.controlador.IObservador;

public interface IObservable {
    public void agregarObservador(IObservador observador);
    public void eliminarObservador(IObservador observador);
    public void notificar(Object o);
}
