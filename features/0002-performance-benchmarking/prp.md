# PRP: Performance Benchmarking System for ZPL Parser Commands

## Goal
Implement a comprehensive benchmarking system for ZPL command parsing to ensure the parser maintains high-performance standards and catches performance degradation early. The system will benchmark each existing ZPL command (^XA, ^XZ, ^FO, ^FD, ^A) for both execution time and memory usage, automatically benchmark any new commands added in the future, and integrate into validation workflow with dedicated Gradle task.

## Why
- **Performance Assurance** - Ensure parser maintains sub-millisecond performance suitable for internet transfer scenarios
- **Regression Detection** - Catch performance degradation early in development cycle before it reaches production
- **Baseline Management** - Establish and maintain aggressive performance thresholds targeting very low millisecond execution times
- **CI/CD Integration** - Report warnings when performance degrades without failing builds to maintain development velocity
- **Future-Proofing** - Automatically benchmark any new ZPL commands added to the parser

## What
A complete benchmarking infrastructure that measures both execution time and memory allocation for ZPL parsing operations, with automatic baseline comparison and performance regression warnings.

### Success Criteria
- [ ] Benchmarks implemented for all 5 existing ZPL commands (^XA, ^XZ, ^FO, ^FD, ^A)
- [ ] Both execution time and memory allocation benchmarks operational
- [ ] Performance thresholds established: <0.1ms for simple commands, <1ms for complex commands
- [ ] Dedicated Gradle task `./gradlew benchmark` integrated into build pipeline
- [ ] Baseline comparison system with >10% degradation warnings
- [ ] HTML reports generated in `build/reports/benchmarks/`
- [ ] Documentation updated in CLAUDE.md and PRP base template

## Context & Documentation

### Required Reading
```yaml
# MUST READ - Include these in your context window
- file: src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt
  why: Core parsing logic to benchmark, understand performance-critical sections
  
- file: src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt
  why: E2E test patterns and ZPL examples to use as benchmark input data
  
- file: build.gradle.kts
  why: Current build configuration, dependency management patterns for kotlinx-benchmark integration
  
- file: src/main/kotlin/com/whitespacesystems/parser/Main.kt
  why: Demo application examples showing typical ZPL parsing workflows to benchmark
  
- doc: https://github.com/Kotlin/kotlinx-benchmark
  section: JVM Configuration and Setup
  critical: Proper allopen plugin configuration required for JMH to work with Kotlin
  
- doc: https://github.com/melix/jmh-gradle-plugin
  section: Gradle Integration and Task Configuration  
  critical: Alternative approach - JMH gradle plugin vs kotlinx-benchmark comparison
```

### Current Codebase Structure
```bash
src/
├── main/kotlin/com/whitespacesystems/parser/
│   ├── Main.kt                           # Demo application entry point
│   ├── ast/                              # AST node definitions (5 commands)
│   ├── lexer/                           # Lexical analysis
│   ├── parser/                          # ZplParser.kt (core parsing logic)
│   └── utils/                           # AstPrinter.kt
└── test/kotlin/com/whitespacesystems/parser/
    ├── ErrorHandlingTest.kt             # Parser error scenarios
    ├── lexer/                           # Tokenizer tests
    ├── parser/                          # Parser component tests + E2E
    └── utils/                           # Utility tests

build.gradle.kts                        # Current: Kotest, Detekt, Ktlint, Jacoco
```

### Desired Structure
```bash
# New benchmarking infrastructure
src/benchmark/kotlin/com/whitespacesystems/parser/
├── CommandBenchmarks.kt                 # Individual command benchmarks
├── E2EBenchmarks.kt                     # Complete ZPL label parsing
├── MemoryBenchmarks.kt                  # Memory allocation measurement
└── data/
    ├── SimpleBenchmarkData.kt           # Small ZPL test cases
    └── ComplexBenchmarkData.kt          # Large, complex ZPL labels

benchmarks/
├── baselines/
│   └── baseline.json                    # Performance baseline storage
└── reports/                             # Generated HTML reports (gitignore)

# Updated build configuration
build.gradle.kts                        # + kotlinx-benchmark plugin and dependencies
```

