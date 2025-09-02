## FEATURE:

**Performance Benchmarking System for ZPL Parser Commands**

Implement a comprehensive benchmarking system for ZPL command parsing to ensure the parser maintains high-performance standards and catches performance degradation early. The system should:

- Benchmark each existing ZPL command (^XA, ^XZ, ^FO, ^FD, ^A) for both execution time and memory usage
- Automatically benchmark any new commands added in the future
- Set aggressive performance thresholds targeting very low millisecond execution times (suitable for internet transfer scenarios)
- Integrate into validation workflow with dedicated Gradle task
- Report warnings when performance degrades without failing builds
- Test with both small and complex ZPL input data
- Update documentation (CLAUDE.md and prp_base) to reflect new architecture and quality checks

## EXAMPLES:

No existing benchmarking examples in the codebase. Will need to establish new patterns and reference existing code structure:

- Existing test patterns in `src/test/kotlin/` using Kotest StringSpec for structure reference
- Command implementations in `src/main/kotlin/com/whitespacesystems/parser/ast/` for integration points
- Parser logic in `src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt` for measurement points
- Build configuration in `build.gradle.kts` for new Gradle task integration
- Existing quality tools (Detekt, Ktlint, Jacoco) for workflow integration patterns

## DOCUMENTATION:

Research and reference documentation will be needed for:

- **JMH (Java Microbenchmark Harness)** - Industry standard for JVM performance testing
- **Kotlin benchmarking libraries** - Kotlin-specific performance testing tools and best practices
- **Gradle integration** - Documentation for creating custom Gradle tasks and integrating with existing build pipeline
- **JVM performance testing best practices** - Memory profiling, garbage collection considerations, warmup strategies
- **Baseline establishment methodologies** - Techniques for establishing and maintaining performance baselines
- **CI/CD benchmarking integration** - Automated performance testing in continuous integration workflows

## OTHER CONSIDERATIONS:

- **Performance Thresholds**: Establish aggressive sub-millisecond targets for individual command parsing (e.g., <0.1ms for simple commands, <1ms for complex commands) suitable for internet transfer scenarios
- **Gradle Task**: Create dedicated `./gradlew benchmark` task separate from regular testing workflow
- **Failure Strategy**: Report warnings only when performance degrades - do not fail builds to maintain development velocity
- **Baseline Management**: Implement automatic baseline establishment and storage mechanism, with ability to update baselines when intentional changes occur
- **Dual Metrics**: Benchmark both execution time (speed) and memory allocation/usage patterns
- **Test Data Variety**: Create benchmark suite with small ZPL labels (simple ^FO^FD combinations) and complex labels (multiple commands, large data, nested structures)
- **Documentation Updates**: Update CLAUDE.md sections on development workflow, quality checks, and architecture to include benchmarking requirements and process
- **Performance Regression Detection**: Implement percentage-based degradation detection (e.g., warn if >10% slower than baseline)
- **Warmup Considerations**: Account for JVM warmup periods in benchmark design to ensure accurate measurements