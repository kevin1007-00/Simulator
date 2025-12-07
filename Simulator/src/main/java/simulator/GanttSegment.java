/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

public class GanttSegment {
    public final String pid;
    public final int start;
    public final int end;
    public GanttSegment(String pid, int start, int end) {
        this.pid = pid;
        this.start = start;
        this.end = end;
    }
    @Override
    public String toString() {
        return String.format("%s:[%d-%d]", pid, start, end);
    }
}

