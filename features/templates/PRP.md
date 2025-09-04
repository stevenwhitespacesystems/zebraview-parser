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

## 6. Task Breakdown
<!-- GENERATOR INSTRUCTIONS:
Create tasks following TDD workflow with agent assignments
Mark tasks that can run in PARALLEL with [P] prefix
Sequential tasks with [S] prefix
Each task should specify which agent type is best suited

Agent Types:
- general-purpose: Complex multi-step tasks, searches, research
- code-writer: File creation, implementation, code modifications
- test-runner: Running tests, validation, benchmarks
- reviewer: Code review, quality checks
-->

### Execution State Management
<!-- CRITICAL: Single source of truth -->
State file: `features/[feature-id]/agents/state.json`
```json
{
  "current_phase": "[1|2|3]",
  "current_iteration": 0,
  "max_iterations": 50,
  "phase_2_parallel": {
    "ast_node": "pending|in_progress|complete",
    "visitor_interface": "pending|in_progress|complete",
    "visitor_impl": "pending|in_progress|complete"
  },
  "validation_loop": {
    "iteration": 0,
    "last_failure": "none",
    "fixes_applied": []
  }
}
```
All agents MUST update this atomically.

### Phase 1: Test Creation (RED Phase)
**[S] Task 1.1: Create Test File** @code-writer
- Create: `src/test/kotlin/.../parser/[CommandName]Test.kt`
- Output: `features/[feature-id]/agents/phase-1/tests-created.md`
- Context format:
  ```markdown
  ## Tests Created
  - [ ] Test file created: [CommandName]Test.kt
  - [ ] Basic functionality tests (X tests)
  - [ ] Edge case tests (Y tests)
  - [ ] Error scenario tests (Z tests)
  ```

**[S] Task 1.2: Confirm RED** @test-runner  
- Run: `./gradlew test --tests "*[CommandName]Test"`
- Output: `features/[feature-id]/agents/phase-1/red-confirmation.md`
- Context format:
  ```markdown
  ## RED Phase Confirmation
  - [x] All tests failing as expected
  - Total tests: X
  - All failing: ✓
  ```

### Phase 2: Initial Implementation (Parallel Work)
**[P] Task 2.1: Create AST Node** @code-writer
- Create: `src/main/kotlin/.../ast/[CommandName]Command.kt`
- From: Section 3 AST Node Structure
- Output: `features/[feature-id]/agents/phase-2/ast-node.md`

**[P] Task 2.2: Update Visitor Interface** @code-writer
- Update: `ZplNodeVisitor.kt`
- Add: `fun visit([commandName]: [CommandName]Command): T`
- Output: `features/[feature-id]/agents/phase-2/visitor-interface.md`

**[P] Task 2.3: Implement Visitor** @code-writer
- Update: `AstPrinterVisitorImpl.kt`
- Add: Visitor implementation
- Output: `features/[feature-id]/agents/phase-2/visitor-impl.md`

**[S] Task 2.4: Add Parser Method** @code-writer
- Update: `ZplParser.kt`
- Add: `parse[CommandName]()` method
- Integrate: Add to main parser switch
- Output: `features/[feature-id]/agents/phase-2/parser-method.md`

**[S] Task 2.5: Update Lexer** @code-writer
- Update: `Lexer.kt` commandInfo map
- Add: Command registration
- Output: `features/[feature-id]/agents/phase-2/lexer-update.md`

**[S] Task 2.6: Create Utilities** @code-writer (if needed)
- Skip if: No complex parsing needed
- Create: `[CommandName]ParsingUtils.kt`
- Output: `features/[feature-id]/agents/phase-2/utilities.md`

### Phase 3: Validation & Fix Loop

**LOOP START:**

**Loop Tracker File:** `features/[feature-id]/agents/validation/loop-tracker.md`
```markdown
## Validation Loop Tracker
- Loop Count: [N]/50
- Started: [timestamp]
- Current Status: [phase]

### Iteration History
1. [timestamp] - Failed: Tests (5 failures) → Fixed
2. [timestamp] - Failed: Coverage (75%) → Fixed  
3. [timestamp] - Failed: Detekt (3 violations) → Fixed

### Current Iteration: [N]
- [ ] Tests: PENDING
- [ ] Coverage: PENDING
- [ ] Static Analysis: PENDING
- [ ] Linting: PENDING
- [ ] Performance: PENDING
- [ ] Integration: PENDING
- [ ] Demo: PENDING
```

**[S] Task 3.1: Run Tests** @test-runner
- Run: `./gradlew test --tests "*[CommandName]Test"`
- Update: `loop-tracker.md` - mark Tests checkbox
- Success (100% pass): → Continue to 3.2
- Failure: → Go to FIX-3.1
- Output: `features/[feature-id]/agents/validation/test-results.md`

