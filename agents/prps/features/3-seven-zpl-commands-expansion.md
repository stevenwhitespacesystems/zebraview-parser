# PRP: Implement 7 Additional ZPL Commands (FX, CF, GB, FR, FS, BY, BC)

## Goal
Expand the ZPL parser to support 7 new commands: FX (comment), CF (change font), GB (graphic box), FR (field reverse), FS (field separator), BY (barcode field default), and BC (Code 128 barcode). This enhancement will significantly expand parser capabilities for handling complex ZPL labels including comments, graphics, and barcode generation while maintaining full backward compatibility and performance requirements.

## Why
- **Expanded ZPL Coverage**: Moves from 5 basic commands to 12 comprehensive commands, covering text, graphics, and barcode use cases
- **Production Readiness**: Adds critical structural commands (^FS) and configuration commands (^CF, ^BY) essential for real-world ZPL labels
- **Barcode Support**: Introduces foundational barcode infrastructure with Code 128 implementation for complex label workflows
- **Parser Completeness**: Establishes patterns for future ZPL command additions with comprehensive validation and testing frameworks

## What
Implement 7 new ZPL commands with full AST support, parser logic, comprehensive testing, and performance benchmarks:

### Success Criteria
- [ ] **Command Implementation**: All 7 AST nodes with proper visitor pattern integration
- [ ] **Parser Integration**: Lexer recognition and parser logic for all new commands
- [ ] **Test Coverage**: 80%+ coverage with Kotest StringSpec for each command (individual + E2E)
- [ ] **Performance Validation**: <0.1ms simple commands, <1ms complex commands, no regression
- [ ] **Backward Compatibility**: All existing ZPL programs parse identically
- [ ] **Quality Gates**: All checks pass (`./gradlew check`, benchmarks, coverage)

## Context & Documentation

### Required Reading
```yaml
# EXISTING COMMAND PATTERNS - Critical for consistent implementation
- file: src/main/kotlin/com/whitespacesystems/parser/ast/FieldOriginCommand.kt
  why: Complex parameter validation pattern (coordinates with range checking)
  
- file: src/main/kotlin/com/whitespacesystems/parser/ast/FontCommand.kt
  why: Multiple optional parameter handling with default values
  
- file: src/main/kotlin/com/whitespacesystems/parser/ast/FieldDataCommand.kt  
  why: String parameter handling and validation
  
- file: src/main/kotlin/com/whitespacesystems/parser/ast/StartFormatCommand.kt
  why: Simple parameterless command pattern
  
- file: src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt
  why: Base sealed class and visitor pattern implementation
  
# PARSER & LEXER PATTERNS
- file: src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt
  why: Command recognition, token consumption, error handling patterns
  
- file: src/main/kotlin/com/whitespacesystems/parser/lexer/Lexer.kt
  why: Command tokenization and recognition logic
  
# TESTING PATTERNS - Essential for 80% coverage requirement
- file: src/test/kotlin/com/whitespacesystems/parser/parser/FieldOriginCommandTest.kt
  why: Kotest StringSpec structure for parameter validation testing
  
- file: src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt
  why: End-to-end testing patterns for complete ZPL programs
  
# PERFORMANCE BENCHMARKING - Mandatory for new commands
- file: src/benchmark/kotlin/com/whitespacesystems/parser/CommandBenchmarks.kt
  why: Individual command benchmark patterns with @Benchmark annotations
  
- file: src/benchmark/kotlin/com/whitespacesystems/parser/E2EBenchmarks.kt
  why: Complete label parsing performance patterns

# ZPL COMMAND SPECIFICATIONS - Implementation authority
- file: data/zpl/FX.md, data/zpl/CF.md, data/zpl/GB.md, data/zpl/FR.md, data/zpl/FS.md, data/zpl/BY.md, data/zpl/BC.md
  why: Authoritative syntax, parameter validation, and behavior specifications
```

