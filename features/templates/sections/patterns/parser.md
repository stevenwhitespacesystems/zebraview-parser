# PARSER PATTERNS & GOTCHAS

## Parser-Specific Patterns from ZPL Codebase

### 1. Command Variant Handling - Embedded parameters in command tokens
```kotlin
// From ZplParser.kt: Commands like CFB, BCR, A0N have variants in the token itself
if (commandToken.value.length > 2) {
    font = commandToken.value[2]  // Extract 'B' from "CFB" command
}
```

### 2. Parser error handling with position information
```kotlin
// From ZplParser.kt: All exceptions include token position
throw ParseException("Expected coordinate parameter", token.position)
```

### 3. Context-aware parsing integration
```kotlin
// Parser must coordinate with lexer's field data state
// Handle transitions between command and field data modes
```

## Parser Implementation Requirements

### ✅ Required Patterns
- Handle command variants embedded in tokens (CFB, BCR, A0N)
- Include position information in all ParseExceptions
- Integrate with lexer's context-aware parsing modes
- Use recursive descent parsing strategy
- Follow existing command parsing patterns

### ❌ Anti-Patterns to Avoid
- Don't skip error position tracking in ParseExceptions
- Don't create mutable AST nodes (use immutable data classes)
- Don't ignore command variant handling
- Don't break context-aware parsing coordination