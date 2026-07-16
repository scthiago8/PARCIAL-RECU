/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial.recu;

public class Socio extends Persona implements IIdentificable {
    private String id;
    private String tipo; // "Estudiante" o "Docente"

    public Socio(String id, String nombre, String email, String tipo) {
        super(nombre, email);
        this.id = id;
        this.tipo = tipo;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    // Polimorfismo: Límite de préstamos según el tipo de socio
    public int getLimitePrestamos() {
        if (tipo.equalsIgnoreCase("Docente")) {
            return 5;
        }
        return 3;
    }

    @Override
    public String toString() {
        return "Socio [ID: " + id + " | Nombre: " + getNombre() + " | Tipo: " + tipo + " | Límite: " + getLimitePrestamos() + "]";
    }
}
