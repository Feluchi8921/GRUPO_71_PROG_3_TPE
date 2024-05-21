package tpe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import tpe.Procesador;
import tpe.Tarea;


public class CSVReader {
	public CSVReader() {

	}
	//Modificar el retorno para que me devuelva algo que pueda leer
	//por ej que me devuelva una lista de tareas
	public HashMap<String, Tarea> readTasksId (String taskPath) {
		HashMap<String, Tarea> task = new HashMap<>();
		// Obtengo una lista con las lineas del archivo
		// lines.get(0) tiene la primer linea del archivo
		// lines.get(1) tiene la segunda linea del archivo... y así
		ArrayList<String[]> lines = this.readContent(taskPath);

		for (String[] line: lines) {
			// Cada linea es un arreglo de Strings, donde cada posicion guarda un elemento
			String id = line[0].trim();
			String nombre = line[1].trim();
			Integer tiempo = Integer.parseInt(line[2].trim());
			Boolean critica = Boolean.parseBoolean(line[3].trim());
			Integer prioridad = Integer.parseInt(line[4].trim());

			// Crear un nuevo objeto Tarea con los datos leídos
			Tarea tarea = new Tarea(id, nombre, tiempo, critica, prioridad);

			// Agregar la tarea al mapa
			task.put(id, tarea);
		}

		return task;
	}

	public List<Tarea> readTasks(String taskPath) {
		List<Tarea> task = new ArrayList<>();
		// Obtengo una lista con las lineas del archivo
		// lines.get(0) tiene la primer linea del archivo
		// lines.get(1) tiene la segunda linea del archivo... y así
		ArrayList<String[]> lines = this.readContent(taskPath);

		for (String[] line: lines) {
			// Cada linea es un arreglo de Strings, donde cada posicion guarda un elemento
			String id = line[0].trim();
			String nombre = line[1].trim();
			Integer tiempo = Integer.parseInt(line[2].trim());
			Boolean critica = Boolean.parseBoolean(line[3].trim());
			Integer prioridad = Integer.parseInt(line[4].trim());

			// Crear un nuevo objeto Tarea con los datos leídos
			Tarea tarea = new Tarea(id, nombre, tiempo, critica, prioridad);

			// Agregar la tarea al mapa
			task.add(tarea);
		}

		return task;
	}

	public HashMap<Boolean, List<Tarea>> readTasksCriticidad (String taskPath) {
		HashMap<Boolean, List<Tarea>> task = new HashMap<>();
		// Obtengo una lista con las lineas del archivo
		// lines.get(0) tiene la primer linea del archivo
		// lines.get(1) tiene la segunda linea del archivo... y así
		ArrayList<String[]> lines = this.readContent(taskPath);

		for (String[] line: lines) {
			// Cada linea es un arreglo de Strings, donde cada posicion guarda un elemento
			String id = line[0].trim();
			String nombre = line[1].trim();
			Integer tiempo = Integer.parseInt(line[2].trim());
			Boolean critica = Boolean.parseBoolean(line[3].trim());
			Integer prioridad = Integer.parseInt(line[4].trim());

			// Crear un nuevo objeto Tarea con los datos leídos
			Tarea tarea = new Tarea(id, nombre, tiempo, critica, prioridad);
			// Recuperar la lista de tareas para la criticidad actual
			List<Tarea> tareasCriticidad = task.getOrDefault(critica, new ArrayList<>());

			// Agregar la tarea a la lista correspondiente
			tareasCriticidad.add(tarea);

			// Actualizar el mapa con la lista actualizada
			task.put(critica, tareasCriticidad);
		}

		return task;
	}

	//----------------------------Procesadores----------------------
	public LinkedList<Procesador> readProcessors(String processorPath) {
		LinkedList<Procesador> processors = new LinkedList<>();

		// Obtengo una lista con las lineas del archivo
		// lines.get(0) tiene la primer linea del archivo
		// lines.get(1) tiene la segunda linea del archivo... y así
		ArrayList<String[]> lines = this.readContent(processorPath);

		for (String[] line : lines) {
			// Cada linea es un arreglo de Strings, donde cada posicion guarda un elemento
			String id = line[0].trim();
			String codigo = line[1].trim();
			Boolean refrigerado = Boolean.parseBoolean(line[2].trim());
			Integer anio = Integer.parseInt(line[3].trim());

			// Crear un nuevo objeto Procesador con los datos leídos
			Procesador procesador = new Procesador(id, codigo, refrigerado, anio);

			// Agregar el procesador a la lista
			processors.add(procesador);
		}

		return processors;

	}

	private ArrayList<String[]> readContent(String path) {
		ArrayList<String[]> lines = new ArrayList<String[]>();

		File file = new File(path);
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				lines.add(line.split(";"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

		return lines;
	}


}
