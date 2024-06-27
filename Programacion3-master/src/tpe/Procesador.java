package tpe;

import java.util.ArrayList;
import java.util.List;

public class Procesador {

    //Atributos
    private String idProcesador;
    private String codigoProcesador;
    private boolean refrigerado;
    private int anioFuncionamiento;
    private List<Tarea> tareas;

    //Constructor
    public Procesador(String idProcesador, String codigoProcesador, boolean refrigerado, int anioFuncionamiento) {
        this.idProcesador = idProcesador;
        this.codigoProcesador = codigoProcesador;
        this.refrigerado = refrigerado;
        this.anioFuncionamiento = anioFuncionamiento;
        this.tareas = new ArrayList<>();
    }

    //Getters and Setters
    public String getIdProcesador() {
        return idProcesador;
    }

    public String getCodigoProcesador() {
        return codigoProcesador;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    public int getAnioFuncionamiento() {
        return anioFuncionamiento;
    }

    public void addTareaAsignada(Tarea t){
        this.tareas.add(t);
    }
    public void removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
    }

    public ArrayList<Tarea> getTareas() {
        return new ArrayList<>(this.tareas);
    }

    // Obtiene la suma total de tiempo de las tareas asignadas
    public int getTiempoTotal() {
        int total = 0;
        for (Tarea t : this.tareas) {
            total += t.getTiempoEjecucion();
        }
        return total;
    }

    // Obtiene la cantidad total de tareas críticas
    public int getCantCriticas() {
        int total = 0;
        for (Tarea t : this.tareas) {
            if (t.isCritica())
                total++;
        }
        return total;
    }




    //toString
    public String toString() {
        return "Procesador{" +
                "id='" + idProcesador + '\'' +
                ", codigo='" + codigoProcesador + '\'' +
                ", refrigerado=" + refrigerado +
                ", anio=" + anioFuncionamiento +
                '}';
    }
}
