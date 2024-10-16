# Motor de Inferencia con Backward Chaining

Este proyecto es una implementación de un motor de inferencia utilizando el algoritmo de encadenamiento hacia atrás (backward chaining). El sistema utiliza archivos de texto para cargar una base de hechos e hipótesis (reglas), y permite realizar inferencias para verificar si una hipótesis es verdadera a partir de los hechos conocidos.

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura de los Archivos de Entrada](#estructura-de-los-archivos-de-entrada)
- [Ejemplo de Uso](#ejemplo-de-uso)
- [Contribuciones](#contribuciones)

## Descripción del Proyecto

El motor de inferencia utiliza archivos de texto que definen un conjunto de **hechos** y **hipótesis** (reglas). A partir de estos archivos, el sistema construye un grafo lógico y permite verificar si un hecho o hipótesis (objetivo) puede deducirse a partir de los hechos conocidos usando la técnica de **encadenamiento hacia atrás**.

El objetivo principal de la aplicación es verificar si una hipótesis puede ser verdadera, explorando recursivamente las condiciones necesarias para que dicha hipótesis sea válida.

## Arquitectura del Proyecto

El proyecto tiene la siguiente estructura básica:

- **Main.java**: El punto de entrada de la aplicación.
- **Fact.java**: Representa un hecho en la base de conocimientos.
- **Condition.java**: Representa una condición que debe cumplirse para validar una hipótesis.
- **Hypothesis.java**: Define una hipótesis (regla) con un hecho principal y un conjunto de condiciones.
- **backwardChaining()**: Implementa el algoritmo de encadenamiento hacia atrás.
- **Archivos de Entrada**: 
  - `hechos.txt`: Contiene los hechos conocidos.
  - `hipotesis.txt`: Contiene las hipótesis que definen las reglas.

## Requisitos

- **Java 8 o superior** debe estar instalado en tu sistema.
- Un editor de texto o IDE compatible con Java, como **IntelliJ IDEA** o **Eclipse**.

## Instalación

1. Clona el repositorio del proyecto o descárgalo como archivo ZIP:

   ```bash
   git clone https://github.com/tu-usuario/motor-inferencia-backward.git
