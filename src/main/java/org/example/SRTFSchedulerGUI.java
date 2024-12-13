package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SRTFSchedulerGUI extends JFrame {

    private JTextField numProcessesField;
    private JTextArea executionOrderArea;
    private JTextArea waitingTimesArea;
    private JTextArea turnaroundTimesArea;
    private JTextArea inputDisplayArea;
    private JLabel avgWaitingTimeLabel;
    private JLabel avgTurnaroundTimeLabel;
    private Process[] processes;
    private String executionOrder;
    private JComboBox<String> schedulerComboBox;

    public SRTFSchedulerGUI() {
        setTitle("Scheduling Algorithms: SRTF, SJF, Priority");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Enter number of processes:"));
        numProcessesField = new JTextField(5);
        inputPanel.add(numProcessesField);

        // Scheduler ComboBox for SRTF, SJF, and Priority
        String[] schedulers = {"SRTF", "SJF", "Priority"};
        schedulerComboBox = new JComboBox<>(schedulers);
        inputPanel.add(schedulerComboBox);

        JButton submitButton = new JButton("Submit");
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.NORTH);

        // Output Panel
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridLayout(7, 1));

        executionOrderArea = new JTextArea(5, 20);
        executionOrderArea.setEditable(false);
        outputPanel.add(new JScrollPane(executionOrderArea));

        waitingTimesArea = new JTextArea(2, 20);
        waitingTimesArea.setEditable(false);
        outputPanel.add(new JScrollPane(waitingTimesArea));

        turnaroundTimesArea = new JTextArea(2, 20);
        turnaroundTimesArea.setEditable(false);
        outputPanel.add(new JScrollPane(turnaroundTimesArea));

        avgWaitingTimeLabel = new JLabel("Average Waiting Time: ");
        outputPanel.add(avgWaitingTimeLabel);

        avgTurnaroundTimeLabel = new JLabel("Average Turnaround Time: ");
        outputPanel.add(avgTurnaroundTimeLabel);

        // New panel to display user input
        inputDisplayArea = new JTextArea(5, 20);
        inputDisplayArea.setEditable(false);
        outputPanel.add(new JScrollPane(inputDisplayArea));

        add(outputPanel, BorderLayout.CENTER);

        // Custom Chart Panel
        CustomChartPanel chartPanel = new CustomChartPanel();
        chartPanel.setPreferredSize(new Dimension(700, 200));
        add(chartPanel, BorderLayout.SOUTH);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numProcesses = Integer.parseInt(numProcessesField.getText());
                processes = new Process[numProcesses];
                StringBuilder inputDisplay = new StringBuilder("User Input:\n");

                // Collect user input
                for (int i = 0; i < numProcesses; i++) {
                    int arrivalTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Arrival Time for Process " + (i + 1)));
                    int burstTime = Integer.parseInt(JOptionPane.showInputDialog("Enter Burst Time for Process " + (i + 1)));
                    int priority = Integer.parseInt(JOptionPane.showInputDialog("Enter Priority for Process " + (i + 1)));
                    processes[i] = new Process(i, arrivalTime, burstTime, priority);

                    // Store input for display
                    inputDisplay.append("Process " + (i + 1) + ": Arrival Time = " + arrivalTime + ", Burst Time = " + burstTime + ", Priority = " + priority + "\n");
                }

                // Display user input
                inputDisplayArea.setText(inputDisplay.toString());

                // Get selected scheduler and run it
                String selectedScheduler = (String) schedulerComboBox.getSelectedItem();
                Scheduler scheduler;

                if ("SRTF".equals(selectedScheduler)) {
                    scheduler = new SRTFScheduler();
                } else if ("SJF".equals(selectedScheduler)) {
                    scheduler = new SJFScheduler();
                } else {
                    scheduler = new PriorityScheduler();
                }

                // Run the selected scheduler
                scheduler.schedule(processes);

                // Get the execution order and display results
                executionOrder = scheduler.getExecutionOrder();
                displayResults(processes, scheduler);

                // Repaint the chart to reflect the scheduling
                chartPanel.repaint();
            }
        });
    }

    // Method to display results in the GUI
    private void displayResults(Process[] processes, Scheduler scheduler) {
        executionOrderArea.setText("Execution Order: \n" + executionOrder);

        StringBuilder waitingTimes = new StringBuilder();
        StringBuilder turnaroundTimes = new StringBuilder();
        double totalWaitingTime = 0; // Use double for accurate division
        double totalTurnaroundTime = 0; // Use double for accurate division

        for (Process p : processes) {
            waitingTimes.append("P" + p.id + ": " + p.waitingTime + "\n");
            turnaroundTimes.append("P" + p.id + ": " + p.turnaroundTime + "\n");
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }

        waitingTimesArea.setText(waitingTimes.toString());
        turnaroundTimesArea.setText(turnaroundTimes.toString());

        double avgWaitingTime = totalWaitingTime / processes.length;
        double avgTurnaroundTime = totalTurnaroundTime / processes.length;

        avgWaitingTimeLabel.setText(String.format("Average Waiting Time: %.2f", avgWaitingTime));
        avgTurnaroundTimeLabel.setText(String.format("Average Turnaround Time: %.2f", avgTurnaroundTime));
    }

    // Custom chart panel to draw process execution on the screen
    private class CustomChartPanel extends JPanel {
        private final Color[] colors = {
                Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA,
                Color.CYAN, Color.YELLOW, Color.PINK, Color.GRAY, Color.LIGHT_GRAY
        };

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (processes == null || executionOrder == null) return;

            // Graphical parameters
            int barWidth = 50;
            int barHeight = 40;
            int margin = 10;
            int x = margin;
            int timeUnit = 10;  // Space between each execution unit

            // Draw process execution chart
            String[] order = executionOrder.split(",");
            for (String pidStr : order) {
                int pid = Integer.parseInt(pidStr);

                // Assign a unique color for each process based on its ID
                g.setColor(colors[pid % colors.length]);
                g.fillRect(x, getHeight() / 2 - barHeight / 2, barWidth, barHeight);

                // Draw process ID in white for readability
                g.setColor(Color.WHITE);
                g.drawString("P" + pid, x + barWidth / 3, getHeight() / 2);

                // Update x position for the next process
                x += barWidth + timeUnit;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SRTFSchedulerGUI gui = new SRTFSchedulerGUI();
            gui.setVisible(true);
        });
    }
}
