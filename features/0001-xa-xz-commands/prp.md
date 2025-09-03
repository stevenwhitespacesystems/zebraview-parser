# PRP: Implement ZPL ^XA (Start Format) and ^XZ (End Format) Commands

## Goal
Add support for ^XA and ^XZ commands to the ZebraView Parser. These are fundamental ZPL commands where ^XA marks the start of a ZPL label format and ^XZ marks the end of a ZPL label format, enabling the parser to handle complete, valid ZPL II label definitions.

## Why
- **Essential ZPL II Compliance**: Valid ZPL II format requires labels to start with ^XA and end with ^XZ
- **Complete Label Parsing**: Currently the parser can handle individual commands but cannot parse complete ZPL label formats
- **Foundation for Advanced Features**: ^XA/^XZ boundary detection enables future features like label validation, format optimization, and multi-label parsing
- **Industry Standard**: These commands are universally required in ZPL printing workflows

## What
Implement two new AST command nodes that represent the start and end boundaries of ZPL label formats:

- `StartFormatCommand` for ^XA (no parameters, marks label beginning)  
- `EndFormatCommand` for ^XZ (no parameters, marks label end and triggers printing)

### Success Criteria
- [ ] Parser correctly handles ^XA at start of ZPL input
- [ ] Parser correctly handles ^XZ at end of ZPL input
- [ ] Both commands integrate with existing visitor pattern
- [ ] Comprehensive test coverage (80%+ including edge cases)
- [ ] All quality checks pass (ktlint, detekt, tests)
- [ ] End-to-end parsing of complete ZPL labels works

## Context & Documentation

### Required Reading
```yaml
# MUST READ - Include these in your context window
- file: src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt
  why: Base sealed class pattern and visitor interface that must be extended
  
- file: src/main/kotlin/com/whitespacesystems/parser/ast/FieldOriginCommand.kt  
  why: Perfect example of simple command with validation (no-parameter pattern to follow)
  
- file: src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt
  why: Command dispatch logic (lines 51-63) and parsing method patterns to follow
  
- file: src/test/kotlin/com/whitespacesystems/parser/parser/FieldOriginCommandTest.kt
  why: Kotest StringSpec testing pattern and structure to replicate
  
- file: data/zpl/XA.md
  why: ^XA command specification (no parameters, boundary marker)
  
- file: data/zpl/XZ.md  
  why: ^XZ command specification (no parameters, triggers printing)
  
- doc: https://labelary.com/zpl.html
  section: "ZPL Format Commands"
  critical: ^XA and ^XZ are essential bookends for all valid ZPL II labels
  
- doc: https://files.tracerplus.com/public/docs/3rdparty/manuals/ZPL_Vol1.pdf
  section: "Format Commands ^XA and ^XZ"  
  critical: Official Zebra documentation on start/end format boundary behavior
```

### Current Codebase Structure
```bash
src/main/kotlin/com/whitespacesystems/parser/
├── ast/
│   ├── ZplNode.kt                    # Base sealed class + visitor interface
│   ├── ZplProgram.kt                 # Root AST node
│   ├── FieldOriginCommand.kt         # Example: command with parameters
│   ├── FieldDataCommand.kt           # Example: command with string data  
│   └── FontCommand.kt                # Example: complex command parsing
├── parser/
│   └── ZplParser.kt                  # Recursive descent parser (extend parseCommand())
├── lexer/
│   ├── Lexer.kt                      # Tokenizer (already handles XA/XZ)
│   └── Token.kt                      # Token definitions
└── utils/
    └── AstPrinter.kt                 # Visitor implementation (extend for new commands)

src/test/kotlin/com/whitespacesystems/parser/
├── parser/
│   ├── FieldOriginCommandTest.kt     # Testing pattern to follow
│   ├── FieldDataCommandTest.kt       # Additional test examples
│   └── ZplParserE2ETest.kt           # End-to-end test integration point
```

### Desired Structure
```bash
# Files to ADD (following existing patterns):
src/main/kotlin/com/whitespacesystems/parser/ast/
├── StartFormatCommand.kt             # ^XA AST node (no parameters)
└── EndFormatCommand.kt               # ^XZ AST node (no parameters)

src/test/kotlin/com/whitespacesystems/parser/parser/  
├── StartFormatCommandTest.kt         # ^XA parsing tests
└── EndFormatCommandTest.kt           # ^XZ parsing tests

# Files to MODIFY:
src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt
# Add: visitStartFormatCommand() and visitEndFormatCommand() to ZplNodeVisitor interface

src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt  
# Add: "XA" -> parseStartFormatCommand() and "XZ" -> parseEndFormatCommand() cases

src/main/kotlin/com/whitespacesystems/parser/utils/AstPrinter.kt
# Add: visitStartFormatCommand() and visitEndFormatCommand() implementations

src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt
# Add: end-to-end tests with complete ^XA...^XZ boundaries
```

