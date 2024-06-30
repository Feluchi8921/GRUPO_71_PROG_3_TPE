package tpe;
import tpe.utils.CSVReader;
import java.util.*;

public class Servicios {

	//Atributos primera parte
	private static final int PRIORIDAD_MINIMA = 1;
	private static final int PRIORIDAD_MAXIMA = 100;
	private ArrayList<Tarea> tareas;
	private HashMap<String, Tarea> tareasId;
	private HashMap<Boolean, List<Tarea>> tareasCriticidad;
	private ArrayList<Tarea> tareasPrioridad;

	//Atributos segunda parte
	private ArrayList<Procesador> procesadores;
	private static final int MAX_TAREAS_CRITICAS = 2;
	private int maxTiempoEjecucion;
	private HashMap<Procesador, List<Tarea>> tareasAsignadas;
	private int tiempoMaxNoRefrigerado;
	private int tiempoMejor;
	private int estadosGeneradosBack;
	private int estadosGeneradosGreedy;


	//La complejidad del constructor de la clase Servicios es O(n), donde n es la cantidad de tareas almacenadas en los archivos CSV
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasks(pathTareas);
		this.tareasId = new HashMap<>();
		this.tareasCriticidad = new HashMap<>();
		this.tareasPrioridad = new ArrayList<>();
		this.addTareas(this.tareas); //Se cargan las tareas en cada estructura
		this.procesadores = reader.readProcessors(pathProcesadores);
		this.tiempoMaxNoRefrigerado = 0;
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
		List<Tarea> resultado = new ArrayList<>();

		if (prioridadInferior < PRIORIDAD_MINIMA || prioridadSuperior > PRIORIDAD_MAXIMA) {
			return resultado;
		}

		for (Tarea tarea : this.tareasPrioridad) {
			int prioridad = tarea.getNivelPrioridad();

			if (prioridad >= prioridadInferior && prioridad <= prioridadSuperior) {
				resultado.add(tarea);
			}

			else if (prioridad > prioridadSuperior) {
				break;
			}
		}
		return resultado;
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

