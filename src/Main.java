import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Clase principal de la aplicación.
 */
public class Main {

    static void main() {
        Scanner teclado = new Scanner(System.in);

        Camarero camarero = new Camarero("Sergio", "S01");

        Producto[] catalogo = {
                new Bebida("Café", 1.80, "Bebidas", 10, "mediano"),
                new Bebida("Zumo", 2.20, "Bebidas", 10, "grande"),
                new Comida("Churrasco", 10.50, "Comidas", 10, true),
                new Comida("Ensaladilla", 8.00, "Comidas", 10, false),
                new Producto("Filloas", 1.00, "Postres", 10)
        };

        HashMap<String, ArrayList<Producto>> categorias =
                agruparPorCategoria(catalogo);

        GestorTickets gestorTickets = new GestorTickets();
        ColaTickets colaTickets = new ColaTickets();
        ExportadorJSON exportador = new ExportadorJSON();

        // Demuestra las nuevas funcionalidades con datos de ejemplo.
        demostrarNovedades(camarero, catalogo, categorias, exportador);

        System.out.println("\n=== NUEVO TICKET ===");
        Cliente cliente = crearCliente(teclado);
        Ticket ticketActual = new Ticket(cliente, camarero);

        int opcion;

        do {
            mostrarMenu(ticketActual, colaTickets);
            opcion = leerEntero(teclado, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    mostrarCatalogo(catalogo);
                    break;

                case 2:
                    agregarProductoAlTicket(teclado, catalogo, ticketActual);
                    break;

                case 3:
                    ticketActual.mostrarTicket();
                    break;

                case 4:
                    aplicarDescuento(teclado, ticketActual);
                    break;

                case 5:
                    System.out.printf(
                            "Total actual: %.2f €%n",
                            ticketActual.calcularTotal()
                    );
                    break;

                case 6:
                    if (gestorTickets.guardarTicket(ticketActual)) {
                        System.out.println("Ticket guardado correctamente.");
                        cliente = crearCliente(teclado);
                        ticketActual = new Ticket(cliente, camarero);
                    } else {
                        System.out.println(
                                "No se pudo guardar el ticket. "
                                        + "Comprueba que tenga productos "
                                        + "y revisa los posibles errores de escritura."
                        );
                    }
                    break;

                case 7:
                    gestorTickets.mostrarTicketsGuardados();
                    break;

                case 8:
                    eliminarProductoDelTicket(teclado, ticketActual);
                    break;

                case 9:
                    mostrarCategorias(categorias);
                    break;

                case 10:
                    if (ticketActual.getContadorProductos() == 0) {
                        System.out.println(
                                "No se puede enviar un ticket vacío."
                        );
                    } else {
                        colaTickets.agregarTicket(ticketActual);

                        System.out.println(
                                "Ticket nº " + ticketActual.getNumeroTicket()
                                        + " añadido a la cola."
                        );

                        cliente = crearCliente(teclado);
                        ticketActual = new Ticket(cliente, camarero);
                    }
                    break;

                case 11:
                    atenderTicket(colaTickets);
                    break;

                case 12:
                    if (exportador.exportarTicket(ticketActual)) {
                        System.out.println(
                                "Ticket exportado a ticket.json."
                        );
                    }
                    break;

                case 0:
                    if (ticketActual.getContadorProductos() > 0) {
                        System.out.println(
                                "ATENCIÓN: el ticket actual tiene productos "
                                        + "y no se ha finalizado."
                        );
                    }

                    if (colaTickets.getCantidadPendientes() > 0) {
                        System.out.println(
                                "ATENCIÓN: quedan pedidos pendientes en la cola."
                        );
                    }

                    System.out.println(
                            "Gracias por utilizar Cafetería Java."
                    );
                    break;

                default:
                    System.out.println("Opción incorrecta.");
            }

            System.out.println();

        } while (opcion != 0);

        teclado.close();
    }

    /**
     * Agrupa los productos del catálogo según su categoría.
     */
    private static HashMap<String, ArrayList<Producto>> agruparPorCategoria(
            Producto[] catalogo) {

        HashMap<String, ArrayList<Producto>> categorias = new HashMap<>();

        for (Producto producto : catalogo) {
            String categoria = producto.getCategoria();

            if (!categorias.containsKey(categoria)) {
                categorias.put(categoria, new ArrayList<>());
            }

            categorias.get(categoria).add(producto);
        }

        return categorias;
    }

