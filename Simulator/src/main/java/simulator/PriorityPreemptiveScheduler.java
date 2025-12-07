/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

import java.util.*;
import java.util.stream.Collectors;

/**
 * Scheduler preemptivo por prioridad (menor valor = mayor prioridad).
 * Soporta contexto cada unidad de tiempo y simula cambio de contexto como en RRScheduler if needed.
 */
public class PriorityPreemptiveScheduler extends AbstractScheduler {
    private final int contextSwitchOverhead;
    public PriorityPreemptiveScheduler() { this(0); }
    public PriorityPreemptiveScheduler(int contextSwitchOverhead) { this.contextSwitchOverhead = contextSwitchOverhead; }

    @Override public String name() { return "Priority (preemptive, overhead=" + contextSwitchOverhead + ")"; }

    @Override
    public Result run(List<Process> processes) {
        List<Process> all = cloneAndSortByArrival(processes);
        int n = all.size(), i = 0, time = 0;
        List<GanttSegment> gantt = new ArrayList<>();
        PriorityQueue<Process> ready = new PriorityQueue<>(Comparator.comparingInt((Process p)->p.priority).thenComparingInt(p->p.arrival));
        Process current = null;
        String lastPid = null;
        while (i<n || !ready.isEmpty() || current != null) {
            while (i<n && all.get(i).arrival <= time) { ready.add(all.get(i)); i++; }
            if (current == null) {
                if (ready.isEmpty()) {
                    time = (i<n)? all.get(i).arrival : time;
                    continue;
                }
                current = ready.poll();
                // context switch overhead if we switched
                if (lastPid != null && !lastPid.equals(current.pid) && contextSwitchOverhead > 0) {
                    gantt.add(new GanttSegment("CS", time, time + contextSwitchOverhead));
                    time += contextSwitchOverhead;
                }
            }
            int start = time;
            time += 1;
            current.remaining -= 1;
            gantt.add(new GanttSegment(current.pid, start, time));
            while (i<n && all.get(i).arrival <= time) { ready.add(all.get(i)); i++; }
            if (current.remaining == 0) {
                current.completion = time;
                lastPid = current.pid;
                current = null;
            } else {
                if (!ready.isEmpty() && ready.peek().priority < current.priority) {
                    // preempt
                    ready.add(current);
                    lastPid = current.pid;
                    current = null;
                }
            }
        }
        return calcMetrics(all.stream().sorted(Comparator.comparing(p->p.pid)).collect(Collectors.toList()), gantt);
    }
}

