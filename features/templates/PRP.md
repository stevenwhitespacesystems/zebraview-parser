# PRP Template - AI Implementation Guide

## Purpose
Streamlined template optimized for AI agents to implement features with comprehensive context and self-validation capabilities.

## Core Principles
1. **Context is King**: Include ALL necessary documentation, examples, and caveats
2. **Validation Loops**: Provide executable tests/lints the AI can run and fix
3. **Information Dense**: Use keywords and patterns from the codebase
4. **Progressive Success**: Start simple, validate, then enhance
5. **Global Rules**: Be sure to follow all rules in CLAUDE.md
6. **STRICT QUALITY GATES**: ALL linting and static analysis must pass - no feature is complete until all checks pass (NO EXCEPTIONS)
7. **🚨 CONFIGURATION IMMUTABLE**: NEVER modify detekt.yml, ktlint config, or any static analysis configuration files
8. **🚨 NO SUPPRESSIONS WITHOUT APPROVAL**: NEVER use @Suppress annotations without explicit user approval and documented justification

## 🚨 CRITICAL: Foundation Requirements
**This PRP includes comprehensive foundation materials. See the following sections for complete details:**
- [Quality Gates & Validation](sections/quality-gates.md)
- [Foundation Reference](sections/foundation.md)
- [TDD Workflow](sections/tdd-workflow.md)
- [Performance Requirements](sections/performance.md)
- [Lexer Patterns](sections/patterns/lexer.md)
- [Parser Patterns](sections/patterns/parser.md)
- [AST Patterns](sections/patterns/ast.md)

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

// 1. CRITICAL TOKEN PRECEDENCE - expectingFieldData MUST come first
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

// 2. Dynamic Character Support - Never hardcode ZPL characters
// From Lexer.kt: Support for CC/CD/CT commands that change syntax characters
private var caretChar: Char = '^'      // Format command prefix (changed by ^CC)
private var tildeChar: Char = '~'      // Control command prefix (changed by ^CT)
private var delimiterChar: Char = ','  // Parameter delimiter (changed by ^CD)
// Always use these variables instead of hardcoded '^', '~', ','

// 3. Command Lookup Optimization - O(1) command recognition
// From Lexer.kt: HashMap for performance instead of sequential checks
private val commandInfo = hashMapOf(
    "FD" to CommandInfo(2, true),           // Field data - has string data
    "CF" to CommandInfo(2, false, true),    // Change font - has variants (CFB, CF0)
    "BC" to CommandInfo(2, false, true)     // Code 128 - has variants (BCR, BCN)
)

// 4. Command Variant Handling - Embedded parameters in command tokens
// From ZplParser.kt: Commands like CFB, BCR, A0N have variants in the token itself
if (commandToken.value.length > 2) {
    font = commandToken.value[2]  // Extract 'B' from "CFB" command
}

// 5. Sealed class hierarchy for type-safe AST nodes
// From ZplNode.kt: All command nodes extend sealed class
sealed class ZplNode {
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}

// 6. Parser error handling with position information
// From ZplParser.kt: All exceptions include token position
throw ParseException("Expected coordinate parameter", token.position)

// 7. Context-aware lexing - Field data vs command parsing modes
// From Lexer.kt: Field data mode after ^FD commands treats everything as string until next ^
expectingFieldData = (finalCommandName == "FD") || (finalCommandName == "FX")

// 8. Performance-critical parsing - avoid object allocations in loops
// Use StringBuilder for string building, reuse collections where possible
```

## Implementation Blueprint

### Data Models
```kotlin
// Core AST nodes with sealed class hierarchy and visitor pattern needed
```

### Task List (in execution order)
```yaml
# TDD Implementation Tasks (STRICT ORDER - Tests First!)
Task 1: CREATE TEST FILE src/test/kotlin/com/whitespacesystems/parser/parser/NewCommandTest.kt
  - PATTERN: Use Kotest StringSpec with descriptive test names
  - CRITICAL: Write ALL test cases FIRST (before any implementation)
  - TESTS WILL FAIL - That's correct and expected
  - VERIFY: ./gradlew test --tests "*NewCommand*" shows failures

Task 2: RUN TESTS TO CONFIRM RED PHASE
  - RUN: ./gradlew test --tests "*NewCommand*"
  - VERIFY: All new tests are RED (failing) as expected
  - DOCUMENT: List of expected test failures in notes

Task 3: CREATE MINIMAL AST NODE src/main/kotlin/com/whitespacesystems/parser/ast/NewCommand.kt
  - PATTERN: Follow existing AST node structure (FieldOriginCommand.kt)  
  - CRITICAL: ONLY what's needed for FIRST test to pass
  - NO extra features, NO optimizations
  - VERIFY: Extends ZplNode sealed class, basic visitor pattern