### Known Codebase Patterns & Gotchas
```kotlin
// CRITICAL: Project-specific patterns from ZPL parser codebase

// 1. Context-aware lexing - Field data vs command parsing
// From Lexer.kt: Field data mode after ^FD commands treats everything as string until next ^
if (command == "FD") {
    fieldDataMode = true
}

// 2. Sealed class hierarchy for type-safe AST nodes
// From ZplNode.kt: All command nodes extend sealed class
sealed class ZplNode {
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}

// 3. Parser error handling with position information
// From ZplParser.kt: All exceptions include token position
throw ParseException("Expected coordinate parameter", token.position)

// 4. Kotest StringSpec testing pattern
// From existing tests: Descriptive test names with should/when format
"should parse FO command with coordinates" {
    val lexer = Lexer("^FO100,50")
    val parser = ZplParser(lexer.tokenize())
    // Test implementation
}

// 5. Performance-critical parsing - avoid object allocations in loops
// Use StringBuilder for string building, reuse collections where possible

// 6. kotlinx-benchmark specific requirements
// Classes must be open (allopen plugin) for JMH proxy generation
@State(Scope.Benchmark) 
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ZplParserBenchmark {
    @Benchmark
    fun benchmarkFieldOriginCommand(): ZplProgram {
        val lexer = Lexer("^FO100,50")
        return ZplParser(lexer.tokenize()).parse()
    }
}

// 7. Memory benchmarking with GC profiler
// Track allocation rate and GC overhead
@BenchmarkMode(Mode.AverageTime, Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class MemoryBenchmarks {
    // Memory allocation measurement patterns
}
```

## Implementation Blueprint

### Data Models
```kotlin
// Benchmark data classes for result storage and comparison
data class BenchmarkResult(
    val commandName: String,
    val averageTimeNs: Double,
    val memoryAllocatedBytes: Long,
    val throughputOpsPerSec: Double,
    val timestamp: Long
)

data class PerformanceBaseline(
    val results: Map<String, BenchmarkResult>,
    val createdAt: Long,
    val version: String
)

// Test data generators for consistent benchmark inputs
object BenchmarkData {
    val SIMPLE_COMMANDS = listOf(
        "^FO100,50",           // Field Origin
        "^FDHello World",      // Field Data  
        "^A0N,30,30",         // Font Command
        "^XA",                // Start Format
        "^XZ"                 // End Format
    )
    
    val COMPLEX_LABELS = listOf(
        "^XA^FO100,50^A0N,30,30^FDHello World^XZ",
        "^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)",
        // More complex multi-field labels
    )
}
```

### Task List (in execution order)
```yaml
Task 1: UPDATE build.gradle.kts - Add kotlinx-benchmark configuration
  - PATTERN: Follow existing plugin configuration (detekt, ktlint patterns)
  - CRITICAL: Add allopen plugin for @State classes, kotlinx-benchmark runtime dependency
  - VERIFY: ./gradlew tasks shows new benchmark tasks

Task 2: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/data/BenchmarkData.kt
  - PATTERN: Follow existing data organization patterns from test directories
  - CRITICAL: Create simple and complex ZPL test case generators
  - VERIFY: Data generators produce consistent, realistic ZPL inputs

Task 3: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/CommandBenchmarks.kt
  - PATTERN: Use kotlinx-benchmark @State and @Benchmark annotations
  - CRITICAL: Individual benchmarks for each ZPL command (^XA, ^XZ, ^FO, ^FD, ^A)
  - VERIFY: Benchmarks execute and measure sub-millisecond performance

Task 4: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/E2EBenchmarks.kt
  - PATTERN: Follow ZplParserE2ETest.kt structure for complete label parsing
  - CRITICAL: Benchmark complete ZPL label parsing workflows (simple to complex)
  - VERIFY: End-to-end parsing performance measured accurately

Task 5: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/MemoryBenchmarks.kt
  - PATTERN: Use JMH GC profiler annotations for memory tracking
  - CRITICAL: Measure memory allocation per parse operation
  - VERIFY: Memory usage reported in bytes per parsing operation

Task 6: CREATE benchmarks/baselines/baseline.json - Performance baseline storage
  - PATTERN: JSON structure for storing benchmark results over time
  - CRITICAL: Include timestamp, version, and all benchmark metrics
  - VERIFY: Baseline storage and retrieval working correctly

Task 7: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/BaselineComparison.kt
  - PATTERN: Utility class for comparing current vs baseline performance
  - CRITICAL: Detect >10% performance degradation, generate warnings
  - VERIFY: Performance regression detection working with clear warnings

Task 8: UPDATE build.gradle.kts - Add benchmark Gradle tasks and reporting
  - PATTERN: Follow existing task configuration (jacoco, test task patterns)
  - CRITICAL: ./gradlew benchmark, ./gradlew benchmarkQuick tasks
  - VERIFY: Tasks execute benchmarks and generate HTML reports

Task 9: CREATE benchmark report generation and HTML output
  - PATTERN: Similar to jacoco HTML reports in build/reports/
  - CRITICAL: Readable performance reports with baseline comparison
  - VERIFY: HTML reports generated in build/reports/benchmarks/

Task 10: INTEGRATE benchmark task with existing quality gates
  - PATTERN: Follow existing check task integration patterns
  - CRITICAL: Optional benchmark execution (warnings only, no build failures)  
  - VERIFY: Quality pipeline includes performance validation

Task 11: CREATE comprehensive benchmark test suite
  - PATTERN: Use Kotest StringSpec for benchmark validation
  - CRITICAL: Test benchmark execution, baseline comparison, report generation
  - VERIFY: Benchmark infrastructure thoroughly tested

Task 12: UPDATE CLAUDE.md - Add benchmarking workflow and requirements
  - PATTERN: Follow existing Development Environment section structure
  - CRITICAL: Document performance targets, commands, baseline management
  - VERIFY: Documentation complete and clear for future developers

Task 13: UPDATE agents/prps/templates/PRP.md - Add performance validation
  - PATTERN: Extend existing Quality Assurance Guide section
  - CRITICAL: Include benchmark execution in standard PRP validation gates
  - VERIFY: Future PRPs will include performance validation when applicable

Task 14: VALIDATION - Execute full benchmark suite and verify all systems
  - PATTERN: Run complete quality gate sequence: build, test, benchmark
  - CRITICAL: All benchmarks pass performance thresholds, reports generated
  - VERIFY: ./gradlew check && ./gradlew benchmark succeeds completely

Task 15: DEMONSTRATION - Update Main.kt with performance demonstration
  - PATTERN: Extend existing demo application with benchmark examples
  - CRITICAL: Show performance characteristics of different ZPL parsing scenarios
  - VERIFY: Demo shows parsing performance in human-readable format
```

