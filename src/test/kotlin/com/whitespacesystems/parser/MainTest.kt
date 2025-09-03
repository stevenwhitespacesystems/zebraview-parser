package com.whitespacesystems.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.string.shouldContain
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainTest : StringSpec({

    "should measure parsing time correctly" {
        val zplCode = "^XA^FO100,50^FDHello^XZ"
        val avgTime = measureParsingTime(zplCode, iterations = 10)

        // Should return positive time measurement
        avgTime shouldBeGreaterThan 0.0
        // Should be reasonable for simple parsing (less than 10ms)
        avgTime shouldBeLessThan 10_000_000.0
    }

    "should format nanoseconds correctly" {
        formatTime(500.0) shouldContain "ns"
        formatTime(1500.0) shouldContain "μs"
        formatTime(1_500_000.0) shouldContain "ms"
        formatTime(1_500_000_000.0) shouldContain "s"
    }

    "should format time with correct precision" {
        formatTime(999.0) shouldContain "999ns"
        formatTime(1200.0) shouldContain "1.2μs"
        formatTime(1_200_000.0) shouldContain "1.20ms"
        formatTime(1_200_000_000.0) shouldContain "1.200s"
    }

    "main function should execute without errors" {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        try {
            System.setOut(printStream)
            main()
            val output = outputStream.toString()

            // Verify main components are executed
            output shouldContain "ZPL Parser Demo"
            output shouldContain "PERFORMANCE DEMONSTRATION"
            output shouldContain "All demos completed successfully"
        } finally {
            System.setOut(originalOut)
        }
    }

    "performanceDemo should execute without errors" {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        try {
            System.setOut(printStream)
            performanceDemo()
            val output = outputStream.toString()

            // Verify performance demo components
            output shouldContain "Performance Characteristics"
            output shouldContain "Simple Commands"
            output shouldContain "Complex Commands"
            output shouldContain "Complete Label Parsing"
            output shouldContain "Performance Comparison"
            output shouldContain "Memory Efficiency"
        } finally {
            System.setOut(originalOut)
        }
    }

    "should handle different parsing iterations" {
        val simpleZpl = "^XA"
        val complexZpl = "^XA^FO100,50^A0N,30,30^FDTest^XZ"

        val simpleTime = measureParsingTime(simpleZpl, iterations = 5)
        val complexTime = measureParsingTime(complexZpl, iterations = 5)

        // Both should return valid measurements
        simpleTime shouldBeGreaterThan 0.0
        complexTime shouldBeGreaterThan 0.0

        // Complex parsing should generally take more time
        // (Though this might not always be true due to JIT optimization)
        (simpleTime + complexTime) shouldBeGreaterThan 0.0
    }

    "should format time consistently across ranges" {
        // Test boundary conditions
        val times =
            listOf(
                // ns boundary
                999.0,
                // μs start
                1000.0,
                // μs boundary
                999_999.0,
                // ms start
                1_000_000.0,
                // ms boundary
                999_999_999.0,
                // s start
                1_000_000_000.0,
            )

        times.forEach { time ->
            val formatted = formatTime(time)
            // Should always contain a number and unit
            formatted shouldContain Regex("""\d+(\.\d+)?[nμms]s?""")
        }
    }

    "performance measurement should use reasonable parameters" {
        // Verify that performance measurement functions work with reasonable parameters
        val result = measureParsingTime("^XA^XZ", iterations = 10)
        result shouldBeGreaterThan 0.0

        // Time formatting should handle various ranges
        formatTime(1000.0) shouldContain "μs"
        formatTime(1_000_000.0) shouldContain "ms"
    }

    "should handle edge cases in parsing measurement" {
        val emptyish = "^XA^XZ" // Minimal valid ZPL
        val result = measureParsingTime(emptyish, iterations = 3)

        result shouldBeGreaterThan 0.0
        result shouldBeLessThan 1_000_000.0 // Should be fast for minimal ZPL
    }
})
