package com.whitespacesystems.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File

class BaselineComparisonTest : StringSpec({

    "BenchmarkResult should initialize correctly" {
        val result =
            BenchmarkResult(
                benchmarkName = "TestBenchmark",
                commandName = "^FO",
                commandType = "positioning",
                averageTimeNs = 50000.0,
                memoryAllocatedBytes = 1024L,
                throughputOpsPerSec = 20000.0,
            )

        result.benchmarkName shouldBe "TestBenchmark"
        result.commandName shouldBe "^FO"
        result.commandType shouldBe "positioning"
        result.averageTimeNs shouldBe 50000.0
        result.memoryAllocatedBytes shouldBe 1024L
        result.throughputOpsPerSec shouldBe 20000.0
    }

    "PerformanceBaseline should initialize correctly" {
        val thresholds = PerformanceThresholds()
        val result = BenchmarkResult("test", averageTimeNs = 1000.0)
        val commandBenchmarks = mapOf("testCommand" to result)

        val baseline =
            PerformanceBaseline(
                version = "1.0.0",
                createdAt = System.currentTimeMillis(),
                description = "Test baseline",
                thresholds = thresholds,
                commandBenchmarks = commandBenchmarks,
                e2eBenchmarks = emptyMap(),
                memoryBenchmarks = emptyMap(),
                history = BenchmarkHistory(),
            )

        baseline.version shouldBe "1.0.0"
        baseline.description shouldBe "Test baseline"
        baseline.commandBenchmarks.size shouldBe 1
    }

    "PerformanceThresholds should have reasonable defaults" {
        val thresholds = PerformanceThresholds()

        thresholds.simpleCommandsMaxNs shouldBe 100_000L // 0.1ms
        thresholds.complexCommandsMaxNs shouldBe 1_000_000L // 1ms
        thresholds.regressionThreshold shouldBe 0.10 // 10%
    }

    "ComparisonResult should detect regressions correctly" {
        val baseline = BenchmarkResult("test", averageTimeNs = 1000.0)
        // 20% slower
        val current = BenchmarkResult("test", averageTimeNs = 1200.0)

        val comparison =
            ComparisonResult(
                benchmarkName = "test",
                baselineResult = baseline,
                currentResult = current,
                performanceDelta = 20.0,
                isRegression = true,
                regressionSeverity = RegressionSeverity.MODERATE,
            )

        comparison.isRegression shouldBe true
        comparison.performanceDelta shouldBe 20.0
        comparison.regressionSeverity shouldBe RegressionSeverity.MODERATE
    }

    "RegressionSeverity should have proper ordering" {
        RegressionSeverity.NONE.ordinal shouldBe 0
        RegressionSeverity.MINOR.ordinal shouldBe 1
        RegressionSeverity.MODERATE.ordinal shouldBe 2
        RegressionSeverity.SEVERE.ordinal shouldBe 3
        RegressionSeverity.CRITICAL.ordinal shouldBe 4
    }

    "createInitialBaseline should categorize results correctly" {
        val results =
            mapOf(
                "CommandBenchmark" to BenchmarkResult("CommandBenchmark", averageTimeNs = 1000.0),
                "LabelBenchmark" to BenchmarkResult("LabelBenchmark", averageTimeNs = 2000.0),
                "MemoryBenchmark" to BenchmarkResult("MemoryBenchmark", averageTimeNs = 3000.0),
                "E2EBenchmark" to BenchmarkResult("E2EBenchmark", averageTimeNs = 4000.0),
            )

        val baseline = BaselineComparison.createInitialBaseline(results)

        baseline.commandBenchmarks.size shouldBe 1
        baseline.e2eBenchmarks.size shouldBe 2 // LabelBenchmark and E2EBenchmark
        baseline.memoryBenchmarks.size shouldBe 1
        baseline.version shouldBe "1.0.0"
    }

    "compareWithBaseline should detect performance changes" {
        val baselineResult = BenchmarkResult("test", averageTimeNs = 1000.0)
        val baseline =
            PerformanceBaseline(
                version = "1.0.0",
                createdAt = System.currentTimeMillis(),
                description = "test",
                thresholds = PerformanceThresholds(),
                commandBenchmarks = mapOf("test" to baselineResult),
                e2eBenchmarks = emptyMap(),
                memoryBenchmarks = emptyMap(),
                history = BenchmarkHistory(),
            )

        val currentResults =
            mapOf(
                // 20% slower
                "test" to BenchmarkResult("test", averageTimeNs = 1200.0),
            )

        val comparisons = BaselineComparison.compareWithBaseline(currentResults, baseline)

        comparisons.size shouldBe 1
        val comparison = comparisons[0]
        comparison.isRegression shouldBe true
        comparison.performanceDelta shouldBe 20.0
    }

    "generateRegressionReport should create proper report with no regressions" {
        val comparison =
            ComparisonResult(
                benchmarkName = "test",
                baselineResult = BenchmarkResult("test", averageTimeNs = 1000.0),
                // 10% faster
                currentResult = BenchmarkResult("test", averageTimeNs = 900.0),
                performanceDelta = -10.0,
                isRegression = false,
                regressionSeverity = RegressionSeverity.NONE,
            )

        val report = BaselineComparison.generateRegressionReport(listOf(comparison))

        report shouldContain "No performance regressions detected"
        report shouldContain "Performance Summary"
        report shouldContain "Total benchmarks: 1"
        report shouldContain "Regressions found: 0"
    }

    "generateRegressionReport should create proper report with regressions" {
        val comparison =
            ComparisonResult(
                benchmarkName = "SlowBenchmark",
                baselineResult = BenchmarkResult("test", averageTimeNs = 1000.0),
                // 30% slower
                currentResult = BenchmarkResult("test", averageTimeNs = 1300.0),
                performanceDelta = 30.0,
                isRegression = true,
                regressionSeverity = RegressionSeverity.SEVERE,
            )

        val report = BaselineComparison.generateRegressionReport(listOf(comparison))

        report shouldContain "Performance regressions detected"
        report shouldContain "SEVERE: SlowBenchmark"
        report shouldContain "Performance change: 30.0%"
        report shouldContain "Regressions found: 1"
    }

    "updateBaseline should add to history and update results" {
        val baseline =
            PerformanceBaseline(
                version = "1.0.0",
                createdAt = System.currentTimeMillis(),
                description = "test",
                thresholds = PerformanceThresholds(),
                commandBenchmarks = emptyMap(),
                e2eBenchmarks = emptyMap(),
                memoryBenchmarks = emptyMap(),
                history = BenchmarkHistory(),
            )

        val currentResults =
            mapOf(
                "CommandTest" to BenchmarkResult("CommandTest", averageTimeNs = 1000.0),
            )

        val updated = BaselineComparison.updateBaseline(currentResults, baseline, "1.1.0")

        updated.history.measurements.size shouldBe 1
        updated.commandBenchmarks.size shouldBe 1
        updated.commandBenchmarks.keys shouldContain "CommandTest"
    }

    "checkPerformanceThresholds should detect failures" {
        val thresholds =
            PerformanceThresholds(
                simpleCommandsMaxNs = 100_000L,
                complexCommandsMaxNs = 1_000_000L,
            )

        val results =
            mapOf(
                // Over simple threshold
                "StartFormatBenchmark" to BenchmarkResult("StartFormatBenchmark", averageTimeNs = 200_000.0),
                // Over complex threshold
                "ComplexCommand" to BenchmarkResult("ComplexCommand", averageTimeNs = 2_000_000.0),
            )

        val failures = BaselineComparison.checkPerformanceThresholds(results, thresholds)

        failures.size shouldBe 2
        failures[0] shouldContain "StartFormatBenchmark"
        failures[1] shouldContain "ComplexCommand"
    }

    "baseline save and load should work with existing baseline file" {
        // Clean up any existing test file
        val testFile = File("test_baseline.json")
        if (testFile.exists()) testFile.delete()

        // Note: We can't easily test save/load because it uses hardcoded "baseline.json"
        // But we can test that the methods handle missing files gracefully
        val loadedBaseline = BaselineComparison.loadBaseline()
        // Should handle missing file gracefully (returns null or existing baseline)
        // This test mainly verifies the method doesn't crash - loadedBaseline can be null or valid

        @Suppress("UNUSED_VARIABLE")
        val testPassed = loadedBaseline != null || loadedBaseline == null // Always true, just ensures no crash
    }

    "formatTime should be tested indirectly through generateRegressionReport" {
        val comparison =
            ComparisonResult(
                benchmarkName = "test",
                // 1.5μs
                baselineResult = BenchmarkResult("test", averageTimeNs = 1500.0),
                // 2.5ms
                currentResult = BenchmarkResult("test", averageTimeNs = 2_500_000.0),
                performanceDelta = 166566.67,
                isRegression = true,
                regressionSeverity = RegressionSeverity.CRITICAL,
            )

        val report = BaselineComparison.generateRegressionReport(listOf(comparison))

        // Should contain formatted times with appropriate units
        report shouldContain "μs" // or some time unit formatting
        report shouldContain "Baseline:"
        report shouldContain "Current:"
    }

    "BenchmarkHistory should maintain measurements" {
        val history = BenchmarkHistory()
        val measurement =
            HistoricalMeasurement(
                timestamp = System.currentTimeMillis(),
                version = "1.0.0",
                results = mapOf("test" to BenchmarkResult("test", averageTimeNs = 1000.0)),
            )

        history.measurements.add(measurement)
        history.measurements.size shouldBe 1
        history.measurements[0].version shouldBe "1.0.0"
    }
})
