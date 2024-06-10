package tpe;

public class Tarea {

    //Atributos
    private String idTarea;
    private String nombreTarea;
    private int tiempoEjecucion;
    private boolean critica;
    private int nivelPrioridad;


    //Constructor
    public Tarea(String idTarea, String nombreTarea, int tiempoEjecucion, boolean esCritica, int nivelPrioridad) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.tiempoEjecucion = tiempoEjecucion;
        this.critica = esCritica;
        this.nivelPrioridad = nivelPrioridad;
    }

    //Agrego solo los getters porque no se modifican las tareas entrantes
    public String getIdTarea() {
        return idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public boolean isCritica() {
        return critica;
    }

    public int getNivelPrioridad() {
        return nivelPrioridad;
    }


    public String toString() {
        return "Tarea{" +
                "id='" + idTarea + '\'' +
                ", nombre='" + nombreTarea + '\'' +
                ", tiempo=" + tiempoEjecucion +
                ", critica=" + critica +
                ", prioridad=" + nivelPrioridad +
                '}';
    }
}
