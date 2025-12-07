/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

import java.util.List;
import java.util.Map;

public class Result {
    public final List<Map<String,Object>> metrics; // pid,arrival,burst,completion,tat,wt
    public final double avgTAT, avgWT, cpuUtil;
    public final List<GanttSegment> gantt;
    public final int throughput;
    public final int contextSwitches;
    public final int makespan;

    public Result(List<Map<String,Object>> metrics, double avgTAT, double avgWT, double cpuUtil,
                  List<GanttSegment> gantt, int throughput, int contextSwitches, int makespan) {
        this.metrics = metrics;
        this.avgTAT = avgTAT;
        this.avgWT = avgWT;
        this.cpuUtil = cpuUtil;
        this.gantt = gantt;
        this.throughput = throughput;
        this.contextSwitches = contextSwitches;
        this.makespan = makespan;
    }
}
