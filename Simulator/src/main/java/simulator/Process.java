/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

public class Process implements Cloneable {
    public final String pid;
    public final int arrival;
    public final int burst;
    public final int priority; // menor = mayor prioridad
    public int remaining;
    public Integer completion = null;

    public Process(String pid, int arrival, int burst) {
        this(pid, arrival, burst, 0);
    }
    public Process(String pid, int arrival, int burst, int priority) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remaining = burst;
    }

    public Process cloneFresh() {
        return new Process(pid, arrival, burst, priority);
    }

    @Override
    public String toString() {
        return String.format("%s(arr=%d,burst=%d,pr=%d)", pid, arrival, burst, priority);
    }
}


