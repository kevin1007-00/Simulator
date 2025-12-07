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
 * SRTF - preemptive SJF (prioriza menor remaining).
 * Soporta contexto por unidad de tiempo (unidad discreta).
 */
public class SRTFScheduler extends AbstractScheduler {
    @Override public String name() { return "SRTF (preemptive SJF)"; }

    @Override
    public Result run(List<Process> processes) {
        List<Process> all = cloneAndSortByArrival(processes);
        int n = all.size(), i = 0, time = 0;
        List<GanttSegment> gantt = new ArrayList<>();
        // ready ordered by remaining then arrival
        PriorityQueue<Process> ready = new PriorityQueue<>(Comparator.comparingInt((Process p)->p.remaining).thenComparingInt(p->p.arrival));
        Process current = null;
        while (i<n || !ready.isEmpty() || current != null) {
            while (i<n && all.get(i).arrival <= time) {
                ready.add(all.get(i)); i++;
            }
            if (current == null) {
                if (ready.isEmpty()) {
                    time = (i<n) ? all.get(i).arrival : time;
                    continue;
                }
                current = ready.poll();
            }
            // run 1 unit
            int start = time;
            time += 1;
            current.remaining -= 1;
            gantt.add(new GanttSegment(current.pid, start, time));
            while (i<n && all.get(i).arrival <= time) {
                ready.add(all.get(i)); i++;
            }
            if (current.remaining == 0) {
                current.completion = time;
                current = null;
            } else {
                // preempt if someone in ready has less remaining
                if (!ready.isEmpty() && ready.peek().remaining < current.remaining) {
                    ready.add(current);
                    current = null;
                }
            }
        }
        return calcMetrics(all, gantt);
    }
}

