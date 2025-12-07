/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulator;

/**
 *
 * @author KEVIN
 */

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SimulatorApp extends Application {

    private TableView<ProcRow> table = new TableView<>();
    private ObservableList<ProcRow> rows = FXCollections.observableArrayList();
    private TextArea output = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulador de Planificación CPU - Java");

        // table columns
        TableColumn<ProcRow, String> c1 = new TableColumn<>("PID");
        c1.setCellValueFactory(d -> d.getValue().pidProperty());
        TableColumn<ProcRow, Integer> c2 = new TableColumn<>("Arrival");
        c2.setCellValueFactory(d -> d.getValue().arrivalProperty().asObject());
        TableColumn<ProcRow, Integer> c3 = new TableColumn<>("Burst");
        c3.setCellValueFactory(d -> d.getValue().burstProperty().asObject());
        TableColumn<ProcRow, Integer> c4 = new TableColumn<>("Priority");
        c4.setCellValueFactory(d -> d.getValue().priorityProperty().asObject());

        table.getColumns().addAll(c1,c2,c3,c4);
        table.setItems(rows);
        table.setPrefHeight(200);

        TextField pidField = new TextField("P1");
        TextField arrField = new TextField("0");
        TextField burstField = new TextField("5");
        TextField prField = new TextField("0");
        Button addBtn = new Button("Agregar");
        addBtn.setOnAction(e -> {
            rows.add(new ProcRow(pidField.getText(), Integer.parseInt(arrField.getText()), Integer.parseInt(burstField.getText()), Integer.parseInt(prField.getText())));
            pidField.setText("P" + (rows.size()+1));
        });

        HBox addBox = new HBox(8, new Label("PID"), pidField, new Label("Arrival"), arrField, new Label("Burst"), burstField, new Label("Pr"), prField, addBtn);
        addBox.setPadding(new Insets(8));

        ComboBox<String> algBox = new ComboBox<>();
        algBox.getItems().addAll("FCFS","SJF (non-preemptive)","SRTF (preemptive)","Round Robin","Priority (preemptive)");
        algBox.getSelectionModel().select(0);
        TextField quantumField = new TextField("2");
        TextField overheadField = new TextField("0");

        Button runBtn = new Button("Ejecutar");
        Button exportBtn = new Button("Exportar CSV");

        runBtn.setOnAction(e -> {
            try {
                runSimulation(algBox.getValue(), Integer.parseInt(quantumField.getText()), Integer.parseInt(overheadField.getText()));
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        exportBtn.setOnAction(e -> {
            try {
                // run and export
                Result r = runAndReturn(algBox.getValue(), Integer.parseInt(quantumField.getText()), Integer.parseInt(overheadField.getText()));
                String pathPrefix = "sim_result_" + algBox.getValue().replaceAll("[^A-Za-z0-9]","") + "_" + System.currentTimeMillis();
                CSVExporter.exportResultCSV(r, pathPrefix);
                output.appendText("\nExportado CSV a: " + pathPrefix + "_*.csv\n");
            } catch (Exception ex) {
                output.appendText("\nExport error: " + ex.getMessage()+"\n");
            }
        });

        HBox controlBox = new HBox(8, new Label("Algoritmo:"), algBox, new Label("Quantum:"), quantumField, new Label("Overhead:"), overheadField, runBtn, exportBtn);
        controlBox.setPadding(new Insets(8));

        output.setPrefHeight(300);
        output.setEditable(false);

        VBox root = new VBox(6, addBox, table, controlBox, output);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runSimulation(String alg, int quantum, int overhead) {
        Result r = runAndReturn(alg, quantum, overhead);
        displayResult(r);
    }

    private Result runAndReturn(String alg, int quantum, int overhead) {
        List<Process> procs = rows.stream().map(pr -> new Process(pr.getPid(), pr.getArrival(), pr.getBurst(), pr.getPriority())).collect(Collectors.toList());
        Scheduler s;
        switch (alg) {
            case "FCFS": s = new FCFSScheduler(); break;
            case "SJF (non-preemptive)": s = new SJFNonPreemptiveScheduler(); break;
            case "SRTF (preemptive)": s = new SRTFScheduler(); break;
            case "Round Robin": s = new RRScheduler(quantum, overhead); break;
            case "Priority (preemptive)": s = new PriorityPreemptiveScheduler(overhead); break;
            default: s = new FCFSScheduler();
        }
        return s.run(procs);
    }

    private void displayResult(Result r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Gantt:\n");
        for (GanttSegment g : r.gantt) {
            sb.append(g.toString()).append(" ");
        }
        sb.append("\n\nSummary:\n");
        sb.append(String.format("Avg TAT=%.2f  Avg WT=%.2f  CPU Util=%.3f  Throughput=%d  ContextSwitches=%d  Makespan=%d\n",
                r.avgTAT, r.avgWT, r.cpuUtil, r.throughput, r.contextSwitches, r.makespan));
        sb.append("\nPer-process:\n");
        for (var m : r.metrics) {
            sb.append(m).append("\n");
        }
        output.setText(sb.toString());
    }

    public static class ProcRow {
        private final StringProperty pid = new SimpleStringProperty();
        private final IntegerProperty arrival = new SimpleIntegerProperty();
        private final IntegerProperty burst = new SimpleIntegerProperty();
        private final IntegerProperty priority = new SimpleIntegerProperty();
        public ProcRow(String pid, int arrival, int burst, int priority) {
            this.pid.set(pid);
            this.arrival.set(arrival);
            this.burst.set(burst);
            this.priority.set(priority);
        }
        public StringProperty pidProperty() { return pid; }
        public IntegerProperty arrivalProperty() { return arrival; }
        public IntegerProperty burstProperty() { return burst; }
        public IntegerProperty priorityProperty() { return priority; }
        public String getPid() { return pid.get(); }
        public int getArrival() { return arrival.get(); }
        public int getBurst() { return burst.get(); }
        public int getPriority() { return priority.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
