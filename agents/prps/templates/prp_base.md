# PRP Template - AI Implementation Guide

## Purpose
Template optimized for AI agents to implement features with sufficient context and self-validation capabilities to achieve working code through iterative refinement.

## Core Principles
1. **Context is King**: Include ALL necessary documentation, examples, and caveats
2. **Validation Loops**: Provide executable tests/lints the AI can run and fix
3. **Information Dense**: Use keywords and patterns from the codebase
4. **Progressive Success**: Start simple, validate, then enhance
5. **Global Rules**: Be sure to follow all rules in CLAUDE.md

## 🚨 CRITICAL: ALWAYS Include Complete Foundation Section
**When creating a PRP, you MUST copy the entire "FOUNDATION REFERENCE" section from this template into your PRP. Do NOT leave it blank or summarize it. The Foundation section contains essential architecture patterns, performance optimization guidelines, Kotest testing patterns, and Kotlin coding standards that are critical for proper ZPL parser implementation.**

---

# PRP CREATION TEMPLATE

## Goal
[What needs to be built - be specific about the end state and desires]

## Why
- [Business value and user impact]
- [Integration with existing features]
- [Problems this solves and for whom]

## What
[User-visible behavior and technical requirements]

### Success Criteria
- [ ] [Specific measurable outcomes]

## Context & Documentation

### Required Reading
```yaml
# MUST READ - Include these in your context window
- file: [path/to/relevant/file.kt]
  why: [Pattern to follow, gotchas to avoid]
  
- file: [path/to/test/example.kt]
  why: [Testing patterns and structure to follow]
  
- doc: [External documentation URL if needed] 
  section: [Specific section about implementation details]
  critical: [Key insight that prevents common errors]
```

### Current Codebase Structure
```bash
# Run `tree` in the root of the project to get overview
```

### Desired Structure
```bash
# Show files to be added and responsibility of each file
```

### Known Codebase Patterns & Gotchas
```kotlin
// CRITICAL: Project-specific patterns from ZPL parser codebase

// 1. CRITICAL TOKEN PRECEDENCE - expectingFieldData MUST come first
// Bug fix: Field data starting with digits was incorrectly tokenized
when {
    expectingFieldData -> {
        // MUST be first priority - field data reads everything as string
        readString(start, startLine, startColumn)
    }
    current.isDigit() -> readNumber(start, startLine, startColumn)  // After field data check
    current.isLetter() -> readCommand(start, startLine, startColumn)
}
// Bug example: "^FD123 Main St" was tokenized as NUMBER("123") + STRING(" Main St")
// Fixed by prioritizing expectingFieldData check

// 2. Dynamic Character Support - Never hardcode ZPL characters
// From Lexer.kt: Support for CC/CD/CT commands that change syntax characters
private var caretChar: Char = '^'      // Format command prefix (changed by ^CC)
private var tildeChar: Char = '~'      // Control command prefix (changed by ^CT)
private var delimiterChar: Char = ','  // Parameter delimiter (changed by ^CD)
// Always use these variables instead of hardcoded '^', '~', ','

// 3. Command Lookup Optimization - O(1) command recognition
// From Lexer.kt: HashMap for performance instead of sequential checks
private val commandInfo = hashMapOf(
    "FD" to CommandInfo(2, true),           // Field data - has string data
    "CF" to CommandInfo(2, false, true),    // Change font - has variants (CFB, CF0)
    "BC" to CommandInfo(2, false, true)     // Code 128 - has variants (BCR, BCN)
)

// 4. Command Variant Handling - Embedded parameters in command tokens
// From ZplParser.kt: Commands like CFB, BCR, A0N have variants in the token itself
if (commandToken.value.length > 2) {
    font = commandToken.value[2]  // Extract 'B' from "CFB" command
}

// 5. Sealed class hierarchy for type-safe AST nodes
// From ZplNode.kt: All command nodes extend sealed class
sealed class ZplNode {
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}

// 6. Parser error handling with position information
// From ZplParser.kt: All exceptions include token position
throw ParseException("Expected coordinate parameter", token.position)

// 7. Context-aware lexing - Field data vs command parsing modes
// From Lexer.kt: Field data mode after ^FD commands treats everything as string until next ^
expectingFieldData = (finalCommandName == "FD") || (finalCommandName == "FX")

// 8. Performance-critical parsing - avoid object allocations in loops
// Use StringBuilder for string building, reuse collections where possible
```

