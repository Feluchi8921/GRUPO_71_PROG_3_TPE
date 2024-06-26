package tpe;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		// Cargar las tareas y procesadores desde los archivos CSV
		Servicios servicios = new Servicios("Programacion3-master/src/tpe/datasets/Procesadores.csv", "Programacion3-master/src/tpe/datasets/Tareas.csv");

		//----------------------------------Servicios 1, 2 y 3-------------------------------------
		// Servicio 1
		System.out.println("--------------La tarea con ID T1 es:-------------");
		System.out.println(servicios.servicio1("T1"));
		// Servicio 2
		System.out.println("\n--------------Las tareas criticas son:------------------- ");
		System.out.println(servicios.servicio2(true));
		System.out.println("\n--------------Las tareas no criticas son:------------------- ");
		System.out.println(servicios.servicio2(false));
		// Servicio 3
		int prioridadInferior = 1;
		int prioridadSuperior = 80;
		System.out.println("\n--------------Las tareas dentro del rango de prioridad especificado son:-------------------");
		System.out.println(servicios.servicio3(prioridadInferior, prioridadSuperior));


		int tiempoMaxNoRefrigerado = 200;

		System.out.println("\n-----------Asignación Backtracking:-------------------");
		HashMap<Procesador, List<Tarea>> tareasAsignadas = servicios.asignarTareasBacktracking(tiempoMaxNoRefrigerado);
		if (tareasAsignadas == null || tareasAsignadas.isEmpty()) {
			System.out.println("No hay solucion");
		} else {
			for (Map.Entry<Procesador, List<Tarea>> entry : tareasAsignadas.entrySet()) {
				Procesador procesador = entry.getKey();
				List<Tarea> tareas = entry.getValue();

				System.out.println("\n+Procesador: " + procesador);
				System.out.println(" -Tareas asignadas:");
				for (Tarea tarea : tareas) {
					System.out.println("	- " + tarea);
				}
			}

			System.out.println(
					"\n-----------Tiempo máximo de ejecución: (cantidad de datos considerados): -------------------" +
							"\nEl tiempo máximo es: " +
							servicios.getMaxTiempoEjecucion() + "\n" +
							"\n-----------Métrica (cantidad de datos considerados): -------------------" +
							"\nLos estados generados fueron: " + servicios.getEstadosGeneradosBack()

			);
		}



		System.out.println("\n-----------------Asignación Greedy:------------------------");
		// Asignar tareas usando el algoritmo greedy
		HashMap<Procesador, List<Tarea>> asignacion = servicios.asignarTareasGreedy(tiempoMaxNoRefrigerado);

		// Imprimir resultado de la asignación
		if (asignacion != null) {
			System.out.println("Asignación Greedy exitosa:");
			for (Procesador procesador : asignacion.keySet()) {
				System.out.println("+Procesador: " + procesador);
				System.out.println(" -Tareas asignadas:");
				for (Tarea tarea : asignacion.get(procesador)) {
					System.out.println("    - " + tarea);
				}
			}
		} else {
			System.out.println("No es posible realizar la asignación");
		}

		System.out.println(
				"\n-----------Tiempo máximo de ejecución: (cantidad de datos considerados): -------------------" +
						"\nEl tiempo máximo es: "+
						servicios.getMaxTiempoEjecucion()+"\n"+
						"\n-----------Métrica (cantidad de datos considerados): -------------------" +
						"\nLos estados generados fueron: "+servicios.getEstadosGeneradosGreedy()

		);


	}

}

