# Simulator

Simulador de Planificación de CPU — Proyecto Técnico
Descripción General

Este proyecto implementa un simulador interactivo de planificación de CPU escrito en Java utilizando JavaFX para la interfaz gráfica. Su propósito es modelar el funcionamiento de los algoritmos clásicos de planificación de procesos y permitir su comparación mediante métricas de rendimiento.

El simulador permite al usuario:

Ingresar procesos con diferentes atributos (tiempo de llegada, duración, prioridad).

Seleccionar el algoritmo de planificación a evaluar.

Ejecutar la simulación paso a paso de manera visual.

Analizar métricas como tiempo de espera, tiempo de retorno y uso de CPU.

Comparar el rendimiento entre algoritmos.

El objetivo general es comprender el impacto de cada estrategia de planificación y proporcionar una herramienta educativa clara, modular y extensible.

Objetivos
Objetivo General

Desarrollar un simulador funcional de planificación CPU que implemente algoritmos estándar y permita comparar su rendimiento mediante métricas representativas del comportamiento del sistema operativo.

Objetivos Específicos

Modelar procesos mediante clases que representen sus atributos y estado.

Implementar algoritmos clásicos de planificación:

FCFS (First Come First Served)

SJF (Shortest Job First)

Round Robin

Prioridades

Diseñar una interfaz visual intuitiva que permita ingresar datos y observar la simulación.

Calcular métricas:

Tiempo de espera promedio

Tiempo de retorno

Tiempo de respuesta

Tasa de uso de CPU

Permitir la comparación entre al menos dos algoritmos.

Arquitectura del Proyecto

El proyecto está organizado bajo un enfoque modular donde cada componente cumple una función clara:

1. Modelo (model/)

Incluye las clases que representan los datos:

Process
Representa un proceso con sus atributos:
id, arrivalTime, burstTime, remainingTime, priority, startTime, finishTime.

SchedulerResult
Modela los resultados y métricas obtenidas tras ejecutar un algoritmo.

2. Algoritmos (algorithms/)

Cada algoritmo se implementa como una clase separada, facilitando extensibilidad:

FCFS

SJF

RoundRobin

PriorityScheduler

Cada uno contiene un método:

public SchedulerResult run(List<Process> processes)

3. Lógica del simulador (core/)

Controla el flujo general, ejecución paso a paso y recolección de métricas.

4. Interfaz gráfica (JavaFX) (ui/)

Ventanas para:

Ingreso de procesos

Selección del algoritmo

Visualización de Gantt

Consola de eventos de simulación

Comparación de métricas

5. Clase principal (SimulatorApp)

Inicia JavaFX y carga la vista principal.

Tecnologías Utilizadas

Java 17 / 21 (según tu instalación)

JavaFX 21

NetBeans 17 o superior

Maven (opcional, si se deseó automatizar librerías)

Requisitos del Sistema

JDK 17 o superior instalado

JavaFX configurado (ruta añadida en VM Options)

NetBeans con soporte JavaFX

Sistema operativo: Windows, Linux o macOS

Cómo Ejecutar el Proyecto en NetBeans
1️. Clonar o descomprimir el proyecto ZIP
2️. Abrir en NetBeans

File, Open Project y Seleccionar carpeta del proyecto

3️. Configurar JavaFX

En Run → Set Project Configuration:

VM Options:

--module-path "C:\javafx\lib" --add-modules javafx.controls,javafx.fxml


(Adaptar ruta según instalación)

4️. Ejecutar

Botón Run e inicia SimulatorApp.

Cómo Usar el Simulador

Ingresar los procesos con sus parámetros.

Seleccionar un algoritmo de la lista.

Iniciar simulación.

Visualizar:

Diagrama de Gantt

La cola de listos

Procesos en ejecución y terminados

Revisar métricas generadas automáticamente.

Comparar con otro algoritmo.

Métricas Calculadas
Métrica	Descripción
Tiempo de Espera	Tiempo total que el proceso estuvo en cola
Tiempo de Retorno	Tiempo desde llegada hasta finalización
Tiempo de Respuesta	Tiempo hasta que ejecuta por primera vez
CPU Utilization	Porcentaje de tiempo activo de CPU
Estructura del Proyecto

 src/
  simulator/
    Process.java
    GanttSegment.java
    Result.java
    Scheduler.java
    AbstractScheduler.java
    FCFSScheduler.java
    SJFNonPreemptiveScheduler.java
    SRTFScheduler.java
    RRScheduler.java
    PriorityPreemptiveScheduler.java
    CSVExporter.java
    SimulatorApp.java    // JavaFX UI
    Main.java      
/README.md

Principales Decisiones de Diseño

Separación estricta modelo–vista–lógica (similar a MVC)
Facilita mantenimiento y escalabilidad.

Algoritmos implementados como módulos independientes
Permite añadir nuevos fácilmente.

Uso de JavaFX
Por su capacidad para interfaces modernas, controles avanzados y renderizado gráfico del diagrama de Gantt.

Ejemplo de Entrada

Proceso A: Llegada 0, ráfaga 5
Proceso B: Llegada 2, ráfaga 3
Proceso C: Llegada 4, ráfaga 1

Estado del Proyecto

- Funcional
- Soporta múltiples algoritmos
- Métricas automáticas
- Interfaz JavaFX