## Implementation Blueprint

### Data Models
```kotlin
// Core AST nodes with sealed class hierarchy and visitor pattern needed
```

### Task List (in execution order)
```yaml
# TDD Implementation Tasks (STRICT ORDER - Tests First!)
Task 1: CREATE TEST FILE src/test/kotlin/com/whitespacesystems/parser/parser/NewCommandTest.kt
  - PATTERN: Use Kotest StringSpec with descriptive test names
  - CRITICAL: Write ALL test cases FIRST (before any implementation)
  - TESTS WILL FAIL - That's correct and expected
  - VERIFY: ./gradlew test --tests "*NewCommand*" shows failures

Task 2: RUN TESTS TO CONFIRM RED PHASE
  - RUN: ./gradlew test --tests "*NewCommand*"
  - VERIFY: All new tests are RED (failing) as expected
  - DOCUMENT: List of expected test failures in notes

Task 3: CREATE MINIMAL AST NODE src/main/kotlin/com/whitespacesystems/parser/ast/NewCommand.kt
  - PATTERN: Follow existing AST node structure (FieldOriginCommand.kt)  
  - CRITICAL: ONLY what's needed for FIRST test to pass
  - NO extra features, NO optimizations
  - VERIFY: Extends ZplNode sealed class, basic visitor pattern

Task 4: CREATE MINIMAL PARSER LOGIC in ZplParser.kt
  - ADD: parseNewCommand() method with basic parsing
  - CRITICAL: ONLY enough logic for tests to pass
  - NO complex parameter handling yet
  - VERIFY: Method integrates with main parse() switch

Task 5: VERIFY GREEN PHASE - All Tests Pass
  - RUN: ./gradlew test --tests "*NewCommand*"
  - VERIFY: All tests now pass (GREEN phase achieved)
  - DOCUMENT: Confirm transition from RED to GREEN

Task 6: REFACTOR PHASE - Optimize and Clean Up
  - NOW add full parameter parsing, error handling
  - Add performance optimizations
  - Improve code structure and naming
  - VERIFY: ./gradlew test still passes after refactoring
  - VERIFY: 80%+ coverage via ./gradlew jacocoTestReport

# Performance Validation Tasks (MANDATORY for new ZPL commands)
Task N: CREATE src/benchmark/kotlin/.../NewCommandBenchmarks.kt
  - PATTERN: Follow CommandBenchmarks.kt structure with @State and @Benchmark annotations
  - CRITICAL: Include both isolated command and E2E benchmarks
  - VERIFY: Performance <0.1ms simple commands, <1ms complex commands
  - INCLUDE: Individual command benchmark + complete label benchmark

Task N+1: VALIDATE no performance regression
  - RUN: ./gradlew benchmark
  - CHECK: BaselineComparison output for >10% degradation warnings
  - VERIFY: No regression in existing commands
  - IF REGRESSION: STOP and create regression fix PRP before completing feature

Task N+2: UPDATE baseline performance data
  - RUN: BaselineComparison utilities to update baseline.json if performance improved
  - VERIFY: New performance measurements recorded
  - DOCUMENT: Any optimization insights discovered

# Final Integration Tasks
Task FINAL-1: Complete quality gates including performance
  - RUN: ./gradlew check (includes ktlint, detekt, tests)
  - RUN: ./gradlew benchmark (performance validation)
  - VERIFY: All quality gates pass

Task FINAL: Integration verification
  - RUN: ./gradlew run (test demo application)
  - VERIFY: New command works in complete parsing workflows
```

