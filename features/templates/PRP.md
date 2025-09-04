# PRP Template

## Quick Example: ^FO Command
<!-- EXAMPLE - Shows agents exactly what to produce -->
Given research: ^FO sets field origin with X,Y,Z parameters
This template produces:
- AST: `data class FieldOriginCommand(val x: Int, val y: Int?, val z: Int = 0)`
- Test: `"should parse ^FO10,20,0" { ... }`
- Parser: `private fun parseFieldOrigin(): FieldOriginCommand { ... }`
Complete example helps agents understand expectations.

---

## 1. Feature Summary
<!-- GENERATOR INSTRUCTIONS:
Source: features/[feature-id]/agents/zpl-command-researcher/findings.md
Extract from "Command Overview" and "Implementation Analysis" sections:
- Command: The ZPL command notation (e.g., ^BC, ^FO)
- Purpose: One-sentence from "Purpose" in findings
- Complexity: Rating from "Parsing Complexity" with brief reason
- Affects: Optional - only if command interacts with other systems
-->

Command: [^XX notation]
Purpose: [One-line description from research findings]
Complexity: [Simple/Medium/Complex] ([brief 3-5 word reason])
Affects: [Optional - what this impacts in the system]

## 2. Command Specification
<!-- GENERATOR INSTRUCTIONS:
Source: features/[feature-id]/agents/zpl-command-researcher/findings.md
Extract from "Technical Specifications" section:
1. ZPL Format - exact syntax from findings
2. Parameters table - all parameters with details
3. Field Data Requirements - ONLY if mentioned in findings
4. Mode Complexity - ONLY if command has modes
5. Validation Rules - ONLY if special validation mentioned
-->

### Syntax
```
[Exact ZPL format from "ZPL Format" in findings]
```

### Parameters
[Copy parameter table directly from "Parameters" section in findings]

### Field Data Requirements
<!-- Include ONLY if "Field Data Requirements" exists in findings -->
[Copy field data requirements from findings]

### Modes
<!-- Include ONLY if "Mode Complexity" exists in findings -->
[Copy mode descriptions from findings]

### Validation Rules
<!-- Include ONLY if findings mention special validation, constraints, or calculations -->
[List any special validation requirements, parameter interactions, or constraints]

## 3. Dependencies & Prerequisites
<!-- GENERATOR INSTRUCTIONS:
Extract from research "Dependencies" or "Architecture Impact" sections
Check codebase for actual dependencies
-->

### Required Prior Implementations
- [ ] List commands that must exist first
- [ ] Check: Are these already in codebase?

### Shared Utilities
- [ ] Check CommandParsingUtils for existing helpers
- [ ] List any new utilities needed

## 4. Implementation Design
<!-- GENERATOR INSTRUCTIONS:
Primary source: features/[feature-id]/agents/zpl-command-researcher/findings.md
Secondary verification: Check actual codebase patterns

Steps:
1. Start with "Suggested AST Node Structure" from findings
2. VERIFY by checking similar commands in src/main/kotlin/.../ast/
3. ADAPT the structure to match actual codebase patterns
4. CHECK if utility functions already exist before creating new ones
-->

### AST Node Structure
```kotlin
[Adapt from findings "Suggested AST Node Structure"]
[VERIFY against existing commands like FieldOriginCommand.kt, BCCommand.kt]
[ENSURE: extends ZplNode, includes visitor pattern, follows project conventions]
```

### Parser Integration
```kotlin
// VERIFY exact patterns by checking ZplParser.kt
private fun parse[CommandName](): [CommandName]Command {
    // Check similar commands in ZplParser for parameter parsing patterns
}

// VERIFY commandInfo structure in Lexer.kt before adding
"[XX]" to CommandInfo([length], [hasFieldData], [hasVariants])
```

### Integration Points
<!-- CHECK codebase for these files and their current structure -->
| File | Action | Purpose |
|------|--------|---------|
| `ast/[CommandName]Command.kt` | Create | AST node implementation |
| `parser/ZplParser.kt` | Update | Add parse[CommandName]() method |
| `lexer/Lexer.kt` | Update | Register in commandInfo map |
| `ast/ZplNodeVisitor.kt` | Update | Add visit() signature |
| `utils/AstPrinterVisitorImpl.kt` | Update | Implement visitor |
| `parser/[CommandName]ParsingUtils.kt` | Create if needed | Only if complex parsing logic |