---

# FOUNDATION REFERENCE
**COPY THIS ENTIRE SECTION INTO EVERY PRP**

## ⚠️ Critical Performance Requirements

### Performance is Paramount
- **High-performance parsing**: Parser must handle large ZPL files efficiently
- **Memory efficiency**: Minimize object allocations during parsing
- **Single-pass parsing**: Avoid multiple passes over input where possible
- **Lazy evaluation**: Use sequences and lazy collections for token processing
- **Context-aware parsing**: Field data (^FD) vs command parsing modes

### Performance Optimization Guidelines
- **Avoid string concatenation** in hot paths - use StringBuilder
- **Reuse objects** where possible - consider object pooling for tokens
- **Minimize regex usage** - use character-by-character parsing for speed
- **Profile regularly** - measure parsing performance on large ZPL files
- **Cache frequently accessed data** - command mappings, token types

## 🏗️ ZPL Parser Architecture

### Core Architecture Pattern
The parser follows a classic **lexer → parser → AST** architecture optimized for ZPL parsing:

**Lexer** (`src/main/kotlin/com/whitespacesystems/parser/lexer/`)
- `Lexer.kt` - Tokenizes ZPL strings into tokens for parsing
- `Token.kt` - Token types and data structures
- Handles ZPL-specific syntax: `^` commands, `~` control commands, comma-separated parameters
- Context-aware parsing (field data mode after `^FD` commands)

**Parser** (`src/main/kotlin/com/whitespacesystems/parser/parser/`)
- `ZplParser.kt` - Recursive descent parser that converts tokens to AST
- Follows grammar: `Program → Command*` where commands are `^FO`, `^FD`, `^A` variants
- Throws `ParseException` for syntax errors

**AST** (`src/main/kotlin/com/whitespacesystems/parser/ast/`)
- `ZplNode.kt` - Base sealed class with visitor pattern support
- `ZplProgram.kt` - Root AST node containing command list
- Command nodes: `FieldOriginCommand.kt`, `FieldDataCommand.kt`, `FontCommand.kt`
- Visitor pattern enables extensible AST operations

### Key Design Patterns
- **Visitor Pattern** - AST traversal and operations
- **Recursive Descent Parsing** - Predictable, maintainable parsing strategy  
- **Sealed Classes** - Type-safe AST nodes with exhaustive pattern matching
- **Context-Aware Lexing** - Special handling for field data vs command contexts

## 🚨 Development Requirements

### TDD (MANDATORY - 80% Coverage Minimum)
```bash
# RED-GREEN-REFACTOR cycle with Kotest
# 1. Write test FIRST (Kotest StringSpec)
# 2. See it fail (RED)
# 3. Implement parser logic
# 4. See it pass (GREEN)
# 5. Check coverage: ./gradlew jacocoTestReport
```

**⚠️ CODE WITHOUT TESTS WILL BE REJECTED. NO EXCEPTIONS.**

