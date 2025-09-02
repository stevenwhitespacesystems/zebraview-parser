package com.whitespacesystems.parser

import com.whitespacesystems.parser.BenchmarkData
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.ast.ZplProgram
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

/**
 * Performance benchmarks for individual ZPL command parsing.
 * 
 * Measures parsing performance for each of the 5 supported ZPL commands:
 * - ^XA (StartFormatCommand) - Target: <0.1ms 
 * - ^XZ (EndFormatCommand) - Target: <0.1ms
 * - ^FO (FieldOriginCommand) - Target: <1ms
 * - ^FD (FieldDataCommand) - Target: <1ms  
 * - ^A (FontCommand) - Target: <1ms
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
open class CommandBenchmarks {

    // === Simple Commands (Target: <0.1ms = 100,000ns) ===

    /**
     * Benchmark parsing of ^XA (Start Format) command.
     * Simple command - should execute in <100,000ns (0.1ms).
     */
    @Benchmark
    fun benchmarkStartFormatCommand(): ZplProgram {
        val lexer = Lexer("^XA")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^XZ (End Format) command.
     * Simple command - should execute in <100,000ns (0.1ms).
     */
    @Benchmark
    fun benchmarkEndFormatCommand(): ZplProgram {
        val lexer = Lexer("^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Complex Commands (Target: <1ms = 1,000,000ns) ===

    /**
     * Benchmark parsing of ^FO (Field Origin) command with coordinates.
     * Complex command - should execute in <1,000,000ns (1ms).
     */
    @Benchmark
    fun benchmarkFieldOriginCommand(): ZplProgram {
        val lexer = Lexer("^FO100,50")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^FO command with large coordinates.
     * Tests coordinate parsing performance with larger numbers.
     */
    @Benchmark
    fun benchmarkFieldOriginLargeCoordinates(): ZplProgram {
        val lexer = Lexer("^FO999,999")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^FO command at origin.
     * Tests minimal coordinate parsing.
     */
    @Benchmark
    fun benchmarkFieldOriginAtOrigin(): ZplProgram {
        val lexer = Lexer("^FO0,0")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^FD (Field Data) command with text.
     * Complex command - should execute in <1,000,000ns (1ms).
     */
    @Benchmark
    fun benchmarkFieldDataCommand(): ZplProgram {
        val lexer = Lexer("^FDHello World")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^FD command with empty data.
     * Tests minimal field data parsing.
     */
    @Benchmark
    fun benchmarkFieldDataEmpty(): ZplProgram {
        val lexer = Lexer("^FD")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^FD command with special characters.
     * Tests field data parsing with pricing and special characters.
     */
    @Benchmark
    fun benchmarkFieldDataSpecialChars(): ZplProgram {
        val lexer = Lexer("^FDPrice: \$29.99")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^A (Font) command with full parameters.
     * Complex command - should execute in <1,000,000ns (1ms).
     */
    @Benchmark
    fun benchmarkFontCommandFull(): ZplProgram {
        val lexer = Lexer("^A0N,30,30")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^A command with minimal parameters.
     * Tests font command parsing with only font type and orientation.
     */
    @Benchmark
    fun benchmarkFontCommandMinimal(): ZplProgram {
        val lexer = Lexer("^A0N")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of ^A command with different font type.
     * Tests alternative font configurations.
     */
    @Benchmark
    fun benchmarkFontCommandAlternative(): ZplProgram {
        val lexer = Lexer("^ABN,25,25")
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Variable Command Benchmarks ===

    /**
     * Benchmark parsing of random simple commands.
     * Provides variable load testing for simple commands.
     */
    @Benchmark
    fun benchmarkRandomSimpleCommand(): ZplProgram {
        val command = BenchmarkData.randomSimpleCommand()
        val lexer = Lexer(command)
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Multiple Commands in Single Parse ===

    /**
     * Benchmark parsing multiple simple commands in sequence.
     * Tests parser performance with multiple commands.
     */
    @Benchmark
    fun benchmarkMultipleSimpleCommands(): ZplProgram {
        val lexer = Lexer("^XA^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing sequence of different command types.
     * Tests parser performance with mixed command types.
     */
    @Benchmark
    fun benchmarkMixedCommandSequence(): ZplProgram {
        val lexer = Lexer("^FO100,50^A0N,30,30^FDTest")
        return ZplParser(lexer.tokenize()).parse()
    }
}