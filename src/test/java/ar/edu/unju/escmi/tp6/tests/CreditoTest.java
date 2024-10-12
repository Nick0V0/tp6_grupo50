package ar.edu.unju.escmi.tp6.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.dominio.Credito;
import ar.edu.unju.escmi.tp6.dominio.Detalle;
import ar.edu.unju.escmi.tp6.dominio.Factura;
import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.TarjetaCredito;


 class CreditoTest {


    private Credito credito;
    private Factura factura;
    private static final double LIMITE_TOTAL = 1500000.0;
    
    @BeforeEach
    public void setUp() {
        credito = new Credito();
        factura = new Factura();
        
        Producto producto1 = new Producto(1, "Producto A", 500000, "Argentina");
        Producto producto2 = new Producto(2, "Producto B", 100000, "Argentina");

        Detalle detalle1 = new Detalle(1, 500000, producto1); 
        Detalle detalle2 = new Detalle(1, 1000000, producto2);
        
        List<Detalle> detalles = new ArrayList<Detalle>();
        detalles.add(detalle1);
        detalles.add(detalle2);
        
        TarjetaCredito tarjeta = new TarjetaCredito(1234567890123456L, LocalDate.of(2025, 12, 31), null, 1500000);

        factura.setDetalles(detalles);
        
        credito.setFactura(factura);
        credito.setTarjetaCredito(tarjeta);
        
    }

    @Test
    public void testMontoTotalCreditoSuperaLimite() {
        // Verificar que el total del crédito no exceda $1.500.000
        double totalCredito = credito.getFactura().calcularTotal();
        assertTrue(totalCredito <= 1500000, "El total del crédito no debe exceder $1.500.000");

    }
    
    @Test
    public void testSumaImportesDetallesIgualTotalFactura() {
        double totalDetalles = 0.0;
        for (Detalle detalle : credito.getFactura().getDetalles()) {
            totalDetalles += detalle.getImporte();
        }

        double totalFactura = credito.getFactura().calcularTotal();

        // Verificar que la suma de los importes de los detalles sea igual al total de la factura
        assertEquals(totalDetalles, totalFactura, "La suma de los importes de los detalles debe ser igual al total de la factura.");
    }
    
    @Test
    public void testMontoTotalCreditoNoSuperaLimite() {
    	
    	double totalFactura = credito.getFactura().calcularTotal();
        double limiteTarjeta = credito.getTarjetaCredito().getLimiteCompra();

        // Verificar que el total de la compra no exceda $1.500.000
        assertTrue(totalFactura <= LIMITE_TOTAL, "El total de la compra no debe exceder $1.500.000.");

        // Verificar que el total de la compra no exceda el límite de la tarjeta
        assertTrue(totalFactura <= limiteTarjeta, "El total de la compra no debe exceder el límite de la tarjeta de crédito.");
    }
}
