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

public class FCFSScheduler extends AbstractScheduler {
    @Override public String name() { return "FCFS"; }

    @Override
    public Result run(List<Process> processes) {
        List<Process> procs = cloneAndSortByArrival(processes);
        int time = 0;
        List<GanttSegment> gantt = new ArrayList<>();
        for (Process p : procs) {
            if (time < p.arrival) time = p.arrival;
            int start = time;
            time += p.burst;
            p.completion = time;
            gantt.add(new GanttSegment(p.pid, start, time));
        }
        return calcMetrics(procs, gantt);
    }
}

