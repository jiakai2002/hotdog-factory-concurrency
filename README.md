# Hot Dog Factory Concurrency Simulation

## Project Overview

This is a multi-threaded Java simulation of a hot dog factory production system, designed to demonstrate concurrency, synchronization, and thread management principles. The simulation models the complex interactions between making and packing machines in a controlled manufacturing environment.

### Key Learning Objectives

- Gain practical experience with multi-threaded programming
- Understand OS concepts including:
  - Process concurrency
  - Synchronization
  - Mutual exclusion

## System Architecture

### Components

- **Making Machines**: Threads that produce hot dogs
- **Packing Machines**: Threads that pack hot dogs from a shared pool
- **Manager (Main Thread)**: Coordinates the entire production process

### Workflow

1. Making machines create hot dogs and place them in a shared pool
2. Packing machines retrieve and pack hot dogs from the pool
3. A log file tracks all production activities
4. Final summary generated after all production is complete

## Usage

### Prerequisites

- Java Development Kit (JDK)
- Command-line interface

### Running the Simulation

```bash
java HotDogManager <N> <S> <M> <P>

Where:
- N: Total number of hot dogs to produce
- S: Number of slots in the hot dog pool
- M: Number of making machines
- P: Number of packing machines
```

### Example

```bash
java HotDogManager 10 3 2 2
```

This will:
- Produce 10 hot dogs
- Use a pool with 3 slots
- Use 2 making machines
- Use 2 packing machines

## Simulation Constraints

- Making machines take 4 time units to make a hot dog
- Packing machines take 2 time units to pack a hot dog
- Maximum of 30 machines per type
- First-come-first-serve packing order

## Logging

A `log.txt` file is generated, recording:
- Hot dog production and packing events
- Machine-specific statistics
- Final production summary

## License

This project is part of an academic assignment and is not licensed for external use.
