import java.util.Random;

public class GeneradorProcesadores {

    // Atributos para generar datos aleatorios
    private static final String[] MARCAS = {"Intel", "AMD", "ARM", "Apple"};
    private static final String[] MODELOS = {"Core i7", "Ryzen 9", "Cortex-A8", "M1"};
    private static final String[] CODIGOS = {"i7-12700K", "Ryzen 9 5950X", "Cortex-A83", "M1 Pro"};
    private static final int[] ANIOS_MIN = {2015, 2017, 2018, 2020};
    private static final int[] ANIOS_MAX = {2023, 2023, 2023, 2023};
    private static final Random random = new Random();

    // Método para generar un procesador aleatorio
    public static String generarProcesador() {
        int indiceMarca = random.nextInt(MARCAS.length);
        int indiceCodigo = random.nextInt(CODIGOS.length);
        int anioFuncionamiento = ANIOS_MIN[indiceMarca] + random.nextInt(ANIOS_MAX[indiceMarca] - ANIOS_MIN[indiceMarca] + 1);
        boolean refrigerado = random.nextBoolean();

        String marca = MARCAS[indiceMarca];
        String codigo = CODIGOS[indiceCodigo];

        return marca + ";" + codigo +";" + refrigerado+ ";" + anioFuncionamiento;
    }

    // Método principal para generar y mostrar procesadores
    public static void main(String[] args) {
        int cantidadProcesadores = 10; // Cantidad de procesadores a generar

        for (int i = 0; i < cantidadProcesadores; i++) {
            String datosProcesador = generarProcesador();
            System.out.println(datosProcesador);
        }
    }
}
