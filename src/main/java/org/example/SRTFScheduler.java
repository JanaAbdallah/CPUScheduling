package org.example;

import java.util.Arrays;
public class SRTFScheduler extends Scheduler {
    private StringBuilder executionOrder = new StringBuilder();

    @Override
    public void schedule(Process[] processes) {
        int currentTime = 0;
        int completedProcesses = 0;
        int n = processes.length;
        boolean[] isCompleted = new boolean[n];

        Arrays.sort(processes, (p1, p2) -> p1.arrivalTime - p2.arrivalTime);

        while (completedProcesses < n) {
            Process currentProcess = null;
            int minRemainingTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && processes[i].arrivalTime <= currentTime) {
                    if (processes[i].remainingTime < minRemainingTime) {
                        minRemainingTime = processes[i].remainingTime;
                        currentProcess = processes[i];
                    }
                }
            }

            if (currentProcess != null) {
                currentTime += 1;
                executionOrder.append(currentProcess.id).append(",");

                currentProcess.remainingTime -= 1;

                if (currentProcess.remainingTime == 0) {
                    currentProcess.setCompletionTime(currentTime);
                    currentProcess.setTurnaroundTime(currentProcess.completionTime - currentProcess.arrivalTime);
                    currentProcess.setWaitingTime(currentProcess.turnaroundTime - currentProcess.burstTime);
                    isCompleted[currentProcess.id] = true;
                    completedProcesses++;
                }
            } else {
                currentTime++; // Idle time
            }
        }
    }

    @Override
    public String getExecutionOrder() {
        return executionOrder.toString();
    }
}