### Current Codebase Structure
```bash
src/main/kotlin/com/whitespacesystems/parser/
├── ast/                                 # 5 existing AST command nodes
│   ├── ZplNode.kt                      # Base sealed class + visitor interface
│   ├── ZplProgram.kt                   # Root program node
│   ├── StartFormatCommand.kt           # Simple ^XA command
│   ├── EndFormatCommand.kt             # Simple ^XZ command  
│   ├── FieldOriginCommand.kt           # Complex ^FO(x,y) command
│   ├── FieldDataCommand.kt             # String ^FD command
│   └── FontCommand.kt                  # Complex ^A font variants
├── lexer/
│   ├── Lexer.kt                        # Context-aware tokenization
│   └── Token.kt                        # Token definitions
├── parser/
│   └── ZplParser.kt                    # Recursive descent parser
└── utils/
    └── AstPrinter.kt                   # Visitor pattern implementation

src/benchmark/kotlin/com/whitespacesystems/parser/
├── CommandBenchmarks.kt                # Individual command benchmarks
├── E2EBenchmarks.kt                   # Complete parsing workflows
└── data/BenchmarkData.kt              # Test data generation
```

### Desired Structure
```bash
# 7 NEW AST NODES TO ADD
src/main/kotlin/com/whitespacesystems/parser/ast/
├── CommentCommand.kt                   # ^FX - Simple string parameter
├── ChangeFontCommand.kt                # ^CF - Font configuration (3 params)  
├── GraphicBoxCommand.kt                # ^GB - Graphics (5 complex params)
├── FieldReverseCommand.kt              # ^FR - Simple parameterless
├── FieldSeparatorCommand.kt            # ^FS - Simple parameterless
├── BarCodeDefaultCommand.kt            # ^BY - Barcode config (3 complex params)
└── Code128Command.kt                   # ^BC - Very complex barcode (6 params)

# 7 NEW TEST FILES TO ADD
src/test/kotlin/com/whitespacesystems/parser/parser/
├── CommentCommandTest.kt
├── ChangeFontCommandTest.kt
├── GraphicBoxCommandTest.kt  
├── FieldReverseCommandTest.kt
├── FieldSeparatorCommandTest.kt
├── BarCodeDefaultCommandTest.kt
└── Code128CommandTest.kt

# PERFORMANCE BENCHMARKS TO ADD
src/benchmark/kotlin/com/whitespacesystems/parser/
└── NewCommandsBenchmarks.kt            # Benchmarks for all 7 commands

# PARSER UPDATES
├── parser/ZplParser.kt                 # Add 7 new parseXCommand() methods
├── lexer/Lexer.kt                      # Add command recognition logic
└── utils/AstPrinter.kt                 # Add 7 visitor method implementations
```

### Known Codebase Patterns & Gotchas
```kotlin
// CRITICAL: Project-specific patterns from ZPL parser codebase

// 1. AST Node Pattern - All commands extend sealed class
data class NewCommand(
    val param1: Int,
    val param2: String = "default"
) : ZplNode() {
    init {
        require(param1 > 0) { "Parameter validation with position info" }
    }
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitNewCommand(this)
}

// 2. Parser Integration - Token consumption pattern
private fun parseNewCommand(): NewCommand {
    expect(TokenType.COMMAND, "FX") // Command recognition
    val param1 = expect(TokenType.NUMBER).value.toInt()
    expect(TokenType.COMMA)
    val param2 = expectOptionalString() ?: "default"
    return NewCommand(param1, param2)
}

// 3. Visitor Pattern - Must update interface and implementations
interface ZplNodeVisitor<T> {
    fun visitNewCommand(command: NewCommand): T
    // ... existing methods
}

// 4. Error Handling with Position - All ParseExceptions need position
throw ParseException(
    "Expected parameter for ${commandName}", 
    currentToken.position
)

// 5. Performance Critical - Context-aware lexing for ^FS
// ^FS terminates field data mode - affects tokenization
if (commandName == "FS") {
    fieldDataMode = false
}

// 6. Complex Validation - Parameter dependencies (e.g., ^GB width depends on thickness)
init {
    require(width >= thickness) { 
        "Width ($width) must be >= thickness ($thickness)" 
    }
}

// 7. Kotest Testing Structure - Descriptive test names
class NewCommandTest : StringSpec({
    "should parse command with valid parameters" {
        val lexer = Lexer("^FX100,test")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        program.commands.size shouldBe 1
        val command = program.commands[0].shouldBeInstanceOf<CommentCommand>()
        command.text shouldBe "100,test"
    }
})
```

