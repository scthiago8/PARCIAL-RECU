/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parcial.recu;

import java.io.File;
import java.util.List;

public class Main {
    private static final String ARCHIVO_DATOS = "biblioteca.dat";
    private static Biblioteca biblioteca;

    public static void main(String[] args) {
        cargarDatos();
        Consola.imprimir("------------------------------------------------");
        Consola.imprimir("   SISTEMA DE BIBLIOTECA DIGITAL UTN FRA 2026   ");
        Consola.imprimir("------------------------------------------------");

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = Consola.leerEntero("Seleccione opcion: ");
            try {
                switch (opcion) {
                    case 1 -> registrarSocio();
                    case 2 -> registrarLibro();
                    case 3 -> listarSocios();
                    case 4 -> listarLibros();
                    case 5 -> buscarLibros();
                    case 6 -> registrarPrestamo();
                    case 7 -> registrarDevolucion();
                    case 8 -> mostrarPrestamosActivos();
                    case 9 -> generarInforme();
                    case 10 -> {
                        guardarDatos();
                        salir = true;
                    }
                    default -> Consola.imprimir("Opcion incorrecta.");
                }
            } catch (Exception e) {
                Consola.imprimir("ERROR: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        Consola.imprimir("\n==== MENU DE OPCIONES ====");
        Consola.imprimir("1. Registrar Socio");
        Consola.imprimir("2. Registrar Libro");
        Consola.imprimir("3. Listar Socios");
        Consola.imprimir("4. Listar Libros Disponibles");
        Consola.imprimir("5. Buscar Libro");
        Consola.imprimir("6. Registrar Prestamo");
        Consola.imprimir("7. Registrar Devolucion");
        Consola.imprimir("8. Mostrar Prestamos Activos");
        Consola.imprimir("9. Generar Informe (.txt)");
        Consola.imprimir("10. Guardar y Salir");
        Consola.imprimir("==========================");
    }

    private static void registrarSocio() {
        String id = Consola.leerTexto("ID del Socio (Legajo/DNI): ");
        if (biblioteca.getRepoSocios().buscarPorId(id) != null) {
            Consola.imprimir("Error: Ya existe un socio con este ID.");
            return;
        }
        String nombre = Consola.leerTexto("Nombre completo: ");
        String email = Consola.leerTexto("Email: ");
        
        Consola.imprimir("Tipo de Socio:\n1. Estudiante\n2. Docente");
        int tipoSeleccion = Consola.leerEntero("Seleccione tipo: ");
        String tipo = (tipoSeleccion == 2) ? "Docente" : "Estudiante";

        biblioteca.getRepoSocios().agregar(new Socio(id, nombre, email, tipo));
        Consola.imprimir("Socio registrado.");
    }

    private static void registrarLibro() {
        String isbn = Consola.leerTexto("ISBN del libro: ");
        if (biblioteca.getRepoLibros().buscarPorId(isbn) != null) {
            Consola.imprimir("Error: Ya existe un libro con este ISBN.");
            return;
        }
        String titulo = Consola.leerTexto("Título: ");
        String autor = Consola.leerTexto("Autor: ");
        int copias = Consola.leerEntero("Cantidad de ejemplares totales: ");

        if (copias <= 0) {
            Consola.imprimir("Error: Ingrese al menos 1 ejemplar.");
            return;
        }

        biblioteca.getRepoLibros().agregar(new Libro(isbn, titulo, autor, copias));
        Consola.imprimir("Libro incorporado.");
    }

    private static void listarSocios() {
        List<Socio> lista = biblioteca.getRepoSocios().obtenerTodos();
        if (lista.isEmpty()) {
            Consola.imprimir("No hay socios registrados.");
        } else {
            lista.forEach(s -> Consola.imprimir(s.toString()));
        }
    }

    private static void listarLibros() {
        List<Libro> lista = biblioteca.getRepoLibros().obtenerTodos();
        if (lista.isEmpty()) {
            Consola.imprimir("Catálogo vacío.");
        } else {
            lista.forEach(l -> Consola.imprimir(l.toString()));
        }
    }

    private static void buscarLibros() {
        String filtro = Consola.leerTexto("Ingrese título a buscar: ").toLowerCase();
        List<Libro> encontrados = biblioteca.getRepoLibros().obtenerTodos().stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(filtro))
                .toList();

        if (encontrados.isEmpty()) {
            Consola.imprimir("No se encontraron coincidencias.");
        } else {
            encontrados.forEach(l -> Consola.imprimir(l.toString()));
        }
    }

    private static void registrarPrestamo() throws Exception {
        String socioId = Consola.leerTexto("ID del Socio: ");
        String isbn = Consola.leerTexto("ISBN del Libro: ");
        biblioteca.registrarPrestamo(socioId, isbn);
        Consola.imprimir("¡Préstamo registrado!");
    }

    private static void registrarDevolucion() throws Exception {
        String prestamoId = Consola.leerTexto("ID del Préstamo: ");
        biblioteca.registrarDevolucion(prestamoId);
        Consola.imprimir("¡Devolución registrada!");
    }

    private static void mostrarPrestamosActivos() {
        List<Prestamo> lista = biblioteca.getRepoPrestamos().obtenerTodos().stream()
                .filter(Prestamo::isActivo)
                .toList();

        if (lista.isEmpty()) {
            Consola.imprimir("No hay préstamos activos.");
        } else {
            lista.forEach(p -> Consola.imprimir(p.toString()));
        }
    }

    private static void generarInforme() {
        try {
            String informe = biblioteca.exportarAFormatoTexto();
            ArchivoUtil.guardarTexto(informe, "informe.txt");
            Consola.imprimir("Informe guardado en 'informe.txt'.");
        } catch (Exception e) {
            Consola.imprimir("Error al exportar: " + e.getMessage());
        }
    }

    private static void guardarDatos() {
        try {
            ArchivoUtil.guardarObjeto(biblioteca, ARCHIVO_DATOS);
            Consola.imprimir("Datos serializados con éxito.");
        } catch (Exception e) {
            Consola.imprimir("Error al guardar: " + e.getMessage());
        }
    }

    private static void cargarDatos() {
        File f = new File(ARCHIVO_DATOS);
        if (f.exists()) {
            try {
                biblioteca = (Biblioteca) ArchivoUtil.cargarObjeto(ARCHIVO_DATOS);
                Consola.imprimir("Datos restaurados correctamente.");
            } catch (Exception e) {
                Consola.imprimir("Iniciando biblioteca vacía.");
                biblioteca = new Biblioteca();
            }
        } else {
            biblioteca = new Biblioteca();
        }
    }
}