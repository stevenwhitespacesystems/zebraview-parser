# QUALITY GATES & VALIDATION

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

## 🚨 CRITICAL: Static Analysis Rules (NON-NEGOTIABLE)

### Configuration Files - STRICTLY FORBIDDEN TO MODIFY
- **NEVER** modify `config/detekt.yml`
- **NEVER** modify any ktlint configuration 
- **NEVER** modify `build.gradle.kts` static analysis sections
- **NEVER** change linting rules, thresholds, or exclusions
- **NEVER** add ignore patterns or disable rules

### Code Quality Resolution - REQUIRED APPROACH
- **ALL detekt warnings/errors** must be fixed by changing CODE, not configuration
- **ALL ktlint formatting issues** must be fixed by changing CODE, not configuration
- **Fix violations through**: Refactoring, renaming, restructuring, optimization
- **Permitted quality commands**: `./gradlew ktlintFormat` (auto-fixes code)

### @Suppress Annotation Protocol - EXPLICIT APPROVAL REQUIRED
If a @Suppress annotation seems necessary:

1. **STOP implementation immediately**
2. **Document the exact issue**:
   ```
   Static Analysis Issue Requiring Suppression:
   - Rule: [detekt rule name or ktlint rule]
   - Error message: [exact error text]
   - Location: [file:line]
   - Attempted fixes: [what code changes were tried]
   - Why suppression needed: [detailed justification]
   ```
3. **Request explicit user approval** before proceeding
4. **Wait for confirmation** before adding any @Suppress annotation
5. **Document suppression reason** in code comment when approved

### Quality Gate Compliance
- `./gradlew check` MUST pass completely
- `./gradlew detekt` MUST show zero violations  
- `./gradlew ktlintCheck` MUST show zero violations
- Static analysis failures = incomplete implementation
- **NO exceptions, NO workarounds, NO configuration changes**

## Quality Checks (Run in Order)
```bash
# RECOMMENDED: All-in-one validation
./gradlew check                      # Runs ktlint, detekt, tests, coverage

# Individual steps if needed
./gradlew ktlintFormat               # Auto-fix code formatting
./gradlew detekt                     # Static analysis
./gradlew test                       # All tests
./gradlew jacocoTestReport           # Coverage report
./gradlew benchmark                  # Performance (when applicable)
./gradlew build && ./gradlew run     # Build and demo verification
```

## Final Implementation Checklist

### Prerequisites (MANDATORY BEFORE FEATURE COMPLETION)
- [ ] **FINAL QUALITY GATE RESOLUTION COMPLETED**: All linting, static analysis, and tests pass
  - [ ] `./gradlew ktlintCheck` - Zero violations
  - [ ] `./gradlew detekt` - Zero violations  
  - [ ] `./gradlew test` - 100% pass rate (not 99%, not 101/102)
  - [ ] `./gradlew check` - BUILD SUCCESSFUL

### Implementation Quality
- [ ] **TDD Compliance**: RED-GREEN-REFACTOR cycle followed strictly
  - [ ] Tests written FIRST before any implementation
  - [ ] Confirmed RED phase (tests failed initially)
  - [ ] Achieved GREEN phase (minimal implementation passes tests)
  - [ ] Completed REFACTOR phase (optimized without breaking tests)
- [ ] **Static Analysis Compliance**: Zero violations, NO configuration modifications, NO @Suppress without approval
- [ ] **Code Coverage**: 80%+ maintained via `./gradlew jacocoTestReport`
- [ ] **Build Verification**: `./gradlew clean build` and `./gradlew run` succeed