Task 4: CREATE MINIMAL PARSER LOGIC in ZplParser.kt
  - ADD: parseNewCommand() method with basic parsing
  - CRITICAL: ONLY enough logic for tests to pass
  - NO complex parameter handling yet
  - VERIFY: Method integrates with main parse() switch

Task 5: VERIFY GREEN PHASE - All Tests Pass
  - RUN: ./gradlew test --tests "*NewCommand*"
  - VERIFY: All tests now pass (GREEN phase achieved)
  - DOCUMENT: Confirm transition from RED to GREEN

Task 6: REFACTOR PHASE - Optimize and Clean Up
  - NOW add full parameter parsing, error handling
  - Add performance optimizations
  - Improve code structure and naming
  - VERIFY: ./gradlew test still passes after refactoring
  - VERIFY: 80%+ coverage via ./gradlew jacocoTestReport

# Performance Validation Tasks (MANDATORY for new ZPL commands)
Task N: CREATE src/benchmark/kotlin/.../NewCommandBenchmarks.kt
  - PATTERN: Follow CommandBenchmarks.kt structure with @State and @Benchmark annotations
  - CRITICAL: Include both isolated command and E2E benchmarks
  - VERIFY: Performance <0.1ms simple commands, <1ms complex commands
  - INCLUDE: Individual command benchmark + complete label benchmark

Task N+1: VALIDATE no performance regression
  - RUN: ./gradlew benchmark
  - CHECK: BaselineComparison output for >10% degradation warnings
  - VERIFY: No regression in existing commands
  - IF REGRESSION: STOP and create regression fix PRP before completing feature

Task N+2: UPDATE baseline performance data
  - RUN: BaselineComparison utilities to update baseline.json if performance improved
  - VERIFY: New performance measurements recorded
  - DOCUMENT: Any optimization insights discovered