			//Tareas por prioridad
			this.tareasPrioridad.add(t);
		}
		//Ordeno las tareas por prioridad
		Collections.sort(this.tareasPrioridad);
	}

	//-------------------------------Backtracking------------------------------------------------------------------------

	/*La complejidad temporal de este método es O(n^m), donde n es la cantidad de tareas y m es la cantidad de procesadores.
	Esto se debe a que el algoritmo de backtracking puede generar un número exponencial de posibles asignaciones.*/

	/*Este metodo aplica el algoritmo Backtracking de búsqueda exhaustiva que explora recursivamente todas las posibles
	asignaciones de tareas a procesadores, manteniendo un registro del tiempo total de ejecución actual y
	la mejor solución encontrada hasta el momento. Si una asignación no cumple con las restricciones o no mejora la mejor
	 solución,se descarta y se exploran otras alternativas. El proceso continúa hasta encontrar la asignación óptima o hasta
	 que se agotan todas las posibilidades.
	 Modificación: Mejoré el manejo de los estados
	 */
	public HashMap<Procesador, List<Tarea>> asignarTareasBacktracking(int tiempoMaxNoRefrigerado) {
		//Le asigno el valor a los procesadores np refirgerados
		this.tiempoMaxNoRefrigerado = tiempoMaxNoRefrigerado;
		HashMap<Procesador, List<Tarea>> estado = new HashMap<>(); // PENSAR
		this.asignarTareasBacktracking(0, 0, estado);
		return this.copiarHashMap(this.tareasAsignadas);
	}

	private void asignarTareasBacktracking(int tareaIndex, int tiempoParcial, HashMap<Procesador, List<Tarea>> estado) {
		// Si el índice de la tarea es igual al tamaño, ya se asignaron todas y se interrumpe la recursión
		if (tareaIndex == this.tareasPrioridad.size()) { //condición de corte
			int tiempoMaximo = calcularTiempoMaximoProcesadores();
			if (tiempoMaximo <= this.tiempoMejor) {
				this.tiempoMejor = tiempoMaximo; // Se actualiza el tiempo máximo
				this.tareasAsignadas = this.copiarHashMap(estado);
			}
			return;
		}

		// Se obtiene la tarea con el índice dado
		Tarea t = this.tareasPrioridad.get(tareaIndex);

		for (Procesador p : procesadores) {
			if (this.puedeAsignar(t, p, estado)) { // Poda
				// Se asigna la tarea y se calcula el nuevo tiempo parcial
				this.asignar(t, p, estado);
				int nuevoTiempoParcial = tiempoParcial + t.getTiempoEjecucion();
				this.estadosGeneradosBack++;

				this.asignarTareasBacktracking(tareaIndex + 1, nuevoTiempoParcial, estado); // Llamado recursivo

				this.desasignar(t, p, estado); // Se libera la tarea asignada (deshacer cambios)
			}
		}
	}

	// Método que verifica si puede asignarse una determinada tarea a un procesador dado
	private boolean puedeAsignar(Tarea tarea, Procesador procesador, HashMap<Procesador, List<Tarea>> estado) {
		// Verificar límite de tareas críticas
		int criticasAsignadas = 0;
		List<Tarea> tareasAsignadas = estado.get(procesador);

		if (tareasAsignadas == null)
			tareasAsignadas = new ArrayList<>();

		for (Tarea t : tareasAsignadas) {
			if (t.isCritica())
				criticasAsignadas++;
		}

		if (tarea.isCritica() && criticasAsignadas >= MAX_TAREAS_CRITICAS)
			return false; // No se pueden asignar más de 2 tareas críticas en total

		// Verificar tiempo límite en procesadores no refrigerados
		if (!procesador.isRefrigerado()) {
			int tiempoTotal = 0;
			for (Tarea t : tareasAsignadas) {
				tiempoTotal += t.getTiempoEjecucion();
			}

			if (tiempoTotal + tarea.getTiempoEjecucion() > this.tiempoMaxNoRefrigerado)
				return false; // Se excede el tiempo límite
		}

		return true; // La asignación cumple con todas las restricciones
	}

	// Método que asigna una tarea a un procesador
	private void asignar(Tarea tarea, Procesador procesador, HashMap<Procesador, List<Tarea>> estado) {
		// Si no existe el procesador en el HashMap lo agrega
		if (!estado.containsKey(procesador)) {
			estado.put(procesador, new ArrayList<>());
		}
		estado.get(procesador).add(tarea);
		procesador.addTareaAsignada(tarea); // Se actualiza la lista de tareas en el objeto Procesador
	}

	// Método que remueve una tarea asignada de procesador
	private void desasignar(Tarea tarea, Procesador procesador, HashMap<Procesador, List<Tarea>> estado) {
		estado.get(procesador).remove(tarea);
		procesador.removeTarea(tarea);
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

	public int calcularTiempoMaximoProcesadores() {
		int tiempoMaximo = 0;
		for (Procesador p : this.procesadores) {
			if (p.getTiempoTotal() > tiempoMaximo)
				tiempoMaximo = p.getTiempoTotal();
		}
		return tiempoMaximo;
	}
	//-------------------------------Greedy------------------------------------------------------------------------

    /*La complejidad temporal de este método es O(n log m), donde n es la cantidad de tareas y m es la cantidad de procesadores.
    Esto se debe a que en cada iteración se selecciona el procesador con el menor tiempo total de ejecución, que puede
    lograrse utilizando una cola de prioridad.

	/*Este étodo aplica el algoritmo Greedy. Es un algoritmo heurístico que toma decisiones a corto plazo
	con el objetivo de optimizar la solución global.
	En este contexto de asignación de tareas a procesadores, el algoritmo Greedy selecciona iterativamente el procesador
	con el menor tiempo total de ejecución actual para cada tarea, sin considerar el impacto en la carga de trabajo
	de los demás procesadores. Este enfoque busca una buena solución en un tiempo computacional menor que el Backtracking,
	 pero no garantiza la optimalidad.
	Modificación: Se agregó el corte dentro del while porque cuando no encontraba solución se generaba un bucle infinito
     */
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
				if (puedeAsignarGreedy(procesador, tarea)) {
					asignarGreedy(procesador, tarea);
					asignada = true;
					this.estadosGeneradosGreedy++; //Llevo el conteo de estados
				}

				//Aca estaba el error del bucle infinito!!!!!!!!!!!!!!
				else{
					this.tareasAsignadas=new HashMap<>();
					this.estadosGeneradosGreedy=0;
					break;
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
	//Metodo que se encarga de establecer las condiciones de asignacion de tareas
	private boolean puedeAsignarGreedy(Procesador procesador, Tarea tarea) {
		List<Tarea> tareasProcesador = tareasAsignadas.getOrDefault(procesador, new ArrayList<>());
		if (tarea.isCritica()) {
			long countCriticas = tareasProcesador.stream().filter(Tarea::isCritica).count();
			if (countCriticas >= MAX_TAREAS_CRITICAS) {
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
	private void asignarGreedy(Procesador procesador, Tarea tarea) {
		tareasAsignadas.putIfAbsent(procesador, new ArrayList<>());
		tareasAsignadas.get(procesador).add(tarea);
		procesador.addTareaAsignada(tarea);
	}

	private int getTiempoEjecucionProcesador(Procesador procesador) {
		return tareasAsignadas.get(procesador).stream().mapToInt(Tarea::getTiempoEjecucion).sum();
	}

	public int getEstadosGeneradosGreedy(){
		return estadosGeneradosGreedy;
	}

// Devuelve una copia del HashMap de asignación de tareas
	private HashMap<Procesador, List<Tarea>> copiarHashMap(HashMap<Procesador, List<Tarea>> original) {
		HashMap<Procesador, List<Tarea>> copia = new HashMap<>();

		// Itera sobre las entradas del HashMap original
		for (Map.Entry<Procesador, List<Tarea>> entrada : original.entrySet()) {
			Procesador procesador = entrada.getKey();
			List<Tarea> tareasAsignadas = entrada.getValue();

			// Crea una nueva lista para almacenar las tareas asignadas al procesador
			List<Tarea> nuevasTareas = new ArrayList<>(tareasAsignadas);

			// Agrega la entrada al HashMap copia
			copia.put(procesador, nuevasTareas);
		}

		return copia;
	}


}