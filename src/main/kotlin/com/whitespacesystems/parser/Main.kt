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
    println("✅ Parser supports: ^XA/^XZ (format boundaries), ^FO (positioning), ^FD (text data), ^A (fonts)")
    println("✅ Handles: complete ZPL II labels, commas in data, empty fields, various font formats")
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

    // Demo 1: Simple label
    val demo1Output = DemoRunner.runSimpleLabelDemo()
    println(DemoRunner.formatDemoOutput("Demo 1: Simple Label", "^FO100,50^A0N,30,30^FDHello World", demo1Output))

    // Demo 2: Price label with special characters
    val demo2Zpl = "^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)"
    val demo2Lexer = Lexer(demo2Zpl)
    val demo2Program = ZplParser(demo2Lexer.tokenize()).parse()
    println(
        DemoRunner.formatDemoOutput(
            "Demo 2: Price Label with Special Characters",
            demo2Zpl,
            printer.print(demo2Program),
        ),
    )

    // Demo 3: Font variations
    val demo3Zpl = "^FO0,0^A^FDDefault^FO0,30^A0^FDScalable^FO0,60^ABR,20^FDRotated"
    val demo3Lexer = Lexer(demo3Zpl)
    val demo3Program = ZplParser(demo3Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 3: Different Font Variations", demo3Zpl, printer.print(demo3Program)))

    // Demo 4: Complete ZPL label with boundaries
    val demo4Output = DemoRunner.runCompleteLabelDemo()
    println(
        DemoRunner.formatDemoOutput(
            "Demo 4: Complete ZPL Label with ^XA/^XZ Format Commands",
            "^XA^FO100,50^A0N,30^FDComplete Label^XZ",
            demo4Output,
        ),
    )

    // Demo 5: Product label
    val demo5Zpl = "^XA^FO300,30^A0N,30^FDProduct Label^FO20,100^A0N,25^FDSKU: 123456^XZ"
    val demo5Lexer = Lexer(demo5Zpl)
    val demo5Program = ZplParser(demo5Lexer.tokenize()).parse()
    println(
        DemoRunner.formatDemoOutput(
            "Demo 5: Real-World Product Label with Format Boundaries",
            demo5Zpl,
            printer.print(demo5Program),
        ),
    )

    // Demo 6: Multiple labels
    val demo6Zpl = "^XA^FO10,10^FDFirst Label^XZ^XA^FO20,20^FDSecond Label^XZ"
    val demo6Lexer = Lexer(demo6Zpl)
    val demo6Program = ZplParser(demo6Lexer.tokenize()).parse()
    println(DemoRunner.formatDemoOutput("Demo 6: Multiple Complete ZPL Labels", demo6Zpl, printer.print(demo6Program)))
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
    println("\n🔧 Complex Commands (Target: <1ms)")
    val complexCommands = listOf("^FO100,50", "^FDHello World", "^A0N,30,30")
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
            "^XA^FO100,50^FDHello^XZ",
            "^XA^FO100,50^A0N,30,30^FDWorld^XZ",
            "^XA^FO50,50^A0N,28^FDProduct Label^FO50,100^ABN,20^FDPrice: $29.99^XZ",
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
    val currentTime = measureParsingTime("^FO100,50", iterations = PerformanceLimits.SIMPLE_ITERATIONS)
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
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDPerformance Test^XZ")
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
