package tpe;

import java.util.ArrayList;
import java.util.List;

public class Procesador {

    //Atributos
    private String idProcesador;
    private String codigoProcesador;
    private boolean refrigerado;
    private int anioFuncionamiento;
    private List<Tarea> tareasAsignadas;

    //Constructor
    public Procesador(String idProcesador, String codigoProcesador, boolean refrigerado, int anioFuncionamiento) {
        this.idProcesador = idProcesador;
        this.codigoProcesador = codigoProcesador;
        this.refrigerado = refrigerado;
        this.anioFuncionamiento = anioFuncionamiento;
        this.tareasAsignadas = new ArrayList<>();
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
        this.tareasAsignadas.add(t);
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
