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
```

## Implementation Blueprint

### Data Models
```kotlin
// Core AST nodes with sealed class hierarchy and visitor pattern needed
```

### Task List (in execution order)
```yaml
Task 1: CREATE src/main/kotlin/com/whitespacesystems/parser/ast/NewCommand.kt
  - PATTERN: Follow existing AST node structure (FieldOriginCommand.kt)
  - CRITICAL: Extend ZplNode sealed class, implement visitor pattern
  - VERIFY: Proper data class with immutable properties

Task 2: CREATE src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt
  - PATTERN: Add parseNewCommand() method following existing patterns
  - CRITICAL: Handle token consumption, error reporting with positions
  - VERIFY: Integrates with main parse() method

Task 3: CREATE src/test/kotlin/com/whitespacesystems/parser/parser/NewCommandTest.kt
  - PATTERN: Use Kotest StringSpec with descriptive test names
  - CRITICAL: Test parsing, validation, error cases, AST generation
  - VERIFY: 80%+ coverage, all edge cases covered

Task N: ...
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
./gradlew test --tests "*NewFeature*" # Run specific new tests
./gradlew test                       # Run all tests
./gradlew jacocoTestReport           # Generate coverage report

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