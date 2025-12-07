/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportResultCSV(Result r, String prefixPath) throws IOException {
        // export metrics
        String metricsPath = prefixPath + "_metrics.csv";
        try (FileWriter fw = new FileWriter(metricsPath)) {
            fw.write("pid,arrival,burst,completion,tat,wt\n");
            for (var m : r.metrics) {
                fw.write(String.format("%s,%s,%s,%s,%s,%s\n",
                    m.get("pid"), m.get("arrival"), m.get("burst"), m.get("completion"), m.get("tat"), m.get("wt")));
            }
        }
        // export gantt
        String ganttPath = prefixPath + "_gantt.csv";
        try (FileWriter fw = new FileWriter(ganttPath)) {
            fw.write("pid,start,end\n");
            for (GanttSegment g : r.gantt) {
                fw.write(String.format("%s,%d,%d\n", g.pid, g.start, g.end));
            }
        }
        // summary
        String summaryPath = prefixPath + "_summary.txt";
        try (FileWriter fw = new FileWriter(summaryPath)) {
            fw.write(String.format("avgTAT=%.3f\navgWT=%.3f\ncpuUtil=%.5f\nthroughput=%d\ncontextSwitches=%d\nmakespan=%d\n",
                    r.avgTAT, r.avgWT, r.cpuUtil, r.throughput, r.contextSwitches, r.makespan));
        }
    }
}