**[S] FIX-3.1: Fix Test Failures** @code-writer
- Input: `validation/test-results.md`
- Update: `loop-tracker.md` - increment loop count
- Context format:
  ```markdown
  ## Test Failures - Iteration [N]
  Loop Count: [N]/50

  ### Failures to Fix
  - [x] Test: "should parse minimal BC command" 
    - Reason: Expected BCCommand but got null
    - Fix: Added BC to parser switch statement
    - Fixed in: ZplParser.kt line 234

  - [ ] Test: "should handle empty field data"
    - Reason: NullPointerException on empty string
    - Fix needed: Add null check in parseBCCommand()
  ```
- → Return to 3.1

**[S] Task 3.2: Check Coverage** @test-runner
- Run: `./gradlew jacocoTestReport`
- Success (≥80%): → Continue to 3.3
- Failure: → Go to FIX-3.2
- Output: `features/[feature-id]/agents/validation/coverage.md`

**[S] FIX-3.2: Add Missing Coverage** @code-writer
- Input: `validation/coverage.md`
- Context format:
  ```markdown
  ## Coverage Gaps - Iteration [N]
  Current Coverage: 75%

  ### Uncovered Code
  - [ ] BCCommand.kt line 45-48 (validation method)
    - Need: Test for invalid mode combinations
  ```
- → Return to 3.1 (rerun all tests)

**[P] Task 3.3: Static Analysis** @test-runner
- Run: `./gradlew detekt`
- Success (zero violations): → Continue to 3.4
- Failure: → Go to FIX-3.3
- Output: `features/[feature-id]/agents/validation/detekt.md`

**[P] FIX-3.3: Fix Static Analysis** @code-writer
- Input: `validation/detekt.md`
- NEVER suppress, always fix
- → Return to 3.3

**[P] Task 3.4: Linting** @test-runner
- Run: `./gradlew ktlintFormat` then `./gradlew ktlintCheck`
- Success (zero violations): → Continue to 3.5
- Failure: → Go to FIX-3.4
- Output: `features/[feature-id]/agents/validation/linting.md`

**[P] FIX-3.4: Fix Linting** @code-writer
- Input: `validation/linting.md`
- → Return to 3.4

**[S] Task 3.5: Create Benchmarks** @code-writer (if Complex)
- Skip if: Simple command OR benchmarks exist
- Create: `src/benchmark/kotlin/.../[CommandName]Benchmarks.kt`
- Output: `features/[feature-id]/agents/validation/benchmark-created.md`

**[S] Task 3.6: Performance Check** @test-runner (if benchmarks exist)
- Run: `./gradlew benchmark`
- Success (no >10% regression): → Continue to 3.7
- Failure: → Go to FIX-3.6
- Output: `features/[feature-id]/agents/validation/performance.md`

