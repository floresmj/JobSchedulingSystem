import java.io.*;
import java.util.*;

// Main class for handling the scheduling logic
public class Scheduler {

    // Reads input from a file and returns a list of jobs
    public List<int[]> readInput(String fileName) throws IOException {
        List<int[]> jobs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int[] job = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
                jobs.add(job);
            }
        }
        return jobs;
    }

    // Task 1: Shortest Processing Time First (SPT)
    public String task1Scheduler(List<int[]> jobs) {
        jobs.sort(Comparator.comparingInt(job -> job[1])); // Sort by processing time

        int totalTime = 0;
        int sumCompletionTime = 0;
        List<Integer> order = new ArrayList<>();

        for (int[] job : jobs) {
            totalTime += job[1];
            sumCompletionTime += totalTime;
            order.add(job[0]);
        }

        double avgCompletionTime = (double) sumCompletionTime / jobs.size();
        return formatOutput(order, avgCompletionTime);
    }

    // Task 2: SPT with Priority Classes
    public String task2Scheduler(List<int[]> jobs) {
        jobs.sort(Comparator.comparingInt((int[] job) -> job[2])
                .thenComparingInt(job -> job[1])); // Sort by priority, then processing time

        int totalTime = 0;
        int sumCompletionTime = 0;
        List<Integer> order = new ArrayList<>();

        for (int[] job : jobs) {
            totalTime += job[1];
            sumCompletionTime += totalTime;
            order.add(job[0]);
        }

        double avgCompletionTime = (double) sumCompletionTime / jobs.size();
        return formatOutput(order, avgCompletionTime);
    }

    // Task 3: Dynamic Job Arrival with SPT
    public String task3Scheduler(List<int[]> jobs) {
        jobs.sort(Comparator.comparingInt(job -> job[2])); // Sort by arrival time

        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(job -> job[1]));
        List<Integer> order = new ArrayList<>();
        int time = 0, sumCompletionTime = 0, i = 0;

        while (i < jobs.size() || !heap.isEmpty()) {
            while (i < jobs.size() && jobs.get(i)[2] <= time) {
                heap.add(jobs.get(i++)); // Add jobs to the heap based on processing time
            }

            if (!heap.isEmpty()) {
                int[] job = heap.poll();
                time += job[1];
                sumCompletionTime += time;
                order.add(job[0]);
            } else {
                time = jobs.get(i)[2]; // Jump to the next job's arrival time
            }
        }

        double avgCompletionTime = (double) sumCompletionTime / jobs.size();
        return formatOutput(order, avgCompletionTime);
    }

    // Helper method to format the output for all tasks
    private String formatOutput(List<Integer> order, double avgTime) {
        return "Execution order: " + order + "\nAverage completion time: " + avgTime + "\n";
    }

    // Main method to run all tasks and write results to README.md
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        try {
            List<int[]> jobs1 = scheduler.readInput("task1-input.txt");
            List<int[]> jobs2 = scheduler.readInput("task2-input.txt");
            List<int[]> jobs3 = scheduler.readInput("task3-input.txt");

            StringBuilder output = new StringBuilder();

            output.append("Team Member 1 Name (ID)\n");
            output.append("Team Member 2 Name (ID)\n\n");

            output.append("Task 1 Output:\n").append(scheduler.task1Scheduler(jobs1)).append("\n");
            output.append("Task 2 Output:\n").append(scheduler.task2Scheduler(jobs2)).append("\n");
            output.append("Task 3 Output:\n").append(scheduler.task3Scheduler(jobs3)).append("\n");

            // Write output to README.md
            try (PrintWriter writer = new PrintWriter("README.md")) {
                writer.write(output.toString());
            }

            System.out.println("Results have been written to README.md");

        } catch (IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
        }
    }
}

