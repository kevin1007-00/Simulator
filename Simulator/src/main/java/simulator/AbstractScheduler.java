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
 * Clase base con utilidades comunes.
 */
public abstract class AbstractScheduler implements Scheduler {

    protected Result calcMetrics(List<Process> procs, List<GanttSegment> gantt) {
        List<Map<String,Object>> metrics = new ArrayList<>();
        double sumTAT=0, sumWT=0;
        for (Process p: procs) {
            if (p.completion == null) p.completion = 0;
            int tat = p.completion - p.arrival;
            int wt = tat - p.burst;
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("pid", p.pid); m.put("arrival", p.arrival); m.put("burst", p.burst);
            m.put("completion", p.completion); m.put("tat", tat); m.put("wt", wt);
            metrics.add(m);
            sumTAT += tat; sumWT += wt;
        }
        int n = procs.size();
        double avgTAT = sumTAT / n;
        double avgWT = sumWT / n;
        int cpuBusy = gantt.stream().mapToInt(g -> g.end - g.start).sum();
        int makespan = gantt.isEmpty() ? 0 : gantt.get(gantt.size()-1).end;
        double cpuUtil = makespan>0 ? (double)cpuBusy / makespan : 0;
        int throughput = (makespan>0) ? (int)Math.round((double)n / makespan) : 0;
        int ctx = countContextSwitches(gantt);
        return new Result(metrics, avgTAT, avgWT, cpuUtil, gantt, throughput, ctx, makespan);
    }

    protected int countContextSwitches(List<GanttSegment> gantt) {
        if (gantt.isEmpty()) return 0;
        String last = gantt.get(0).pid;
        int switches = 0;
        for (int i = 1; i < gantt.size(); i++) {
            String cur = gantt.get(i).pid;
            if (!cur.equals(last)) {
                switches++;
                last = cur;
            }
        }
        return switches;
    }

    protected List<Process> cloneAndSortByArrival(List<Process> processes) {
        return processes.stream().map(Process::cloneFresh).sorted(Comparator.comparingInt(p->p.arrival)).collect(Collectors.toList());
    }
}

