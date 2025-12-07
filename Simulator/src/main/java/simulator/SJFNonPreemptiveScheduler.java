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

public class SJFNonPreemptiveScheduler extends AbstractScheduler {
    @Override public String name() { return "SJF (non-preemptive)"; }

    @Override
    public Result run(List<Process> processes) {
        List<Process> all = cloneAndSortByArrival(processes);
        int n = all.size(), i = 0, time = 0;
        List<GanttSegment> gantt = new ArrayList<>();
        PriorityQueue<Process> ready = new PriorityQueue<>(
    Comparator.comparingInt((Process p) -> p.burst)
              .thenComparingInt(p -> p.arrival)
);

        while (i < n || !ready.isEmpty()) {
            while (i<n && all.get(i).arrival <= time) {
                ready.add(all.get(i)); i++;
            }
            if (ready.isEmpty()) {
                time = all.get(i).arrival;
                continue;
            }
            Process p = ready.poll();
            int start = time;
            time += p.burst;
            p.completion = time;
            gantt.add(new GanttSegment(p.pid, start, time));
        }
        return calcMetrics(all, gantt);
    }
}

