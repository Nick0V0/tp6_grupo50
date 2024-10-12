package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.dominio.Cliente;
import ar.edu.unju.escmi.tp6.dominio.Credito;
import ar.edu.unju.escmi.tp6.dominio.Cuota;
import ar.edu.unju.escmi.tp6.dominio.Factura;
import ar.edu.unju.escmi.tp6.dominio.TarjetaCredito;

 class CuotaTest {

	private Credito credito;
	private TarjetaCredito tarjetaCredito;
	private Factura factura;
	private static final int CANTIDAD_CUOTAS_PERMITIDAS = 30;

	@BeforeEach
	public void setUp() {
		tarjetaCredito = new TarjetaCredito(123456789L, LocalDate.of(2025, 12, 31),new Cliente(1, "Juan Pérez", "Calle Falsa 123", "555-1234"), 1500000);
		factura = new Factura();

		List<Cuota> cuotas = new ArrayList<>();
		credito = new Credito(tarjetaCredito, factura, cuotas);

	}

	@Test
	public void testCuotasNoNulas() {
		// Verificar que la lista de cuotas no sea null
		assertNotNull(credito, "La lista de cuotas no debe ser null.");
	}

	@Test
	public void testListaCuotasTiene30Elementos() {
		// Verifica que la lista de cuotas tenga exactamente 30 elementos
		assertEquals(30, credito.getCuotas().size(), "La lista de cuotas debe contener exactamente 30 cuotas");
	}

	@Test
	public void testCantidadCuotasNoSuperaLimite() {
		Cuota cuotaAdicional = new Cuota(1000.0, 1, LocalDate.now(), LocalDate.now());
		// Intentar agregar una cuota adicional
		credito.getCuotas().add(cuotaAdicional);

		// Verificar que no se debe permitir agregar más de 30 cuotas
		assertTrue(credito.getCuotas().size() <= CANTIDAD_CUOTAS_PERMITIDAS,"La cantidad de cuotas no debe superar el límite de 30.");
	}
}
