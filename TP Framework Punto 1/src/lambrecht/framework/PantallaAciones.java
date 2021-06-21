package lambrecht.framework;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PantallaAciones {
	Accion accion;
	Map<Integer, Accion> acciones = new HashMap<Integer, Accion>();

	public PantallaAciones() {
		Properties prop = new Properties();
		try (InputStream configuracionArchivo = getClass()
				.getResourceAsStream("/framework/config/config.properties");) {

			prop.load(configuracionArchivo);

			String valor = prop.getProperty("acciones");
			String[] valores = valor.split(";");
			Class<?> clase;
			for (int i = 0; i < valores.length; i++) {
				clase = Class.forName(valores[i]);
				acciones.put(i + 1, (Accion) clase.getDeclaredConstructor().newInstance());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("No se pudo crear una instancia de Accion: ");
		}

	}

	public final void desplegarMenu() {

		try (Scanner sn = new Scanner(System.in)) {
			boolean salir = false;
			int opcion;
			while (!salir) {
				System.out.println("\nElija una opcion para ejecutar una accion.....(barras barras)");
				acciones.forEach((k, v) -> System.out.println(k + ": " + v.nombreItemMenu())); // Muestra las clases
																								// agregadas en en
																								// properties

				System.out.println("99: Salir \n\nIngrese una de las opciones a mostrar: ");

				opcion = sn.nextInt();

				if (opcion == 99) {
					System.out.println("Fin...");
					salir = true;
				} else if (acciones.containsKey(opcion)) {
					this.accion = acciones.get(opcion);
					imprimir();
				} else {
					System.out.println("No existe esa opcion...");
				}
			}
		}
	}

	public final void imprimir() {
		System.out.println("Se esta ejecutando... " + accion.nombreItemMenu());
		System.out.println("Descripcion: " + accion.descripcionItemMenu());
		accion.ejecutar();
	}
}
