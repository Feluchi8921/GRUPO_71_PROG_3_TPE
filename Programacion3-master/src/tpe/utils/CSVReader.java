package tpe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import tpe.Procesador;
import tpe.Procesadores;
import tpe.Tarea;


public class CSVReader {
	public CSVReader() {

	}

	/*
	Se leen las tareas y se las guarda en un arrayList que será utilizado en la clase Servicios.
	Primeramente se utilizó esta clase para leer y guardar los datos en las estructuras a utilizar,
	ya que tiene la ventaja de utilizar un solo for de complejidad O(n).
	Como no es responsabilidad de la clase CSVReader el manejo de las estructuras, se decidió hacerlo en
	la clase Servicios, lo que aumenta la complejidad puesto que se debe utilizar otro for pra recorrer las
	tareas y guardarlas en cada estructura, lo que lleva a una complejidad de 2.O(n).
	*/
	public ArrayList<Tarea> readTasks (String taskPath) {
		ArrayList<Tarea> tareas = new ArrayList<>();
		ArrayList<String[]> lines = this.readContent(taskPath);

		for (String[] line: lines) {
			String id = line[0].trim();
			String nombre = line[1].trim();
			Integer tiempo = Integer.parseInt(line[2].trim());
			Boolean critica = Boolean.parseBoolean(line[3].trim());
			Integer prioridad = Integer.parseInt(line[4].trim());

			Tarea tarea = new Tarea(id, nombre, tiempo, critica, prioridad);

			tareas.add(tarea);
		}

		return tareas;
	}

	//----------------------------Procesadores----------------------
	public ArrayList<Procesador> readProcessors(String processorPath) {
		ArrayList<Procesador> processors = new ArrayList<>();

		ArrayList<String[]> lines = this.readContent(processorPath);

		for (String[] line : lines) {
			String id = line[0].trim();
			String codigo = line[1].trim();
			Boolean refrigerado = Boolean.parseBoolean(line[2].trim());
			Integer anio = Integer.parseInt(line[3].trim());

			Procesador procesador = new Procesador(id, codigo, refrigerado, anio);

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