## Implementation Blueprint

### Command Classification & Complexity Analysis
```kotlin
// SIMPLE COMMANDS (No parameters) - Target: <0.1ms each
class FieldReverseCommand : ZplNode()     // ^FR - Flag command
class FieldSeparatorCommand : ZplNode()   // ^FS - Critical structural

// SIMPLE WITH STRING PARAMETER - Target: <0.1ms  
data class CommentCommand(
    val text: String
) : ZplNode()                             // ^FX - Documentation

// MODERATE COMPLEXITY - Target: <1ms
data class ChangeFontCommand(
    val font: Char = 'A',                 // A-Z, 0-9 validation
    val height: Int = 9,                  // 0-32000 range
    val width: Int = 5                    // 0-32000 range  
) : ZplNode()                             // ^CF - Font configuration

// COMPLEX COMMANDS - Target: <1ms each
data class GraphicBoxCommand(
    val width: Int,                       // Dependent on thickness
    val height: Int,                      // Dependent on thickness
    val thickness: Int = 1,               // 1-32000
    val color: Char = 'B',               // 'B' or 'W' validation
    val rounding: Int = 0                 // 0-8 range
) : ZplNode()                             // ^GB - Graphics

data class BarCodeDefaultCommand(
    val moduleWidth: Int = 2,             // 1-10 dots
    val widthRatio: Double = 3.0,         // 2.0-3.0, 0.1 increments
    val height: Int = 10                  // Default height
) : ZplNode()                             // ^BY - Barcode config

// VERY COMPLEX COMMAND - Target: <1ms with optimization
data class Code128Command(
    val orientation: Char = 'N',          // N/R/I/B enum
    val height: Int? = null,              // 1-32000 or ^BY value
    val printInterpretation: Boolean = true,      // Y/N
    val printInterpretationAbove: Boolean = false, // Y/N  
    val uccCheckDigit: Boolean = false,           // Y/N
    val mode: Char = 'N'                  // N/U/A/D modes
) : ZplNode()                             // ^BC - Code 128 barcode
```

