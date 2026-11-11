/**
 * Clase base que representa un producto de la cafetería.
 *
 * Implementa Descontable para calcular un precio rebajado.
 */
public class Producto implements Descontable {

    private String nombre;
    private double precio;
    private String categoria;
    private double IVA;

    /**
     * Crea un producto con nombre, precio, categoría e IVA.
     *
     * @throws IllegalArgumentException si el precio es negativo
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

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            this.nombre = "Producto sin nombre";
        } else {
            this.nombre = nombre.trim();
        }
    }

    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @throws IllegalArgumentException si el precio es negativo
     */
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException(
                    "El precio del producto no puede ser negativo: " + precio
            );
        }

        this.precio = precio;
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

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    /**
     * Devuelve la información básica del producto.
     */
    public String mostrarInformacion() {
        return String.format(
                "%s | %.2f € | Categoría (%s) | IVA %.2f %%",
                nombre, precio, categoria, IVA
        );
    }

    /**
     * Calcula el precio con descuento sin modificar el precio original.
     * Si el porcentaje está fuera del rango 0-100, devuelve el precio original.
     */
    @Override
    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            return precio;
        }

        return precio - (precio * porcentaje / 100.0);
    }
}