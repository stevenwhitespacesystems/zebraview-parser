# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

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
- **TDD is mandatory**: Write tests first, 80% coverage minimum
- **Type safety**: Leverage Kotlin sealed classes for AST nodes
- **Error handling**: Comprehensive exception handling with position information
- **Code quality**: Run ktlint + detekt before committing
- **Documentation**: Reference `data/zpl/` for ZPL command specifications

### Performance Optimization Guidelines
- **Avoid string concatenation** in hot paths - use StringBuilder
- **Reuse objects** where possible - consider object pooling for tokens
- **Minimize regex usage** - use character-by-character parsing for speed
- **Profile regularly** - measure parsing performance on large ZPL files
- **Cache frequently accessed data** - command mappings, token types

## 🏗️ Architecture

The parser follows a classic lexer → parser → AST architecture optimized for ZPL parsing:

### Core Components

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
- Command nodes: `FieldOriginCommand.kt` (`^FO`), `FieldDataCommand.kt` (`^FD`), `FontCommand.kt` (`^A`)
- Visitor pattern enables extensible AST operations

**Utilities**
- `AstPrinter.kt` - Formats AST for debugging and visualization

**Data Directory**
`data/zpl/` contains ZPL command reference documentation (*.md files) - comprehensive ZPL command specifications for future parser extensions.

## 🔑 Key Design Patterns

- **Visitor Pattern** - AST traversal and operations
- **Recursive Descent Parsing** - Predictable, maintainable parsing strategy  
- **Sealed Classes** - Type-safe AST nodes with exhaustive pattern matching
- **Context-Aware Lexing** - Special handling for field data vs command contexts

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
# Required quality checks - fix all errors before committing
./gradlew ktlintFormat             # Auto-fix code formatting
./gradlew detekt                   # Run static analysis
./gradlew test                     # Run all tests
./gradlew jacocoTestReport         # Generate coverage report (80% minimum)
```

### Advanced Commands
```bash
# Advanced Testing
./gradlew test --tests "*.ClassName"        # Run specific test class
./gradlew test --tests "*.ClassName.test name"  # Run specific test method

# Advanced Code Quality  
./gradlew ktlintCheck              # Check code formatting (without auto-fix)
```

## 📁 File Organization

### Project Structure
```
src/
├── main/kotlin/com/whitespacesystems/parser/
│   ├── Main.kt                           # Demo application entry point
│   ├── ast/                              # AST node definitions
│   │   ├── ZplNode.kt                   # Base sealed class
│   │   ├── ZplProgram.kt                # Root AST node
│   │   ├── FieldOriginCommand.kt        # ^FO command node
│   │   ├── FieldDataCommand.kt          # ^FD command node
│   │   └── FontCommand.kt               # ^A command variants
│   ├── lexer/                           # Lexical analysis
│   │   ├── Lexer.kt                     # Main tokenizer
│   │   └── Token.kt                     # Token definitions
│   ├── parser/                          # Syntax analysis
│   │   └── ZplParser.kt                 # Recursive descent parser
│   └── utils/                           # Utilities
│       └── AstPrinter.kt               # AST debugging output
└── test/kotlin/com/whitespacesystems/parser/
    ├── ErrorHandlingTest.kt             # Parser error scenarios
    ├── lexer/
    │   └── LexerTest.kt                 # Tokenizer tests
    ├── parser/                          # Parser component tests
    │   ├── FieldDataCommandTest.kt
    │   ├── FieldOriginCommandTest.kt
    │   ├── FontCommandTest.kt
    │   └── ZplParserE2ETest.kt          # End-to-end workflows
    └── utils/
        └── AstPrinterTest.kt            # Utility tests
```

### File Naming Conventions
- **Source Files**: PascalCase with descriptive names (`FieldOriginCommand.kt`)
- **Test Files**: `[ComponentName]Test.kt` pattern
- **Packages**: Lowercase with clear separation (`lexer`, `parser`, `ast`, `utils`)

### Adding New ZPL Commands
1. **AST Node**: Create sealed class extending `ZplNode` in `ast/`
2. **Parser Logic**: Add command parsing in `ZplParser.kt`
3. **Tests**: Create comprehensive test file in `parser/`

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
- **Files**: Max 300 lines (parser components may exceed for grammar completeness)
- **Functions**: Max 30 lines (parsing functions may be longer for readability)
- **Classes**: Max 200 lines
- **Line length**: Max 120 characters
- **Cyclomatic complexity**: Max 10 per function

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