package tpe;

import tpe.utils.CSVReader;
import java.util.*;

public class Servicios {
	private ArrayList<Tarea> tareas;
	private HashMap<String, Tarea> tareasId;
	private HashMap<Boolean, List<Tarea>> tareasCriticidad;

	//private Procesadores procesadores;

	//La complejidad del constructor de la clase Servicios es O(n), donde n es la cantidad de tareas almacenadas en los archivos CSV
	public Servicios(String pathProcesadores, String pathTareas) {
		CSVReader reader = new CSVReader();
		this.tareas = reader.readTasks(pathTareas);
		this.tareasId = new HashMap<>();
		this.tareasCriticidad = new HashMap<>();
		this.addTareas(this.tareas); //Se cargan las tareas en cada estructura
	}

	//Complejidad temporal: O(1) donde n es la cantidad de tareas
	public Tarea servicio1(String ID) {
		return tareasId.get(ID);
	}

	//Complejidad temporal: O(1) donde n es la cantidad de tareas
	public List<Tarea> servicio2(boolean esCritica) {
		return tareasCriticidad.get(esCritica);
	}

	/*
	Complejidad temporal: O(n) donde n es la cantidad de tareas.
	Se podría mejorar la complejidad utilizando un arbol binario de busqueda balanceado.
	Se decidió no utilizarlo porque no sabemos utilizar las librerías de arboles provistas por Java y
	para utilizar nuestra clase Arbol realizada en la cursada deberíamos utilizar técnicas de balanceo.
	*/

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
	private void addTareas(ArrayList<Tarea> tareas){
		for(Tarea t : tareas){
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
}
