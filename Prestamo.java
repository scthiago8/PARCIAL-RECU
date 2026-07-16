/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial.recu;

import java.io.Serializable;
import java.time.LocalDate;

public class Prestamo implements IIdentificable, Serializable {
    private String id;
    private Socio socio;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private boolean activo;

    public Prestamo(String id, Socio socio, Libro libro) {
        this.id = id;
        this.socio = socio;
        this.libro = libro;
        this.fechaPrestamo = LocalDate.now();
        this.activo = true;
    }

    @Override
    public String getId() {
        return id;
    }

    public Socio getSocio() { return socio; }
    public Libro getLibro() { return libro; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public boolean isActivo() { return activo; }

    public void finalizarPrestamo() {
        this.activo = false;
    }

    @Override
    public String toString() {
        return "Préstamo [ID: " + id + " | Socio: " + socio.getNombre() + " | Libro: " + libro.getTitulo() + " | Estado: " + (activo ? "ACTIVO" : "DEVUELTO") + "]";
    }
}