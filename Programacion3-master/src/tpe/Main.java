package tpe;
import tpe.utils.CSVReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Ejemplo de código `main` para probar la clase `Servicios`.
 *
 * Este código permite:
 * 1. Obtener una tarea por su ID (servicio 1).
 * 2. Obtener la lista de tareas críticas o no críticas (servicio 2).
 * 3. Obtener la lista de tareas dentro de un rango de prioridad (servicio 3).
 */
public class Main {

	public static void main(String[] args) {

		// Cargar las tareas y procesadores desde los archivos CSV
		Servicios servicios = new Servicios("Programacion3-master/src/tpe/datasets/Procesadores.csv", "Programacion3-master/src/tpe/datasets/Tareas.csv");
		//System.out.println("--------------La tarea con ID T1 es:-------------");
		//System.out.println(servicios.servicio1("T1"));
		//System.out.println("--------------Las tareas criticas son:------------------- ");
		//System.out.println(servicios.servicio2(true));
		//System.out.println("--------------Las tareas no criticas son:------------------- ");
		//System.out.println(servicios.servicio2(false));
		// Scanner para leer la entrada del usuario
		/*Scanner scanner = new Scanner(System.in);

		int opcion;
		do {
			System.out.println("\n\nMenú de servicios:");
			System.out.println("1. Obtener tarea por ID");
			System.out.println("2. Obtener tareas críticas o no críticas");
			System.out.println("3. Obtener tareas por rango de prioridad");
			System.out.println("4. Salir");
			System.out.print("Ingrese una opción: ");

			opcion = scanner.nextInt();
			scanner.nextLine(); // Consumir el salto de línea

			switch (opcion) {
				case 1:
					System.out.print("Ingrese el ID de la tarea: ");
					String idTarea = scanner.nextLine();
					Tarea tarea = servicios.servicio1(idTarea);
					if (tarea != null) {
						System.out.println("\nTarea encontrada:");
						System.out.println(tarea);
					} else {
						System.out.println("\nTarea no encontrada con el ID: " + idTarea);
					}
					break;
				case 2:
					System.out.print("¿Desea obtener tareas críticas (true) o no críticas (false)? ");
					boolean esCritica = scanner.nextBoolean();
					scanner.nextLine(); // Consumir el salto de línea
					List<Tarea> tareas = servicios.servicio2(esCritica);
					if (!tareas.isEmpty()) {
						System.out.println("\nLista de tareas " + (esCritica ? "críticas" : "no críticas") + ":");
						for (Tarea t : tareas) {
							System.out.println(t);
						}
					} else {
						System.out.println("\nNo se encontraron tareas " + (esCritica ? "críticas" : "no críticas"));
					}
					break;
				case 3:
					System.out.print("Ingrese la prioridad inferior: ");
					int prioridadInferior = scanner.nextInt();
					scanner.nextLine(); // Consumir el salto de línea
					System.out.print("Ingrese la prioridad superior: ");
					int prioridadSuperior = scanner.nextInt();
					scanner.nextLine(); // Consumir el salto de línea
					List<Tarea> tareasRango = servicios.servicio3(prioridadInferior, prioridadSuperior);
					if (!tareasRango.isEmpty()) {
						System.out.println("\nLista de tareas en el rango de prioridad [" + prioridadInferior + "," + prioridadSuperior + "]:");
						for (Tarea t : tareasRango) {
							System.out.println(t);
						}
					} else {
						System.out.println("\nNo se encontraron tareas en el rango de prioridad especificado.");
					}
					break;
				case 4:
					System.out.println("Saliendo del programa...");
					break;
				default:
					System.out.println("Opción no válida. Intente nuevamente.");
			}
		} while (opcion != 4);

		scanner.close();
		*/
		// Define the max execution time for non-refrigerated processors
		int tiempoMaxNoRefrigerado = 100;

		// Run the backtracking task assignment
		servicios.asignarTareasBacktracking(tiempoMaxNoRefrigerado);
	}
}
