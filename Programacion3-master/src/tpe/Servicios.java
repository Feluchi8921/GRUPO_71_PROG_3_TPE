package tpe;

import tpe.utils.CSVReader;
import java.util.*;

public class Servicios {
	private HashMap<String, Tarea> tareas;
	private HashMap<Boolean, List<Tarea>> tareasCriticasYNoCriticas;
	//private Procesadores procesadores;

	//La complejidad del constructor de la clase Servicios es O(n), donde n es la cantidad de tareas almacenadas en los archivos CSV
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasksId(pathTareas); // Complejidad O(n)
		this.tareasCriticasYNoCriticas = reader.readTasksCriticidad(pathTareas); //Complejidad O(n)
	}

	//Complejidad temporal: O(1) para buscar y devolver las tareas.
	public Tarea servicio1(String ID) {
		return tareas.get(ID);
	}

	//Complejidad temporal: O(1) para buscar y devolver las tareas//
	public List<Tarea> servicio2(boolean esCritica) {
		//Si no existe la clave, se devuelve una lista vacía. Si existe la clave, se devuelve la lista de tareas correspondiente.
		return tareasCriticasYNoCriticas.getOrDefault(esCritica, Collections.emptyList());
	}

	// Complejidad temporal: O(n) donde n es la cantidad de tareas. Ver si se puede mejorar con un arbol binario de busqueda
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