### Task List (in execution order)
```yaml
# PHASE 1: SIMPLE COMMANDS (Build confidence, establish patterns)
Task 1: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/FieldReverseCommand.kt
  - PATTERN: Follow StartFormatCommand.kt (simple parameterless)
  - IMPLEMENTATION: object or simple class extending ZplNode()
  - VERIFY: Visitor pattern method added to interface

Task 2: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/FieldSeparatorCommand.kt  
  - PATTERN: Follow StartFormatCommand.kt pattern
  - CRITICAL: This is structural command - terminates field data mode in lexer
  - VERIFY: Lexer integration documented for fieldDataMode handling

Task 3: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/CommentCommand.kt
  - PATTERN: Follow FieldDataCommand.kt for string handling
  - IMPLEMENTATION: Single text parameter, no validation needed
  - VERIFY: String handling for comment text until next ^ command

# PHASE 2: PARSER INTEGRATION (All 7 commands)
Task 4: UPDATE src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt
  - ADD: 7 new visitor methods to ZplNodeVisitor interface
  - PATTERN: Follow existing visitFieldOrigin, visitFieldData structure
  - VERIFY: All visitor methods properly declared with return type T

Task 5: UPDATE src/main/kotlin/com/whitespacesystems/parser/lexer/Lexer.kt
  - ADD: Recognition logic for FX, CF, GB, FR, FS, BY, BC commands  
  - CRITICAL: ^FS handling to exit field data mode
  - VERIFY: All command tokens properly recognized

Task 6: UPDATE src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt
  - ADD: 7 new parse methods: parseCommentCommand(), parseChangeFontCommand(), etc.
  - ADD: Command recognition in main parseCommand() when statement
  - PATTERN: Follow parseFieldOriginCommand() structure for parameter handling
  - VERIFY: Proper token consumption and error handling for each command

# PHASE 3: MODERATE COMPLEXITY COMMANDS  
Task 7: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/ChangeFontCommand.kt
  - PATTERN: Follow FontCommand.kt for multiple optional parameters
  - VALIDATION: Font character A-Z/0-9, height/width 0-32000 range
  - IMPLEMENTATION: Default values, require validation in init block
  - VERIFY: Parameter validation with descriptive error messages

# PHASE 4: COMPLEX COMMANDS (Advanced validation patterns)
Task 8: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/GraphicBoxCommand.kt
  - PATTERN: Follow FieldOriginCommand.kt for complex parameter validation
  - VALIDATION: Width/height >= thickness, color B/W enum, rounding 0-8
  - IMPLEMENTATION: Parameter dependencies in init block
  - VERIFY: Complex validation logic with clear error messages

Task 9: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/BarCodeDefaultCommand.kt
  - PATTERN: Custom validation for decimal ratio precision
  - VALIDATION: Module width 1-10, ratio 2.0-3.0 with 0.1 increments
  - IMPLEMENTATION: Decimal handling and precision validation
  - VERIFY: Ratio calculation logic and lookup table integration

# PHASE 5: VERY COMPLEX COMMAND (Comprehensive barcode implementation)  
Task 10: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/Code128Command.kt
  - PATTERN: Most complex validation combining multiple patterns
  - VALIDATION: Multiple enums, boolean conversions, mode dependencies
  - IMPLEMENTATION: Extensive parameter validation and mode handling
  - CRITICAL: This is the most complex - optimize for performance
  - VERIFY: All 6 parameters properly validated with error handling

# PHASE 6: VISITOR PATTERN COMPLETION
Task 11: UPDATE src/main/kotlin/com/whitespacesystems/parser/utils/AstPrinter.kt
  - ADD: 7 visitor implementation methods  
  - PATTERN: Follow existing visitFieldOrigin format
  - VERIFY: Pretty printing for all new command types

# PHASE 7: COMPREHENSIVE TESTING (80% coverage requirement)
Task 12: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/CommentCommandTest.kt
  - PATTERN: Follow existing test structure with Kotest StringSpec
  - TEST CASES: Basic parsing, empty comments, special characters
  - VERIFY: String handling edge cases covered

Task 13: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/FieldReverseCommandTest.kt
Task 14: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/FieldSeparatorCommandTest.kt
  - PATTERN: Simple command testing (no parameters)
  - TEST CASES: Basic parsing, integration with other commands
  - VERIFY: Structural behavior tested

Task 15: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/ChangeFontCommandTest.kt
  - PATTERN: Follow FontCommand.kt test structure
  - TEST CASES: Valid parameters, default values, validation failures
  - VERIFY: All parameter combinations and error scenarios

Task 16: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/GraphicBoxCommandTest.kt
  - PATTERN: Complex parameter validation testing
  - TEST CASES: Valid combinations, dependency failures, range violations
  - VERIFY: All validation rules properly tested

Task 17: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/BarCodeDefaultCommandTest.kt
  - PATTERN: Decimal precision and ratio testing
  - TEST CASES: Valid ratios, precision boundaries, calculation logic
  - VERIFY: Decimal handling edge cases covered

Task 18: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/Code128CommandTest.kt
  - PATTERN: Most comprehensive test suite
  - TEST CASES: All 6 parameters, mode combinations, enum validations
  - VERIFY: Complete coverage of barcode functionality

Task 19: UPDATE src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt
  - ADD: E2E scenarios with new commands in complete ZPL programs
  - TEST CASES: Simple labels, complex labels, mixed command usage
  - VERIFY: Integration between new and existing commands

# PHASE 8: PERFORMANCE BENCHMARKING (MANDATORY - No exceptions)
Task 20: CREATE src/benchmark/kotlin/com/whitespacesystems/parser/NewCommandsBenchmarks.kt
  - PATTERN: Follow CommandBenchmarks.kt @Benchmark annotation structure
  - BENCHMARKS: Individual command parsing + complete label scenarios
  - TARGET THRESHOLDS: <0.1ms simple commands, <1ms complex commands
  - VERIFY: All 7 commands meet performance requirements

Task 21: UPDATE src/benchmark/kotlin/com/whitespacesystems/parser/E2EBenchmarks.kt
  - ADD: Complete ZPL labels featuring new commands
  - SCENARIOS: Real-world label examples with graphics and barcodes
  - VERIFY: End-to-end parsing performance maintained

Task 22: VALIDATE no performance regression (CRITICAL GATE)
  - RUN: ./gradlew benchmark
  - CHECK: BaselineComparison output for >10% degradation warnings  
  - VERIFY: No regression in existing commands (^XA, ^XZ, ^FO, ^FD, ^A)
  - IF REGRESSION: STOP and create regression fix PRP before completion

Task 23: UPDATE baseline performance data
  - RUN: BaselineComparison utilities to update baseline.json
  - RECORD: New performance measurements for 7 commands
  - VERIFY: Baseline storage and regression detection working

# PHASE 9: QUALITY GATES & INTEGRATION
Task 24: RUN comprehensive quality checks
  - EXECUTE: ./gradlew ktlintFormat (fix formatting)
  - EXECUTE: ./gradlew detekt (static analysis)
  - EXECUTE: ./gradlew test (all tests pass)
  - EXECUTE: ./gradlew jacocoTestReport (80%+ coverage verification)
  - VERIFY: All quality gates pass before completion

Task 25: FINAL integration verification
  - EXECUTE: ./gradlew clean build (clean build success)
  - EXECUTE: ./gradlew run (demo application with new commands)
  - TEST: Manual verification that new commands work in complete workflows
  - VERIFY: Integration success and no breaking changes
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

---

# FOUNDATION REFERENCE
**MANDATORY CONTEXT FOR ALL ZPL PARSER IMPLEMENTATIONS**

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
- Write tests FIRST (TDD with 80% coverage minimum)
- Use Kotest StringSpec with descriptive test names
- Follow sealed class hierarchy for AST nodes
- Implement visitor pattern for all new AST nodes
- Include position information in all ParseExceptions
- Use context-aware lexing for field data vs commands
- Optimize for performance (avoid object allocations in hot paths)
- **NEW**: Add performance benchmarks for new ZPL commands
- **NEW**: Verify performance thresholds (<0.1ms simple, <1ms complex)
- **NEW**: Use `./gradlew check` for comprehensive validation

### ❌ Anti-Patterns to Avoid  
- Don't create mutable AST nodes (use immutable data classes)
- Don't skip error position tracking in ParseExceptions
- Don't use regex for performance-critical parsing paths
- Don't forget visitor pattern implementation in AST nodes
- Don't skip context-aware lexing for special syntax
- Don't create multiple passes over input (single-pass parsing)
- Don't ignore performance optimization guidelines
- **NEW**: Don't skip performance benchmarks for new commands
- **NEW**: Don't ignore performance regression warnings
- **NEW**: Don't skip `./gradlew check` validation before committing

### Coverage Requirements
- **Core parsing logic**: 100% (lexer, parser, AST generation)
- **Command implementations**: 95%+ (all supported ZPL commands)
- **Error handling**: 100% (exception scenarios)
- **Utilities**: 90%+ (AstPrinter, helper functions)
- **Overall minimum**: 80%

### Final Checklist
- [ ] **Quality Gates Pass**: `./gradlew check` succeeds (includes ktlint, detekt, tests)
- [ ] **Code Coverage**: 80%+ maintained via `./gradlew jacocoTestReport`
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
  - [ ] Context-aware lexing handles new syntax
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

---

# PRP IMPLEMENTATION CONFIDENCE SCORE: 9/10

**Strengths:**
- Comprehensive analysis of existing codebase patterns and all 7 command specifications
- Detailed task breakdown with clear implementation order (simple → complex)
- Complete foundation reference with all architectural patterns
- Extensive testing requirements with Kotest examples
- Mandatory performance benchmarking with regression detection
- Clear validation gates and quality assurance checklist

**Risk Factors:**
- ^BC (Code 128) command is very complex with extensive validation logic
- Performance regression handling requires careful monitoring
- Integration of 7 commands simultaneously may create unexpected interactions

**Mitigation Strategies:**
- Phased implementation starting with simple commands to build confidence
- Comprehensive testing at each phase before moving to more complex commands
- Performance monitoring integrated throughout implementation process
- Clear regression fix workflow if performance issues arise

This PRP provides sufficient context and structure for one-pass implementation success through systematic progression and comprehensive validation.