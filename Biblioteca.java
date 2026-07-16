/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial.recu;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Biblioteca implements IExportable, Serializable {
    private Repositorio<Socio> repoSocios;
    private Repositorio<Libro> repoLibros;
    private Repositorio<Prestamo> repoPrestamos;

    public Biblioteca() {
        this.repoSocios = new Repositorio<>();
        this.repoLibros = new Repositorio<>();
        this.repoPrestamos = new Repositorio<>();
    }

    public Repositorio<Socio> getRepoSocios() { return repoSocios; }
    public Repositorio<Libro> getRepoLibros() { return repoLibros; }
    public Repositorio<Prestamo> getRepoPrestamos() { return repoPrestamos; }

    public void registrarPrestamo(String socioId, String isbn) throws Exception {
        Socio socio = repoSocios.buscarPorId(socioId);
        Libro libro = repoLibros.buscarPorId(isbn);

        if (socio == null) throw new Exception("El socio con ID " + socioId + " no está registrado.");
        if (libro == null) throw new Exception("El libro con ISBN " + isbn + " no existe.");
        if (libro.getEjemplaresDisponibles() <= 0) throw new Exception("No quedan copias físicas de este libro.");

        long activos = 0;
        for (Prestamo p : repoPrestamos.obtenerTodos()) {
            if (p.getSocio().getId().equalsIgnoreCase(socioId) && p.isActivo()) {
                activos++;
            }
        }

        if (activos >= socio.getLimitePrestamos()) {
            throw new Exception("Límite superado. Los " + socio.getTipo() + "s sólo pueden tener " + socio.getLimitePrestamos() + " préstamos activos.");
        }

        libro.prestar();
        String prestamoId = "PR-" + (repoPrestamos.obtenerTodos().size() + 1);
        repoPrestamos.agregar(new Prestamo(prestamoId, socio, libro));
    }

    public void registrarDevolucion(String prestamoId) throws Exception {
        Prestamo prestamo = repoPrestamos.buscarPorId(prestamoId);
        if (prestamo == null) throw new Exception("El préstamo con ID " + prestamoId + " no existe.");
        if (!prestamo.isActivo()) throw new Exception("Este préstamo ya fue devuelto.");

        prestamo.getLibro().devolver();
        prestamo.finalizarPrestamo();
    }

    @Override
    public String exportarAFormatoTexto() {
        int totalSocios = repoSocios.obtenerTodos().size();
        int totalLibros = repoLibros.obtenerTodos().size();
        
        long prestamosActivos = 0;
        for (Prestamo p : repoPrestamos.obtenerTodos()) {
            if (p.isActivo()) {
                prestamosActivos++;
            }
        }

        int totalDisponibles = 0;
        for (Libro l : repoLibros.obtenerTodos()) {
            totalDisponibles += l.getEjemplaresDisponibles();
        }

        Libro masSolicitado = null;
        for (Libro l : repoLibros.obtenerTodos()) {
            if (masSolicitado == null || l.getContadorPrestamos() > masSolicitado.getContadorPrestamos()) {
                masSolicitado = l;
            }
        }
        
        String libroEstrella = (masSolicitado != null && masSolicitado.getContadorPrestamos() > 0)
                ? masSolicitado.getTitulo() + " (Solicitado " + masSolicitado.getContadorPrestamos() + " veces)"
                : "Ninguno";

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("     INFORME INSTITUCIONAL - UTN AVELLANEDA\n");
        sb.append("====================================================\n");
        sb.append("Fecha de generación: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("----------------------------------------------------\n");
        sb.append("Cantidad total de socios registrados: ").append(totalSocios).append("\n");
        sb.append("Cantidad total de libros registrados: ").append(totalLibros).append("\n");
        sb.append("Cantidad de préstamos activos: ").append(prestamosActivos).append("\n");
        sb.append("Cantidad de libros disponibles: ").append(totalDisponibles).append("\n");
        sb.append("Libro más solicitado: ").append(libroEstrella).append("\n");
        sb.append("====================================================\n");

        return sb.toString();
    }
}