## Performance Regression Handling Workflow

### When Performance Regression Detected (>10% degradation)

If `./gradlew benchmark` shows ANY command has degraded >10%:

1. **STOP current PRP completion** - Do not mark the feature complete
2. **Document regression details** in current PRP completion notes:
   ```
   🔍 PERFORMANCE REGRESSION DETECTED:
   - Affected command(s): [command name(s)]  
   - Performance change: [baseline] → [current] ([X%] slower)
   - Likely cause: [recent implementation changes]
   - BaselineComparison output: [paste warning messages]
   
   ⚠️ Current PRP cannot be completed until regression is resolved.
   ```

3. **Create INITIAL.md for regression fix**:
   ```markdown
   # PERFORMANCE REGRESSION DETECTED

   ## Commands Affected
   [List specific commands with >10% degradation and performance deltas]

   ## Performance Impact Analysis
   - [Command 1]: [baseline] → [current] ([X%] degradation)
   - [Command 2]: [baseline] → [current] ([Y%] degradation)
   
   ## Root Cause Investigation Required
   - Analyze recent implementation changes that may have introduced inefficiencies
   - Profile hot paths for object allocations or algorithmic complexity increases
   - Review parsing logic for unnecessary string operations or repeated calculations
   
   ## Requirements
   - Investigate root cause of performance regression
   - Implement performance optimization fixes
   - Validate fixes restore performance to within 5% of baseline
   - Update benchmarks and baseline.json with verified measurements
   - Ensure no other commands regress during optimization
   ```

4. **Execute regression fix using standard PRP workflow**:
   - Run `/prep-prp` on the populated INITIAL.md
   - Generate PRP using `/generate-prp` with regression focus
   - Execute regression fix PRP BEFORE completing original feature PRP
   - Original feature remains incomplete until performance is restored

5. **Verify regression resolution and complete original PRP**:
   ```bash
   ./gradlew benchmark
   # Confirm no >10% degradation warnings
   # Confirm affected commands within acceptable thresholds
   # Complete original PRP with performance validation passed
   ```

### Regression Fix PRP Integration

The regression fix workflow integrates seamlessly with existing PRP tools:
- **INITIAL.md**: Contains regression context and requirements
- **`/prep-prp`**: Processes regression details into structured PRP
- **`/generate-prp`**: Creates focused performance optimization plan
- **Standard PRP execution**: Follows same quality gates and validation

### Regression Fix PRP Requirements

All regression fix PRPs must include:
- Root cause analysis of what caused the degradation
- Specific optimization strategies (avoid allocations, optimize hot paths, etc.)
- Before/after performance measurements with `./gradlew benchmark`
- Verification that the fix doesn't cause other regressions
- Update to baseline.json with new verified measurements
- Integration testing with `./gradlew run` demo application

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

### Performance Benchmarking Requirements
- **Benchmark Coverage**: All new ZPL commands must have performance benchmarks
- **Performance Thresholds**: Simple commands <0.1ms, complex commands <1ms
- **Memory Efficiency**: Minimize object allocation per parsing operation
- **Regression Detection**: >10% performance degradation triggers warnings
- **Benchmark Integration**: Use kotlinx-benchmark for JVM performance measurement
- **Validation Gates**: Include `./gradlew benchmark` in quality checks when applicable

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

### TDD (MANDATORY - Write Tests FIRST!)
**YOU MUST FOLLOW RED-GREEN-REFACTOR STRICTLY:**

1. **RED PHASE - Write failing test FIRST**
   - Create test file BEFORE any implementation code exists
   - Write comprehensive test cases that WILL FAIL
   - Run tests to confirm they fail: `./gradlew test --tests "*NewFeature*"`
   - DO NOT write ANY implementation code yet

2. **GREEN PHASE - Write MINIMAL code to pass**
   - Write ONLY enough code to make tests pass
   - No extra features, no optimizations, no "nice-to-haves"
   - Run tests to confirm they pass
   - Focus on the simplest implementation that works

