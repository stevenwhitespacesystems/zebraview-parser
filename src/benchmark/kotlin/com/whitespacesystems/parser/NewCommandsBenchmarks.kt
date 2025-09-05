package com.whitespacesystems.parser

import com.whitespacesystems.parser.ast.ZplProgram
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

/**
 * Performance benchmarks for the new ZPL commands including BE (EAN-13).
 * Target thresholds: <0.1ms for simple commands, <1ms for complex commands.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class NewCommandsBenchmarks {

    // Simple commands (target: <100,000ns = 0.1ms)

    @Benchmark
    fun benchmarkFieldReverseCommand(): ZplProgram {
        val lexer = Lexer("^FR")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkFieldSeparatorCommand(): ZplProgram {
        val lexer = Lexer("^FS")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkCommentCommand(): ZplProgram {
        val lexer = Lexer("^FXSHIPPING LABEL - Order #12345")
        return ZplParser(lexer.tokenize()).parse()
    }

    // Moderate complexity commands (target: <1ms = 1,000,000ns)

    @Benchmark
    fun benchmarkChangeFontCommand(): ZplProgram {
        val lexer = Lexer("^CF0,30,20")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkChangeFontCommandDefaults(): ZplProgram {
        val lexer = Lexer("^CF")
        return ZplParser(lexer.tokenize()).parse()
    }

    // Complex commands (target: <1ms = 1,000,000ns)

    @Benchmark
    fun benchmarkGraphicBoxCommand(): ZplProgram {
        val lexer = Lexer("^GB150,100,5,W,3")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkBarCodeDefaultCommand(): ZplProgram {
        val lexer = Lexer("^BY3,2.5,100")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkCode128Command(): ZplProgram {
        val lexer = Lexer("^BCN,150,Y,N,N,A")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkCode128CommandComplex(): ZplProgram {
        val lexer = Lexer("^BCI,256,N,Y,Y,D")
        return ZplParser(lexer.tokenize()).parse()
    }

    // End-to-end scenarios with new commands

    @Benchmark
    fun benchmarkSimpleLabelWithNewCommands(): ZplProgram {
        val lexer = Lexer("^XA^FXTEST LABEL^FO100,100^FR^FDTest^FS^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkComplexLabelWithGraphics(): ZplProgram {
        val lexer = Lexer("^XA^CF0,25^FO50,50^GB400,300,4,B,2^FR^FDShipping^FS^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkLabelWithBarcode(): ZplProgram {
        val lexer = Lexer("^XA^BY3,2.5,100^FO100,100^BCN,150,Y,N,N,A^FD1234567890^FS^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkComprehensiveLabel(): ZplProgram {
        val zplCode = """
            ^XA
            ^FXCOMPREHENSIVE SHIPPING LABEL
            ^CFA,25,15
            ^FO50,50^GB400,300,4,B,0^FS
            ^FO100,100^FR^FDShip To:^FS
            ^FO100,150^FDJohn Doe^FS
            ^FO100,200^FD123 Main Street^FS
            ^BY2,2.5,50
            ^FO100,250^BCN,100,Y,N,N,A^FD1234567890^FS
            ^XZ
        """.trimIndent()
        
        val lexer = Lexer(zplCode)
        return ZplParser(lexer.tokenize()).parse()
    }

    // Mixed command scenarios for integration testing

    @Benchmark
    fun benchmarkMixedOldAndNewCommands(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^A0N,30^CF0,25^GB200,100,3^FR^FDTest^FS^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkMultipleGraphicBoxes(): ZplProgram {
        val lexer = Lexer("^XA^GB100,50,2,B,0^GB200,100,3,W,1^GB300,150,4,B,2^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkMultipleFontChanges(): ZplProgram {
        val lexer = Lexer("^XA^CFA,20^CFB,25^CF0,30^FO100,100^FDTest^FS^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }
}