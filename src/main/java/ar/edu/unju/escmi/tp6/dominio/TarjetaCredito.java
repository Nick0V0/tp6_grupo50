package ar.edu.unju.escmi.tp6.dominio;

import java.time.LocalDate;

public class TarjetaCredito {

	 private long numero;
	 private LocalDate fechaCaducacion;
	 private Cliente cliente;
	 private double limiteCompra;

	 public TarjetaCredito() {
     }

    

    public TarjetaCredito(long numero, LocalDate fechaCaducacion, Cliente cliente, double limiteCompra) {
		super();
		this.numero = numero;
		this.fechaCaducacion = fechaCaducacion;
		this.cliente = cliente;
		this.limiteCompra = limiteCompra;
	}



	public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public LocalDate getFechaCaducacion() {
        return fechaCaducacion;
    }

    public void setFechaCaducacion(LocalDate fechaCaducacion) {
	       this.fechaCaducacion = fechaCaducacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setLimiteCompra(double limiteCompra) {
		this.limiteCompra = limiteCompra;
	}

	
	@Override
	public String toString() {
		return "\nNumero: " + numero + " Fecha De Caducacion: " + fechaCaducacion + "\nNombre Titular: "
				+ cliente.getNombre() + ", Limite De Compra Actual:" + limiteCompra;
	}

}
