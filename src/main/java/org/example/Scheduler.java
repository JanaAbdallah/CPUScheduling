package org.example;

public abstract class Scheduler {
    protected StringBuilder executionOrder = new StringBuilder();

    public abstract void schedule(Process[] processes);

    public String getExecutionOrder() {
        return executionOrder.toString();
    }
}
