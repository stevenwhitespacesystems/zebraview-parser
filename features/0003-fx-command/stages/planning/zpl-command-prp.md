# ZPL Command PRP Template

## Quick Example: ^FO Command
<!-- EXAMPLE - Shows agents exactly what to produce -->
Given research: ^FO sets field origin with X,Y,Z parameters
This template produces:
- AST: `data class FieldOriginCommand(val x: Int, val y: Int?, val z: Int = 0)`
- Test: `"should parse ^FO10,20,0" { ... }`
- Parser: `private fun parseFieldOrigin(): FieldOriginCommand { ... }`
Complete example helps agents understand expectations.

## Naming Conventions
<!-- CRITICAL: Use descriptive names based on FUNCTION, not command codes -->
**IMPORTANT**: Always use descriptive names that explain what the command DOES, not what its ZPL code is:

✅ **CORRECT Examples**:
- `CommentCommand` (not `FxCommand`) - ^FX adds comments
- `FieldOriginCommand` (not `FoCommand`) - ^FO sets field origin
- `StartFormatCommand` (not `XaCommand`) - ^XA starts format
- `visitCommentCommand()` (not `visitFxCommand()`)
- `parseComment()` (not `parseFx()`)
- `CommentCommandTest` (not `FxCommandTest`)

❌ **INCORRECT Examples**:
- `FxCommand`, `FoCommand`, `XaCommand` - these use ZPL codes
- `visitFxCommand()`, `parseFx()` - these use ZPL codes
- `FxCommandTest` - uses ZPL code

**Pattern**: If ^XX does Y, name it `YCommand` with methods like `parseY()`, `visitYCommand()`, and test class `YCommandTest`

---

## 1. Feature Summary

Command: ^FX
Purpose: Non-printing comment command for documentation within label formats
Complexity: Simple (Basic text storage with no validation or processing)
Affects: None (standalone non-printing command)

## 2. Command Specification

### Syntax
```
^FXc
```

### Parameters
| Name | Type | Default | Validation |
|------|------|---------|------------|
| c | String | none | Any text string until next ^ or ~ command |

### Validation Rules
- Comment text continues until next ^ or ~ command delimiter
- Preserves exact text including whitespace and special characters
- No parameter validation required (accepts any text)

## 3. Dependencies & Prerequisites

### Required Prior Implementations
- None (standalone simple command)

### Shared Utilities
- None (basic parsing only)

## 4. Implementation Design

### AST Node Structure
```kotlin
// File: src/main/kotlin/com/whitespacesystems/parser/ast/CommentCommand.kt
// CommentCommand (following naming convention for ^FX comment functionality)
data class CommentCommand(
    val text: String
) : ZplNode() {
    override fun accept(visitor: CommandVisitor) = visitor.visitCommentCommand(this)
}
```

### Parser Integration
```kotlin
// Add to ZplParser.kt in the parseCommand() method when statement:
// case "FX" -> return parseComment()

// In ZplParser.kt, add this method:
private fun parseComment(): CommentCommand {
    val text = readUntilDelimiter() // Reads until ^ or ~ delimiter
    return CommentCommand(text)
}

// Add to Lexer.kt commandInfo structure:
"FX" to CommandInfo(2, false, false)

// Add to CommandVisitor.kt interface:
fun visitCommentCommand(command: CommentCommand)
```

### Integration Points
| File | Modification | Purpose |
|------|-------------|---------|
| src/main/kotlin/.../ast/CommentCommand.kt | Create new | AST node definition |
| src/main/kotlin/.../parser/ZplParser.kt | Add parseComment() + case FX | Parser method integration |
| src/main/kotlin/.../ast/AstPrinter.kt | Add visitCommentCommand() | Visitor pattern support |
| src/main/kotlin/.../ast/CommandVisitor.kt | Add visitCommentCommand() | Interface method |
| src/main/kotlin/.../lexer/Lexer.kt | Add "FX" CommandInfo | Lexer recognition |

### Implementation Notes
- Simple text capture with no parameter validation
- Preserves whitespace and special characters exactly
- Non-printing command that doesn't affect label output
- Commonly used for documentation within ZPL code
- Text parsing continues until next ^ or ~ command delimiter
- **Directory Verification**: Ensure output directory `features/0003-fx-command/stages/planning/` exists before creating files
- **Integration Check**: Verify CommandVisitor interface exists and follows project visitor pattern
- **Parser Context**: Add parseComment() method after existing command parsing methods in ZplParser.kt