3. **REFACTOR PHASE - Improve code quality**
   - NOW optimize and clean up the code
   - Add performance improvements
   - Improve naming and structure
   - Tests must still pass after refactoring
   - Check coverage: `./gradlew jacocoTestReport`

**⚠️ VIOLATION OF TDD = IMMEDIATE REJECTION**
**If you write implementation before tests, STOP and start over**
**⚠️ CODE WITHOUT TESTS WILL BE REJECTED. NO EXCEPTIONS.**

### Testing Strategy: Kotest StringSpec with TDD Flow
**RED-GREEN-REFACTOR Testing Pattern:**
```kotlin
// STEP 1: Write test FIRST (before ANY implementation exists)
// This test WILL FAIL initially - that's the RED phase
class NewCommandTest : StringSpec({
    
    "should parse new command with parameters" {
        // This test WILL FAIL because NewCommand doesn't exist yet
        val lexer = Lexer("^NewCommand100,200")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<NewCommand>()
        command.param1 shouldBe 100
        command.param2 shouldBe 200
    }
    
    "should handle new command with default values" {
        val lexer = Lexer("^NewCommand")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        val command = program.commands[0] as NewCommand
        command.param1 shouldBe 0  // default value
        command.param2 shouldBe 0  // default value
    }
    
    "should throw ParseException for invalid syntax" {
        val lexer = Lexer("^NewCommandInvalid")
        val parser = ZplParser(lexer.tokenize())
        
        shouldThrow<ParseException> {
            parser.parse()
        }.message shouldContain "Expected parameter"
    }
})

// STEP 2: Run test to confirm RED phase
// ./gradlew test --tests "NewCommandTest"
// Result: Tests FAIL because NewCommand class doesn't exist

// STEP 3: Create MINIMAL implementation for GREEN phase
// Create NewCommand.kt with bare minimum to pass tests

// STEP 4: Run test to confirm GREEN phase  
// ./gradlew test --tests "NewCommandTest"
// Result: Tests PASS

// STEP 5: REFACTOR - Now optimize and improve the implementation
// Add better error handling, performance improvements, etc.
// Tests must still pass after refactoring
```

### Essential Development Commands
```bash
./gradlew build                     # Build the project
./gradlew run                       # Run demo application
./gradlew test                      # Run all tests
./gradlew ktlintFormat             # Auto-fix code formatting
./gradlew detekt                   # Run static analysis
./gradlew jacocoTestReport         # Generate coverage report
./gradlew check                    # Run ALL verification tasks (recommended)
./gradlew benchmark                # Run performance benchmarks (when applicable)
./gradlew benchmarkQuick           # Quick benchmark profile (when applicable)
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
# RECOMMENDED: Single command runs all verification tasks
./gradlew check                      # Runs ktlint, detekt, tests, coverage - ALL quality gates

# OR: Manual step-by-step approach
# 1. Code Style & Analysis - Fix ALL errors before proceeding
./gradlew ktlintFormat               # Auto-fix code formatting
./gradlew detekt                     # Run static analysis

# 2. Unit & Feature Tests - Must pass with 80%+ coverage
./gradlew test --tests "*NewFeature*" # Run specific new tests
./gradlew test                       # Run all tests
./gradlew jacocoTestReport           # Generate coverage report

# 3. Performance Validation (when applicable)
./gradlew benchmark                  # Comprehensive performance benchmarks
./gradlew benchmarkQuick            # Quick benchmark profile for faster feedback

# 4. Build Verification
./gradlew build                      # Full build verification
./gradlew run                        # Test demo application
```

### Testing Strategy (Kotest StringSpec Approach)

**Parser Components Testing:**
- **Lexer**: Tokenization, context-aware parsing, error handling
- **Parser**: Command parsing, AST generation, syntax validation  
- **AST**: Node creation, visitor pattern, immutability
- **E2E**: Complete ZPL parsing workflows with real examples

