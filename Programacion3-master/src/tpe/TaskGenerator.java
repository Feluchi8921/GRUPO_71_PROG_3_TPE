import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskGenerator {

    public static void main(String[] args) {
        int numTasks = 5000; // Number of tasks to generate

        List<String> tasks = new ArrayList<>(); // List to store generated tasks

        Random random = new Random(); // Create a random number generator

        for (int taskId = 1; taskId <= numTasks; taskId++) {
            String taskString = generateTask(taskId, random);
            tasks.add(taskString);
        }

        // Print the generated tasks
        for (String task : tasks) {
            System.out.println(task);
        }
    }

    private static String generateTask(int taskId, Random random) {
        String taskName = "Tarea" + taskId; // Generate task name

        int estimatedTime = random.nextInt(91) + 10; // Generate estimated time (10 to 100)

        boolean highPriority = random.nextBoolean(); // Generate high priority flag

        int difficulty = random.nextInt(61) + 30; // Generate difficulty (30 to 90)

        return "T" + taskId + ";" + taskName + ";" + estimatedTime + ";" + highPriority + ";" + difficulty;
    }
}