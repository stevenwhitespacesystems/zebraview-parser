package com.whitespacesystems.parser

import com.whitespacesystems.parser.demo.DemoRunner
import com.whitespacesystems.parser.demo.PerformanceReporterUtils
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.utils.AstPrinter

private object PerformanceLimits {
    const val SIMPLE_COMMAND_THRESHOLD_NS = 100_000L // 0.1ms
    const val COMPLEX_COMMAND_THRESHOLD_NS = 1_000_000L // 1ms
    const val BASELINE_TIME_NS = 800_000.0 // 0.8ms baseline
    const val WARMUP_ITERATIONS = 100
    const val SIMPLE_ITERATIONS = 1000
    const val LABEL_ITERATIONS = 100
    const val MEMORY_TEST_ITERATIONS = 1000
    const val GC_SLEEP_MS = 100L
    const val TIME_CONVERSION_FACTOR_US = 1_000.0
    const val TIME_CONVERSION_FACTOR_MS = 1_000_000.0
    const val TIME_CONVERSION_FACTOR_S = 1_000_000_000.0
    const val MEMORY_KB_CONVERSION = 1024
    const val REGRESSION_THRESHOLD_PERCENT = 10.0
    const val IMPROVEMENT_THRESHOLD_PERCENT = -10.0

    // Display and formatting constants
    const val MAX_LABEL_PREVIEW_LENGTH = 40
    const val SIMPLE_LABEL_LENGTH_THRESHOLD = 50
    const val MEMORY_EFFICIENCY_THRESHOLD_KB = 100
    const val BORDER_LENGTH = 60
    const val PERCENTAGE_MULTIPLIER = 100.0
}

fun main() {
    println("🎉 ZPL Parser Demo - Parsing Complete ZPL Programs 🎉")
    println("=".repeat(PerformanceLimits.BORDER_LENGTH))

    // Run demo suite
    runDemoSuite()

    // Performance demonstration
    println("\n" + "=".repeat(PerformanceLimits.BORDER_LENGTH))
    println("📊 PERFORMANCE DEMONSTRATION")
    println("=".repeat(PerformanceLimits.BORDER_LENGTH))
    performanceDemo()

    // Success summary
    println("\n" + "=".repeat(PerformanceLimits.BORDER_LENGTH))
    println("✅ All demos completed successfully!")
    println("✅ Parser supports: ^XA/^XZ (format boundaries)")
    println("✅ Handles: complete ZPL II labels with start/end format commands")
    println("✅ Built with TDD: RED → GREEN → REFACTOR")
    println("✅ Full test coverage with Kotest")
    println("✅ Code quality with Detekt & Ktlint")
    println("✅ NEW: Support for industry-standard ZPL II format boundaries!")
    println("🚀 NEW: Comprehensive performance benchmarking system!")
}

fun performanceDemo() {
    println("\n📊 Demo 7: Performance Characteristics")
    println("Testing parser performance across different ZPL command types...")

    // Command performance demonstrations
    runSimpleCommandDemo()
    runComplexCommandDemo()
    runLabelParsingDemo()

    // Performance comparison
    runPerformanceComparison()

    // Performance guidance
    println("\n💡 Performance Tips:")
    println("  • Use ./gradlew benchmark for detailed performance analysis")
    println("  • Monitor regression with BaselineComparison.kt utilities")
    println("  • Target: Simple commands <0.1ms, Complex commands <1ms")
    println("  • Performance thresholds defined in baseline.json")

    // Memory efficiency test
    runMemoryEfficiencyTest()
}

fun measureParsingTime(
    zplCode: String,
    iterations: Int,
): Double {
    // Warmup
    repeat(PerformanceLimits.WARMUP_ITERATIONS) {
        val lexer = Lexer(zplCode)
        ZplParser(lexer.tokenize()).parse()
    }

    // Measure
    var totalTime = 0L
    repeat(iterations) {
        val lexer = Lexer(zplCode)
        val startTime = System.nanoTime()
        ZplParser(lexer.tokenize()).parse()
        val endTime = System.nanoTime()
        totalTime += (endTime - startTime)
    }

    return totalTime.toDouble() / iterations
}

fun formatTime(nanoseconds: Double): String {
    return when {
        nanoseconds < PerformanceLimits.TIME_CONVERSION_FACTOR_US -> "${"%.0f".format(nanoseconds)}ns"
        nanoseconds < PerformanceLimits.TIME_CONVERSION_FACTOR_MS -> "${"%.1f".format(
            nanoseconds / PerformanceLimits.TIME_CONVERSION_FACTOR_US,
        )}μs"
        nanoseconds < PerformanceLimits.TIME_CONVERSION_FACTOR_S -> "${"%.2f".format(
            nanoseconds / PerformanceLimits.TIME_CONVERSION_FACTOR_MS,
        )}ms"
        else -> "${"%.3f".format(nanoseconds / PerformanceLimits.TIME_CONVERSION_FACTOR_S)}s"
    }
}

