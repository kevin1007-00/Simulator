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
 * Round Robin con quantum y overhead (tiempo consumido por cada cambio de contexto).
 * Emula que al cambiar de proceso se añade el overhead como tiempo ocupado (idle).
 */
public class RRScheduler extends AbstractScheduler {
    private final int quantum;
    private final int contextSwitchOverhead; // en unidades de tiempo (>=0)

    public RRScheduler(int quantum) {
        this(quantum, 0);
    }
    public RRScheduler(int quantum, int contextSwitchOverhead) {
        this.quantum = quantum;
        this.contextSwitchOverhead = contextSwitchOverhead;
    }
    @Override public String name() { return "Round Robin (q=" + quantum + ", overhead=" + contextSwitchOverhead + ")"; }

    @Override
    public Result run(List<Process> processes) {
        List<Process> all = cloneAndSortByArrival(processes);
        int time = 0;
        int i = 0, n = all.size();
        Queue<Process> q = new LinkedList<>();
        List<GanttSegment> gantt = new ArrayList<>();
        String lastPid = null;
        while (i < n || !q.isEmpty()) {
            while (i < n && all.get(i).arrival <= time) { q.add(all.get(i)); i++; }
            if (q.isEmpty()) {
                time = (i<n)? all.get(i).arrival : time;
                continue;
            }
            Process p = q.poll();
            // context switch overhead if different process than last and overhead>0
            if (lastPid != null && !lastPid.equals(p.pid) && contextSwitchOverhead > 0) {
                // treat overhead as idle segment "CS"
                gantt.add(new GanttSegment("CS", time, time + contextSwitchOverhead));
                time += contextSwitchOverhead;
            }
            int start = time;
            int run = Math.min(quantum, p.remaining);
            time += run;
            p.remaining -= run;
            gantt.add(new GanttSegment(p.pid, start, time));
            // add arrivals during execution
            while (i < n && all.get(i).arrival <= time) { q.add(all.get(i)); i++; }
            if (p.remaining > 0) q.add(p); else p.completion = time;
            lastPid = p.pid;
        }
        // Remove CS from context switch count (we count switches via segments method)
        return calcMetrics(all.stream().sorted(Comparator.comparing(pp->pp.pid)).collect(Collectors.toList()), gantt);
    }
}

