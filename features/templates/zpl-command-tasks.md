# ZPL Command Implementation Tasks Template

This template defines the agent workflow for implementing ZPL commands using Test Driven Development.

---

## References
<!-- GENERATOR INSTRUCTIONS:
Use these documents as references when fleshing out the tasks:
-->

**PRP Document**: `features/[FEATURE_NAME]/stages/planning/zpl-command-prp.md`

## Requirements
- We are strictly using Test Driven Development following the RED → GREEN → REFACTOR pattern
- Tests MUST be created first and fail initially
- Code generated must be the minimal amount to pass the tests

---

## Phase 1: Test Creation (RED Phase)

### Task 1.1: Create Test Infrastructure

**Agent Call:**
- **Agent**: @kotlin-tdd-red-phase
- **Context**: Full PRP document from the planning stage
- **Task**: Create comprehensive failing tests and verify RED phase

**Arguments:**
```
Create comprehensive test files for the ZPL command based on the attached PRP document and verify RED phase completion.

REQUIREMENTS:
1. Create test file: src/test/kotlin/com/whitespacesystems/parser/parser/[COMMAND_NAME]Test.kt
2. Use Kotest StringSpec format matching existing tests in the codebase
3. Implement ALL test scenarios from PRP Section 5 (Test Scenarios)
4. Ultrathink about edge cases, boundary conditions, and error scenarios not covered in PRP
5. Follow existing test patterns in the codebase
6. Run tests to verify they fail (RED phase verification)

CONTEXT: This is the RED phase of TDD - create failing tests that will drive the implementation.

PRP DOCUMENT:
[ATTACH: features/[FEATURE_NAME]/stages/planning/zpl-command-prp.md]

STATE_PATH: `features/[FEATURE_NAME]/state.yml`

SUCCESS CRITERIA:
- [ ] Test file created with proper naming convention
- [ ] Tests follow Kotest StringSpec pattern
- [ ] All PRP Section 5 test scenarios implemented
- [ ] Tests compile but fail (RED phase confirmed)
- [ ] Additional edge cases identified and tested
- [ ] Test run output shows expected failures
```