    /**
     * Muestra todas las categorías con sus productos.
     */
    private static void mostrarCategorias(
            HashMap<String, ArrayList<Producto>> categorias) {

        System.out.println("\n=== PRODUCTOS POR CATEGORÍA ===");

        for (String categoria : categorias.keySet()) {
            System.out.println("\n" + categoria + ":");

            for (Producto producto : categorias.get(categoria)) {
                System.out.println("- " + producto.mostrarInformacion());
            }
        }
    }

    private static void mostrarMenu(
            Ticket ticketActual, ColaTickets colaTickets) {

        System.out.println("================================");
        System.out.println("         CAFETERÍA JAVA");
        System.out.println(
                "Ticket actual: nº " + ticketActual.getNumeroTicket()
        );
        System.out.println(
                "Pedidos pendientes: " + colaTickets.getCantidadPendientes()
        );
        System.out.println("================================");
        System.out.println("1. Ver productos disponibles");
        System.out.println("2. Añadir producto al ticket");
        System.out.println("3. Mostrar ticket actual");
        System.out.println("4. Calcular descuento de un producto");
        System.out.println("5. Consultar total actual");
        System.out.println("6. Finalizar y guardar en tickets.txt");
        System.out.println("7. Ver tickets guardados");
        System.out.println("8. Eliminar una línea del ticket");
        System.out.println("9. Ver productos por categoría");
        System.out.println("10. Enviar ticket a la cola y abrir otro");
        System.out.println("11. Atender siguiente ticket");
        System.out.println("12. Exportar ticket actual a JSON");
        System.out.println("0. Salir");
        System.out.println("================================");
    }

    private static Cliente crearCliente(Scanner teclado) {
        String nombre;

        do {
            System.out.print("Nombre del cliente: ");
            nombre = teclado.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
            }
        } while (nombre.isEmpty());

        System.out.print("Teléfono del cliente: ");
        String telefono = teclado.nextLine().trim();

