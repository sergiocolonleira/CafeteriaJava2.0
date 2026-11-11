import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Exporta un ticket a un archivo JSON utilizando Gson.
 */
public class ExportadorJSON {

    private static final String NOMBRE_ARCHIVO = "ticket.json";

    /**
     * Guarda el ticket en ticket.json.
     * Si el archivo ya existe, sustituye su contenido.
     *
     * @return true si se exporta correctamente; false en caso contrario
     */
    public boolean exportarTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("No se puede exportar un ticket nulo.");
            return false;
        }

        JsonObject datosTicket = new JsonObject();

        datosTicket.addProperty(
                "numeroTicket", ticket.getNumeroTicket()
        );

        String nombreCliente = ticket.getCliente() != null
                ? ticket.getCliente().getNombre()
                : "No asignado";

        String nombreCamarero = ticket.getCamarero() != null
                ? ticket.getCamarero().getNombre()
                : "No asignado";

        datosTicket.addProperty("cliente", nombreCliente);
        datosTicket.addProperty("camarero", nombreCamarero);

        ArrayList<Producto> productos = ticket.getProductos();
        ArrayList<Integer> cantidades = ticket.getCantidades();

        JsonArray productosJSON = new JsonArray();

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            int cantidad = cantidades.get(i);

            double baseLinea = producto.getPrecio() * cantidad;
            double importeIVA = baseLinea * producto.getIVA() / 100.0;
            double totalLinea = baseLinea + importeIVA;

            JsonObject datosProducto = new JsonObject();

            datosProducto.addProperty("nombre", producto.getNombre());
            datosProducto.addProperty("precio", producto.getPrecio());
            datosProducto.addProperty("cantidad", cantidad);
            datosProducto.addProperty("iva", producto.getIVA());
            datosProducto.addProperty("totalLinea", totalLinea);

            productosJSON.add(datosProducto);
        }

        datosTicket.add("productos", productosJSON);
        datosTicket.addProperty("total", ticket.calcularTotal());

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (Writer escritor = Files.newBufferedWriter(
                Path.of(NOMBRE_ARCHIVO),
                StandardCharsets.UTF_8)) {

            escritor.write(gson.toJson(datosTicket));
            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error al escribir el archivo JSON: " + e.getMessage()
            );
            return false;
        }
    }
}