### Implementation Notes
<!-- EXTRACT from findings but VERIFY against codebase -->
- [Special parsing considerations from findings]
- [Check if similar patterns already exist in codebase]
- [Note any deviations from findings based on actual code]
- [List any command-specific parsing challenges]
- [Note interactions with dynamic character commands if applicable]

### Utility Functions
<!-- FIRST check if these already exist in CommandParsingUtils.kt or similar -->
<!-- Only create new utilities if truly needed -->
[List required utilities, noting if they already exist or need creation]

## 5. Test Scenarios
<!-- GENERATOR INSTRUCTIONS:
Primary source: features/[feature-id]/agents/zpl-command-researcher/findings.md - "Test Scenarios" section
Secondary: Verify test patterns in src/test/kotlin/.../parser/

CRITICAL: These tests are written FIRST in TDD workflow (before implementation)
Process:
1. Copy test examples from findings
2. CHECK existing test files for Kotest StringSpec format and naming patterns
3. ENSURE tests follow project patterns (descriptive names, proper assertions)
4. ORGANIZE by complexity: basic → edge cases → errors → integration
5. TARGET: 80% code coverage minimum
-->

### Test Implementation
```kotlin
// Test file: src/test/kotlin/com/whitespacesystems/parser/parser/[CommandName]Test.kt
// VERIFY: Use Kotest StringSpec format as seen in existing tests
// NAMING: Use descriptive test names that explain the behavior being tested

class [CommandName]Test : StringSpec({
    // Tests are written FIRST - they will initially FAIL (RED phase)
    
    "should parse minimal [command] command" { }
    "should parse [command] with all parameters" { }
    "should handle field data in [command]" { }  // if applicable
    "should reject invalid [parameter]" { }
})
```

### Basic Functionality
```kotlin
// Copy from findings "Basic Usage Tests" section
// Minimal command usage
"^XA^[COMMAND][minimal params]^XZ"

// Full parameters
"^XA^[COMMAND][all params]^FD[data]^FS^XZ"

// Common variations
[2-3 more examples from findings]
```

### Edge Cases
<!-- Extract from findings "Edge Cases" section -->
- [Boundary values - min/max parameters]
- [Empty or missing optional parameters]
- [Special characters in field data if applicable]
- [Maximum length values]

### Error Scenarios
<!-- Extract from findings "Edge Cases" or error handling -->
- [Invalid parameter values]
- [Missing required parameters]
- [Malformed syntax]
- [Invalid combinations from validation rules]

### Integration Tests
<!-- Extract from findings "Integration Tests" section -->
- [Command interaction with ^BY if barcode-related]
- [Position inheritance from ^FO]
- [Orientation effects from ^FW]
- [Multiple commands in single label]

### Property-Based Tests
<!-- Include for complex commands with many parameter combinations -->
```kotlin
// If command has complex parameter interactions
"should handle all valid parameter combinations" {
    checkAll(/* generators for valid params */) { params ->
        // Test with generated values
    }
}
```

### Performance Benchmarks
<!-- For commands marked as Complex or performance-critical -->
```kotlin
// Benchmark file: src/benchmark/kotlin/.../[CommandName]Benchmarks.kt
// Target: Simple commands <0.1ms, Complex commands <1ms
@State(Scope.Benchmark)
class [CommandName]Benchmarks {
    @Benchmark
    fun parse[CommandName]() { }
}
```

### Coverage Requirements
- Minimum: 80% code coverage
- All error paths must be tested
- All parameter combinations should be covered

## 6. Definition of Done
<!-- CLEAR COMPLETION CRITERIA FOR THE FEATURE -->
### Feature Complete When:
- [ ] Command is recognized by the lexer
- [ ] AST node structure correctly represents the command
- [ ] Parser correctly handles all parameter combinations
- [ ] All test scenarios pass (100%)
- [ ] Code coverage ≥80% for new implementation
- [ ] Zero linting violations
- [ ] Zero static analysis issues
- [ ] No performance regressions >10%
- [ ] Demo application can parse the command
- [ ] Real-world ZPL samples parse correctly (if provided)
- [ ] Error messages are clear and actionable
- [ ] Follows all existing patterns in the codebase

### Acceptance Criteria
<!-- SPECIFIC TO THIS COMMAND -->
- Command syntax matches ZPL II specification exactly
- Parameter validation follows the rules defined in section 2
- Integration with existing commands works correctly
- Visitor pattern implementation is complete
- Performance meets project requirements

### Quality Standards
- All build commands pass: `./gradlew check`
- No @Suppress annotations without approval
- Code follows project conventions and patterns
- Documentation is clear and complete