### Known Codebase Patterns & Gotchas
```kotlin
// CRITICAL: Project-specific patterns from ZPL parser codebase

// 1. Sealed class hierarchy for AST nodes - EXACT pattern to follow
// From ZplNode.kt: All command nodes extend sealed class and implement visitor
sealed class ZplNode {
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}

data class StartFormatCommand() : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitStartFormatCommand(this)
}

// 2. Visitor interface extension - MUST add both new methods
// From ZplNode.kt: Visitor interface needs new visit methods
interface ZplNodeVisitor<T> {
    fun visitStartFormatCommand(command: StartFormatCommand): T
    fun visitEndFormatCommand(command: EndFormatCommand): T
    // ... existing methods
}

// 3. Parser command dispatch pattern - EXACT integration point
// From ZplParser.kt lines 51-63: Add XA/XZ cases to existing when statement
private fun parseCommand(): ZplNode {
    expect(TokenType.CARET)
    val commandToken = expect(TokenType.COMMAND)
    
    return when (commandToken.value) {
        "XA" -> parseStartFormatCommand()    // ADD THIS
        "XZ" -> parseEndFormatCommand()      // ADD THIS  
        "FO" -> parseFieldOriginCommand()
        "FD" -> parseFieldDataCommand()
        "A" -> parseFontCommand(commandToken.value)
        // ... existing cases
    }
}

// 4. No-parameter command parsing pattern - Simple implementation needed
// Both XA and XZ take no parameters, so parsing methods are trivial
private fun parseStartFormatCommand(): StartFormatCommand {
    // No parameters to parse - XA command complete after COMMAND token
    return StartFormatCommand()
}

// 5. Kotest StringSpec testing pattern - EXACT structure to follow
// From FieldOriginCommandTest.kt: Descriptive test names with should/when format
"should parse XA start format command" {
    val lexer = Lexer("^XA")
    val parser = ZplParser(lexer.tokenize())
    val program = parser.parse()

    program.commands.size shouldBe 1
    val command = program.commands[0]
    command.shouldBeInstanceOf<StartFormatCommand>()
}

// 6. Performance consideration - No validation needed (no parameters)
// Unlike FieldOriginCommand.kt with coordinate validation, XA/XZ need no init blocks

// 7. AstPrinter visitor implementation - Simple string representation
// From AstPrinter.kt: Add visitor methods for debugging output
override fun visitStartFormatCommand(command: StartFormatCommand): String {
    return "StartFormatCommand()"
}
```

## Implementation Blueprint

### Data Models
```kotlin
// AST nodes with sealed class hierarchy and visitor pattern
// Pattern: No parameters needed, simple data classes

data class StartFormatCommand() : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitStartFormatCommand(this)
}

data class EndFormatCommand() : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitEndFormatCommand(this)
}
```

### Task List (in execution order)
```yaml
Task 1: EXTEND src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt
  - PATTERN: Add visitStartFormatCommand() and visitEndFormatCommand() to ZplNodeVisitor<T> interface
  - CRITICAL: Both methods must return T and take respective command parameters
  - VERIFY: Interface compiles and existing implementations need updates

Task 2: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/StartFormatCommand.kt  
  - PATTERN: Follow FieldDataCommand.kt simple structure (no validation needed)
  - CRITICAL: Extend ZplNode sealed class, implement accept() method with visitor pattern
  - VERIFY: Data class with no parameters, proper visitor method call

Task 3: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/EndFormatCommand.kt
  - PATTERN: Mirror StartFormatCommand.kt exactly (both are parameter-less)
  - CRITICAL: Extend ZplNode sealed class, implement accept() method with visitor pattern  
  - VERIFY: Data class with no parameters, proper visitor method call

Task 4: EXTEND src/main/kotlin/com/whitespacesystems/parser/utils/AstPrinter.kt
  - PATTERN: Add visitStartFormatCommand() and visitEndFormatCommand() implementations
  - CRITICAL: Return simple string representations for debugging
  - VERIFY: Compiles and integrates with visitor pattern correctly

Task 5: EXTEND src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt  
  - PATTERN: Add "XA" and "XZ" cases to parseCommand() when statement (lines 51-63)
  - CRITICAL: Add parseStartFormatCommand() and parseEndFormatCommand() methods
  - VERIFY: Parser correctly dispatches to new command parsing methods

Task 6: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/StartFormatCommandTest.kt
  - PATTERN: Follow FieldOriginCommandTest.kt Kotest StringSpec structure exactly
  - CRITICAL: Test basic parsing, edge cases, error scenarios
  - VERIFY: All tests pass, proper AST node creation, type checking

Task 7: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/EndFormatCommandTest.kt  
  - PATTERN: Mirror StartFormatCommandTest.kt testing approach
  - CRITICAL: Test basic parsing, edge cases, error scenarios
  - VERIFY: All tests pass, proper AST node creation, type checking

Task 8: EXTEND src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt
  - PATTERN: Add complete ZPL label tests with ^XA...^XZ boundaries  
  - CRITICAL: Test real-world ZPL examples with start/end format commands
  - VERIFY: End-to-end parsing works, AST structure correct, AstPrinter integration

Task 9: VALIDATION - Run quality checks and verify coverage
  - PATTERN: Execute ./gradlew ktlintFormat, ./gradlew detekt, ./gradlew test
  - CRITICAL: All quality gates pass, 80%+ test coverage maintained
  - VERIFY: No regressions, new functionality fully tested
```

