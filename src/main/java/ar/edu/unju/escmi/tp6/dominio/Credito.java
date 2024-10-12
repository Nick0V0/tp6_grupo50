package ar.edu.unju.escmi.tp6.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Credito {

	private TarjetaCredito tarjetaCredito;
	private Factura factura;
	private List<Cuota> cuotas = new ArrayList<Cuota>();

	public Credito() {
	}

	public Credito(TarjetaCredito tarjetaCredito, Factura factura, List<Cuota> cuotas) {
		this.tarjetaCredito = tarjetaCredito;
		this.factura = factura;
		this.cuotas = cuotas;
		generarCuotas();
	}

	public Credito(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

	public TarjetaCredito getTarjetaCredito() {
		return tarjetaCredito;
	}

	public void setTarjetaCredito(TarjetaCredito tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public List<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}
	
	public void generarCuotas() {
		double montoCuota = this.factura.calcularTotal() / 30;
		int nroCuota = 0;
		LocalDate currentDate = LocalDate.now();
		LocalDate auxDate = LocalDate.now();

		for (int i = 0; i < 30; i++) {
			nroCuota++;
			Cuota cuota = new Cuota();
			cuota.setMonto(montoCuota);
			cuota.setNroCuota(nroCuota);
			cuota.setFechaGeneracion(currentDate); 
			auxDate = auxDate.plusMonths(1);
			cuota.setFechaVencimiento(auxDate);
			cuotas.add(cuota);
		}

	}

	public void mostarCredito() {
		System.out.println("Tarjeta De Credito: " + tarjetaCredito + "\n" + factura + "\nCant. Cuotas:\n");
		for(Cuota cuota: cuotas) {
			System.out.println(cuota);
		}
	}
}
