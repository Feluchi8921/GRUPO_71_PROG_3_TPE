package tpe;

public class Procesador {

    //Atributos
    private String idProcesador;
    private String codigoProcesador;
    private boolean estaRefrigerado;
    private int anioFuncionamiento;


    //Constructor
    public Procesador(String idProcesador, String codigoProcesador, boolean estaRefrigerado, int anioFuncionamiento) {
        this.idProcesador = idProcesador;
        this.codigoProcesador = codigoProcesador;
        this.estaRefrigerado = estaRefrigerado;
        this.anioFuncionamiento = anioFuncionamiento;
    }

    //Getters and Setters
    public String getIdProcesador() {
        return idProcesador;
    }

    public String getCodigoProcesador() {
        return codigoProcesador;
    }

    public void setCodigoProcesador(String codigoProcesador) {
        this.codigoProcesador = codigoProcesador;
    }

    public boolean isEstaRefrigerado() {
        return estaRefrigerado;
    }

    public void setEstaRefrigerado(boolean estaRefrigerado) {
        this.estaRefrigerado = estaRefrigerado;
    }

    public int getAnioFuncionamiento() {
        return anioFuncionamiento;
    }

    public void setAnioFuncionamiento(int anioFuncionamiento) {
        this.anioFuncionamiento = anioFuncionamiento;
    }

    //toString
    public String toString() {
        return "Procesador{" +
                "id='" + idProcesador + '\'' +
                ", codigo='" + codigoProcesador + '\'' +
                ", refrigerado=" + estaRefrigerado +
                ", anio=" + anioFuncionamiento +
                '}';
    }
}
