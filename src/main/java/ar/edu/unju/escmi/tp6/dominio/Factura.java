package ar.edu.unju.escmi.tp6.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Factura {

	private LocalDate fecha;
    private long nroFactura;
    private Cliente cliente;
    private List<Detalle> detalles = new ArrayList<Detalle>();

    public Factura() {

    }

    public Factura(LocalDate fecha, long nroFactura, Cliente cliente, List<Detalle> detalles) {
        this.fecha = fecha;
        this.nroFactura = nroFactura;
        this.cliente = cliente;
        this.detalles = detalles;
        calcularTotal();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public long getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(long nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public double calcularTotal() {
        double total = 0;
        for (Detalle detalle : detalles) {
            total += detalle.getImporte();
        }
        return total;
    }

    @Override
    public String toString() {
        return  "\n\n******************** Factura ********************"
                + "\nFecha: " + fecha + " N° de Factura: " + nroFactura
                + "\nCliente: " + cliente.getNombre() 
                + "\n************ Detalles de la Factura *************"
                + "\n" + detalles.toString().replaceAll("\\[|\\]", "").replaceAll(", ", "") + "\n";
    }
}
