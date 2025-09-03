# AST PATTERNS & GOTCHAS

## AST-Specific Patterns from ZPL Codebase

### 1. Sealed class hierarchy for type-safe AST nodes
```kotlin
// From ZplNode.kt: All command nodes extend sealed class
sealed class ZplNode {
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}
```

### 2. Visitor Pattern Implementation
```kotlin
// All new AST nodes must implement visitor pattern
interface ZplNodeVisitor<T> {
    fun visitNewCommand(node: NewCommand): T
    // ... other visit methods
}
```

### 3. Immutable Data Classes
```kotlin
// AST nodes should be immutable data classes
data class NewCommand(
    val param1: Int,
    val param2: Int
) : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T {
        return visitor.visitNewCommand(this)
    }
}
```

## AST Implementation Requirements

### ✅ Required Patterns
- Follow sealed class hierarchy for AST nodes
- Implement visitor pattern for all new AST nodes
- Use immutable data classes
- Extend ZplNode sealed class
- Add corresponding visit methods to ZplNodeVisitor interface

### ❌ Anti-Patterns to Avoid
- Don't create mutable AST nodes (use immutable data classes)
- Don't forget visitor pattern implementation in AST nodes
- Don't skip adding visit methods to visitor interface
- Don't break sealed class hierarchy