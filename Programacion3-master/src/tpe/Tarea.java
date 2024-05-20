package tpe;

public class Tarea {

    //Atributos
    private String idTarea;
    private String nombreTarea;
    private int tiempoEjecucion;
    private boolean esCritica;
    private int nivelPrioridad;


    //Constructor
    public Tarea(String idTarea, String nombreTarea, int tiempoEjecucion, boolean esCritica, int nivelPrioridad) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.tiempoEjecucion = tiempoEjecucion;
        this.esCritica = esCritica;
        this.nivelPrioridad = nivelPrioridad;
    }

    //Getters and Setters
    public String getIdTarea() {
        return idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public boolean isEsCritica() {
        return esCritica;
    }

    public void setEsCritica(boolean esCritica) {
        this.esCritica = esCritica;
    }

    public int getNivelPrioridad() {
        return nivelPrioridad;
    }

    public void setNivelPrioridad(int nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    //toString
    public String toString() {
        return "Tarea{" +
                "id='" + idTarea + '\'' +
                ", nombre='" + nombreTarea + '\'' +
                ", tiempo=" + tiempoEjecucion +
                ", critica=" + esCritica +
                ", prioridad=" + nivelPrioridad +
                '}';
    }
}
