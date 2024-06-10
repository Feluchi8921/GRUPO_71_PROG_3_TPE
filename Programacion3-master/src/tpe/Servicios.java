package tpe;
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
	private int estadosGeneradosBack;
	private int estadosGeneradosGreedy;


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
		this.estadosGeneradosBack=0;
		estadosGeneradosGreedy=0;
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

	/*Este metodo aplica el algoritmo Backtracking de búsqueda exhaustiva que explora recursivamente todas las posibles
	asignaciones de tareas a procesadores, manteniendo un registro del tiempo total de ejecución actual y
	la mejor solución encontrada hasta el momento. Si una asignación no cumple con las restricciones o no mejora la mejor
	 solución,se descarta y se exploran otras alternativas. El proceso continúa hasta encontrar la asignación óptima o hasta
	 que se agotan todas las posibilidades.*/
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
		this.estadosGeneradosBack++;
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
		this.estadosGeneradosBack++; //Por cada estado que genero voy sumando
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
	public int getEstadosGeneradosBack() {
		return estadosGeneradosBack;
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


	//-------------------------------Greedy------------------------------------------------------------------------

    /*La complejidad temporal de este método es O(n log m), donde n es la cantidad de tareas y m es la cantidad de procesadores.
    Esto se debe a que en cada iteración se selecciona el procesador con el menor tiempo total de ejecución, que puede
    lograrse utilizando una cola de prioridad.*/

	/*Este étodo aplica el algoritmo Greedy. Es unalgoritmo heurístico que toma decisiones a corto plazo
	con el objetivo de optimizar la solución global.
	En este contexto de asignación de tareas a procesadores, el algoritmo Greedy selecciona iterativamente el procesador
	con el menor tiempo total de ejecución actual para cada tarea, sin considerar el impacto en la carga de trabajo
	de los demás procesadores. Este enfoque busca una buena solución en un tiempo computacional menor que el Backtracking,
	 pero no garantiza la optimalidad.*/
	public HashMap<Procesador, List<Tarea>> asignarTareasGreedy(int tiempoMaxNoRefrigerado) {
		//Le asigno el tiempo de ejecucion maxima para los procesadores no refirgerados
		this.maxTiempoEjecucion = tiempoMaxNoRefrigerado;
		//Nuevo Hashmap
		this.tareasAsignadas = new HashMap<>();

		//Creo una cola de prioridad de procesadores ordenada por su tiempo total de ejecución actual
		PriorityQueue<Procesador> minHeap = new PriorityQueue<>(Comparator.comparingInt(this::getTiempoEjecucionProcesador));

		//Agrego cada procesador a la cola de prioridad
		for (Procesador procesador : procesadores) {
			this.tareasAsignadas.put(procesador, new ArrayList<>());
			minHeap.offer(procesador);
		}

		//Recorro las tareas
		for (Tarea tarea : tareas) {
			boolean asignada = false;

			//Mientras la tarea no este asignada y la cola de prioridad no este vacía
			while (!asignada && !minHeap.isEmpty()) {
				//Obtengo el procesador con menor tiempo total de ejecucion actual
				Procesador procesador = minHeap.poll();

				//Si la tarea se puede asignar (condiciones de asignacion), la agrego
				if (puedeAsignar(procesador, tarea)) {
					asignar(procesador, tarea);
					asignada = true;
					this.estadosGeneradosGreedy++; //Llevo el conteo de estados
				}

				//Actualizo la cola
				minHeap.offer(procesador);
			}

			if (!asignada) {
				return null; // No es posible realizar la asignación
			}
		}

		//Recupero las tareas asignadas
		getTareasAsignadas();
		return tareasAsignadas;
	}

	private int getTiempoEjecucionProcesador(Procesador procesador) {
		return tareasAsignadas.get(procesador).stream().mapToInt(Tarea::getTiempoEjecucion).sum();
	}

	public int getEstadosGeneradosGreedy(){
		return estadosGeneradosGreedy;
	}
}
