# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- **Build project**: `./gradlew build`
- **Run tests**: `./gradlew test`
- **Run linting**: `./gradlew ktlintCheck`
- **Auto-fix linting**: `./gradlew ktlintFormat`
- **Run static analysis**: `./gradlew detekt`
- **Run single test class**: `./gradlew test --tests "ClassName"`
- **Code coverage report**: `./gradlew jacocoTestReport` (outputs to `build/jacocoHtml`)
- **Run application**: `./gradlew run`

## Benchmarking Commands

- **Run full benchmarks**: `./gradlew benchmark`
- **Run quick benchmarks**: `./gradlew benchmarkQuick`
- **Generate benchmark reports**: `./gradlew generateBenchmarkReport`
- **Benchmark validation**: `./gradlew benchmarkValidation`

## Architecture

This is a **ZPL (Zebra Programming Language) II parser** built with Kotlin using a **recursive descent parsing approach**. The architecture follows a classic compiler pipeline:

### Core Components

1. **Lexer** (`src/main/kotlin/.../lexer/`): Tokenizes ZPL input into structured tokens
   - Dynamic character support (^CC, ^CD, ^CT commands can change delimiters)
   - Performance optimized for high-throughput parsing
   - Handles format commands (^), control commands (~), and parameters

2. **Parser** (`src/main/kotlin/.../parser/`): Converts tokens to Abstract Syntax Tree (AST)
   - `ZplParser.kt`: Main recursive descent parser
   - Command-specific parsing utilities in separate files
   - Registry-based command parsing for extensibility

3. **AST Nodes** (`src/main/kotlin/.../ast/`): Typed representation of ZPL commands
   - Sealed class hierarchy with visitor pattern support
   - Each ZPL command (^XA, ^FO, ^FD, ^A, etc.) has its own AST node

4. **Utilities**: 
   - `AstPrinter`: Visitor implementation for debugging/display
   - `DemoRunner`: Performance testing and examples
   - `BaselineComparison`: Performance regression detection

### Code Quality Setup

- **Testing**: Kotest framework with property-based testing
- **Linting**: Ktlint (version 1.0.1) with experimental rules enabled
- **Static Analysis**: Detekt with custom configuration in `config/detekt.yml`
- **Coverage**: JaCoCo with HTML reports
- **Benchmarking**: kotlinx-benchmark with JMH backend

### Performance Requirements

- Simple commands (^XA, ^XZ): < 0.1ms (100,000 ns)
- Complex commands (^FO, ^FD, ^A): < 1ms (1,000,000 ns)
- Regression threshold: > 10% degradation triggers warnings
- Memory efficient parsing with GC optimization

### Development Patterns

- **TDD Approach**: RED → GREEN → REFACTOR workflow
- **Visitor Pattern**: Used for AST traversal and operations
- **Command Pattern**: Registry-based command parsing
- **Builder Pattern**: Used in parsing utilities for parameter handling
- **Sealed Classes**: Ensure exhaustive when expressions in AST handling

### Main Entry Points

- **Main.kt**: Demo application showcasing parser capabilities
- **ZplParser**: Primary parsing interface
- **Lexer**: Tokenization entry point
- **AstPrinter**: For debugging and visualization

When adding new ZPL commands, follow the pattern: create AST node, add parsing logic, update visitor interface, and write comprehensive tests.