/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial.recu;

import java.io.Serializable;

public class Libro implements IIdentificable, Serializable {
    private String isbn;
    private String titulo;
    private String autor;
    private int ejemplaresTotales;
    private int ejemplaresPrestados;
    private int contadorPrestamos;

    public Libro(String isbn, String titulo, String autor, int ejemplaresTotales) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresPrestados = 0;
        this.contadorPrestamos = 0;
    }

    @Override
    public String getId() {
        return isbn;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getEjemplaresTotales() { return ejemplaresTotales; }
    public int getEjemplaresPrestados() { return ejemplaresPrestados; }
    public int getContadorPrestamos() { return contadorPrestamos; }

    public int getEjemplaresDisponibles() {
        return ejemplaresTotales - ejemplaresPrestados;
    }

    public void prestar() {
        this.ejemplaresPrestados++;
        this.contadorPrestamos++;
    }

    public void devolver() {
        if (this.ejemplaresPrestados > 0) {
            this.ejemplaresPrestados--;
        }
    }

    @Override
    public String toString() {
        return "Libro [ISBN: " + isbn + " | Título: " + titulo + " | Autor: " + autor + " | Disponibles: " + getEjemplaresDisponibles() + "/" + ejemplaresTotales + "]";
    }
}