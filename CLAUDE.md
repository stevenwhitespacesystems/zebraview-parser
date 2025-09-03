# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 📋 AI Agent Quick Reference

### Common Tasks
- **Add new ZPL command**: AST Node → Parser Logic → Registry → Visitor → Tests → Benchmarks (8 steps) → See [Adding New ZPL Commands](#adding-new-zpl-commands)
- **Performance check**: `./gradlew benchmark` → Check BaselineComparison warnings → See [Performance Benchmarking](#performance-benchmarking-requirements)
- **Quality gates**: `./gradlew check` (runs ktlint + detekt + tests + coverage)
- **Emergency performance regression**: Stop current work → Create regression fix PRP → See [Performance Regression Detection](#performance-regression-detection--resolution)

### Critical Parsing Patterns (MUST Follow)
- **Field data precedence**: `expectingFieldData` check MUST come before `current.isDigit()` in lexer
- **Dynamic characters**: Never hardcode `'^'`, `'~'`, `','` - use `caretChar`, `tildeChar`, `delimiterChar` variables
- **O(1) command lookup**: Use HashMap-based command recognition, avoid sequential checks
- **Context-aware parsing**: Handle field data mode vs command mode correctly
- **TDD mandatory**: Write tests FIRST, confirm RED phase, achieve GREEN, then REFACTOR

### Performance Thresholds
- **Simple commands** (`^XA`, `^XZ`): < 0.1ms (100,000ns)  
- **Complex commands** (`^FO`, `^FD`, `^A`): < 1ms (1,000,000ns)
- **Coverage minimum**: 80% (non-negotiable)
- **Regression threshold**: >10% degradation triggers warnings

### Emergency Procedures
- **Test failures**: No feature complete until 100% pass rate + 80% coverage
- **Performance regression**: STOP → Document → Create fix PRP → Execute before completing original feature
- **Static analysis errors**: Fix code, NOT configuration files - no @Suppress without approval

---

## 🎯 Project Overview

**ZebraView Parser** - High-performance ZPL (Zebra Programming Language) parser and AST generator written in Kotlin. Converts ZPL commands into structured Abstract Syntax Trees for label analysis and manipulation.

### Technical Stack
- **Kotlin** with **Java 21** toolchain
- **Gradle** build system with Kotlin DSL
- **Kotest** for comprehensive testing and property-based testing
- **Detekt** static analysis with **ktlint** formatting
- **Jacoco** for test coverage reporting

### Key Features
- **Lexical Analysis** - Context-aware tokenization of ZPL syntax
- **Recursive Descent Parsing** - Predictable, maintainable parsing strategy
- **AST Generation** - Type-safe nodes with visitor pattern support
- **Error Handling** - Comprehensive syntax error reporting
- **Extensible Design** - Easy addition of new ZPL commands

## ⚠️ Critical Reminders

- **Performance is paramount**: Parser must be high-performance for large ZPL files
- **Memory efficiency**: Minimize object allocations during parsing
- **Single-pass parsing**: Avoid multiple passes over input where possible
- **Lazy evaluation**: Use sequences and lazy collections for token processing
- **Context-aware parsing**: Field data (^FD) vs command parsing modes
- **Dynamic character support**: Format commands (^), control commands (~), and delimiters (,) can be changed at runtime
- **TDD is mandatory**: Write tests first, 80% coverage minimum
- **Type safety**: Leverage Kotlin sealed classes for AST nodes
- **Error handling**: Comprehensive exception handling with position information
- **Code quality**: Run ktlint + detekt before committing
- **Documentation**: Reference `data/zpl/` for ZPL command specifications

## 🚀 Performance Requirements & Benchmarking

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
- **Cache frequently accessed data** - command mappings, token types using HashMap lookup tables
- **Benchmark new commands** - all new ZPL commands must have performance benchmarks
- **Monitor performance regression** - >10% degradation triggers warnings in CI/CD
- **Optimize token precedence** - Critical parsing checks (like field data mode) must come before general checks

### Performance Benchmarking Requirements
- **Benchmark Coverage**: All new ZPL commands must have performance benchmarks
- **Performance Thresholds**: Simple commands <0.1ms, complex commands <1ms
- **Memory Efficiency**: Minimize object allocation per parsing operation
- **Regression Detection**: >10% performance degradation triggers warnings
- **Benchmark Integration**: Use kotlinx-benchmark for JVM performance measurement
- **Validation Gates**: Include `./gradlew benchmark` in quality checks when applicable

### Benchmarking Commands
```bash
# COMPREHENSIVE BENCHMARKING - Measure all aspects of ZPL parser performance
./gradlew benchmark                # Run complete performance benchmark suite
./gradlew benchmarkQuick          # Quick command benchmark profile (faster feedback)
./gradlew generateBenchmarkReport # Generate HTML performance reports

# Performance validation integrated with quality gates (non-blocking)
./gradlew check                   # Includes benchmark validation warnings
```

### Performance Regression Detection & Resolution

When implementing ANY new feature or command, follow this systematic approach:

#### 1. Pre-Implementation Baseline
```bash
./gradlew benchmark
# Record baseline performance for comparison
```

#### 2. Post-Implementation Validation
```bash
./gradlew benchmark
# Check BaselineComparison output for regression warnings
```

#### 3. If Regression Detected (>10% degradation)
**IMMEDIATE ACTIONS:**
- **Document regression details**: Affected command(s), performance change, likely cause
- **Create regression fix PRP**: Generate focused PRP for performance restoration
- **Execute fix BEFORE considering original feature complete**

**Performance Regression Policy:**
- >10% degradation triggers mandatory fix PRP
- No feature is complete until performance is restored
- Regressions must be fixed before original PRP completion
- All fixes must be validated with benchmarks

## 🏗️ Architecture

The parser follows a classic lexer → parser → AST architecture optimized for ZPL parsing:

### Core Components

**Lexer** (`src/main/kotlin/com/whitespacesystems/parser/lexer/`)
- `Lexer.kt` - High-performance tokenizer with context-aware parsing
- `Token.kt` - Token types and data structures
- `LexerUtils.kt` - Lexical analysis utility functions
- `CommandRecognitionUtils.kt` - O(1) command lookup optimization
- Handles ZPL-specific syntax: `^` commands, `~` control commands, comma-separated parameters
- Context-aware parsing (field data mode after `^FD` commands)
- **Dynamic Character Tracking** - Supports changeable format characters via CC/CD/CT commands

**Parser** (`src/main/kotlin/com/whitespacesystems/parser/parser/`)
- `ZplParser.kt` - Main recursive descent parser that converts tokens to AST
- Modular parsing utilities: `BasicCommandParsingUtils.kt`, `ParameterParsingUtils.kt`, `BarCodeParsingUtils.kt`
- `CommandParserRegistry.kt` - Extensible command parser registration system
- Follows grammar: `Program → Command*` with support for all ZPL command types
- Throws `ParseException` for syntax errors with position information

**AST** (`src/main/kotlin/com/whitespacesystems/parser/ast/`)
- `ZplNode.kt` - Base sealed class with visitor pattern support
- `ZplProgram.kt` - Root AST node containing command list
- Command nodes: Sealed class hierarchy for all supported ZPL commands
- Visitor pattern enables extensible AST operations

**Utilities** (`src/main/kotlin/com/whitespacesystems/parser/utils/`)
- `AstPrinter.kt` - Main AST visualization and debugging
- `AstPrinterVisitorImpl.kt` - Visitor implementation for printing
- `AstPrinterUtils.kt` - Additional printing utilities

**Performance & Benchmarking**
- `BaselineComparison.kt` - Performance regression detection system
- Comprehensive benchmarking in `src/benchmark/kotlin/` directory
- See [Performance Requirements & Benchmarking](#-performance-requirements--benchmarking) section

**Demo Infrastructure** (`src/main/kotlin/com/whitespacesystems/parser/demo/`)
- `DemoRunner.kt` - Interactive demo with multiple ZPL parsing scenarios
- `PerformanceReporterUtils.kt` - Performance demonstration and reporting

**Data Directory**
`data/zpl/` contains ZPL command reference documentation (*.md files) - comprehensive ZPL command specifications for future parser extensions.

## 🔑 Key Design Patterns

- **Visitor Pattern** - AST traversal and operations
- **Recursive Descent Parsing** - Predictable, maintainable parsing strategy  
- **Sealed Classes** - Type-safe AST nodes with exhaustive pattern matching
- **Context-Aware Lexing** - Special handling for field data vs command contexts
- **Dynamic Character Tracking** - Runtime support for changeable ZPL characters (CC/CD/CT commands)
- **Command Lookup Optimization** - O(1) command recognition using HashMap for performance

### 🚨 Critical Parsing Patterns

#### Dynamic Character Support
```kotlin
// ZPL allows runtime changes to format characters via CC/CD/CT commands
private var caretChar: Char = '^'      // Format command prefix (changed by ^CC)
private var tildeChar: Char = '~'      // Control command prefix (changed by ^CT) 
private var delimiterChar: Char = ','  // Parameter delimiter (changed by ^CD)

// Always use variables, never hardcode characters
when (current == caretChar || current == tildeChar) {
    // Handle command prefix
}
```

#### Command Lookup Optimization
```kotlin
// O(1) command recognition using HashMap instead of sequential checks
private val commandInfo = hashMapOf(
    "FD" to CommandInfo(2, true),           // Field data - has string data
    "CF" to CommandInfo(2, false, true),    // Change font - has variants (CFB, CF0)
    "BC" to CommandInfo(2, false, true)     // Code 128 - has variants (BCR, BCN)
)

private data class CommandInfo(
    val minLength: Int,
    val hasStringData: Boolean,
    val hasVariants: Boolean = false
)
```

#### Critical Token Precedence
```kotlin
// CRITICAL: expectingFieldData check MUST come before digit recognition
// Bug example: "^FD123 Main St" incorrectly tokenized as NUMBER("123") + STRING(" Main St")
when {
    expectingFieldData -> {
        // Field data always reads as string regardless of first character
        readString(start, startLine, startColumn)  // MUST be first priority
    }
    current.isDigit() -> readNumber(start, startLine, startColumn)  // After field data check
    current.isLetter() -> readCommand(start, startLine, startColumn)
    // ... other token types
}
```

#### Command Variant Handling
```kotlin
// Commands like CF, BC, A support embedded variants (CFB, BCR, A0N)
when (commandName) {
    "CF" -> {
        // Check if command token contains font variant (CFB -> font='B')
        if (commandToken.value.length > 2) {
            font = commandToken.value[2]  // Extract 'B' from "CFB"
        }
    }
    "BC" -> {
        // Check if command token contains orientation (BCR -> orientation='R')
        if (commandToken.value.length > 2) {
            orientation = commandToken.value[2]  // Extract 'R' from "BCR"
        }
    }
}
```

## 🛠️ Development Environment

### Quick Start
```bash
./gradlew build                     # Build the project
./gradlew run                       # Run the main demo application  
./gradlew clean build              # Clean and rebuild
./gradlew test                     # Run all tests
```

### Code Quality (Run Before Committing)
```bash
# RECOMMENDED: Single command for all quality checks
./gradlew check                    # Runs ALL verification tasks (ktlint, detekt, tests, coverage)

# Manual approach - Required quality checks - fix all errors before committing
./gradlew ktlintFormat             # Auto-fix code formatting
./gradlew detekt                   # Run static analysis
./gradlew test                     # Run all tests
./gradlew jacocoTestReport         # Generate coverage report (80% minimum)
```

### Development Commands

See [Performance Requirements & Benchmarking](#-performance-requirements--benchmarking) section for detailed performance workflows and regression handling.

### Advanced Commands
```bash
# Advanced Testing
./gradlew test --tests "*.ClassName"        # Run specific test class
./gradlew test --tests "*.ClassName.test name"  # Run specific test method

# Advanced Code Quality  
./gradlew ktlintCheck              # Check code formatting (without auto-fix)
```

## 📁 File Organization

For detailed component structure, see the [Architecture](#-architecture) section which covers:
- **Lexer components** with utilities and command recognition
- **Parser components** with modular parsing utilities  
- **AST nodes** with sealed class hierarchy
- **Performance & Benchmarking infrastructure**
- **Demo and utility components**

### Test Organization
- **Unit Tests**: Component-specific functionality (LexerTest, individual command tests)
- **Integration Tests**: Multi-component workflows (ZplParserE2ETest)  
- **Error Tests**: Exception and error handling (ErrorHandlingTest)
- **Utility Tests**: Helper function testing (AstPrinterTest)
- **Benchmark Tests**: Performance validation and benchmark system tests

Test file naming: `[ComponentName]Test.kt`

### File Naming Conventions
- **Source Files**: PascalCase with descriptive names (`FieldOriginCommand.kt`)
- **Test Files**: `[ComponentName]Test.kt` pattern
- **Packages**: Lowercase with clear separation (`lexer`, `parser`, `ast`, `utils`)

### Adding New ZPL Commands
1. **AST Node**: Create sealed class extending `ZplNode` in `ast/`
2. **Parser Logic**: Add command parsing in `ZplParser.kt` or create dedicated parsing utils
3. **Parser Registration**: Register command in `CommandParserRegistry.kt` if using modular approach
4. **Visitor Pattern**: Update `ZplNodeVisitor<T>` interface with new visit method
5. **Tests**: Create comprehensive test file in `parser/` following Kotest StringSpec pattern
6. **Benchmarks**: Create performance benchmarks in `src/benchmark/kotlin/`
7. **Regression Check**: Validate no performance degradation in existing commands
8. **Baseline Update**: Update baseline.json if performance improved

## 📋 Code Standards

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

### Code Limits & Quality
- **Files**: Max 300 lines (NO EXCEPTIONS)
- **Functions**: Max 30 lines (NO EXCEPTIONS)
- **Classes**: Max 200 lines (NO EXCEPTIONS)
- **Line length**: Max 120 characters (NO EXCEPTIONS)
- **Cyclomatic complexity**: Max 10 per function (NO EXCEPTIONS)
- **ALL linting and static analysis must pass - no feature is complete until all checks pass**

### Documentation
- **Public APIs**: KDoc comments for all public classes and functions
- **Complex logic**: Inline comments explaining ZPL-specific behavior
- **Examples**: Include ZPL examples in KDoc when applicable

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

## 🚨 Testing Strategy (MANDATORY)

### Non-Negotiable Requirements
1. **Test-Driven Development** - Write tests before implementation
2. **80% minimum coverage** - Measured via Jacoco reports
3. **No feature without tests** - Everything must have comprehensive tests
4. **Property-based testing** - Use Kotest properties for edge cases

### Testing Framework: Kotest StringSpec
Uses **Kotest StringSpec** for readable, descriptive tests:

#### Lexer Testing Pattern
```kotlin
class LexerTest : StringSpec({

    "should tokenize simple FO command" {
        val lexer = Lexer("^FO100,50")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 6 // CARET, COMMAND, NUMBER, COMMA, NUMBER, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FO", 1)
        tokens[2] shouldBe Token(TokenType.NUMBER, "100", 3)
        tokens[3] shouldBe Token(TokenType.COMMA, ",", 6)
        tokens[4] shouldBe Token(TokenType.NUMBER, "50", 7)
        tokens[5] shouldBe Token(TokenType.EOF, "", 9)
    }

    "should handle FD command with comma in string data" {
        val lexer = Lexer("^FDHello, World")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 4 // CARET, COMMAND, STRING, EOF
        tokens[2] shouldBe Token(TokenType.STRING, "Hello, World", 3)
    }
})
```

#### Parser Testing Pattern  
```kotlin
class FieldOriginCommandTest : StringSpec({

    "should parse simple FO command with coordinates" {
        val lexer = Lexer("^FO100,50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 100
        command.y shouldBe 50
    }

    "should parse FO command with whitespace" {
        val lexer = Lexer("^FO 100, 50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 100
        command.y shouldBe 50
    }
})
```

#### End-to-End Testing Pattern
```kotlin
class ZplParserE2ETest : StringSpec({

    "should parse complete simple ZPL label" {
        val zplCode = "^FO100,50^A0N,30,30^FDHello World"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3

        // Check field origin command
        val foCommand = program.commands[0]
        foCommand.shouldBeInstanceOf<FieldOriginCommand>()
        foCommand.x shouldBe 100
        foCommand.y shouldBe 50

        // Check font command
        val fontCommand = program.commands[1]
        fontCommand.shouldBeInstanceOf<FontCommand>()
        fontCommand.font shouldBe '0'
        fontCommand.orientation shouldBe 'N'
        fontCommand.height shouldBe 30
        fontCommand.width shouldBe 30

        // Check field data command
        val fdCommand = program.commands[2]
        fdCommand.shouldBeInstanceOf<FieldDataCommand>()
        fdCommand.data shouldBe "Hello World"
    }

    "should generate readable AST output" {
        val zplCode = "^FO100,50^A0N,30,30^FDHello World"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        val printer = AstPrinter()
        val ast = printer.print(program)

        ast.contains("ZplProgram") shouldBe true
        ast.contains("FieldOriginCommand(x=100, y=50)") shouldBe true
        ast.contains("FontCommand(font='0', orientation='N', height=30, width=30)") shouldBe true
        ast.contains("FieldDataCommand(data=\"Hello World\")") shouldBe true
    }
})
```

### Coverage Requirements
- **Core parsing logic**: 100% (lexer, parser, AST generation)
- **Command implementations**: 95%+ (all supported ZPL commands)
- **Error handling**: 100% (exception scenarios)
- **Utilities**: 90%+ (AstPrinter, helper functions)
- **Overall minimum**: 80%

### Test Organization
- **Unit Tests**: Component-specific functionality (LexerTest, individual command tests)
- **Integration Tests**: Multi-component workflows (ZplParserE2ETest)
- **Error Tests**: Exception and error handling (ErrorHandlingTest)
- **Utility Tests**: Helper function testing (AstPrinterTest)

Test file naming: `[ComponentName]Test.kt`

## 🔄 Git Workflow

### Branches
- `main` - Production-ready code
- `feature/*` - New ZPL command implementations, enhancements
- `fix/*` - Bug fixes and error handling improvements
- `refactor/*` - Code structure improvements

### Commit Format
```
type(scope): description

- Detail 1
- Detail 2

Examples:
feat(parser): add support for ^BC barcode commands
fix(lexer): handle escaped characters in field data
test(e2e): add comprehensive ZPL label parsing scenarios
docs(readme): update installation and usage instructions
```

**Types**: feat, fix, docs, style, refactor, test, chore

### Pre-Commit Checklist
```bash
./gradlew ktlintFormat          # Fix formatting
./gradlew detekt                # Static analysis
./gradlew test                  # All tests pass
./gradlew jacocoTestReport      # Coverage ≥80%
```

## ⚙️ Configuration & Resources

### Configuration
- **Detekt**: `config/detekt.yml` - Custom rules, magic number exceptions, complexity thresholds
- **Kotlin**: Java 21 toolchain, official code style
- **Dependencies**: Kotest for testing, Detekt + Ktlint for code quality
- **Gradle**: Both detekt and ktlint are configured to run with `autoCorrect = true`

### Development Documentation
- [Kotlin Language Documentation](https://kotlinlang.org/docs/home.html)
- [Kotest Testing Framework](https://kotest.io/)
- [Gradle Build Tool](https://docs.gradle.org/)
- [Detekt Static Analysis](https://detekt.dev/)

### ZPL Documentation
- **Local ZPL Reference**: `data/zpl/` - Comprehensive ZPL command specifications
- **Supported Commands**: See individual `.md` files for command details (^FO, ^FD, ^A, etc.)

---
*Update this document as the parser evolves and new ZPL commands are added.*