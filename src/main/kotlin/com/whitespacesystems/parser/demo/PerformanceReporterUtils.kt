package com.whitespacesystems.parser.demo

/**
 * Utility functions for performance reporting extracted from Main.kt.
 */
object PerformanceReporterUtils {
    private object Thresholds {
        const val REGRESSION_THRESHOLD_PERCENT = 10.0
        const val IMPROVEMENT_THRESHOLD_PERCENT = -5.0
        const val MEMORY_EFFICIENCY_BYTES = 1024 * 100 // 100KB
    }

    fun formatPerformanceComparison(deltaPercent: Double): String {
        return when {
            deltaPercent > Thresholds.REGRESSION_THRESHOLD_PERCENT -> "📈 REGRESSION (${"%.1f".format(
                deltaPercent,
            )}% slower)"
            deltaPercent < Thresholds.IMPROVEMENT_THRESHOLD_PERCENT -> "📉 IMPROVEMENT (${"%.1f".format(
                -deltaPercent,
            )}% faster)"
            else -> "✅ STABLE (${"%.1f".format(deltaPercent)}% change)"
        }
    }

    fun formatMemoryEfficiency(memoryUsedBytes: Long): String {
        return if (memoryUsedBytes < Thresholds.MEMORY_EFFICIENCY_BYTES) "✅ EFFICIENT" else "⚠️ HIGH"
    }

    fun generatePerformanceTips(): List<String> {
        return listOf(
            "Use StringBuilder for string concatenation in hot paths",
            "Cache frequently used objects to reduce allocations",
            "Profile with JProfiler or similar tools for optimization",
            "Consider object pooling for high-frequency operations",
        )
    }
}
