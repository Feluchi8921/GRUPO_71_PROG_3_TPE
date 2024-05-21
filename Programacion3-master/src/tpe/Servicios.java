package tpe;

import tpe.utils.CSVReader;

import java.util.*;


//Servicios se va a encargar del servicio tareas para poder usar distintas estructuras

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	private HashMap<String, Tarea> tareas;
	private HashMap<Boolean, List<Tarea>> tareasCriticasYNoCriticas; // Almacenar las tareas por criticidad
	//private Procesadores procesadores; // Atributo no inicializado

	/*
	 * Expresar la complejidad temporal del constructor.
	 */
	public Servicios(String pathProcesadores, String pathTareas) {
		//this.procesadores = new Procesadores(); // No se inicializa el atributo
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasksId(pathTareas);
		this.tareasCriticasYNoCriticas = reader.readTasksCriticidad(pathTareas);
	}

	/*
	 * Complejidad temporal: O(1) para buscar y devolver las tareas.
	 */
	public Tarea servicio1(String ID) {
		return tareas.get(ID);
	}

	//Complejidad temporal: O(1) para buscar y devolver las tareas//
	public List<Tarea> servicio2(boolean esCritica) {
		//Si no existe la clave, se devuelve una lista vacía. Si existe la clave, se devuelve la lista de tareas correspondiente.
		return tareasCriticasYNoCriticas.getOrDefault(esCritica, Collections.emptyList());
	}

	/*
	 * Complejidad temporal: O(n) donde n es la cantidad de tareas.
	 */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> tareasFiltradas = new ArrayList<>();
		for (Tarea tarea : tareas.values()) {
			int prioridadTarea = tarea.getNivelPrioridad();
			if (prioridadTarea >= prioridadInferior && prioridadTarea <= prioridadSuperior) {
				tareasFiltradas.add(tarea);
			}
		}
		return tareasFiltradas;
	}
}
