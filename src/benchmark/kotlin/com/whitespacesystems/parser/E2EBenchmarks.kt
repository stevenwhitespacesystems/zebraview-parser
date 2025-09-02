package com.whitespacesystems.parser

import com.whitespacesystems.parser.BenchmarkData
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.ast.ZplProgram
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

/**
 * End-to-end performance benchmarks for complete ZPL label parsing workflows.
 * 
 * Tests realistic parsing scenarios from simple labels to complex multi-field
 * labels that represent real-world ZPL usage patterns. These benchmarks measure
 * the complete parsing pipeline: lexing → parsing → AST generation.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
open class E2EBenchmarks {

    // === Simple Label Benchmarks ===

    /**
     * Benchmark parsing of basic ZPL label with single field.
     * Minimal complete label: ^XA^FO100,50^FDHello^XZ
     */
    @Benchmark
    fun benchmarkSimpleSingleFieldLabel(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^FDHello^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of label with font specification.
     * Basic label with font: ^XA^FO100,50^A0N,30,30^FDWorld^XZ
     */
    @Benchmark
    fun benchmarkSimpleLabelWithFont(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDWorld^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of empty label (format markers only).
     * Minimal ZPL structure: ^XA^XZ
     */
    @Benchmark
    fun benchmarkEmptyLabel(): ZplProgram {
        val lexer = Lexer("^XA^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of label with minimal positioning.
     * Basic positioning test: ^XA^FO10,10^FDTest Label^XZ
     */
    @Benchmark
    fun benchmarkMinimalPositioningLabel(): ZplProgram {
        val lexer = Lexer("^XA^FO10,10^FDTest Label^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Complex Label Benchmarks ===

    /**
     * Benchmark parsing of multi-field product label.
     * Real-world product label with pricing, expiration, and promotional text.
     */
    @Benchmark
    fun benchmarkProductLabel(): ZplProgram {
        val zplCode = "^XA^FO50,50^A0N,28^FDProduct Label^FO50,100^ABN,20^FDPrice: \$29.99^FO50,130^ABN,16^FD(50% OFF!)^FO50,180^A0N,14^FDExp: 12/31/2024^XZ"
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of multi-field inventory label.
     * Business document with multiple positioned fields and data.
     */
    @Benchmark
    fun benchmarkInventoryLabel(): ZplProgram {
        val zplCode = "^XA^FO10,10^A0N,25^FDItem: ABC123^FO10,40^A0N,20^FDQty: 5^FO200,10^A0N,25^FDPrice: \$99.99^FO200,40^A0N,20^FDTotal: \$499.95^XZ"
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of shipping label.
     * Complex label with address fields, tracking, and priority marking.
     */
    @Benchmark
    fun benchmarkShippingLabel(): ZplProgram {
        val zplCode = "^XA^FO30,30^A0N,30^FDSHIP TO:^FO30,70^A0N,20^FDJohn Smith^FO30,100^A0N,20^FD123 Main St^FO30,130^A0N,20^FDCityville, ST 12345^FO30,180^A0B,25^FDTRACKING: 1Z999E999^FO400,30^A0N,40^FDPRIORITY^XZ"
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of retail price label.
     * Pricing label with original price, sale price, and savings calculation.
     */
    @Benchmark
    fun benchmarkRetailPriceLabel(): ZplProgram {
        val zplCode = "^XA^FO50,20^A0N,35^FDWireless Mouse^FO50,70^A0N,20^FDModel: WM-2024^FO50,100^A0N,20^FDSKU: 123456789^FO50,140^A0N,50^FD\$24.99^FO200,140^A0N,20^FDwas \$39.99^FO50,200^A0N,16^FDSave 37%!^XZ"
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Large-Scale Label Benchmarks ===

    /**
     * Benchmark parsing of large inventory report label.
     * Stress test with 15+ inventory items (many fields and commands).
     */
    @Benchmark
    fun benchmarkLargeInventoryLabel(): ZplProgram {
        val zplCode = BenchmarkData.LARGE_LABELS[0] // Generated large inventory label
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of repeated field patterns label.
     * Scalability test with repetitive field structures.
     */
    @Benchmark
    fun benchmarkRepeatedFieldsLabel(): ZplProgram {
        val zplCode = BenchmarkData.LARGE_LABELS[1] // Generated repeated fields label
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Variable Load Benchmarks ===

    /**
     * Benchmark parsing of random simple labels.
     * Variable load testing with different simple label patterns.
     */
    @Benchmark
    fun benchmarkRandomSimpleLabel(): ZplProgram {
        val zplCode = BenchmarkData.SIMPLE_LABELS.random()
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of random complex labels.
     * Variable load testing with different complex label patterns.
     */
    @Benchmark
    fun benchmarkRandomComplexLabel(): ZplProgram {
        val zplCode = BenchmarkData.randomComplexLabel()
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark parsing of random large labels.
     * Stress testing with variable large label structures.
     */
    @Benchmark
    fun benchmarkRandomLargeLabel(): ZplProgram {
        val zplCode = BenchmarkData.randomLargeLabel()
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Throughput Benchmarks ===

    /**
     * Benchmark throughput with simple label parsing.
     * Measures operations per second for simple labels.
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    fun benchmarkSimpleLabelThroughput(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^FDHello^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    /**
     * Benchmark throughput with complex label parsing.
     * Measures operations per second for complex labels.
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    fun benchmarkComplexLabelThroughput(): ZplProgram {
        val zplCode = BenchmarkData.COMPLEX_LABELS[0] // First complex label
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    // === Parser Pipeline Component Benchmarks ===

    /**
     * Benchmark just the lexing phase for complex labels.
     * Isolates lexer performance from parser performance.
     */
    @Benchmark
    fun benchmarkLexingPhaseOnly(): List<com.whitespacesystems.parser.lexer.Token> {
        val zplCode = BenchmarkData.randomComplexLabel()
        val lexer = Lexer(zplCode)
        return lexer.tokenize()
    }

    /**
     * Benchmark the complete parsing pipeline with pre-tokenized input.
     * Isolates parser performance (assumes tokens are already available).
     */
    @Benchmark
    fun benchmarkParsingPhaseOnly(): ZplProgram {
        // Use a pre-computed token list to isolate parser performance
        val lexer = Lexer("^XA^FO100,50^A0N,30,30^FDTest Label^XZ")
        val tokens = lexer.tokenize() // This happens in @Setup in real scenarios
        return ZplParser(tokens).parse()
    }
}