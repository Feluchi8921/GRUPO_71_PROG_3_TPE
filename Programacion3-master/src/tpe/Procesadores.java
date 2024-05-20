package tpe;

import java.util.LinkedList;

public class Procesadores {
    //Atributo
    private LinkedList<Procesador> procesadores;

    //Constructor


    public Procesadores() {
        this.procesadores = new LinkedList<>();
    }

    //Getters and Setters
    public void addProcesador(Procesador p){
        if(isProcesador(p)){
            procesadores.add(p);
        }
    }

    public Procesador deleteProcesador(Procesador p){
        if(isProcesador(p)){
            procesadores.remove(p);
            return p;
        }
        return null;
    }

    public boolean isProcesador(Procesador p){
        return procesadores.contains(p);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Procesadores:\n");
        for (Procesador procesador : procesadores) {
            stringBuilder.append(procesador.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
