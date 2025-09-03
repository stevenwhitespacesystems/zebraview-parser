# LEXER PATTERNS & GOTCHAS

## CRITICAL: Project-specific patterns from ZPL parser codebase

### 1. CRITICAL TOKEN PRECEDENCE - expectingFieldData MUST come first
```kotlin
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
```

### 2. Dynamic Character Support - Never hardcode ZPL characters
```kotlin
// From Lexer.kt: Support for CC/CD/CT commands that change syntax characters
private var caretChar: Char = '^'      // Format command prefix (changed by ^CC)
private var tildeChar: Char = '~'      // Control command prefix (changed by ^CT)
private var delimiterChar: Char = ','  // Parameter delimiter (changed by ^CD)
// Always use these variables instead of hardcoded '^', '~', ','
```

### 3. Command Lookup Optimization - O(1) command recognition
```kotlin
// From Lexer.kt: HashMap for performance instead of sequential checks
private val commandInfo = hashMapOf(
    "FD" to CommandInfo(2, true),           // Field data - has string data
    "CF" to CommandInfo(2, false, true),    // Change font - has variants (CFB, CF0)
    "BC" to CommandInfo(2, false, true)     // Code 128 - has variants (BCR, BCN)
)
```

### 4. Context-aware lexing - Field data vs command parsing modes
```kotlin
// From Lexer.kt: Field data mode after ^FD commands treats everything as string until next ^
expectingFieldData = (finalCommandName == "FD") || (finalCommandName == "FX")
```

### 5. Performance-critical parsing - avoid object allocations in loops
```kotlin
// Use StringBuilder for string building, reuse collections where possible
```

## ZPL Parser Lexer Guidelines

### ✅ Required Patterns
- **CRITICAL**: Use expectingFieldData check BEFORE digit recognition in lexer
- Use dynamic character variables (caretChar, tildeChar, delimiterChar) - never hardcode
- Use command lookup HashMap for O(1) performance
- Handle command variants embedded in tokens (CFB, BCR, A0N)
- Optimize for performance (avoid object allocations in hot paths)

### ❌ Anti-Patterns to Avoid  
- Don't put digit recognition before expectingFieldData check (causes tokenization bugs)
- Don't hardcode '^', '~', ',' characters (breaks CC/CD/CT command support)
- Don't use regex for performance-critical parsing paths
- Don't skip context-aware lexing for special syntax
- Don't create multiple passes over input (single-pass parsing)
- Don't ignore performance optimization guidelines