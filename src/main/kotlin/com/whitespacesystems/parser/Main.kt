package com.whitespacesystems.parser

import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.utils.AstPrinter

fun main() {
    val printer = AstPrinter()

    println("🎉 ZPL Parser Demo - Parsing Complete ZPL Programs 🎉")
    println("=".repeat(60))

    // Demo 1: Simple label
    println("\n📋 Demo 1: Simple Label")
    println("ZPL: ^FO100,50^A0N,30,30^FDHello World")
    val demo1Lexer = Lexer("^FO100,50^A0N,30,30^FDHello World")
    val demo1Parser = ZplParser(demo1Lexer.tokenize())
    val demo1Program = demo1Parser.parse()
    println("AST:")
    println(printer.print(demo1Program))

    // Demo 2: Multiple fields with special characters
    println("\n📋 Demo 2: Price Label with Special Characters")
    println("ZPL: ^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)")
    val demo2Lexer = Lexer("^FO10,10^ABN,25^FDPrice: \$29.99^FO10,40^ABN,20^FD(50% OFF!)")
    val demo2Parser = ZplParser(demo2Lexer.tokenize())
    val demo2Program = demo2Parser.parse()
    println("AST:")
    println(printer.print(demo2Program))

    // Demo 3: Various font styles
    println("\n📋 Demo 3: Different Font Variations")
    println("ZPL: ^FO0,0^A^FDDefault^FO0,30^A0^FDScalable^FO0,60^ABR,20^FDRotated")
    val demo3Lexer = Lexer("^FO0,0^A^FDDefault^FO0,30^A0^FDScalable^FO0,60^ABR,20^FDRotated")
    val demo3Parser = ZplParser(demo3Lexer.tokenize())
    val demo3Program = demo3Parser.parse()
    println("AST:")
    println(printer.print(demo3Program))

    // Demo 4: NEW! Complete ZPL Label with ^XA/^XZ boundaries
    println("\n📋 Demo 4: Complete ZPL Label with ^XA/^XZ Format Commands")
    println("ZPL: ^XA^FO100,50^A0N,30^FDComplete Label^XZ")
    val demo4Lexer = Lexer("^XA^FO100,50^A0N,30^FDComplete Label^XZ")
    val demo4Parser = ZplParser(demo4Lexer.tokenize())
    val demo4Program = demo4Parser.parse()
    println("AST:")
    println(printer.print(demo4Program))

    // Demo 5: Real-world product label with boundaries
    println("\n📋 Demo 5: Real-World Product Label with Format Boundaries")
    println("ZPL: ^XA^FO300,30^A0N,30^FDProduct Label^FO20,100^A0N,25^FDSKU: 123456^XZ")
    val demo5Lexer = Lexer("^XA^FO300,30^A0N,30^FDProduct Label^FO20,100^A0N,25^FDSKU: 123456^XZ")
    val demo5Parser = ZplParser(demo5Lexer.tokenize())
    val demo5Program = demo5Parser.parse()
    println("AST:")
    println(printer.print(demo5Program))

    // Demo 6: Multiple complete labels
    println("\n📋 Demo 6: Multiple Complete ZPL Labels")
    println("ZPL: ^XA^FO10,10^FDFirst Label^XZ^XA^FO20,20^FDSecond Label^XZ")
    val demo6Lexer = Lexer("^XA^FO10,10^FDFirst Label^XZ^XA^FO20,20^FDSecond Label^XZ")
    val demo6Parser = ZplParser(demo6Lexer.tokenize())
    val demo6Program = demo6Parser.parse()
    println("AST:")
    println(printer.print(demo6Program))

    // Demo 7: Performance Demonstration
    println("\n" + "=".repeat(60))
    println("📊 PERFORMANCE DEMONSTRATION")
    println("=".repeat(60))
    
    performanceDemo(printer)
    
    println("\n" + "=".repeat(60))
    println("✅ All demos completed successfully!")
    println("✅ Parser supports: ^XA/^XZ (format boundaries), ^FO (positioning), ^FD (text data), ^A (fonts)")
    println("✅ Handles: complete ZPL II labels, commas in data, empty fields, various font formats")
    println("✅ Built with TDD: RED → GREEN → REFACTOR")
    println("✅ Full test coverage with Kotest")
    println("✅ Code quality with Detekt & Ktlint")
    println("✅ NEW: Support for industry-standard ZPL II format boundaries!")
    println("🚀 NEW: Comprehensive performance benchmarking system!")
}

