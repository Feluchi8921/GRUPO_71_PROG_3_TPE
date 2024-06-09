package tpe;

import com.sun.source.tree.NewArrayTree;
import tpe.utils.CSVReader;
import java.util.*;

public class Servicios {

	//Atributos primera parte
	private ArrayList<Tarea> tareas;
	private HashMap<String, Tarea> tareasId;
	private HashMap<Boolean, List<Tarea>> tareasCriticidad;

	//Atributos segunda parte
	private ArrayList<Procesador> procesadores;
	private final int maxTareasCritPorProc = 2;
	private int maxTiempoEjecucion;
	private HashMap<Procesador, List<Tarea>> tareasAsignadas;
	private int tiempoMejor;
	private List<Tarea> tareasMejor;
	private int estadosGenerados;

	//La complejidad del constructor de la clase Servicios es O(n), donde n es la cantidad de tareas almacenadas en los archivos CSV
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasks(pathTareas);
		this.tareasId = new HashMap<>();
		this.tareasCriticidad = new HashMap<>();
		this.addTareas(this.tareas); //Se cargan las tareas en cada estructura
		this.procesadores = reader.readProcessors(pathProcesadores);
		this.tareasAsignadas = new HashMap<>();
		this.tiempoMejor =  Integer.MAX_VALUE; // Inicializar a un valor grande
		this.tareasMejor = new ArrayList<>();
		this.estadosGenerados=0;
	}

	//-------------------------------Servicio 1:-------------------------------------------------

	//Complejidad temporal: O(1) donde n es la cantidad de tareas
	public Tarea servicio1(String ID) {
		return tareasId.get(ID);
	}


	//-------------------------------Servicio 2:-------------------------------------------------
	//Complejidad temporal: O(1) donde n es la cantidad de tareas
	public List<Tarea> servicio2(boolean esCritica) {
		return tareasCriticidad.get(esCritica);
	}


	//-------------------------------Servicio 3:-------------------------------------------------
	/*	Complejidad temporal: O(n) donde n es la cantidad de tareas.
	Se podría mejorar la complejidad utilizando un arbol binario de busqueda balanceado.
	Se decidió no utilizarlo porque no sabemos utilizar las librerías de arboles provistas por Java y
	para utilizar nuestra clase Arbol realizada en la cursada deberíamos utilizar técnicas de balanceo.*/

	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> tareasFiltradas = new ArrayList<>();
		for (Tarea tarea : tareas) {
			int prioridadTarea = tarea.getNivelPrioridad();
			if (prioridadTarea >= prioridadInferior && prioridadTarea <= prioridadSuperior) {
				tareasFiltradas.add(tarea);
			}
		}
		return tareasFiltradas;
	}

	//Este metodo se encarga de la carga de las estructuras a utilizar.
	private void addTareas(ArrayList<Tarea> tareas) {
		for (Tarea t : tareas) {
			// Se crea un nuevo objeto Tarea con los datos leídos
			Tarea tarea = new Tarea(t.getIdTarea(), t.getNombreTarea(), t.getTiempoEjecucion(), t.isCritica(), t.getNivelPrioridad());

			//Se agrega las tareas al hashmap de servicio 1:
			tareasId.put(t.getIdTarea(), t);

			//Se agrega las tareas al hashmap de servicio 2:
			//Se recupera la lista de tareas para la criticidad actual
			List<Tarea> tareasCriticidad = this.tareasCriticidad.getOrDefault(t.isCritica(), new ArrayList<>());

			//Se agrega la tarea a la lista correspondiente
			tareasCriticidad.add(tarea);

			//Se actualiza el mapa con la lista actualizada
			this.tareasCriticidad.put(t.isCritica(), tareasCriticidad);
		}
	}

	//-------------------------------Backtracking------------------------------------------------------------------------

	/*La complejidad temporal de este método es O(n^m), donde n es la cantidad de tareas y m es la cantidad de procesadores.
	Esto se debe a que el algoritmo de backtracking puede generar un número exponencial de posibles asignaciones.*/


	public HashMap<Procesador, List<Tarea>> asignarTareasBacktracking(int tiempoMaxNoRefrigerado) {
		//Le asigno el valor a los procesadores np refirgerados
		this.maxTiempoEjecucion = tiempoMaxNoRefrigerado;
		List<Tarea> asignacion = new ArrayList<>();
		if (asignarTareasBacktracking(0, asignacion, 0)) {
			// Devuelvo una copia de las tareas Asignadas
			return getTareasAsignadas();
		}
			return new HashMap<>();
	}

	private boolean asignarTareasBacktracking(int tareaIndex, List<Tarea> asignacion, int tiempoParcial) {
		//LLevo la cuenta de los estados generados
		this.estadosGenerados++;
		// Si el indice actual es igual a la cant de tareas es porque se asignaron todas
		if (tareaIndex == tareas.size()) {
			if (tiempoParcial < tiempoMejor) {
				tiempoMejor = tiempoParcial;
				return true;
			}
			return false;
		}

		// Obtengo la tarea del indice actual
		Tarea tarea = tareas.get(tareaIndex);

		// Recorro los procesadores
		for (Procesador procesador : procesadores) {
			// Verifico si es asignable
			if (puedeAsignar(procesador, tarea)) {
				// Calculo el nuevo tiempo parcial
				int nuevoTiempoParcial = tiempoParcial + tarea.getTiempoEjecucion();
				//System.out.println(nuevoTiempoParcial);
				// La agrego
				asignar(procesador, tarea);
				// Llamo a la recursion con el siguiente índice de tarea y la asignacion actual
				if (asignarTareasBacktracking(tareaIndex + 1, asignacion, nuevoTiempoParcial)) {
					return true;
				}
				// Elimino el agregar
				desasignar(procesador, tarea);
			}
		}

		return false;
	}

	//Metodo que se encarga de establecer las condiciones de asignacion de tareas
	private boolean puedeAsignar(Procesador procesador, Tarea tarea) {
		List<Tarea> tareasProcesador = tareasAsignadas.getOrDefault(procesador, new ArrayList<>());

		if (tarea.isCritica()) {
			long countCriticas = tareasProcesador.stream().filter(Tarea::isCritica).count();
			if (countCriticas >= maxTareasCritPorProc) {
				return false;
			}
		}

		if (!procesador.isRefrigerado()) {
			int tiempoTotal = tareasProcesador.stream().mapToInt(Tarea::getTiempoEjecucion).sum();
			if (tiempoTotal + tarea.getTiempoEjecucion() > maxTiempoEjecucion) {
				return false;
			}
		}

		return true;
	}

	//Este metodo asigna las tareas al procesador
	private void asignar(Procesador procesador, Tarea tarea) {
		tareasAsignadas.putIfAbsent(procesador, new ArrayList<>());
		tareasAsignadas.get(procesador).add(tarea);
		procesador.addTareaAsignada(tarea);
		//System.out.println(tareasAsignadas+"\n");
	}

	private void desasignar(Procesador procesador, Tarea tarea) {
		tareasAsignadas.get(procesador).remove(tarea);
	}

	public HashMap<Procesador, List<Tarea>> getTareasAsignadas() {
		return this.tareasAsignadas;
	}

	public int getTiempoEjecucion(List<Tarea> asignacion){
		int tiempo = 0;
		for(Tarea t : asignacion){
			tiempo += t.getTiempoEjecucion();
		}
		return tiempo;
	}

	//Metodo que devuelve la cantidad de estados de Backtracking
	public int getEstadosGenerados() {
		return estadosGenerados;
	}

	//Tiempo de ejecucion (El procesador que más tarda)
	public int getMaxTiempoEjecucion() {
		int maxTiempo = 0;
		for (Map.Entry<Procesador, List<Tarea>> entry : this.tareasAsignadas.entrySet()) {
			Procesador procesador = entry.getKey();
			List<Tarea> tareas = entry.getValue();
			int tiempoEjecucionProcesador = getTiempoEjecucion(tareas);
			if (tiempoEjecucionProcesador > maxTiempo) {
				maxTiempo = tiempoEjecucionProcesador;
			}
		}
		return maxTiempo;
	}

	public void imprimirMaxTiempoEjecucion() {
		int maxTiempo = getMaxTiempoEjecucion();
		System.out.println("El tiempo máximo de ejecución es: " + maxTiempo);
	}


	//-------------------------------Greedy Algorithm------------------------------------------------------------------------

    /*La complejidad temporal de este método es O(n log m), donde n es la cantidad de tareas y m es la cantidad de procesadores.
    Esto se debe a que en cada iteración se selecciona el procesador con el menor tiempo total de ejecución, que puede lograrse utilizando una cola de prioridad.*/

	public HashMap<Procesador, List<Tarea>> asignarTareasGreedy(int tiempoMaxNoRefrigerado) {
		this.maxTiempoEjecucion = tiempoMaxNoRefrigerado;
		this.tareasAsignadas = new HashMap<>();

		PriorityQueue<Procesador> minHeap = new PriorityQueue<>(Comparator.comparingInt(this::getTiempoEjecucionProcesador));

		for (Procesador procesador : procesadores) {
			this.tareasAsignadas.put(procesador, new ArrayList<>());
			minHeap.offer(procesador);
		}

		for (Tarea tarea : tareas) {
			boolean asignada = false;

			while (!asignada && !minHeap.isEmpty()) {
				Procesador procesador = minHeap.poll();

				if (puedeAsignar(procesador, tarea)) {
					asignar(procesador, tarea);
					asignada = true;
				}

				minHeap.offer(procesador);
			}

			if (!asignada) {
				return null; // No es posible realizar la asignación
			}
		}

		getTareasAsignadas();
		return tareasAsignadas;
	}

	private int getTiempoEjecucionProcesador(Procesador procesador) {
		return tareasAsignadas.get(procesador).stream().mapToInt(Tarea::getTiempoEjecucion).sum();
	}


}
