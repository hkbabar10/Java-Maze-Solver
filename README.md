# Java Maze Solver & Visualizer

An interactive desktop maze-solving application built in **Java Swing**. Watch a **depth-first search (DFS)** algorithm explore a maze, backtrack from dead ends, and highlight a route to the exit.

## Features

- Animated depth-first search with backtracking
- Five included sample mazes, selectable from a dropdown
- Start, pause/resume, and reset controls
- Adjustable animation speed
- Search progress and final path length
- Input validation for malformed maze files

## Getting started

Requires **Java JDK 11 or newer**. From the repository root:

```bash
javac Maze.java MazeSolver.java MazeVisualizer.java
java MazeSolver
```

Run these commands on a machine with a desktop graphical environment (macOS, Windows, or Linux). GitHub Codespaces usually cannot display Java Swing windows directly.

## Maze format

Maze files live in `mazes/`. Each is a rectangular text grid using:

| Symbol | Meaning |
| --- | --- |
| `#` | Wall |
| ` ` | Walkable corridor |
| `S` | Start |
| `E` | Exit |

Each maze must contain exactly one `S` and one `E`.

## How it works

DFS explores neighbors in this order: down, up, right, left. Previously visited cells are tracked to prevent cycles. When a branch reaches a dead end, the search backtracks. Once it reaches the exit, the current search stack is highlighted as the final route.

**Note:** DFS finds a route when one exists, but does not necessarily find the shortest route.

## Project structure

```text
Java-Maze-Solver/
├── Maze.java
├── MazeSolver.java
├── MazeVisualizer.java
├── mazes/
│   ├── maze1.txt
│   ├── maze2.txt
│   ├── maze3.txt
│   ├── maze4.txt
│   └── maze5.txt
├── .gitignore
└── README.md
```

## Built with

Java, Java Swing, depth-first search, and backtracking.