### Utility Functions
None (basic string parsing only)

## 5. Test Scenarios

### Test Implementation
```kotlin
// Test file: src/test/kotlin/com/whitespacesystems/parser/parser/CommentCommandTest.kt
// Using Kotest StringSpec format following project patterns

import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.ast.CommentCommand
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class CommentCommandTest : StringSpec({
    // Tests are written FIRST - they will initially FAIL (RED phase)
    
    "should parse minimal comment command" {
        val parser = ZplParser("^FX^FS")
        val result = parser.parseComment()
        result shouldBe CommentCommand("")
    }
    
    "should parse comment with text content" {
        val parser = ZplParser("^FXThis is a comment^FS")  
        val result = parser.parseComment()
        result shouldBe CommentCommand("This is a comment")
    }
    
    "should handle empty comment text" { /* empty case covered above */ }
    "should preserve whitespace in comments" { /* test preserving exact spacing */ }
    "should handle special characters in comments" { /* test !@#$%^&*() chars */ }
    "should stop at command delimiters" { /* test ^ and ~ delimiters */ }
    "should reject incomplete comment" { /* test ParseError scenarios */ }
})
```

### Basic Functionality
```kotlin
// Test case 1: Basic comment parsing
"^FXThis is a comment^FS" should parse to CommentCommand("This is a comment")

// Test case 2: Empty comment (defaults)
"^FX^FS" should parse to CommentCommand("")

// Test case 3: Comment with special characters
"^FXLong comment with special chars !@#$%^&*()^FS" should parse to CommentCommand("Long comment with special chars !@#$%^&*()")
```

### Edge Cases
```kotlin
// Test case 4: Long comment text (boundary)
"^FX<1000 character string>^FS" should parse to CommentCommand("<1000 character string>")

// Test case 6: Comment preserving whitespace and tabs
"^FXComment with spaces   and   tabs	^FS" should parse to CommentCommand("Comment with spaces   and   tabs	")

// Test case 7: Comment ending with tilde delimiter
"^FXComment ending with tilde~GA" should parse to CommentCommand("Comment ending with tilde")
```

### Error Scenarios  
```kotlin
// Test case 5: Invalid incomplete command
"^FX" should throw ParseError("Unexpected end of input, expected text or command delimiter")
```

### Integration Tests
```kotlin
// Integration with other commands
"^XA^FXThis is a comment^FO10,20^FDHello^FS^XZ" should parse correctly with comment preserved

// Multiple comments in sequence
"^FXFirst comment^FXSecond comment^FS" should parse both comments
```

### Performance Benchmarks
```kotlin
// Benchmark file: src/benchmark/kotlin/.../CommentCommandBenchmarks.kt  
// Target: <0.1ms (Simple command)
@State(Scope.Benchmark)
class CommentCommandBenchmarks {
    @Benchmark
    fun parseCommentCommand() {
        val parser = ZplParser("^FXThis is a test comment^FS")
        parser.parseComment()
    }
}
```

### Coverage Requirements
- Minimum: 80% code coverage
- All error paths must be tested
- All parameter combinations should be covered

## 6. Definition of Done
### Feature Complete When:
- [ ] Command is recognized by the lexer
- [ ] AST node structure correctly represents the command
- [ ] Parser correctly handles all parameter combinations
- [ ] All test scenarios pass (100%)
- [ ] Code coverage ≥80% for new implementation
- [ ] Zero linting violations
- [ ] Zero static analysis issues
- [ ] Performance meets target: <0.1ms (Simple)
- [ ] No performance regressions >10%
- [ ] Demo application can parse the command
- [ ] Real-world ZPL samples parse correctly (if provided)
- [ ] Error messages are clear and actionable
- [ ] Follows all existing patterns in the codebase

### Acceptance Criteria
- Command syntax matches ZPL II specification exactly (^FXc format)
- Text capture works correctly until command delimiters (^ or ~)
- Integration with existing commands works correctly
- Visitor pattern implementation is complete (visitCommentCommand)
- Performance meets project requirements (<0.1ms for simple commands)

### Quality Standards
- All build commands pass: `./gradlew check`
- No @Suppress annotations without approval
- Code follows project conventions and patterns
- Documentation is clear and complete