**[S] FIX-3.6: Optimize Performance** @code-writer
- Input: `validation/performance.md`
- → Return to 3.1 (ensure optimization didn't break tests)

**[S] Task 3.7: Integration Test** @test-runner
- Run: `./gradlew check`
- Success (BUILD SUCCESSFUL): → Continue to 3.8
- Failure: → Analyze and fix specific failure
- Output: `features/[feature-id]/agents/validation/integration.md`

**[S] Task 3.8: Demo Validation** @test-runner
- Run: `./gradlew run`
- Success: → COMPLETE
- Failure: → Go to FIX-3.8
- Output: `features/[feature-id]/agents/validation/demo.md`

**[S] FIX-3.8: Fix Runtime Issues** @code-writer
- Input: `validation/demo.md`
- → Return to 3.1

**LOOP END**

### Master Progress (User-Visible)
`features/[feature-id]/agents/progress.md`:
```markdown
## Feature Implementation Progress
**Auto-updated by agents - visible in real-time**

### Current Status: VALIDATION LOOP
- Loop Iteration: [N]/50
- Last Update: [timestamp]

### Phase Progress
✅ Phase 1: Test Creation - COMPLETE
✅ Phase 2: Implementation - COMPLETE
🔄 Phase 3: Validation - IN PROGRESS

### Validation Status (Iteration [N])
- ⏳ Tests: 90% passing (2/20 failing)
- ⏳ Coverage: 77% (target 80%)
- ⏳ Static Analysis: 2 violations remaining
- ✅ Linting: PASSED
- ⏸️ Performance: NOT RUN YET

### Loop History
| Iteration | Failed On | Issues | Time |
|-----------|-----------|--------|------|
| 1 | Tests | 20 failures | 2 min |
| 2 | Tests | 8 failures | 1 min |
| 3 | Coverage | 65% | 3 min |
```

### Recovery Patterns
<!-- GENERATOR: Include as-is -->
#### Intelligent Failure Handling
- Iterations 1-5: Focus on implementation fixes
- Iterations 6-10: Check test correctness
- Iterations 11-15: Consider architectural changes
- Iterations 16+: Document blockers for escalation

#### Circular Fix Detection
If same test fails 3 times after being "fixed":
- Flag as potential test design issue
- Try alternative implementation approach
- Document pattern for human review

#### Performance Recovery
If regression persists after 5 iterations:
- Profile specific slow operations
- Check for unnecessary object creation
- Consider algorithm change needed

### Escalation Conditions
<!-- WHEN TO STOP AND ASK FOR HELP -->
STOP and escalate if:
- Iteration count exceeds 50
- Research findings conflict with codebase reality
- Circular dependency detected between fixes
- Performance requirement appears impossible
- Test expectations seem incorrect (not implementation)
- Architecture change required (new base classes, etc.)

Escalation output: `features/[feature-id]/agents/ESCALATION.md`
Include: Full state.json, last 5 iterations, specific blocker

### Loop Control
```yaml
LOOP_START: Task 3.1
LOOP_CONDITION: Any validation task fails
LOOP_MAX_ITERATIONS: 50
LOOP_TRACKER: validation/loop-tracker.md
ABORT_CONDITION: Loop count > 50
SUCCESS_CONDITION: All validation checkboxes marked complete
```

### Parallelization Notes
- Phase 2: Tasks 2.1-2.3 run in parallel, then 2.4-2.6 sequential
- Phase 3: Tasks 3.3-3.4 can run in parallel after tests pass
- Fix tasks are targeted to specific issues, not full reimplementation
- Loop iteration limit: 50

## 7. Project Standards
<!-- STATIC SECTION - Copy exactly as shown -->
See [CLAUDE.md](../../CLAUDE.md) for complete project guidelines.

### Build Commands
```bash
./gradlew build          # Build project
./gradlew test           # Run tests
./gradlew ktlintCheck    # Check linting
./gradlew ktlintFormat   # Auto-fix formatting
./gradlew detekt         # Static analysis
./gradlew benchmark      # Performance tests
./gradlew check          # All quality gates
```

### Requirements
- **Coverage**: ≥80% minimum
- **Performance**: <0.1ms simple, <1ms complex commands
- **Quality**: Zero violations (linting, static analysis)
- **Config Files**: NEVER edit detekt.yml, ktlint config without approval
- **Suppressions**: NEVER use @Suppress without explicit approval

## 8. Architecture Patterns
<!-- STATIC SECTION - Copy exactly as shown -->
### Critical Implementation Rules
1. **Token Precedence**: `expectingFieldData` MUST come first in lexer
2. **Dynamic Characters**: Never hardcode `^`, `~`, `,` - use variables
3. **Command Lookup**: Use HashMap for O(1) recognition
4. **Visitor Pattern**: All nodes implement accept() method
5. **Error Handling**: Include position in ParseExceptions

### Parser Flow
```
Lexer.kt → Token → Parser.kt → AST Node → Visitor Pattern
```

## 9. TDD Workflow
<!-- STATIC SECTION - Copy exactly as shown -->
### Mandatory Process
1. **RED**: Write ALL tests first (they must fail)
2. **GREEN**: Write minimal code to pass tests
3. **REFACTOR**: Optimize only after tests pass

### Never
- Write implementation before tests
- Skip the RED phase
- Suppress test failures

## 10. Final Quality Checklist
<!-- STATIC SECTION - Copy exactly as shown -->
**NO FEATURE IS COMPLETE UNTIL ALL PASS:**

- [ ] `./gradlew ktlintFormat` → `ktlintCheck` (zero violations)
- [ ] `./gradlew detekt` (zero violations)
- [ ] `./gradlew test` (100% pass)
- [ ] `./gradlew jacocoTestReport` (≥80% coverage)
- [ ] `./gradlew benchmark` (no >10% regression)
- [ ] `./gradlew check` (BUILD SUCCESSFUL)
- [ ] `./gradlew run` (demo works)

Feature is INCOMPLETE if ANY check fails.

## 11. Definition of Done
<!-- BEYOND JUST PASSING TESTS -->
### Implementation Complete When:
- [x] All tests passing (100%)
- [x] Coverage ≥80%
- [x] Zero linting violations
- [x] Zero static analysis issues
- [x] No performance regressions >10%
- [x] Demo application runs successfully
- [x] Real-world ZPL samples parse correctly (if provided)
- [x] Error messages are clear and actionable
- [x] No TODOs or code smells remaining
- [x] Follows all existing patterns

### Final Metrics
- Total iterations: [N]
- Total time: [duration]
- Files modified: [count]
- Final coverage: [%]