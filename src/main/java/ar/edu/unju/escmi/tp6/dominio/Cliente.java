package ar.edu.unju.escmi.tp6.dominio;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unju.escmi.tp6.collections.CollectionFactura;

public class Cliente {

	private long dni;
	private String nombre;
	private String direccion;
	private String telefono;
	
	public Cliente() {

	}


	public Cliente(long dni, String nombre, String direccion, String telefono) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
	}



	public long getDni() {
		return dni;
	}

	public void setDni(long dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Factura> consultarCompras() {
		List<Factura> shoppingList = new ArrayList<Factura>();
		if (CollectionFactura.facturas != null) {
			for (Factura fac : CollectionFactura.facturas) {
				if (fac.getCliente().getDni() == this.dni) {
					shoppingList.add(fac);
				}
			}
		} else {
			shoppingList = null;
		}
		return shoppingList;
	}
}
