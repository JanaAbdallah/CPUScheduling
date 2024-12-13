package org.example;

import java.util.Arrays;

public class SJFScheduler extends Scheduler {

    @Override
    public void schedule(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int n = processes.length;
        boolean[] isCompleted = new boolean[n];

        Arrays.sort(processes, (p1, p2) -> p1.arrivalTime - p2.arrivalTime);

        while (completedProcesses < n) {
            Process currentProcess = null;
            int minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && processes[i].arrivalTime <= currentTime) {
                    if (processes[i].burstTime < minBurstTime) {
                        minBurstTime = processes[i].burstTime;
                        currentProcess = processes[i];
                    }
                }
            }

            if (currentProcess != null) {
                currentTime += currentProcess.burstTime;
                executionOrder.append(currentProcess.id).append(",");

                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.completionTime - currentProcess.arrivalTime);
                currentProcess.setWaitingTime(currentProcess.turnaroundTime - currentProcess.burstTime);

                isCompleted[currentProcess.id] = true;
                completedProcesses++;
            }
        }
    }

    @Override
    public String getExecutionOrder() {
        return executionOrder.toString();
    }
}

