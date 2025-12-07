/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Si quieres UI, ejecuta SimulatorApp (javafx). Aquí mostramos ejemplo CLI.
        List<Process> workload = Arrays.asList(
            new Process("P1", 0, 5, 2),
            new Process("P2", 1, 3, 1),
            new Process("P3", 2, 8, 3),
            new Process("P4", 3, 1, 2)
        );

        List<Scheduler> schedulers = Arrays.asList(
            new FCFSScheduler(),
            new SJFNonPreemptiveScheduler(),
            new SRTFScheduler(),
            new RRScheduler(2, 1), // rr with quantum=2, overhead=1
            new PriorityPreemptiveScheduler(1)
        );

        for (Scheduler s : schedulers) {
            System.out.println("=== Scheduler: " + s.name() + " ===");
            Result r = s.run(workload);
            printResult(r);
            // export CSV example
            String prefix = "out_" + s.name().replaceAll("[^A-Za-z0-9]","") + "_" + System.currentTimeMillis();
            CSVExporter.exportResultCSV(r, prefix);
            System.out.println("Exported CSV to: " + prefix + "_*.csv");
            System.out.println();
        }

        System.out.println("Para usar la interfaz gráfica: ejecuta SimulatorApp (requiere JavaFX).");
    }

    private static void printResult(Result r) {
        System.out.println("Gantt:");
        r.gantt.forEach(seg -> System.out.print(seg + " "));
        System.out.println();
        System.out.printf("Avg TAT=%.2f  Avg WT=%.2f  CPU Util=%.2f  Throughput=%d  ContextSwitches=%d  Makespan=%d\n",
            r.avgTAT, r.avgWT, r.cpuUtil, r.throughput, r.contextSwitches, r.makespan);
        System.out.println("Per-process:");
        for (var m : r.metrics) {
            System.out.println(m);
        }
    }
}

