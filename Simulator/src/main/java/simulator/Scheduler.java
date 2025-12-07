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

public interface Scheduler {
    /**
     * Ejecuta la simulación sobre una lista de procesos (los procesos NO deben modificarse por el caller).
     * Devuelve un Result con métricas y Gantt.
     */
    Result run(List<Process> processes);
    String name();
}

