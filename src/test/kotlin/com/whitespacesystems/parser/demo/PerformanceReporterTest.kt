package com.whitespacesystems.parser.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.string.shouldContain

class PerformanceReporterTest : StringSpec({

    "should format performance comparison result" {
        val result = PerformanceReporterUtils.formatPerformanceComparison(-8.0)
        result.shouldContain("IMPROVEMENT")
        result.shouldContain("8.0% faster")
    }

    "should format regression performance result" {
        val result = PerformanceReporterUtils.formatPerformanceComparison(15.0)
        result.shouldContain("REGRESSION")
        result.shouldContain("15.0% slower")
    }

    "should format stable performance result" {
        val result = PerformanceReporterUtils.formatPerformanceComparison(2.0)
        result.shouldContain("STABLE")
        result.shouldContain("2.0% change")
    }

    "should format memory efficiency result for efficient usage" {
        val result = PerformanceReporterUtils.formatMemoryEfficiency(50 * 1024) // 50KB
        result.shouldContain("EFFICIENT")
    }

    "should format memory efficiency result for high usage" {
        val result = PerformanceReporterUtils.formatMemoryEfficiency(150 * 1024) // 150KB
        result.shouldContain("HIGH")
    }

    "should generate performance tips" {
        val tips = PerformanceReporterUtils.generatePerformanceTips()
        tips shouldContain "Use StringBuilder for string concatenation in hot paths"
        tips shouldContain "Cache frequently used objects to reduce allocations"
        tips shouldContain "Profile with JProfiler or similar tools for optimization"
    }
})