### Testing Strategy: Kotest StringSpec
**Testing Framework Pattern:**
```kotlin
class NewFeatureTest : StringSpec({
    "should parse new ZPL command" {
        val lexer = Lexer("^NewCommand100,200")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<NewCommand>()
        // Verify command-specific properties
    }
})
```

### Essential Development Commands
```bash
./gradlew build                     # Build the project
./gradlew run                       # Run demo application
./gradlew test                      # Run all tests
./gradlew ktlintFormat             # Auto-fix code formatting
./gradlew detekt                   # Run static analysis
./gradlew jacocoTestReport         # Generate coverage report
./gradlew check                    # Run ALL verification tasks
./gradlew benchmark                # Run performance benchmarks (NEW)
./gradlew benchmarkQuick           # Quick benchmark profile (NEW)
```

## 📋 Kotlin Standards & Patterns

### Kotlin Requirements
- **Type safety** - Explicit types for public APIs, inference for locals
- **Null safety** - Leverage Kotlin's null safety features
- **Immutability** - Prefer `val` over `var`, immutable data classes
- **Sealed classes** - Use for type-safe hierarchies (AST nodes)

### Code Conventions
- **Classes**: PascalCase (`ZplParser`, `FieldOriginCommand`)
- **Functions**: camelCase (`parseCommand()`, `tokenize()`)
- **Properties**: camelCase (`tokenType`, `position`)
- **Constants**: SCREAMING_SNAKE_CASE (`MAX_COORDINATE_VALUE`)
- **Packages**: lowercase (`lexer`, `parser`, `ast`, `utils`)

### Adding New ZPL Commands Pattern
1. **AST Node**: Create sealed class extending `ZplNode` in `ast/`
2. **Parser Logic**: Add command parsing in `ZplParser.kt`
3. **Tests**: Create comprehensive test file in `parser/`
4. **Benchmarks**: Add performance benchmarks for new command

### Documentation Pattern
```kotlin
/**
 * Parses a Field Origin (^FO) command for positioning text/graphics.
 * 
 * @param tokens Token sequence starting with ^FO command
 * @return FieldOriginCommand with x,y coordinates
 * @throws ParseException if coordinates are missing or invalid
 * 
 * Example ZPL: ^FO100,200 positions field at x=100, y=200
 */
fun parseFieldOrigin(tokens: List<Token>): FieldOriginCommand
```

### Error Handling Pattern
```kotlin
// Comprehensive exception handling with position information
throw ParseException(
    message = "Expected numeric parameter for coordinate",
    position = currentToken.position,
    token = currentToken
)
```

---

# QUALITY ASSURANCE GUIDE

## Validation & Testing

### Quality Checks (Run in Order)
```bash
# 1. Code Style & Analysis - Fix ALL errors before proceeding
./gradlew ktlintFormat               # Auto-fix code formatting
./gradlew detekt                     # Run static analysis

# 2. Unit & Feature Tests - Must pass with 80%+ coverage
./gradlew test --tests "*Benchmark*" # Run benchmark-specific tests
./gradlew test                       # Run all tests
./gradlew jacocoTestReport           # Generate coverage report

# 3. Performance Validation - Measure and verify performance
./gradlew benchmark                  # Run comprehensive benchmarks
./gradlew benchmarkQuick            # Quick benchmark profile

# 4. Build Verification
./gradlew check                      # Run ALL verification tasks
./gradlew clean build               # Clean build verification
./gradlew run                        # Test demo application
```

### Testing Strategy (Kotest StringSpec Approach)

**Performance Benchmark Testing:**
- **Command Benchmarks**: Individual ZPL command performance measurement
- **E2E Benchmarks**: Complete ZPL label parsing workflows
- **Memory Benchmarks**: Memory allocation and GC overhead measurement
- **Baseline Comparison**: Performance regression detection and warnings

### Benchmark Testing Pattern
```kotlin
class BenchmarkSystemTest : StringSpec({

    "should execute command benchmarks successfully" {
        val benchmarkResult = runBenchmark(CommandBenchmarks::class)
        benchmarkResult.results shouldNotBeEmpty()
        benchmarkResult.results.forEach { (command, result) ->
            result.averageTimeNs shouldBeLessThan 1_000_000 // < 1ms
        }
    }

    "should detect performance regression" {
        val currentResults = runBenchmark(CommandBenchmarks::class)
        val baseline = loadBaseline()
        val comparison = BaselineComparison.compare(currentResults, baseline)
        
        if (comparison.hasRegression) {
            println("PERFORMANCE WARNING: ${comparison.regressionDetails}")
        }
        // Should warn but not fail build
    }
})
```

