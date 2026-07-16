/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial.recu;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Repositorio<T extends IIdentificable> implements Serializable {
    private List<T> elementos;

    public Repositorio() {
        this.elementos = new ArrayList<>();
    }

    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    public List<T> obtenerTodos() {
        return new ArrayList<>(elementos);
    }

    public T buscarPorId(String id) {
        for (T elemento : elementos) {
            if (elemento.getId().equalsIgnoreCase(id)) {
                return elemento;
            }
        }
        return null;
    }
}