---

# FOUNDATION REFERENCE
**COMPLETE CONTEXT FOR AI AGENT IMPLEMENTATION**

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
# 1. Code Style & Analysis - Fix ALL errors before proceeding
./gradlew ktlintFormat               # Auto-fix code formatting
./gradlew detekt                     # Run static analysis

# 2. Unit & Feature Tests - Must pass with 80%+ coverage
./gradlew test --tests "*StartFormat*" # Run specific new tests
./gradlew test --tests "*EndFormat*"   # Run specific new tests
./gradlew test                         # Run all tests
./gradlew jacocoTestReport             # Generate coverage report

# 3. Build Verification
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
class StartFormatLexerTest : StringSpec({

    "should tokenize XA command" {
        val lexer = Lexer("^XA")
        val tokens = lexer.tokenize()
        
        tokens shouldHaveSize 3 // CARET, COMMAND, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "XA", 1)
        tokens[2] shouldBe Token(TokenType.EOF, "", 3)
    }

    "should handle XA in complete ZPL context" {
        val lexer = Lexer("^XA^FO100,100^FDTest^XZ")
        val tokens = lexer.tokenize()
        
        // Verify XA tokenizes correctly in multi-command context
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "XA", 1)
    }
})
```

### Parser Testing Pattern
```kotlin
class StartFormatCommandTest : StringSpec({

    "should parse XA start format command" {
        val lexer = Lexer("^XA")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse XA with whitespace" {
        val lexer = Lexer("^XA ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        program.commands.size shouldBe 1
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
    }
})
```

### End-to-End Testing Pattern
```kotlin
class XAXZCommandE2ETest : StringSpec({

    "should parse complete ZPL with XA/XZ boundaries" {
        val zplCode = "^XA^FO100,100^FDTest Label^FS^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 4
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[2].shouldBeInstanceOf<FieldDataCommand>()
        program.commands[3].shouldBeInstanceOf<EndFormatCommand>()
        
        // Verify AST printer integration
        val printer = AstPrinter()
        val output = printer.print(program)
        output shouldContain "StartFormatCommand()"
        output shouldContain "EndFormatCommand()"
    }

    "should parse real-world product label with boundaries" {
        val zplCode = """
            ^XA
            ^CF0,30
            ^FO300,30^FDProduct Label^FS
            ^CF0,25
            ^FO20,100^FDSKU: 112345678000001107^FS
            ^XZ
        """.trimIndent()
        
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        // First and last commands should be XA/XZ
        program.commands.first().shouldBeInstanceOf<StartFormatCommand>()
        program.commands.last().shouldBeInstanceOf<EndFormatCommand>()
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

### ❌ Anti-Patterns to Avoid  
- Don't create mutable AST nodes (use immutable data classes)
- Don't skip error position tracking in ParseExceptions
- Don't use regex for performance-critical parsing paths
- Don't forget visitor pattern implementation in AST nodes
- Don't skip context-aware lexing for special syntax
- Don't create multiple passes over input (single-pass parsing)
- Don't ignore performance optimization guidelines

### Coverage Requirements
- **Core parsing logic**: 100% (lexer, parser, AST generation)
- **Command implementations**: 95%+ (all supported ZPL commands)
- **Error handling**: 100% (exception scenarios)
- **Utilities**: 90%+ (AstPrinter, helper functions)
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
- [ ] ZPL command reference updated in `data/zpl/` if needed

### External References
- **Official ZPL Documentation**: https://files.tracerplus.com/public/docs/3rdparty/manuals/ZPL_Vol1.pdf
- **ZPL Command Examples**: https://labelary.com/zpl.html
- **Real-world ZPL Examples**: https://gist.github.com/metafloor/773bc61480d1d05a976184d45099ef56

---

## Confidence Score: 9/10

**Rationale for High Confidence:**
- ✅ **Complete codebase analysis** with specific file references and line numbers
- ✅ **Exact patterns identified** from existing command implementations
- ✅ **Comprehensive external research** with official Zebra documentation
- ✅ **Clear implementation path** with step-by-step tasks in execution order
- ✅ **Executable validation commands** for iterative refinement and quality assurance
- ✅ **Real-world examples** from external sources showing XA/XZ usage patterns
- ✅ **Performance considerations** built into the implementation blueprint
- ✅ **TDD approach** with specific testing patterns and coverage requirements

**Minor uncertainty (-1 point):** Edge case interactions with context-aware lexing modes, though patterns from existing implementations provide strong guidance.

This PRP provides comprehensive context and clear implementation guidance for successful one-pass implementation of ^XA and ^XZ command support in the ZebraView Parser.