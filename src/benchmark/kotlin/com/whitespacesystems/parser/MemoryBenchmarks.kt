package com.whitespacesystems.parser

import com.whitespacesystems.parser.BenchmarkData
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.ast.ZplProgram
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

/**
 * Memory allocation benchmarks for ZPL parser performance analysis.
 * 
 * Measures memory allocation patterns and garbage collection overhead
 * during ZPL parsing operations. These benchmarks help identify memory
 * efficiency bottlenecks and ensure minimal allocation per parse operation.
 * 
 * Key metrics measured:
 * - Memory allocation rate (bytes per operation)
 * - GC overhead and frequency  
 * - Object allocation patterns across different parsing scenarios
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgsAppend = [
    "-XX:+UseG1GC",  // Use G1 for consistent GC behavior
    "-XX:MaxGCPauseMillis=50", // Low pause time for accurate measurements
    "-Xms256m", "-Xmx512m" // Consistent heap size
])
open class MemoryBenchmarks {

    // === Small Allocation Benchmarks ===

    /**
     * Benchmark memory allocation for minimal ZPL parsing.
     * Smallest possible allocation: single character field data.
     */
    @Benchmark
    fun measureSmallAllocation(blackhole: Blackhole): ZplProgram {
        val lexer = Lexer("^XA^FO10,10^FDX^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program) // Prevent dead code elimination
        return program
    }

    /**
     * Benchmark memory allocation for empty label parsing.
     * Tests minimal ZPL structure memory footprint.
     */
    @Benchmark
    fun measureEmptyLabelAllocation(blackhole: Blackhole): ZplProgram {
        val lexer = Lexer("^XA^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    // === Medium Allocation Benchmarks ===

    /**
     * Benchmark memory allocation for simple complete labels.
     * Tests memory usage with basic multi-command parsing.
     */
    @Benchmark
    fun measureMediumAllocation(blackhole: Blackhole): ZplProgram {
        val zplCode = BenchmarkData.SIMPLE_LABELS.random()
        val lexer = Lexer(zplCode)
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    /**
     * Benchmark memory allocation for field positioning.
     * Tests coordinate parsing memory efficiency.
     */
    @Benchmark
    fun measureFieldPositioningAllocation(blackhole: Blackhole): ZplProgram {
        val lexer = Lexer("^XA^FO100,200^A0N,30,30^FDTest Field^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    // === Large Allocation Benchmarks ===

    /**
     * Benchmark memory allocation for complex labels.
     * Tests memory efficiency with real-world ZPL scenarios.
     */
    @Benchmark
    fun measureLargeAllocation(blackhole: Blackhole): ZplProgram {
        val zplCode = BenchmarkData.randomComplexLabel()
        val lexer = Lexer(zplCode)
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    /**
     * Benchmark memory allocation for very large labels.
     * Stress test memory usage with many fields and commands.
     */
    @Benchmark
    fun measureVeryLargeAllocation(blackhole: Blackhole): ZplProgram {
        val zplCode = BenchmarkData.randomLargeLabel()
        val lexer = Lexer(zplCode)
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    // === Token Generation Memory Benchmarks ===

    /**
     * Benchmark memory allocation during lexical analysis.
     * Isolates tokenization memory usage from parsing.
     */
    @Benchmark
    fun measureTokenizationAllocation(blackhole: Blackhole): List<com.whitespacesystems.parser.lexer.Token> {
        val zplCode = BenchmarkData.randomComplexLabel()
        val lexer = Lexer(zplCode)
        val tokens = lexer.tokenize()
        blackhole.consume(tokens)
        return tokens
    }

    /**
     * Benchmark memory allocation during AST construction.
     * Isolates parsing phase memory usage (assumes tokens exist).
     */
    @Benchmark
    fun measureAstConstructionAllocation(blackhole: Blackhole): ZplProgram {
        // Pre-tokenize to isolate AST construction memory
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDMemory Test^XZ")
        val tokens = lexer.tokenize()
        val program = ZplParser(tokens).parse()
        blackhole.consume(program)
        return program
    }

    // === Memory Efficiency Pattern Benchmarks ===

    /**
     * Benchmark memory allocation with repeated parsing.
     * Tests memory efficiency with parser reuse patterns.
     */
    @Benchmark
    fun measureRepeatedParsingAllocation(blackhole: Blackhole): ZplProgram {
        var result: ZplProgram? = null
        
        // Parse multiple simple labels to test allocation patterns
        repeat(10) {
            val lexer = Lexer("^XA^FO${it * 10},${it * 10}^FDField${it}^XZ")
            result = ZplParser(lexer.tokenize()).parse()
        }
        
        blackhole.consume(result)
        return result!!
    }

    /**
     * Benchmark memory allocation with different string lengths.
     * Tests field data memory scaling with text length.
     */
    @Benchmark
    fun measureVariableStringAllocation(blackhole: Blackhole): ZplProgram {
        val longText = "This is a longer text field that tests memory allocation with variable string lengths in field data"
        val lexer = Lexer("^XA^FO50,50^FD${longText}^XZ")
        val program = ZplParser(lexer.tokenize()).parse()
        blackhole.consume(program)
        return program
    }

    // === Garbage Collection Overhead Benchmarks ===

    /**
     * Benchmark with forced GC measurement.
     * Tests GC overhead during intensive parsing operations.
     */
    @Benchmark
    @Fork(value = 1, jvmArgsAppend = [
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:+LogVMOutput",
        "-verbose:gc"
    ])
    fun measureGcOverhead(blackhole: Blackhole): ZplProgram {
        // Parse multiple complex labels to trigger GC
        var result: ZplProgram? = null
        
        repeat(50) { i ->
            val zplCode = when (i % 3) {
                0 -> BenchmarkData.SIMPLE_LABELS.random()
                1 -> BenchmarkData.randomComplexLabel()
                else -> BenchmarkData.randomLargeLabel()
            }
            
            val lexer = Lexer(zplCode)
            result = ZplParser(lexer.tokenize()).parse()
        }
        
        blackhole.consume(result)
        return result!!
    }

    // === Memory Profiling Integration ===

    /**
     * Benchmark with allocation profiling enabled.
     * Provides detailed memory allocation profiling data.
     */
    @Benchmark
    @Fork(value = 1, jvmArgsAppend = [
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:+DebugNonSafepoints",
        "-XX:+FlightRecorder",
        "-XX:StartFlightRecording=duration=30s,filename=memory-profile.jfr"
    ])
    fun measureAllocationProfile(blackhole: Blackhole): ZplProgram {
        val testData = BenchmarkData.MEMORY_TEST_DATA
        var result: ZplProgram? = null
        
        // Use all memory test data sizes for comprehensive profiling
        testData.forEach { zplCode ->
            val lexer = Lexer(zplCode)
            result = ZplParser(lexer.tokenize()).parse()
            blackhole.consume(result)
        }
        
        return result!!
    }

    // === Parser Component Memory Analysis ===

    /**
     * Benchmark memory usage of different command types.
     * Compares allocation patterns across ZPL command varieties.
     */
    @Benchmark
    fun measureCommandTypeAllocation(blackhole: Blackhole): List<ZplProgram> {
        val results = mutableListOf<ZplProgram>()
        
        // Test each command type individually
        BenchmarkData.SIMPLE_COMMANDS.forEach { command ->
            val lexer = Lexer(command)
            val program = ZplParser(lexer.tokenize()).parse()
            results.add(program)
        }
        
        blackhole.consume(results)
        return results
    }
}