        return new Cliente(nombre, telefono);
    }

    private static void mostrarCatalogo(Producto[] catalogo) {
        System.out.println("\n=== CATÁLOGO ===");

        for (int i = 0; i < catalogo.length; i++) {
            System.out.println(
                    (i + 1) + ". " + catalogo[i].mostrarInformacion()
            );
        }
    }

    private static void agregarProductoAlTicket(
            Scanner teclado, Producto[] catalogo, Ticket ticket) {

        mostrarCatalogo(catalogo);

        int numeroProducto = leerEntero(
                teclado, "Seleccione el número del producto: "
        );

        if (numeroProducto < 1 || numeroProducto > catalogo.length) {
            System.out.println("El producto seleccionado no existe.");
            return;
        }

        int cantidad = leerEntero(teclado, "Introduzca la cantidad: ");
        Producto producto = catalogo[numeroProducto - 1];

        if (ticket.agregarProducto(producto, cantidad)) {
            System.out.println("Producto añadido: " + producto.getNombre());
        } else {
            System.out.println(
                    "No se pudo añadir. La cantidad debe ser mayor que cero."
            );
        }
    }

    /**
     * Elimina una línea completa, incluyendo todas sus unidades.
     */
    private static void eliminarProductoDelTicket(
            Scanner teclado, Ticket ticket) {

        if (ticket.getContadorProductos() == 0) {
            System.out.println("El ticket está vacío.");
            return;
        }

        ticket.mostrarTicket();

        int numeroLinea = leerEntero(
                teclado, "Número de la línea que desea eliminar: "
        );

        if (numeroLinea < 1
                || numeroLinea > ticket.getContadorProductos()) {
            System.out.println("La línea seleccionada no existe.");
            return;
        }

        ticket.eliminarProducto(numeroLinea - 1);
        System.out.println("Línea eliminada correctamente.");
    }

    /**
     * Calcula y muestra el descuento sin modificar el precio original.
     */
    private static void aplicarDescuento(
            Scanner teclado, Ticket ticket) {

        if (ticket.getContadorProductos() == 0) {
            System.out.println("No hay productos en el ticket.");
            return;
        }

        ArrayList<Producto> productos = ticket.getProductos();

        for (int i = 0; i < productos.size(); i++) {
            System.out.println(
                    (i + 1) + ". " + productos.get(i).mostrarInformacion()
            );
        }

        int numeroProducto = leerEntero(
                teclado, "Seleccione el número del producto: "
        );

        if (numeroProducto < 1 || numeroProducto > productos.size()) {
            System.out.println("El producto seleccionado no existe.");
            return;
        }

        double porcentaje = leerDouble(
                teclado, "Porcentaje de descuento (0-100): "
        );

        if (porcentaje < 0 || porcentaje > 100) {
            System.out.println("El porcentaje debe estar entre 0 y 100.");
            return;
        }

        Producto producto = productos.get(numeroProducto - 1);
        Descontable descontable = producto;

        System.out.println("Producto: " + producto.getNombre());
        System.out.printf(
                "Precio original sin IVA: %.2f €%n",
                producto.getPrecio()
        );
        System.out.printf(
                "Precio con descuento sin IVA: %.2f €%n",
                descontable.aplicarDescuento(porcentaje)
        );
    }

    /**
     * Atiende y muestra el siguiente ticket de la cola.
     */
    private static void atenderTicket(ColaTickets colaTickets) {
        Ticket atendido = colaTickets.atenderSiguiente();

        if (atendido == null) {
            System.out.println("No hay tickets pendientes de atender.");
        } else {
            System.out.println("\n=== TICKET ATENDIDO ===");
            atendido.mostrarTicket();
        }

        System.out.println(
                "Tickets pendientes: " + colaTickets.getCantidadPendientes()
        );
    }

    /**
     * Demuestra los cinco apartados de la EC2.
     * Utiliza una cola de ejemplo independiente de la del menú.
     */
    private static void demostrarNovedades(
            Camarero camarero,
            Producto[] catalogo,
            HashMap<String, ArrayList<Producto>> categorias,
            ExportadorJSON exportador) {

        System.out.println("=== DEMOSTRACIÓN EC2 ===");

        Ticket primero = new Ticket(
                new Cliente("Andrea", "600111222"), camarero
        );
        Ticket segundo = new Ticket(
                new Cliente("Sergio", "600333444"), camarero
        );
        Ticket tercero = new Ticket(
                new Cliente("Lucía", "600555666"), camarero
        );

        primero.agregarProducto(catalogo[0], 2);
        primero.agregarProducto(catalogo[2], 1);
        primero.agregarProducto(catalogo[4], 3);

        System.out.println("\n--- Antes de eliminar la galleta ---");
        primero.mostrarTicket();

        primero.eliminarProducto(catalogo[4]);

        System.out.println("\n--- Después de eliminar la galleta ---");
        primero.mostrarTicket();

        segundo.agregarProducto(catalogo[1], 1);
        tercero.agregarProducto(catalogo[3], 2);

        mostrarCategorias(categorias);

        System.out.println("\n--- Validación de precio negativo ---");

        try {
            new Producto("Producto de prueba", -2.00, "Postres", 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado: " + e.getMessage());
        }

        System.out.println("\n--- Cola de tres tickets: orden FIFO ---");

        ColaTickets colaEjemplo = new ColaTickets();
        colaEjemplo.agregarTicket(primero);
        colaEjemplo.agregarTicket(segundo);
        colaEjemplo.agregarTicket(tercero);

        System.out.println(
                "Tickets añadidos en orden: "
                        + primero.getNumeroTicket() + ", "
                        + segundo.getNumeroTicket() + ", "
                        + tercero.getNumeroTicket()
        );

        atenderTicket(colaEjemplo);
        atenderTicket(colaEjemplo);
        atenderTicket(colaEjemplo);

        System.out.println("\n--- Intento de atender una cola vacía ---");
        atenderTicket(colaEjemplo);

        System.out.println("\n--- Exportación JSON ---");

        if (exportador.exportarTicket(primero)) {
            System.out.println("Ejemplo generado en ticket.json.");
        }

        System.out.println("\n=== FIN DE LA DEMOSTRACIÓN ===");
    }

    private static int leerEntero(Scanner teclado, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número entero.");
            }
        }
    }

    private static double leerDouble(Scanner teclado, String mensaje) {
        while (true) {
            System.out.print(mensaje);

            String entrada = teclado.nextLine()
                    .trim()
                    .replace(',', '.');

            try {
                double valor = Double.parseDouble(entrada);

                if (Double.isFinite(valor)) {
                    return valor;
                }

                System.out.println("Debe introducir un número finito.");
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número válido.");
            }
        }
    }
}
