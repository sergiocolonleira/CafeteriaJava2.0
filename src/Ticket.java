/**
 * Representa una venta de la cafetería.
 *
 * Esta clase utiliza composición porque contiene un Cliente, un Camarero y
 * un array de objetos Producto.
 */
public class Ticket {

    // Contador compartido por todos los tickets para generar números únicos
    // dentro de cada ejecución del programa.
    private static int siguienteNumero = 1;

    private final int numeroTicket;
    private Cliente cliente;
    private Camarero camarero;
    private Producto[] productos;
    private int[] cantidades;
    private int contadorProductos;

    /**
     * Crea un ticket con una capacidad máxima de productos.
     *
     * @param cliente cliente asociado al ticket
     * @param camarero camarero que atiende la venta
     * @param capacidadMaxima tamaño del array de productos
     */
    public Ticket(Cliente cliente, Camarero camarero, int capacidadMaxima) {
        this.numeroTicket = siguienteNumero++;
        this.cliente = cliente;
        this.camarero = camarero;

        // Validación mediante condicional: la capacidad mínima será 1.
        if (capacidadMaxima <= 0) {
            capacidadMaxima = 1;
        }

        this.productos = new Producto[capacidadMaxima];
        this.cantidades = new int[capacidadMaxima];
        this.contadorProductos = 0;
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

    public Producto[] getProductos() {
        return productos;
    }

    public int getContadorProductos() {
        return contadorProductos;
    }

    /**
     * Añade un producto al array si hay espacio disponible.
     *
     * @return true si se añade correctamente; false en caso contrario
     */
    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad == 0) {
            return false;
        }

        if (contadorProductos >= productos.length) {
            System.out.println("No se pueden añadir más productos al ticket.");
            return false;
        }

        productos[contadorProductos] = producto;
        cantidades[contadorProductos] = cantidad;
        contadorProductos++;
        return true;
    }

    /**
     * Recorre el array con un bucle y suma los precios de los productos
     * realmente añadidos al ticket.
     */
    public double calcularTotal() {
        double total = 0;

        for (int i = 0; i < contadorProductos; i++) {
            total += (productos[i].getPrecio() * (1+(productos[i].getIVA()/100)))* cantidades[i];
        }

        return total;
    }

    /**
     * Devuelve el ticket completo como texto.
     *
     * Tener este método separado resulta útil porque el mismo texto se puede
     * mostrar en consola y también guardar en un archivo.
     */
    public String generarTextoTicket() {
        StringBuilder texto = new StringBuilder();

        texto.append("==============================\n");
        texto.append("       CAFETERÍA JAVA\n");
        texto.append("==============================\n");
        texto.append("Ticket nº: ").append(numeroTicket).append("\n");

        if (cliente != null) {
            texto.append("Cliente: ").append(cliente.getNombre()).append("\n");
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

        if (contadorProductos == 0) {
            texto.append("No hay productos en el ticket.\n");
        } else {
            for (int i = 0; i < contadorProductos; i++) {
                double ivaLinea= (productos[i].getIVA()/100) * productos[i].getPrecio() * cantidades[i];
                double totalLinea= (productos[i].getPrecio() * cantidades[i]) + ivaLinea;
                texto.append(i + 1)
                        .append(". ")
                        .append(productos[i].mostrarInformacion())
                        .append(" * ")
                        .append(cantidades[i])
                        .append(",  ")
                        .append(productos[i].getIVA())
                        .append("% ")
                        .append(", " )
                        .append(ivaLinea)
                        .append(" = ")
                        .append(String.format( "Total línea: %.2f €"  ,  totalLinea))
                        .append("\n");
            }
        }

        texto.append(String.format("Total: %.2f €%n", calcularTotal()));
        texto.append("==============================\n");

        return texto.toString();
    }

    /**
     * Muestra por consola toda la información del ticket.
     */
    public void mostrarTicket() {
        System.out.print(generarTextoTicket());
    }
}