# Final Integration Tasks
Task FINAL-1: Complete quality gates including performance
  - RUN: ./gradlew check (includes ktlint, detekt, tests)
  - RUN: ./gradlew benchmark (performance validation)
  - VERIFY: All quality gates pass WITHOUT configuration modifications
  - VERIFY: No @Suppress annotations added without explicit user approval
  - CONFIRM: Zero static analysis violations through code fixes only
  - EXECUTE: Final Quality Gate Resolution sequence (see [Final Quality Gate Resolution](#-final-quality-gate-resolution-mandatory-last-steps))

Task FINAL: Integration verification
  - RUN: ./gradlew run (test demo application)
  - VERIFY: New command works in complete parsing workflows
```

## 🔴 FINAL QUALITY GATE RESOLUTION (MANDATORY LAST STEPS)

### The Last Thing Before Feature Completion
**NO feature is considered complete until ALL THREE quality gates pass:**

1. **Fix ALL Linting Issues** (`./gradlew ktlintFormat` then `./gradlew ktlintCheck`)
2. **Fix ALL Static Analysis Violations** (`./gradlew detekt` - fix through code changes ONLY)  
3. **Fix ALL Test Failures** (`./gradlew test` - must achieve 100% pass rate)

### Final Validation Sequence
```bash
# MANDATORY: Run these commands IN ORDER as the FINAL steps
# A feature is INCOMPLETE if ANY of these fail

# Step 1: Auto-fix formatting issues
./gradlew ktlintFormat

# Step 2: Verify all linting passes
./gradlew ktlintCheck
# ✅ Must show: BUILD SUCCESSFUL with zero violations

# Step 3: Run static analysis
./gradlew detekt
# ✅ Must show: BUILD SUCCESSFUL with zero violations

# Step 4: Run all tests
./gradlew test
# ✅ Must show: 100% tests passed (not 99%, not 101/102)

# Step 5: Verify coverage
./gradlew jacocoTestReport
# ✅ Must show: ≥80% coverage

# Step 6: Final comprehensive check
./gradlew check
# ✅ Must show: BUILD SUCCESSFUL

# ONLY after ALL above pass → Feature is COMPLETE
```

### If ANY Quality Gate Fails
- **STOP** - Do not consider feature complete
- **FIX** - Address violations through code changes only
- **RERUN** - Execute the full validation sequence again
- **REPEAT** - Until ALL quality gates pass

**Remember**: A feature with failing quality gates is an INCOMPLETE feature, regardless of functionality.

## Performance Regression Handling Workflow

### When Performance Regression Detected (>10% degradation)

If `./gradlew benchmark` shows ANY command has degraded >10%:

1. **STOP current PRP completion** - Do not mark the feature complete
2. **Document regression details** in current PRP completion notes:
   ```
   🔍 PERFORMANCE REGRESSION DETECTED:
   - Affected command(s): [command name(s)]  
   - Performance change: [baseline] → [current] ([X%] slower)
   - Likely cause: [recent implementation changes]
   - BaselineComparison output: [paste warning messages]
   
   ⚠️ Current PRP cannot be completed until regression is resolved.
   ```

3. **Create INITIAL.md for regression fix**:
   ```markdown
   # PERFORMANCE REGRESSION DETECTED

   ## Commands Affected
   [List specific commands with >10% degradation and performance deltas]

   ## Performance Impact Analysis
   - [Command 1]: [baseline] → [current] ([X%] degradation)
   - [Command 2]: [baseline] → [current] ([Y%] degradation)
   
   ## Root Cause Investigation Required
   - Analyze recent implementation changes that may have introduced inefficiencies
   - Profile hot paths for object allocations or algorithmic complexity increases
   - Review parsing logic for unnecessary string operations or repeated calculations
   
   ## Requirements
   - Investigate root cause of performance regression
   - Implement performance optimization fixes
   - Validate fixes restore performance to within 5% of baseline
   - Update benchmarks and baseline.json with verified measurements
   - Ensure no other commands regress during optimization
   ```

4. **Execute regression fix using standard PRP workflow**:
   - Run `/prep-prp` on the populated INITIAL.md
   - Generate PRP using `/generate-prp` with regression focus
   - Execute regression fix PRP BEFORE completing original feature PRP
   - Original feature remains incomplete until performance is restored

5. **Verify regression resolution and complete original PRP**:
   ```bash
   ./gradlew benchmark
   # Confirm no >10% degradation warnings
   # Confirm affected commands within acceptable thresholds
   # Complete original PRP with performance validation passed
   ```

### Regression Fix PRP Integration

The regression fix workflow integrates seamlessly with existing PRP tools:
- **INITIAL.md**: Contains regression context and requirements
- **`/prep-prp`**: Processes regression details into structured PRP
- **`/generate-prp`**: Creates focused performance optimization plan
- **Standard PRP execution**: Follows same quality gates and validation

### Regression Fix PRP Requirements

All regression fix PRPs must include:
- Root cause analysis of what caused the degradation
- Specific optimization strategies (avoid allocations, optimize hot paths, etc.)
- Before/after performance measurements with `./gradlew benchmark`
- Verification that the fix doesn't cause other regressions
- Update to baseline.json with new verified measurements
- Integration testing with `./gradlew run` demo application

---

# FOUNDATION REFERENCE
**Essential architecture patterns, performance optimization guidelines, and coding standards for ZPL parser implementation.**

## Reference Sections
The following comprehensive sections provide detailed implementation guidance:

- **[Foundation Reference](sections/foundation.md)** - Core architecture patterns and development requirements
- **[Quality Gates](sections/quality-gates.md)** - Mandatory validation and static analysis requirements
- **[TDD Workflow](sections/tdd-workflow.md)** - Test-driven development patterns with Kotest examples
- **[Performance Requirements](sections/performance.md)** - Benchmarking and regression handling
- **[Lexer Patterns](sections/patterns/lexer.md)** - Critical lexer implementation patterns and gotchas
- **[Parser Patterns](sections/patterns/parser.md)** - Parser-specific implementation requirements
- **[AST Patterns](sections/patterns/ast.md)** - AST node and visitor pattern requirements

## Quick Reference Summary

### Critical Implementation Patterns
- **STRICT TDD**: Write tests FIRST, confirm RED, achieve GREEN, then REFACTOR
- **expectingFieldData FIRST**: Critical token precedence in lexer to avoid bugs
- **Dynamic Characters**: Use caretChar, tildeChar, delimiterChar variables (never hardcode)
- **O(1) Command Lookup**: HashMap-based command recognition for performance
- **Sealed Class Hierarchy**: Type-safe AST nodes with visitor pattern
- **Position Tracking**: Include token position in all ParseExceptions

### Quality Gates (Non-Negotiable)
```bash
./gradlew ktlintFormat  # Auto-fix formatting
./gradlew ktlintCheck   # Zero violations required
./gradlew detekt        # Zero violations required
./gradlew test          # 100% pass rate required
./gradlew check         # All quality gates must pass
```

### Performance Requirements
- Simple commands: <0.1ms (100,000ns)
- Complex commands: <1ms (1,000,000ns)
- Coverage minimum: 80%
- Regression threshold: >10% triggers fix PRP