### Lexer Testing Pattern
```kotlin
class NewLexerTest : StringSpec({

    "should tokenize new ZPL command" {
        val lexer = Lexer("^NewCommand100,200")
        val tokens = lexer.tokenize()
        
        tokens shouldHaveSize expectedSize
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "NewCommand", 1)
        tokens[2] shouldBe Token(TokenType.NUMBER, "100", 11)
        // Verify all token types and positions
    }

    "should handle new command with special characters" {
        val lexer = Lexer("^NewCommand$special,data")
        val tokens = lexer.tokenize()
        
        // Verify proper handling of special syntax
        tokens[2] shouldBe Token(TokenType.STRING, "$special,data", expectedPos)
    }
})
```

### Parser Testing Pattern
```kotlin
class NewCommandTest : StringSpec({

    "should parse new command with parameters" {
        val lexer = Lexer("^NewCommand100,200,option")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<NewCommand>()
        command.param1 shouldBe 100
        command.param2 shouldBe 200
        command.option shouldBe "option"
    }

    "should throw ParseException for invalid syntax" {
        val lexer = Lexer("^NewCommandInvalidSyntax")
        val parser = ZplParser(lexer.tokenize())
        
        shouldThrow<ParseException> {
            parser.parse()
        }.message shouldContain "Expected parameter"
    }
})
```

### End-to-End Testing Pattern
```kotlin
class NewFeatureE2ETest : StringSpec({

    "should parse complete ZPL with new command" {
        val zplCode = "^FO100,100^NewCommand200,300^FDTest^FS"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3
        program.commands[1].shouldBeInstanceOf<NewCommand>()
        
        // Verify AST printer integration
        val printer = AstPrinter()
        val output = printer.print(program)
        output shouldContain "NewCommand"
    }
})
```

### Performance Benchmark Testing Pattern
```kotlin
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class NewCommandBenchmarks {

    @Benchmark
    fun benchmarkNewCommand(): ZplProgram {
        val lexer = Lexer("^NewCommand100,200")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkNewCommandInCompleteLabel(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^NewCommand200,300^FDTest^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }
}
```

### Benchmark Validation Testing Pattern
```kotlin
class BenchmarkSystemTest : StringSpec({

    "should execute benchmarks within performance thresholds" {
        val benchmarkResult = runBenchmark(NewCommandBenchmarks::class)
        benchmarkResult.results shouldNotBeEmpty()
        benchmarkResult.results.forEach { (command, result) ->
            result.averageTimeNs shouldBeLessThan 1_000_000 // < 1ms threshold
        }
    }

    "should detect performance regression" {
        val currentResults = runBenchmark(NewCommandBenchmarks::class)
        val baseline = loadBaseline()
        val comparison = BaselineComparison.compare(currentResults, baseline)
        
        if (comparison.hasRegression) {
            println("PERFORMANCE WARNING: ${comparison.regressionDetails}")
        }
    }
})
```

## ZPL Parser Guidelines

### ✅ Required Patterns
- **STRICT TDD**: Write tests FIRST, confirm RED, achieve GREEN, then REFACTOR
- Use Kotest StringSpec with descriptive test names
- Follow sealed class hierarchy for AST nodes
- Implement visitor pattern for all new AST nodes
- Include position information in all ParseExceptions
- **CRITICAL**: Use expectingFieldData check BEFORE digit recognition in lexer
- Use dynamic character variables (caretChar, tildeChar, delimiterChar) - never hardcode
- Use command lookup HashMap for O(1) performance
- Handle command variants embedded in tokens (CFB, BCR, A0N)
- Optimize for performance (avoid object allocations in hot paths)
- Add performance benchmarks for new ZPL commands
- Verify performance thresholds (<0.1ms simple, <1ms complex)
- Achieve 100% test pass rate (not 99%, not 101/102)
- Use `./gradlew check` for comprehensive validation

