# PERFORMANCE REQUIREMENTS & BENCHMARKING

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

## Performance Validation Tasks (MANDATORY for new ZPL commands)

```yaml
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
```

## Performance Requirements (MANDATORY for new commands)
- [ ] Performance benchmarks created in `src/benchmark/kotlin/`
- [ ] Benchmark thresholds met (<0.1ms simple, <1ms complex)
- [ ] No regression in existing commands (>10% triggers fix PRP)
- [ ] `./gradlew benchmark` passes without degradation warnings
- [ ] baseline.json updated if performance improved