### Performance Benchmark Pattern
```kotlin
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class CommandBenchmarks {

    @Benchmark
    fun benchmarkFieldOriginCommand(): ZplProgram {
        val lexer = Lexer("^FO100,50")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkFieldDataCommand(): ZplProgram {
        val lexer = Lexer("^FDHello World")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkCompleteLabel(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDHello World^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }
}
```

### Memory Benchmark Pattern
```kotlin
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class MemoryBenchmarks {

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = ["-XX:+UnlockDiagnosticVMOptions", "-XX:+LogVMOutput"])
    fun measureMemoryAllocation(): ZplProgram {
        val lexer = Lexer(BenchmarkData.COMPLEX_LABELS.random())
        return ZplParser(lexer.tokenize()).parse()
    }
}
```

## ZPL Parser Guidelines

### ✅ Required Patterns
- Write tests FIRST (TDD with 80% coverage minimum)
- Use Kotest StringSpec with descriptive test names
- Follow sealed class hierarchy for AST nodes
- Implement visitor pattern for all new AST nodes
- Include position information in all ParseExceptions
- Use context-aware lexing for field data vs commands
- Optimize for performance (avoid object allocations in hot paths)
- **NEW**: Add performance benchmarks for new commands
- **NEW**: Verify performance thresholds in benchmarks

### ❌ Anti-Patterns to Avoid  
- Don't create mutable AST nodes (use immutable data classes)
- Don't skip error position tracking in ParseExceptions
- Don't use regex for performance-critical parsing paths
- Don't forget visitor pattern implementation in AST nodes
- Don't skip context-aware lexing for special syntax
- Don't create multiple passes over input (single-pass parsing)
- Don't ignore performance optimization guidelines
- **NEW**: Don't skip benchmark implementation for performance-critical code
- **NEW**: Don't ignore performance regression warnings

### Performance Requirements
- **Simple Commands** (^XA, ^XZ): <0.1ms average execution time
- **Complex Commands** (^FO, ^FD, ^A): <1ms average execution time
- **Memory Allocation**: Minimize object creation per parse operation
- **Throughput**: Support high-frequency parsing for internet transfer scenarios
- **Regression Detection**: >10% performance degradation triggers warnings

### Coverage Requirements
- **Core parsing logic**: 100% (lexer, parser, AST generation)
- **Command implementations**: 95%+ (all supported ZPL commands)
- **Error handling**: 100% (exception scenarios)
- **Utilities**: 90%+ (AstPrinter, helper functions)
- **Benchmarks**: 90%+ (benchmark execution, baseline comparison)
- **Overall minimum**: 80%

### Final Checklist
- [ ] All tests pass: `./gradlew test`
- [ ] No formatting errors: `./gradlew ktlintFormat`
- [ ] No static analysis errors: `./gradlew detekt`  
- [ ] New AST nodes implement visitor pattern
- [ ] Parser integrates with main parse() method
- [ ] Context-aware lexing handles new syntax
- [ ] 80%+ code coverage maintained
- [ ] Performance optimization guidelines followed
- [ ] **NEW**: Performance benchmarks implemented and passing
- [ ] **NEW**: Baseline comparison system operational
- [ ] **NEW**: Performance thresholds met (<0.1ms simple, <1ms complex)
- [ ] **NEW**: HTML benchmark reports generated
- [ ] ZPL command reference updated in `data/zpl/` if needed

### Post-Implementation Review

#### Project Documentation Review
- [ ] **Review implementation changes** - Identify new patterns, optimizations, or architectural insights discovered during development
- [ ] **Update CLAUDE.md if needed** - Add benchmarking workflow, performance requirements, quality gates
- [ ] **Verify CLAUDE.md efficiency** - Ensure the documentation remains well-organized and efficient for future AI agent consumption
- [ ] **Document lessons learned** - Update relevant sections with performance optimization insights, benchmarking best practices

#### PRP Template Review
- [ ] **Evaluate PRP base template effectiveness** - Was the benchmarking section helpful? Were performance validation gates clear?
- [ ] **Update Foundation Reference section** - Add performance benchmarking patterns, memory optimization techniques
- [ ] **Refine task templates** - Improve benchmark implementation flow and validation sequences
- [ ] **Enhance testing examples** - Update with benchmarking patterns and performance testing approaches
- [ ] **Update Known Codebase Patterns & Gotchas** - Add kotlinx-benchmark configuration insights, JMH optimization patterns
- [ ] **Review Quality Assurance checklist** - Add performance validation steps that proved critical during implementation