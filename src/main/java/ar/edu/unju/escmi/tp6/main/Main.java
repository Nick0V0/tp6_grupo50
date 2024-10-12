package ar.edu.unju.escmi.tp6.main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.tp6.collections.CollectionCliente;
import ar.edu.unju.escmi.tp6.collections.CollectionCredito;
import ar.edu.unju.escmi.tp6.collections.CollectionFactura;
import ar.edu.unju.escmi.tp6.collections.CollectionProducto;
import ar.edu.unju.escmi.tp6.collections.CollectionStock;
import ar.edu.unju.escmi.tp6.collections.CollectionTarjetaCredito;
import ar.edu.unju.escmi.tp6.dominio.Cliente;
import ar.edu.unju.escmi.tp6.dominio.Credito;
import ar.edu.unju.escmi.tp6.dominio.Cuota;
import ar.edu.unju.escmi.tp6.dominio.Detalle;
import ar.edu.unju.escmi.tp6.dominio.Factura;
import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.Stock;
import ar.edu.unju.escmi.tp6.dominio.TarjetaCredito;

public class Main {
	public static void main(String[] args) {
		long Numfact = 0;
		CollectionCliente.precargarClientes();
		CollectionProducto.precargarProductos();
		CollectionStock.precargarStocks();
		CollectionTarjetaCredito.precargarTarjetas();
		int opcion = 0;
		Scanner scanner = new Scanner(System.in);
		do {
			try {
				System.out.println("\n====== Menu Principal =====");
				System.out.println("1- Realizar una venta");
				System.out.println("2- Revisar compras realizadas por el cliente (debe ingresar el DNI del cliente)");
				System.out.println("3- Mostrar lista de los electrodomésticos");
				System.out.println("4- Consultar stock");
				System.out.println("5- Revisar créditos de un cliente (debe ingresar el DNI del cliente)");
				System.out.println("6- Registrar nuevo cliente");
				System.out.println("7- Registrar nuevo producto");
				System.out.println("8- Agregar Stock a un Producto");
				System.out.println("9- Salir");

				System.out.print("Ingrese su opción: ");
				opcion = scanner.nextInt();
				scanner.nextLine();
				switch (opcion) {
				case 1:
					Numfact = realizarVenta(scanner, Numfact);
					break;
				case 2:
					comprasRealizadas(scanner);
					break;
				case 3:
					listaElectrodomestico();
					break;
				case 4:
					consultarStock();
					break;
				case 5:
					revisarCreditos(scanner);
					break;
				case 6:
					registrarTarjetaCliente(scanner);
					break;
				case 7:
					cargarProducto(scanner);
					break;
				case 8:
					agregarStockProducto(scanner);
					break;
				case 9:
					System.out.println("Saliendo del sistema...");
					break;
				default:
					System.out.println("Opción inválida, por favor intente nuevamente.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: Entrada no válida. Por favor, ingrese un número entero.");
				scanner.nextLine();
			} catch (Exception e) {
				System.out.println("Error inesperado: " + e.getMessage());
				e.printStackTrace();
			}
		} while (opcion != 9);

		scanner.close();
	}

	public static long realizarVenta(Scanner scanner, long Numfact) {
		int opcion = 0, cantidad = 0;
		double totalElectro = 0, totalCelu = 0;
		final double LIMITE_TOTAL = 1500000, LIMITE_CELULARES = 800000;
		long codigoProducto;
		Stock stock = null;
		Producto prodSelecc = null;
		List<Detalle> detalles = new ArrayList<Detalle>();
		try {
			System.out.println("\n=== Realizar una venta ===");
			do {
				codigoProducto = obtenerCodigoProducto(scanner);
				prodSelecc = CollectionProducto.buscarProducto(codigoProducto);
				stock = CollectionStock.buscarStock(prodSelecc);
				if (prodSelecc == null) {
					System.out.println("Producto no disponible.");
				} else if (stock == null) {
					System.out.println("Producto sin stock.");
				} else {
					cantidad = obtenerCantidad(scanner, stock);
					if (stock.getCantidad() == 0) {
						System.out.println("Producto sin stock.");
					} else {
						System.out.println("\nProducto seleccionado: " + prodSelecc.getDescripcion() + " - Precio: "
								+ prodSelecc.getPrecioUnitario());
						if (cantidad <= stock.getCantidad()) {
							Detalle detalle = new Detalle(cantidad, prodSelecc.getPrecioUnitario(), prodSelecc);
							if (totalElectro + detalle.getImporte() > LIMITE_TOTAL) {
								System.out.println(
										"El total de la compra no puede exceder $1.500.000. No puede agregar este producto.");
							} else if (prodSelecc.getDescripcion().toLowerCase().contains("celular")
									&& totalCelu + detalle.getImporte() > LIMITE_CELULARES) {
								System.out.println(
										"El total de celulares no puede exceder $800.000. No puede agregar este celular.");
							} else {
								detalles.add(detalle);
								totalElectro += detalle.getImporte();
								if (prodSelecc.getDescripcion().toLowerCase().contains("celular")) {
									totalCelu += detalle.getImporte();
								}
							}
						} else {
							System.out.println("Producto sin stock suficiente.");
						}
					}
				}
				opcion = menuContinuarFinalizar(scanner, totalElectro, totalCelu);
				if(opcion==2) {
					return Numfact;
				}
			} while (opcion != 1);

			do {
				TarjetaCredito tarjetaCredito = null;
				while (true) {
					System.out.println("Ingrese el número de su tarjeta de crédito:");
					try {
						tarjetaCredito = CollectionTarjetaCredito.buscarTarjetaCredito(scanner.nextLong());
						scanner.nextLine();
						break;
					} catch (InputMismatchException e) {
						System.out.println("Error: Entrada no válida. Por favor, ingrese un número entero.");
						scanner.nextLine();
					}
				}
				if (tarjetaCredito == null) {
					System.out.println("La tarjeta ingresada no existe.");
				} else if (tarjetaCredito.getLimiteCompra() < totalElectro) {
					System.out.println("No tiene suficiente límite en su tarjeta para realizar la compra.");
				} else {
					CollectionStock.reducirStock(CollectionStock.buscarStock(prodSelecc), cantidad);
					Factura factura = new Factura(LocalDate.now(), ++Numfact, tarjetaCredito.getCliente(), detalles);
					CollectionFactura.agregarFactura(factura);

					// Crear y agregar crédito
					List<Cuota> cuotas = new ArrayList<Cuota>();
					CollectionCredito.agregarCredito(new Credito(tarjetaCredito, factura, cuotas));
					System.out.println("¡Venta realizada exitosamente!");
					return Numfact;
				}
				do {
					System.out.println("0 - Dejar productos y cancelar la compra");
					System.out.println("1 - Reintentar con otra tarjeta");
					try {
						opcion = scanner.nextInt();
						switch (opcion) {
						case 0:
							return Numfact;
						case 1:
							System.out.println("Reintentando con otra tarjeta...");
							break;
						default:
							System.out.println("Opción inválida");
							break;
						}
					} catch (InputMismatchException e) {
						System.out.println("Error: opción inválida.");
						scanner.next();
					}
				} while (opcion != 1);

			} while (true);
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
		}

		return Numfact;
	}

	public static long obtenerCodigoProducto(Scanner scanner) {
		long codigoProducto = -1;
		while (true) {
			System.out.println("Ingrese el código del producto que desea comprar:");
			try {
				codigoProducto = scanner.nextLong();
				scanner.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Error: Entrada no válida. Por favor, ingrese un número entero.");
				scanner.nextLine();
			}
		}
		return codigoProducto;
	}

	public static int obtenerCantidad(Scanner scanner, Stock stock) {
		int cantidad = -1;
		while (true) {
			System.out.println("Ingrese la cantidad que desea comprar:");
			try {
				cantidad = scanner.nextInt();
				scanner.nextLine();
				if (cantidad <= 0) {
					System.out.println("Error: Debe ingresar una cantidad mayor que 0. Inténtelo de nuevo.");
				} else {
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: Debe ingresar un número válido. Inténtelo de nuevo.");
				scanner.next();
			}
		}
		return cantidad;
	}

	public static int menuContinuarFinalizar(Scanner scanner, double totalElectro, double totalCelu) {
		int opcion;
		do {
			System.out.println("Total de los electrodomésticos seleccionados hasta el momento: $" + totalElectro);
			System.out.println("Total solo celulares: $" + totalCelu);
			System.out.println("¿Desea agregar otros productos a su compra?");
			System.out.println("0 - Agregar más productos");
			System.out.println("1 - Finalizar compra");
			System.out.println("2 - Cancelar compra y salir sin comprar");
			opcion = scanner.nextInt();
			scanner.nextLine();

			switch (opcion) {
			case 0:
				System.out.println("Continuando con la compra, agregue otros productos...");
				return 0;
			case 1:
				System.out.println("Finalizando la compra...");
				return 1;
			case 2:
				System.out.println("Ha cancelado la compra. No se han realizado cargos.");
				return 2;
			default:
				System.out.println(
						"Opción inválida. Por favor, ingrese 0 para continuar, 1 para finalizar o 2 para cancelar la compra.");
				break;
			}
		} while (true);

	}

	public static void revisarCreditos(Scanner scanner) {
		long dni = 0;
		while (true) {
			System.out.print("Ingrese el DNI del cliente: ");
			try {
				dni = scanner.nextLong();
				scanner.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Error: Entrada no válida. Por favor, ingrese un número entero.");
				scanner.nextLine();
			}
		}
		Cliente cliente = CollectionCliente.buscarCliente(dni);
		if (cliente != null) {
			List<Factura> facturas = cliente.consultarCompras();
			if (facturas != null && !facturas.isEmpty()) {
				System.out.println("Créditos registrados para el cliente con DNI: " + dni);
				for (Credito credito : CollectionCredito.creditos) {
					if (cliente == credito.getFactura().getCliente()) {
						credito.mostarCredito();
					}
				}
			} else {
				System.out.println("El cliente no tiene créditos registrados.");
			}
		} else {
			System.out.println("Cliente no encontrado.");
		}
	}

	public static void comprasRealizadas(Scanner scanner) {
		System.out.print("Ingrese el DNI del cliente: ");
		Cliente cliente = CollectionCliente.buscarCliente(scanner.nextLong());
		if (cliente != null) {
			List<Factura> facturas = cliente.consultarCompras();
			if (facturas != null && !facturas.isEmpty()) {
				for (Factura factura : facturas) {
					System.out.println(factura.toString());
				}
			} else {
				System.out.println("El cliente no tiene compras registradas.");
			}
		} else {
			System.out.println("Cliente no encontrado.");
		}
	}

	public static void listaElectrodomestico() {
		for (Stock stock : CollectionStock.stocks) {
			if (programaAhora30(stock.getProducto()) && stock.getCantidad() > 0) {
				System.out.println(stock.getProducto().toString());
			}
		}
	}

	public static void consultarStock() {
		System.out.println("Electrodomésticos disponibles en el programa 'Ahora 30':");
		for (Stock stock : CollectionStock.stocks) {
			if (programaAhora30(stock.getProducto())) {
				System.out.println("Producto: " + stock.getProducto().getDescripcion() + " Stock disponible: "
						+ stock.getCantidad());
			}
		}
	}

	public static boolean programaAhora30(Producto producto) {
		if (producto.getOrigenFabricacion().toLowerCase().equals("argentina")) {
			if (!producto.getDescripcion().toLowerCase().contains("celular")) {
				if (producto.getPrecioUnitario() <= 1500000) {
					return true;
				}
			} else {
				if (producto.getPrecioUnitario() <= 800000) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean opcionReintentar(Scanner scanner, String tipo) {
		int opcion;
		do {
			try {
				System.out.println("¿Desea reintentar ingresar los datos del " + tipo + " o salir?");
				System.out.println("1 - Reintentar");
				System.out.println("2 - Salir");
				opcion = scanner.nextInt();

				switch (opcion) {
				case 1:
					System.out.println("Reintentando ...");
					return true;
				case 2:
					return false;
				default:
					System.out.println("Opción inválida. Intente nuevamente.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: opción inválida.");
				scanner.nextLine();
			}
		} while (true);
	}

	public static void registrarTarjetaCliente(Scanner scanner) {
		boolean datosCorrectos = false;
		while (!datosCorrectos) {
			try {
				System.out.println("\n=== Registrar Nuevo Cliente ===");

				System.out.println("Ingrese el DNI del cliente:");
				long dni = scanner.nextLong();
				scanner.nextLine();

				System.out.println("Ingrese el nombre del cliente:");
				String nombre = scanner.nextLine();

				System.out.println("Ingrese la dirección del cliente:");
				String direccion = scanner.nextLine();

				System.out.println("Ingrese el teléfono del cliente:");
				String telefono = scanner.nextLine();

				Cliente nuevoCliente = new Cliente(dni, nombre, direccion, telefono);

				boolean tarjetaCorrecta = false;
				while (!tarjetaCorrecta) {
					try {
						System.out.println("\n=== Registrar Tarjeta de Crédito ===");
						System.out.println("Ingrese el número de la tarjeta de crédito:");
						long nroTarjeta = scanner.nextLong();

						System.out.println("Ingrese el límite de la tarjeta de crédito:");
						double limiteCompra = scanner.nextDouble();
						scanner.nextLine();

						System.out.println("Ingrese la fecha de caducación de la tarjeta (formato: yyyy-MM-dd):");
						String fechaCaducacionInput = scanner.nextLine();
						LocalDate fechaCaducacion = LocalDate.parse(fechaCaducacionInput);

						TarjetaCredito nuevaTarjeta = new TarjetaCredito(nroTarjeta, fechaCaducacion, nuevoCliente,
								limiteCompra);

						CollectionCliente.agregarCliente(nuevoCliente);
						CollectionTarjetaCredito.agregarTarjetaCredito(nuevaTarjeta);

						System.out.println("Cliente y tarjeta registrados exitosamente!");
						datosCorrectos = true;
						tarjetaCorrecta = true;
					} catch (InputMismatchException e) {
						System.out.println("Error: Ingreso de datos inválido. Intente nuevamente.");
						scanner.nextLine();
					} catch (DateTimeParseException e) {
						System.out.println("Error: Fecha de caducación inválida. Use el formato yyyy-MM-dd.");
					}

					if (!tarjetaCorrecta) {
						if (!opcionReintentar(scanner, "tarjeta")) {
							System.out.println("Registro cancelado.");
							return;
						}
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("Error: Ingreso de datos inválido. Intente nuevamente.");
				scanner.nextLine();
			}

			if (!datosCorrectos) {
				if (!opcionReintentar(scanner, "cliente")) {
					System.out.println("Registro cancelado.");
					return;
				}
			}
		}
	}

	public static void cargarProducto(Scanner scanner) {

		System.out.println("\n=== Cargar Nuevo Producto ===");
		boolean datosCorrectos = false;
		while (!datosCorrectos) {
			try {
				System.out.println("Ingrese el código del producto:");
				long codigo = scanner.nextLong();
				scanner.nextLine();

				System.out.println("Ingrese la descripción del producto:");
				String descripcion = scanner.nextLine();

				System.out.println("Ingrese el precio unitario del producto:");
				double precioUnitario = scanner.nextDouble();
				scanner.nextLine();

				System.out
						.println("Ingrese el origen de fabricación (Los productos de Argentina entran al programa ):");
				String origenFabricacion = scanner.nextLine();

				System.out.println("Ingrese la cantidad en stock:");
				int cantidad = scanner.nextInt();
				scanner.nextLine();

				Producto nuevoProducto = new Producto(codigo, descripcion, precioUnitario, origenFabricacion);
				Stock nuevoStock = new Stock(cantidad, nuevoProducto);
				CollectionStock.agregarStock(nuevoStock);
				CollectionProducto.agregarProducto(nuevoProducto);

				datosCorrectos = true;
				System.out.println("Producto y stock cargados exitosamente!");
			} catch (InputMismatchException e) {
				System.out.println("Error: Ingreso de datos inválido. Intente nuevamente.");
				scanner.nextLine();
			}
			if (!datosCorrectos) {
				if (!opcionReintentar(scanner, "producto")) {
					System.out.println("Registro cancelado.");
					return;
				}
			}
		}
	}

	public static void agregarStockProducto(Scanner scanner) {
		System.out.println("\n=== Agregar Stock a un Producto ===");
		System.out.print("Ingrese el código del producto: ");
		long codigoProducto = scanner.nextLong();
		scanner.nextLine();
		Producto producto = CollectionProducto.buscarProducto(codigoProducto);
		Stock stock = CollectionStock.buscarStock(producto);

		if (stock != null) {
			System.out.print("Ingrese la cantidad a agregar al stock: ");
			int cantidadAgregar = scanner.nextInt();
			scanner.nextLine();

			if (cantidadAgregar <= 0) {
				System.out.println("La cantidad a agregar debe ser mayor que cero.");
				return;
			}
			stock.setCantidad(stock.getCantidad() + cantidadAgregar);
			System.out.println("Se han agregado " + cantidadAgregar + " unidades al stock del producto: "
					+ producto.getDescripcion());
		} else {
			System.out.println("No se encontró stock para el producto.");
		}

	}
}
