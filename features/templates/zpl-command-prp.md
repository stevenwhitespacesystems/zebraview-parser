# ZPL Command PRP Template

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
Extract from research results header line:
- Command: Extract ^XX notation from "Command: ^XX (Complexity: X)" header
- Purpose: Copy exactly from "Purpose:" line in research results
- Complexity: Extract from parentheses in header (Simple/Medium/Complex)
- Affects: Optional - extract from "Deps:" field in Implementation section if present
-->

Command: [Extract from "Command: ^XX (Complexity: X)" header]
Purpose: [Copy from "Purpose:" line in research results]
Complexity: [Extract complexity rating from header] ([extract reason from Implementation complexity note])
Affects: [Optional - extract from Implementation "Deps:" field if present]

## 2. Command Specification
<!-- GENERATOR INSTRUCTIONS:
Extract from research results:
1. Syntax - copy from "Format:" line in research results
2. Parameters table - copy the entire parameters table directly
3. Field Data Requirements - ONLY if mentioned in "Notes" section
4. Mode Complexity - ONLY if command has modes (check parameters for mode indicators)
5. Validation Rules - extract from "Validation" column in parameters table
-->

### Syntax
```
[Copy from "Format:" line in research results]
```

### Parameters
[Copy parameter table directly from "Parameters" section in research results]

### Field Data Requirements
<!-- Include ONLY if mentioned in "Notes" section of research results -->
[Copy field data requirements from "Notes" section]

### Modes
<!-- Include ONLY if command has mode parameters (check Validation column for mode indicators like N/R/I/B) -->
[Extract mode descriptions from parameters and notes]

### Validation Rules
<!-- Extract from "Validation" column in parameters table and any special validation from "Notes" -->
[List validation requirements from parameters table Validation column and Notes section]

## 3. Dependencies & Prerequisites
<!-- GENERATOR INSTRUCTIONS:
Extract from research results "Implementation" section:
- Parse "Deps:" field for critical dependencies
- Parse "Utils:" field for utility functions needed
Check codebase for actual dependencies
-->

### Required Prior Implementations
- [ ] [Extract from "Deps:" field in Implementation section]
- [ ] Check: Are these already in codebase?

### Shared Utilities
- [ ] [Extract from "Utils:" field in Implementation section]
- [ ] Check CommandParsingUtils for existing helpers

## 4. Implementation Design
<!-- GENERATOR INSTRUCTIONS:
Primary source: research results "Implementation" section
Secondary verification: Check actual codebase patterns

Steps:
1. Extract AST class name from "AST:" field (e.g., "BtCommand")
2. Extract parser method from "Parser:" field (e.g., "parseBtCommand()")  
3. Parse "Files:" field for file modifications needed
4. VERIFY by checking similar commands in src/main/kotlin/.../ast/
5. ADAPT the structure to match actual codebase patterns
-->

### AST Node Structure
```kotlin
// Extract class name from "AST:" field in Implementation section
// Example: "AST: `BtCommand` [Complex dual-barcode with strict data validation]"
[Extract class name from "AST:" field in research results]
[VERIFY against existing commands like FieldOriginCommand.kt, BCCommand.kt]
[ENSURE: extends ZplNode, includes visitor pattern, follows project conventions]
```

### Parser Integration
```kotlin
// Extract method name from "Parser:" field in Implementation section
// Example: "Parser: `parseBtCommand()` [Float parsing for r1 parameter, DPI-aware defaults]"
[Extract parser method name from "Parser:" field in research results]
// Check similar commands in ZplParser for parameter parsing patterns

// VERIFY commandInfo structure in Lexer.kt before adding
"[XX]" to CommandInfo([length], [hasFieldData], [hasVariants])
```

### Integration Points
<!-- Extract from "Files:" field in Implementation section -->
<!-- Example: "Files: ast/BarcodeCommands.kt, parser/BarcodeCommandParser.kt, parser/ParameterUtils.kt" -->
[Parse "Files:" field from research results to create file modification table]

### Implementation Notes
<!-- EXTRACT from research results "Notes" section and Implementation field details -->
- [Extract from "Notes" section in research results]
- [Extract special parsing considerations from Implementation field brackets]
- [Check if similar patterns already exist in codebase]
- [Note any command-specific parsing challenges from Parser field notes]

### Utility Functions
<!-- Extract from "Utils:" field in Implementation section -->
<!-- Example: "Utils: `parseFloatParameter()`, `validateEciNumber()`, `validateSerialNumber()`" -->
[Extract utility functions from "Utils:" field, noting if they already exist or need creation]

## 5. Test Scenarios
<!-- GENERATOR INSTRUCTIONS:
Primary source: research results "Test Cases" section - numbered list of test scenarios
Secondary: Verify test patterns in src/test/kotlin/.../parser/

CRITICAL: These tests are written FIRST in TDD workflow (before implementation)
Process:
1. Extract numbered test cases from research results (e.g., "1. Basic:", "2. Defaults:", etc.)
2. Transform into categorized test groups (Basic, Edge Cases, Error Scenarios)
3. CHECK existing test files for Kotest StringSpec format and naming patterns
4. ENSURE tests follow project patterns (descriptive names, proper assertions)
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
// Extract from numbered test cases 1-3 in research results (typically "Basic", "Defaults", "Max params")
// Example from BT: "1. Basic: ^BT^FD123456,SERIAL123 → BtCommand(N, 2, 2.0f, 40, 2, 4) with validated data"
[Extract test cases 1-3 from research results and format as test scenarios]
```

### Edge Cases
<!-- Extract from numbered test cases 4-6 in research results (typically "Boundary", "Edge") -->
<!-- Example: "6. Edge: ^BT,2,2.1,40,2,4 → BtCommand(N, 2, 2.1f, 40, 2, 4)" -->
[Extract boundary and edge test cases from research results numbered list]

### Error Scenarios  
<!-- Extract from numbered test cases with "Invalid" or "ParseError" in research results -->
<!-- Example: "5. Invalid: ^BT,11,2.0,40,2,4 → ParseError(\"w1 must be between 1 and 10\")" -->
[Extract error test cases from research results numbered list]

### Integration Tests
<!-- Extract from numbered test cases related to data validation or command interactions -->
<!-- Example: "8. Data validation: ^FD12345,SERIAL → ParseError(\"ECI number must be exactly 6 digits\")" -->
[Extract integration and data validation test cases from research results numbered list]

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
<!-- Extract performance target from "Target:" field in Implementation section -->
<!-- Example: "Target: <1ms (Complex)" -->
```kotlin
// Benchmark file: src/benchmark/kotlin/.../[CommandName]Benchmarks.kt  
// Target: [Extract from "Target:" field in research results Implementation section]
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
- [ ] Performance meets target: [Extract from "Target:" field in research results]
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