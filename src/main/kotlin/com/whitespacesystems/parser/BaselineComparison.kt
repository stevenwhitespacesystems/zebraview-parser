package com.whitespacesystems.parser

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.abs

/**
 * Data model for individual benchmark results.
 */
@Serializable
data class BenchmarkResult(
    val benchmarkName: String,
    val commandName: String? = null,
    val commandType: String? = null,
    val averageTimeNs: Double,
    val memoryAllocatedBytes: Long = 0L,
    val throughputOpsPerSec: Double = 0.0,
    val gcOverheadPercent: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * Data model for performance baseline storage.
 */
@Serializable
data class PerformanceBaseline(
    val version: String,
    val createdAt: Long,
    val lastUpdated: Long = System.currentTimeMillis(),
    val description: String,
    val thresholds: PerformanceThresholds,
    val commandBenchmarks: Map<String, BenchmarkResult>,
    val e2eBenchmarks: Map<String, BenchmarkResult>,
    val memoryBenchmarks: Map<String, BenchmarkResult>,
    val history: BenchmarkHistory,
)

/**
 * Performance thresholds for regression detection.
 */
@Serializable
data class PerformanceThresholds(
    // 0.1ms
    val simpleCommandsMaxNs: Long = 100_000L,
    // 1ms
    val complexCommandsMaxNs: Long = 1_000_000L,
    // 10%
    val regressionThreshold: Double = 0.10,
    val description: String = "Performance thresholds for ZPL parser",
)

/**
 * Historical benchmark data for trend analysis.
 */
@Serializable
data class BenchmarkHistory(
    val description: String = "Historical performance measurements",
    val measurements: MutableList<HistoricalMeasurement> = mutableListOf(),
)

/**
 * Single historical measurement entry.
 */
@Serializable
data class HistoricalMeasurement(
    val timestamp: Long,
    val version: String,
    val results: Map<String, BenchmarkResult>,
)

/**
 * Comparison result between current and baseline performance.
 */
data class ComparisonResult(
    val benchmarkName: String,
    val baselineResult: BenchmarkResult?,
    val currentResult: BenchmarkResult,
    // Percentage change
    val performanceDelta: Double,
    val isRegression: Boolean,
    val regressionSeverity: RegressionSeverity,
)

/**
 * Severity levels for performance regression.
 */
enum class RegressionSeverity {
    NONE, // No regression or improvement
    MINOR, // 5-10% degradation
    MODERATE, // 10-25% degradation
    SEVERE, // 25-50% degradation
    CRITICAL, // >50% degradation
}

/**
 * Baseline comparison and regression detection utility.
 */
object BaselineComparison {
    // Performance analysis constants
    private object AnalysisConstants {
        const val REGRESSION_MINOR_THRESHOLD = 5.0
        const val REGRESSION_MODERATE_THRESHOLD = 10.0
        const val REGRESSION_SEVERE_THRESHOLD = 25.0
        const val REGRESSION_CRITICAL_THRESHOLD = 50.0

        const val TIME_FORMAT_NS_TO_US = 1_000.0
        const val TIME_FORMAT_NS_TO_MS = 1_000_000.0
        const val TIME_FORMAT_NS_TO_S = 1_000_000_000.0

        const val PERCENTAGE_CONVERSION = 100.0
        const val IMPROVEMENT_THRESHOLD = -5.0
    }

    private val json =
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

    private val baselineFile = File("baseline.json")

    /**
     * Load performance baseline from storage.
     */
    fun loadBaseline(): PerformanceBaseline? {
        return try {
            if (baselineFile.exists()) {
                val jsonText = baselineFile.readText()
                json.decodeFromString<PerformanceBaseline>(jsonText)
            } else {
                null
            }
        } catch (e: SerializationException) {
            println("Warning: Failed to load baseline due to serialization error: ${e.message}")
            null
        } catch (e: SecurityException) {
            println("Warning: Failed to load baseline due to security error: ${e.message}")
            null
        } catch (e: java.io.IOException) {
            println("Warning: Failed to load baseline due to IO error: ${e.message}")
            null
        }
    }

    /**
     * Save performance baseline to storage.
     */
    fun saveBaseline(baseline: PerformanceBaseline) {
        try {
            baselineFile.parentFile.mkdirs()
            val jsonText = json.encodeToString(baseline)
            baselineFile.writeText(jsonText)
        } catch (e: SerializationException) {
            println("Warning: Failed to save baseline due to serialization error: ${e.message}")
        } catch (e: SecurityException) {
            println("Warning: Failed to save baseline due to security error: ${e.message}")
        } catch (e: java.io.IOException) {
            println("Warning: Failed to save baseline due to IO error: ${e.message}")
        }
    }

    /**
     * Create initial baseline from current benchmark results.
     */
    fun createInitialBaseline(results: Map<String, BenchmarkResult>): PerformanceBaseline {
        val commandBenchmarks = mutableMapOf<String, BenchmarkResult>()
        val e2eBenchmarks = mutableMapOf<String, BenchmarkResult>()
        val memoryBenchmarks = mutableMapOf<String, BenchmarkResult>()

        // Categorize results by benchmark type
        results.forEach { (name, result) ->
            when {
                name.contains("Command") -> commandBenchmarks[name] = result
                name.contains("Label") || name.contains("E2E") -> e2eBenchmarks[name] = result
                name.contains("Memory") || name.contains("Allocation") ||
                    name.contains("Gc") -> memoryBenchmarks[name] = result
            }
        }

        return PerformanceBaseline(
            version = "1.0.0",
            createdAt = System.currentTimeMillis(),
            description = "Initial performance baseline for ZPL parser",
            thresholds = PerformanceThresholds(),
            commandBenchmarks = commandBenchmarks,
            e2eBenchmarks = e2eBenchmarks,
            memoryBenchmarks = memoryBenchmarks,
            history = BenchmarkHistory(),
        )
    }

    /**
     * Compare current results against baseline and detect regressions.
     */
    fun compareWithBaseline(
        currentResults: Map<String, BenchmarkResult>,
        baseline: PerformanceBaseline,
    ): List<ComparisonResult> {
        val comparisons = mutableListOf<ComparisonResult>()
        val allBaselineResults = baseline.commandBenchmarks + baseline.e2eBenchmarks + baseline.memoryBenchmarks

        currentResults.forEach { (name, current) ->
            val baselineResult = allBaselineResults[name]
            val comparison = compareResults(name, baselineResult, current, baseline.thresholds)
            comparisons.add(comparison)
        }

        return comparisons
    }

    /**
     * Compare individual benchmark results.
     */
    private fun compareResults(
        benchmarkName: String,
        baseline: BenchmarkResult?,
        current: BenchmarkResult,
        thresholds: PerformanceThresholds,
    ): ComparisonResult {
        val performanceDelta =
            if (baseline != null) {
                val deltaRatio = (current.averageTimeNs - baseline.averageTimeNs) / baseline.averageTimeNs
                deltaRatio * AnalysisConstants.PERCENTAGE_CONVERSION
            } else {
                0.0
            }

        val isRegression = performanceDelta > (thresholds.regressionThreshold * AnalysisConstants.PERCENTAGE_CONVERSION)
        val severity = calculateRegressionSeverity(performanceDelta)

        return ComparisonResult(
            benchmarkName = benchmarkName,
            baselineResult = baseline,
            currentResult = current,
            performanceDelta = performanceDelta,
            isRegression = isRegression,
            regressionSeverity = severity,
        )
    }

    /**
     * Calculate regression severity based on performance delta.
     */
    private fun calculateRegressionSeverity(performanceDelta: Double): RegressionSeverity {
        val absDelta = abs(performanceDelta)
        return when {
            absDelta < AnalysisConstants.REGRESSION_MINOR_THRESHOLD -> RegressionSeverity.NONE
            absDelta < AnalysisConstants.REGRESSION_MODERATE_THRESHOLD -> RegressionSeverity.MINOR
            absDelta < AnalysisConstants.REGRESSION_SEVERE_THRESHOLD -> RegressionSeverity.MODERATE
            absDelta < AnalysisConstants.REGRESSION_CRITICAL_THRESHOLD -> RegressionSeverity.SEVERE
            else -> RegressionSeverity.CRITICAL
        }
    }

    /**
     * Generate performance regression report.
     */
    fun generateRegressionReport(comparisons: List<ComparisonResult>): String {
        val report = StringBuilder()
        val regressions = comparisons.filter { it.isRegression }

        report.appendLine("═══ ZPL Parser Performance Regression Report ═══")
        report.appendLine()

        if (regressions.isEmpty()) {
            report.appendLine("✅ No performance regressions detected!")
            report.appendLine("All benchmarks are within acceptable performance thresholds.")
        } else {
            report.appendLine("⚠️  Performance regressions detected:")
            report.appendLine()

            regressions.sortedByDescending { it.performanceDelta }.forEach { regression ->
                val severity =
                    when (regression.regressionSeverity) {
                        RegressionSeverity.MINOR -> "⚠️  MINOR"
                        RegressionSeverity.MODERATE -> "🔶 MODERATE"
                        RegressionSeverity.SEVERE -> "🔺 SEVERE"
                        RegressionSeverity.CRITICAL -> "🚨 CRITICAL"
                        else -> "✅ OK"
                    }

                report.appendLine("$severity: ${regression.benchmarkName}")
                report.appendLine("  Performance change: ${"%.1f".format(regression.performanceDelta)}%")

                if (regression.baselineResult != null) {
                    report.appendLine("  Baseline: ${formatTime(regression.baselineResult.averageTimeNs)}")
                }
                report.appendLine("  Current:  ${formatTime(regression.currentResult.averageTimeNs)}")
                report.appendLine()
            }
        }

        // Performance summary
        report.appendLine("═══ Performance Summary ═══")
        report.appendLine("Total benchmarks: ${comparisons.size}")
        report.appendLine("Regressions found: ${regressions.size}")

        val improvements = comparisons.filter { it.performanceDelta < AnalysisConstants.IMPROVEMENT_THRESHOLD }
        if (improvements.isNotEmpty()) {
            report.appendLine("Performance improvements: ${improvements.size}")
        }

        return report.toString()
    }

    /**
     * Update baseline with current results and add to history.
     */
    fun updateBaseline(
        currentResults: Map<String, BenchmarkResult>,
        baseline: PerformanceBaseline,
        version: String = "1.0.0",
    ): PerformanceBaseline {
        // Add current results to history
        val historicalMeasurement =
            HistoricalMeasurement(
                timestamp = System.currentTimeMillis(),
                version = version,
                results = currentResults,
            )

        baseline.history.measurements.add(historicalMeasurement)

        // Update baseline with current results
        val updatedCommandBenchmarks = baseline.commandBenchmarks.toMutableMap()
        val updatedE2eBenchmarks = baseline.e2eBenchmarks.toMutableMap()
        val updatedMemoryBenchmarks = baseline.memoryBenchmarks.toMutableMap()

        currentResults.forEach { (name, result) ->
            when {
                name.contains("Command") -> updatedCommandBenchmarks[name] = result
                name.contains("Label") || name.contains("E2E") -> updatedE2eBenchmarks[name] = result
                name.contains("Memory") || name.contains("Allocation") ||
                    name.contains("Gc") -> updatedMemoryBenchmarks[name] = result
            }
        }

        return baseline.copy(
            lastUpdated = System.currentTimeMillis(),
            commandBenchmarks = updatedCommandBenchmarks,
            e2eBenchmarks = updatedE2eBenchmarks,
            memoryBenchmarks = updatedMemoryBenchmarks,
        )
    }

    /**
     * Check if results meet performance thresholds.
     */
    fun checkPerformanceThresholds(
        results: Map<String, BenchmarkResult>,
        thresholds: PerformanceThresholds,
    ): List<String> {
        val failures = mutableListOf<String>()

        results.forEach { (name, result) ->
            val threshold =
                when {
                    name.contains("StartFormat") || name.contains("EndFormat") -> thresholds.simpleCommandsMaxNs
                    else -> thresholds.complexCommandsMaxNs
                }

            if (result.averageTimeNs > threshold) {
                failures.add(
                    "$name: ${formatTime(result.averageTimeNs)} exceeds threshold ${formatTime(threshold.toDouble())}",
                )
            }
        }

        return failures
    }

    /**
     * Format time duration for human-readable output.
     */
    private fun formatTime(nanoseconds: Double): String {
        return when {
            nanoseconds < AnalysisConstants.TIME_FORMAT_NS_TO_US -> "${"%.0f".format(nanoseconds)}ns"
            nanoseconds < AnalysisConstants.TIME_FORMAT_NS_TO_MS -> "${"%.1f".format(
                nanoseconds / AnalysisConstants.TIME_FORMAT_NS_TO_US,
            )}μs"
            nanoseconds < AnalysisConstants.TIME_FORMAT_NS_TO_S -> "${"%.2f".format(
                nanoseconds / AnalysisConstants.TIME_FORMAT_NS_TO_MS,
            )}ms"
            else -> "${"%.3f".format(nanoseconds / AnalysisConstants.TIME_FORMAT_NS_TO_S)}s"
        }
    }
}