private fun runDemoSuite() {
    val printer = AstPrinter()

    // Demo 1: Simple XA/XZ
    val demo1Zpl = "^XA^XZ"
    val demo1Lexer = Lexer(demo1Zpl)
    val demo1Program = ZplParser(demo1Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 1: Simple XA/XZ Label", demo1Zpl, printer.print(demo1Program)))

    // Demo 2: Single XA command
    val demo2Zpl = "^XA"
    val demo2Lexer = Lexer(demo2Zpl)
    val demo2Program = ZplParser(demo2Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 2: Start Format Command Only", demo2Zpl, printer.print(demo2Program)))

    // Demo 3: Single XZ command
    val demo3Zpl = "^XZ"
    val demo3Lexer = Lexer(demo3Zpl)
    val demo3Program = ZplParser(demo3Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 3: End Format Command Only", demo3Zpl, printer.print(demo3Program)))

    // Demo 4: Multiple labels
    val demo4Zpl = "^XA^XZ^XA^XZ"
    val demo4Lexer = Lexer(demo4Zpl)
    val demo4Program = ZplParser(demo4Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 4: Multiple Complete ZPL Labels", demo4Zpl, printer.print(demo4Program)))

    // Demo 5: XA/XZ with whitespace
    val demo5Zpl = " ^XA ^XZ "
    val demo5Lexer = Lexer(demo5Zpl)
    val demo5Program = ZplParser(demo5Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 5: XA/XZ with Whitespace", demo5Zpl, printer.print(demo5Program)))
}

private fun runSimpleCommandDemo() {
    println("\n🏃‍♂️ Simple Commands (Target: <0.1ms)")
    val simpleCommands = listOf("^XA", "^XZ")
    simpleCommands.forEach { command ->
        val avgTime = measureParsingTime(command, iterations = PerformanceLimits.SIMPLE_ITERATIONS)
        val status = if (avgTime < PerformanceLimits.SIMPLE_COMMAND_THRESHOLD_NS) "✅ FAST" else "⚠️ SLOW"
        println("  $command: ${formatTime(avgTime)} avg $status")
    }
}

private fun runComplexCommandDemo() {
    println("\n🔧 Complex Sequences (Target: <1ms)")
    val complexCommands = listOf("^XA^XZ", "^XA^XZ^XA^XZ")
    complexCommands.forEach { command ->
        val avgTime = measureParsingTime(command, iterations = PerformanceLimits.SIMPLE_ITERATIONS)
        val status = if (avgTime < PerformanceLimits.COMPLEX_COMMAND_THRESHOLD_NS) "✅ FAST" else "⚠️ SLOW"
        println("  $command: ${formatTime(avgTime)} avg $status")
    }
}

private fun runLabelParsingDemo() {
    println("\n📋 Complete Label Parsing")
    val testLabels =
        listOf(
            "^XA^XZ",
            "^XA^XZ^XA^XZ",
            "^XA^XZ^XA^XZ^XA^XZ",
        )
    testLabels.forEachIndexed { index, label ->
        val avgTime = measureParsingTime(label, iterations = PerformanceLimits.LABEL_ITERATIONS)
        val complexity = if (label.length < PerformanceLimits.SIMPLE_LABEL_LENGTH_THRESHOLD) "Simple" else "Complex"
        println("  $complexity Label ${index + 1}: ${formatTime(avgTime)} avg")
        if (index == 0) {
            val preview = label.take(PerformanceLimits.MAX_LABEL_PREVIEW_LENGTH)
            val suffix = if (label.length > PerformanceLimits.MAX_LABEL_PREVIEW_LENGTH) "..." else ""
            println("    ZPL: $preview$suffix")
        }
    }
}

private fun runPerformanceComparison() {
    println("\n⚖️ Performance Comparison")
    val baselineTime = PerformanceLimits.BASELINE_TIME_NS
    val currentTime = measureParsingTime("^XA^XZ", iterations = PerformanceLimits.SIMPLE_ITERATIONS)
    val deltaPercent = ((currentTime - baselineTime) / baselineTime) * PerformanceLimits.PERCENTAGE_MULTIPLIER
    val comparison = PerformanceReporterUtils.formatPerformanceComparison(deltaPercent)

    println("  Baseline: ${formatTime(baselineTime)}")
    println("  Current:  ${formatTime(currentTime)}")
    println("  Status:   $comparison")
}

private fun runMemoryEfficiencyTest() {
    println("\n🧠 Memory Efficiency")
    val beforeMemory = Runtime.getRuntime().let { it.totalMemory() - it.freeMemory() }

    repeat(PerformanceLimits.MEMORY_TEST_ITERATIONS) {
        val lexer = Lexer("^XA^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        // Consume result to prevent dead code elimination
        program.commands.size
    }

    // Allow JVM to stabilize memory usage
    Thread.sleep(PerformanceLimits.GC_SLEEP_MS)

    val afterMemory = Runtime.getRuntime().let { it.totalMemory() - it.freeMemory() }
    val memoryUsed = afterMemory - beforeMemory
    val memoryEfficiency = PerformanceReporterUtils.formatMemoryEfficiency(memoryUsed)

    println(
        "  Memory usage (${PerformanceLimits.MEMORY_TEST_ITERATIONS} parses): " +
            "${memoryUsed / PerformanceLimits.MEMORY_KB_CONVERSION}KB",
    )
    println("  Average per parse: ${memoryUsed / PerformanceLimits.MEMORY_TEST_ITERATIONS}B")
    println("  Efficiency: $memoryEfficiency")
}