fun performanceDemo(printer: AstPrinter) {
    println("\n📊 Demo 7: Performance Characteristics")
    println("Testing parser performance across different ZPL command types...")
    
    // Simple commands demonstration
    println("\n🏃‍♂️ Simple Commands (Target: <0.1ms)")
    val simpleCommands = listOf("^XA", "^XZ")
    simpleCommands.forEach { command ->
        val avgTime = measureParsingTime(command, iterations = 1000)
        val status = if (avgTime < 100_000) "✅ FAST" else "⚠️ SLOW"
        println("  $command: ${formatTime(avgTime)} avg $status")
    }
    
    // Complex commands demonstration  
    println("\n🔧 Complex Commands (Target: <1ms)")
    val complexCommands = listOf(
        "^FO100,50",
        "^FDHello World",
        "^A0N,30,30"
    )
    complexCommands.forEach { command ->
        val avgTime = measureParsingTime(command, iterations = 1000)
        val status = if (avgTime < 1_000_000) "✅ FAST" else "⚠️ SLOW"
        println("  $command: ${formatTime(avgTime)} avg $status")
    }
    
    // End-to-end label parsing demonstration
    println("\n📋 Complete Label Parsing")
    val testLabels = listOf(
        "^XA^FO100,50^FDHello^XZ",
        "^XA^FO100,50^A0N,30,30^FDWorld^XZ",
        "^XA^FO50,50^A0N,28^FDProduct Label^FO50,100^ABN,20^FDPrice: $29.99^XZ"
    )
    testLabels.forEachIndexed { index, label ->
        val avgTime = measureParsingTime(label, iterations = 100)
        val complexity = if (label.length < 50) "Simple" else "Complex" 
        println("  $complexity Label ${index + 1}: ${formatTime(avgTime)} avg")
        if (index == 0) {
            println("    ZPL: ${label.take(40)}${if (label.length > 40) "..." else ""}")
        }
    }
    
    // Performance comparison demonstration
    println("\n⚖️ Performance Comparison")
    val baselineTime = 800_000.0 // 0.8ms baseline
    val currentTime = measureParsingTime("^FO100,50", iterations = 1000)
    
    val deltaPercent = ((currentTime - baselineTime) / baselineTime) * 100.0
    val comparison = when {
        deltaPercent > 10 -> "📈 REGRESSION (${String.format("%.1f", deltaPercent)}% slower)"
        deltaPercent < -10 -> "📉 IMPROVEMENT (${String.format("%.1f", -deltaPercent)}% faster)" 
        else -> "✅ STABLE (${String.format("%.1f", deltaPercent)}% change)"
    }
    
    println("  Baseline: ${formatTime(baselineTime)}")
    println("  Current:  ${formatTime(currentTime)}")
    println("  Status:   $comparison")
    
    println("\n💡 Performance Tips:")
    println("  • Use ./gradlew benchmark for detailed performance analysis")
    println("  • Monitor regression with BaselineComparison.kt utilities")
    println("  • Target: Simple commands <0.1ms, Complex commands <1ms")
    println("  • Performance thresholds defined in baseline.json")
    
    // Memory efficiency demonstration
    println("\n🧠 Memory Efficiency")
    val beforeMemory = Runtime.getRuntime().let { it.totalMemory() - it.freeMemory() }
    
    repeat(1000) {
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDPerformance Test^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        // Consume result to prevent dead code elimination
        program.commands.size
    }
    
    System.gc() // Suggest garbage collection
    Thread.sleep(100) // Allow GC to run
    
    val afterMemory = Runtime.getRuntime().let { it.totalMemory() - it.freeMemory() }
    val memoryUsed = afterMemory - beforeMemory
    
    println("  Memory usage (1000 parses): ${memoryUsed / 1024}KB")
    println("  Average per parse: ${memoryUsed / 1000}B")
    println("  Efficiency: ${if (memoryUsed < 1024 * 100) "✅ EFFICIENT" else "⚠️ HIGH"}")
}

fun measureParsingTime(zplCode: String, iterations: Int): Double {
    // Warmup
    repeat(100) {
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
        nanoseconds < 1_000 -> "${String.format("%.0f", nanoseconds)}ns"
        nanoseconds < 1_000_000 -> "${String.format("%.1f", nanoseconds / 1_000)}μs"  
        nanoseconds < 1_000_000_000 -> "${String.format("%.2f", nanoseconds / 1_000_000)}ms"
        else -> "${String.format("%.3f", nanoseconds / 1_000_000_000)}s"
    }
}
