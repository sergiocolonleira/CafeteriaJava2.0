import java.util.LinkedList;
import java.util.Queue;

/**
 * Gestiona los tickets pendientes por orden de llegada.
 *
 * Sigue el orden FIFO:
 * el primer ticket que entra es el primero que se atiende.
 */
public class ColaTickets {

    private final Queue<Ticket> ticketsPendientes;

    /**
     * Crea una cola vacía.
     */
    public ColaTickets() {
        this.ticketsPendientes = new LinkedList<>();
    }

    /**
     * Añade un ticket al final de la cola.
     *
     * @return true si se añade; false si el ticket es nulo o está vacío
     */
    public boolean agregarTicket(Ticket ticket) {
        if (ticket == null || ticket.getContadorProductos() == 0) {
            return false;
        }

        return ticketsPendientes.offer(ticket);
    }

    /**
     * Extrae el ticket que lleva más tiempo esperando.
     *
     * poll() devuelve null si la cola está vacía,
     * permitiendo controlar esa situación sin lanzar una excepción.
     *
     * @return siguiente ticket o null si no hay pendientes
     */
    public Ticket atenderSiguiente() {
        return ticketsPendientes.poll();
    }

    /**
     * Devuelve el número de tickets pendientes.
     */
    public int getCantidadPendientes() {
        return ticketsPendientes.size();
    }
}