### ❌ Anti-Patterns to Avoid  
- **NEVER write implementation before tests** (violates TDD)
- Don't accept 99% test success - must be 100%
- Don't put digit recognition before expectingFieldData check (causes tokenization bugs)
- Don't hardcode '^', '~', ',' characters (breaks CC/CD/CT command support)
- Don't create mutable AST nodes (use immutable data classes)
- Don't skip error position tracking in ParseExceptions
- Don't use regex for performance-critical parsing paths
- Don't forget visitor pattern implementation in AST nodes
- Don't skip context-aware lexing for special syntax
- Don't create multiple passes over input (single-pass parsing)
- Don't ignore performance optimization guidelines
- Don't skip performance benchmarks for new commands
- Don't ignore performance regression warnings
- Don't skip `./gradlew check` validation before committing
- Don't compromise performance when fixing bugs

### Coverage Requirements
- **Core parsing logic**: 100% (lexer, parser, AST generation)
- **Command implementations**: 95%+ (all supported ZPL commands)
- **Error handling**: 100% (exception scenarios)
- **Utilities**: 90%+ (AstPrinter, helper functions)
- **Overall minimum**: 80%

### Final Checklist
- [ ] **TDD Compliance**: RED-GREEN-REFACTOR cycle followed strictly
  - [ ] Tests written FIRST before any implementation
  - [ ] Confirmed RED phase (tests failed initially)
  - [ ] Achieved GREEN phase (minimal implementation passes tests)
  - [ ] Completed REFACTOR phase (optimized without breaking tests)
- [ ] **Quality Gates Pass**: `./gradlew check` succeeds (includes ktlint, detekt, tests)
- [ ] **Code Coverage**: 80%+ maintained via `./gradlew jacocoTestReport`
- [ ] **100% Test Pass Requirement**: ALL tests must pass (not 99%, not 101/102)
- [ ] **Performance Requirements (MANDATORY for new commands)**:
  - [ ] Performance benchmarks created in `src/benchmark/kotlin/`
  - [ ] Benchmark thresholds met (<0.1ms simple, <1ms complex)
  - [ ] No regression in existing commands (>10% triggers fix PRP)
  - [ ] `./gradlew benchmark` passes without degradation warnings
  - [ ] baseline.json updated if performance improved
- [ ] **Build Verification**: `./gradlew clean build` and `./gradlew run` succeed
- [ ] **ZPL Parser Specific**:
  - [ ] New AST nodes implement visitor pattern
  - [ ] Parser integrates with main parse() method
  - [ ] Context-aware lexing handles new syntax correctly
  - [ ] Token precedence follows expectingFieldData-first pattern
  - [ ] Dynamic character variables used (no hardcoded '^', '~', ',')
  - [ ] Performance optimization guidelines followed
  - [ ] ZPL command reference updated in `data/zpl/` if needed

### Post-Implementation Review

#### Project Documentation Review
- [ ] **Review implementation changes** - Identify new patterns, optimizations, or architectural insights discovered during development
- [ ] **Update CLAUDE.md if needed** - Add new patterns, update code examples, refine guidelines based on implementation learnings
- [ ] **Verify CLAUDE.md efficiency** - Ensure the documentation remains well-organized and efficient for future AI agent consumption
- [ ] **Document lessons learned** - Update relevant sections with any gotchas, performance insights, or improved approaches discovered

#### PRP Template Review
- [ ] **Evaluate PRP base template effectiveness** - Was the template structure helpful? Were any sections unclear or missing?
- [ ] **Update Foundation Reference section** - Add any new architectural patterns, performance optimizations, or coding standards discovered
- [ ] **Refine task templates** - Improve task breakdown structure based on actual implementation flow and dependencies
- [ ] **Enhance testing examples** - Update testing patterns with real-world examples and edge cases discovered during implementation
- [ ] **Update Known Codebase Patterns & Gotchas** - Add new insights about context-aware lexing, AST patterns, or parser optimizations
- [ ] **Review Quality Assurance checklist** - Add any validation steps that proved critical during implementation