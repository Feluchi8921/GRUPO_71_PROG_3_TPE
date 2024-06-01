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


	//La complejidad del constructor de la clase Servicios es O(n), donde n es la cantidad de tareas almacenadas en los archivos CSV
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasks(pathTareas);
		this.tareasId = new HashMap<>();
		this.tareasCriticidad = new HashMap<>();
		this.addTareas(this.tareas); //Se cargan las tareas en cada estructura
		this.procesadores = reader.readProcessors(pathProcesadores);
		this.tareasAsignadas = new HashMap<>();
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
		this.maxTiempoEjecucion = tiempoMaxNoRefrigerado;
		List<Tarea> asignacion = new ArrayList<>();
		if (asignarTareasBacktracking(0, asignacion)) {
			//Devuelvo una copia de las tareas Asignadas
			return copiaTareasAsignadas();
			}
		return null;
	}

	private boolean asignarTareasBacktracking(int tareaIndex, List<Tarea> asignacion) {
		//Si el indice actual es igual a la cant de tareas es porque se asignaron todas
		if (tareaIndex == tareas.size()) {
			return true;
		}

		//Sino obtengo la tarea del indice actual
		Tarea tarea = tareas.get(tareaIndex);

		//Recorro los procesadores
		for (Procesador procesador : procesadores) {
			//Verifico si es asignable
			if (puedeAsignar(procesador, tarea)) {
				//La agrego
				asignar(procesador, tarea);
				//Llamo a la recursion con el siguiente índice de tarea y la asignacion actual
				if (asignarTareasBacktracking(tareaIndex + 1, asignacion)) {
					return true;
				}
				//Elimino el agregar
				desasignar(procesador, tarea);
			}
		}

		return false;
	}

	//Metodo que se encarga de establecer las condiciones de asignacion de tareas
	private boolean puedeAsignar(Procesador procesador, Tarea tarea) {
		List<Tarea> tareasProcesador = tareasAsignadas.getOrDefault(procesador, new ArrayList<>()); //Si no hay tareas asignadas al procesador, se crea una nueva lista vacía.

		if (tarea.isCritica()) {
			//Si la tarea es crítica, se cuenta el número de tareas críticas ya asignadas al procesador
			long countCriticas = tareasProcesador.stream().filter(Tarea::isCritica).count();
			if (countCriticas >= maxTareasCritPorProc) {
				//Si ya tiene asignadas dos tareas criticas, no se asigna
				return false;
			}
		}
		//Si el procesador no es refrigerado, se calcula el tiempo total de ejecución actual del procesador
		//sumando el tiempo de ejecución de las tareas ya asignadas (tareasProcesador) con el tiempo de ejecución de la nueva tarea
		if (!procesador.isRefrigerado()) {
			int tiempoTotal = tareasProcesador.stream().mapToInt(Tarea::getTiempoEjecucion).sum();
			if (tiempoTotal + tarea.getTiempoEjecucion() > maxTiempoEjecucion) {
				//Si supera el tiempoMaximo establecido por el usuario, no se asigna
				return false;
			}
		}
		//La tarea puede asignarse
		return true;
	}

	//Este metodo asigna las tareas al procesador
	private void asignar(Procesador procesador, Tarea tarea) {
		//Si el procesador no tiene una lista asignada se crea una nueva
		tareasAsignadas.putIfAbsent(procesador, new ArrayList<>());
		//Se agrega la tarea asignada
		tareasAsignadas.get(procesador).add(tarea);
		procesador.addTareaAsignada(tarea);
	}

	//Este metodo elimina la asignación de la tarea al procesador removiéndola de la lista de tareas asociadas
	// al procesador en el HashMap tareasAsignadas
	private void desasignar(Procesador procesador, Tarea tarea) {
		tareasAsignadas.get(procesador).remove(tarea);
	}


	public HashMap<Procesador, List<Tarea>> copiaTareasAsignadas() {
		HashMap<Procesador, List<Tarea>> copiaBacktracking = new HashMap<>();
		for (Procesador p : tareasAsignadas.keySet()) {
			copiaBacktracking.put(p, tareasAsignadas.get(p));
		}
		return copiaBacktracking;
	}
}
