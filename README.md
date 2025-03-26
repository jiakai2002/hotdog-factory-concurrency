# Hot Dog Factory Concurrency Simulation

This project simulates the operation of a hot dog factory using multithreaded programming in Java. The factory consists of multiple making machines and packing machines working concurrently to produce and pack hot dogs. The simulation demonstrates concepts of process concurrency, synchronization, and mutual exclusion using Java's concurrency primitives.

## Assignment Overview

The factory has:
- **Making machines** that make hot dogs.
- **Packing machines** that pack hot dogs from a shared hot dog pool.
- A **manager** (main thread) that coordinates the production process, tracks the number of hot dogs, and writes log records.

### Key Features:
- **Multiple Threads:** Each making machine and packing machine runs on a separate thread.
- **Circular Queue:** The hot dog pool is modeled as a circular queue with limited slots.
- **Logging:** The program logs each action performed by the machines, including when hot dogs are made and packed.

### Requirements:
- The program is designed to simulate the hot dog production process with parameters:
  - `N` (number of hot dogs)
  - `S` (slots in the hot dog pool)
  - `M` (making machines)
  - `P` (packing machines)

### Log Format:
- When a making machine adds a hot dog to the pool:
