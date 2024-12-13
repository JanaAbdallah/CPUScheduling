package org.example;
import java.util.Arrays;

public class PriorityScheduler extends Scheduler {

    @Override
    public void schedule(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int n = processes.length;
        boolean[] isCompleted = new boolean[n];

        // Sort processes by arrival time first, then by priority (ascending)
        Arrays.sort(processes, (p1, p2) -> {
            if (p1.arrivalTime != p2.arrivalTime) {
                return p1.arrivalTime - p2.arrivalTime; // Sort by arrival time first
            } else {
                return p1.priority - p2.priority; // If arrival time is the same, sort by priority
            }
        });

        // To store the execution order (used for visualization)
        executionOrder = new StringBuilder();

        while (completedProcesses < n) {
            Process currentProcess = null;

            // Find the next process to run: check for processes ready to execute
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && processes[i].arrivalTime <= currentTime) {
                    if (currentProcess == null || processes[i].priority < currentProcess.priority) {
                        currentProcess = processes[i];
                    }
                }
            }

            if (currentProcess != null) {
                // Execute the selected process
                executionOrder.append(currentProcess.id).append(",");

                // Update the current time (by adding the burst time of the process)
                currentTime += currentProcess.burstTime;

                // Set the completion, turnaround, and waiting times
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.completionTime - currentProcess.arrivalTime);
                currentProcess.setWaitingTime(currentProcess.turnaroundTime - currentProcess.burstTime);

                // Mark the process as completed
                isCompleted[currentProcess.id] = true;
                completedProcesses++;
            } else {
                // If no process is ready to execute, increment the current time
                currentTime++;
            }
        }

        // Remove trailing comma from execution order string
        if (executionOrder.length() > 0) {
            executionOrder.setLength(executionOrder.length() - 1);
        }
    }

    public String getExecutionOrder() {
        return executionOrder.toString();
    }
}
