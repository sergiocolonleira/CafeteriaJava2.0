/**
 * Clase base que representa un producto de la cafetería.
 *
 * Implementa la interfaz Descontable para que cualquier producto pueda
 * calcular un precio rebajado.
 */
public class Producto implements Descontable {

    // Encapsulación: todos los atributos son privados.
    private String nombre;
    private double precio;
    private String categoria;
    private double IVA;


    /**
     * Constructor completo de Producto.
     */
    public Producto(String nombre, double precio, String categoria, double IVA) {
        setNombre(nombre);
        setPrecio(precio);
        setCategoria(categoria);
        setIVA(IVA);

    }



    public String getNombre() {
        return nombre;
    }

    /**
     * Se valida que el nombre no sea nulo ni esté vacío.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            this.nombre = "Producto sin nombre";
        } else {
            this.nombre = nombre.trim();
        }
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getPrecio() {
        return precio;
    }

    /**
     * No se permiten precios negativos.
     */
    public void setPrecio(double precio) {
        if (precio < 0) {
            this.precio = 0;
        } else {
            this.precio = precio;
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            this.categoria = "Sin categoría";
        } else {
            this.categoria = categoria.trim();
        }
    }

    /**
     * Devuelve una cadena con la información básica del producto.
     * Las clases hijas sobrescriben este método con @Override.
     */
    public String mostrarInformacion() {
        return String.format("%s - %.2f € - Categoría: %s - Iva: %.2f",
                nombre, precio, categoria, IVA);
    }

    /**
     * Calcula el precio tras aplicar un descuento porcentual.
     *
     * El método NO modifica el precio original del producto; únicamente
     * devuelve el precio resultante. Así se puede comparar el precio normal
     * con el precio rebajado.
     */
    @Override
    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            // Si el porcentaje no es válido, se devuelve el precio original.
            return precio;
        }

        return precio - (precio * porcentaje / 100.0);
    }
}
