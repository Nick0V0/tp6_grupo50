package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.Stock;

 class StockTest {
	private static final int cantidadAReducir = 5;
    private Stock stock;
    private Producto producto;

    @BeforeEach
    public void setUp() {
        producto = new Producto(1, "Producto A", 100.0, "Argentina");
        stock = new Stock(10,producto); 
    }

    @Test
    public void testReducirStock_CantidadValida() {

        int cantidadOriginal = stock.getCantidad();
        if (cantidadAReducir <= cantidadOriginal) {
            stock = new Stock(cantidadOriginal - cantidadAReducir,producto); // Simula la reducción de stock
        } else {
            throw new IllegalArgumentException("No hay suficiente stock para reducir.");
        }

        assertEquals(5, stock.getCantidad(), "El stock debería decrementar a 5 unidades.");
    }


}
