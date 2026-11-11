import java.util.ArrayList;

/**
 * Representa una venta de la cafetería.
 *
 * Guarda los productos y sus cantidades en dos listas.
 * Cada producto tiene su cantidad en la misma posición
 * de la lista de cantidades.
 */
public class Ticket {

    private static int siguienteNumero = 1;

    private final int numeroTicket;
    private Cliente cliente;
    private Camarero camarero;

    private final ArrayList<Producto> productos;
    private final ArrayList<Integer> cantidades;

    /**
     * Crea un ticket vacío.
     */
    public Ticket(Cliente cliente, Camarero camarero) {
        this.numeroTicket = siguienteNumero++;
        this.cliente = cliente;
        this.camarero = camarero;
        this.productos = new ArrayList<>();
        this.cantidades = new ArrayList<>();
    }

    /**
     * Conserva la compatibilidad con las llamadas de la EC1.
     *
     * La capacidad máxima ya no se utiliza porque las listas
     * crecen al añadir elementos.
     */
    public Ticket(Cliente cliente, Camarero camarero, int capacidadMaxima) {
        this(cliente, camarero);
    }

    public int getNumeroTicket() {
        return numeroTicket;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Camarero getCamarero() {
        return camarero;
    }

    public void setCamarero(Camarero camarero) {
        this.camarero = camarero;
    }

    /**
     * Devuelve una copia de la lista para evitar modificaciones
     * externas que descoordinen los productos y sus cantidades.
     */
    public ArrayList<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public ArrayList<Integer> getCantidades() {
        return new ArrayList<>(cantidades);
    }

    /**
     * Devuelve el número de líneas de productos del ticket,
     * no la suma de sus unidades.
     */
    public int getContadorProductos() {
        return productos.size();
    }

    /**
     * Añade una línea con un producto y su cantidad.
     *
     * @return true si se añade; false si los datos no son válidos
     */
    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }

        productos.add(producto);
        cantidades.add(cantidad);
        return true;
    }

    /**
     * Elimina una línea completa del ticket por su posición.
     * Las posiciones empiezan en cero.
     *
     * @return true si se elimina; false si la posición no existe
     */
    public boolean eliminarProducto(int indice) {
        if (indice < 0 || indice >= productos.size()) {
            return false;
        }

        productos.remove(indice);
        cantidades.remove(indice);
        return true;
    }

    /**
     * Elimina la primera línea que contiene el producto indicado,
     * incluyendo todas las unidades de esa línea.
     *
     * @return true si se elimina; false si el producto no está
     */
    public boolean eliminarProducto(Producto producto) {
        int indice = productos.indexOf(producto);
        return eliminarProducto(indice);
    }

    /**
     * Calcula el importe total con cantidades e IVA incluidos.
     */
    public double calcularTotal() {
        double total = 0;

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            int cantidad = cantidades.get(i);

            double baseLinea = producto.getPrecio() * cantidad;
            double ivaLinea = baseLinea * producto.getIVA() / 100.0;

            total += baseLinea + ivaLinea;
        }

        return total;
    }

    /**
     * Genera el texto del ticket para mostrarlo o guardarlo.
     */
    public String generarTextoTicket() {
        StringBuilder texto = new StringBuilder();

        texto.append("==============================\n");
        texto.append("       CAFETERÍA JAVA\n");
        texto.append("==============================\n");
        texto.append("Ticket nº: ").append(numeroTicket).append("\n");

        if (cliente != null) {
            texto.append("Cliente: ")
                    .append(cliente.getNombre())
                    .append("\n");
        } else {
            texto.append("Cliente: no asignado\n");
        }

        if (camarero != null) {
            texto.append("Camarero: ")
                    .append(camarero.getNombre())
                    .append(" - Código: ")
                    .append(camarero.getCodigoEmpleado())
                    .append("\n");
        } else {
            texto.append("Camarero: no asignado\n");
        }

        texto.append("Productos:\n");

        if (productos.isEmpty()) {
            texto.append("No hay productos en el ticket.\n");
        } else {
            for (int i = 0; i < productos.size(); i++) {
                Producto producto = productos.get(i);
                int cantidad = cantidades.get(i);

                double baseLinea = producto.getPrecio() * cantidad;
                double ivaLinea = baseLinea * producto.getIVA() / 100.0;
                double totalLinea = baseLinea + ivaLinea;

                texto.append(i + 1)
                        .append(". ")
                        .append(producto.mostrarInformacion())
                        .append(" * ")
                        .append(cantidad)
                        .append("\n");

                texto.append(String.format(
                        "   IVA de la línea: %.2f € | Total línea: %.2f €%n",
                        ivaLinea, totalLinea
                ));
            }
        }

        texto.append(String.format(
                "Total: %.2f €%n", calcularTotal()
        ));
        texto.append("==============================\n");

        return texto.toString();
    }

    /**
     * Muestra el ticket por consola.
     */
    public void mostrarTicket() {
        System.out.print(generarTextoTicket());
    }
}