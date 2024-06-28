package tpe;

import java.util.Comparator;

public class ComparadorTareasTiempo implements Comparator<Tarea> {
    @Override
    public int compare(Tarea t1, Tarea t2) {
        return t1.getTiempoEjecucion().compareTo(t2.getTiempoEjecucion());
    }
}
