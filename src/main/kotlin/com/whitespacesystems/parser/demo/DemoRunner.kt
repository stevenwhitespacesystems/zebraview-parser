package com.whitespacesystems.parser.demo

import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.parser.ZplParser
import com.whitespacesystems.parser.utils.AstPrinter

/**
 * Handles demo execution logic extracted from Main.kt to reduce method length.
 */
object DemoRunner {
    private val printer = AstPrinter()

    fun runSimpleLabelDemo(): String {
        val zplCode = "^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        return printer.print(program)
    }

    fun runCompleteLabelDemo(): String {
        val zplCode = "^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        return printer.print(program)
    }

    fun runPerformanceCommandDemo(): String {
        val result = StringBuilder()
        result.append("Simple Command Performance\n")
        result.append("Complex Command Performance\n")
        return result.toString()
    }

    fun formatDemoOutput(
        title: String,
        zplCode: String,
        astOutput: String,
    ): String {
        return buildString {
            append("📋 $title\n")
            append("ZPL: $zplCode\n")
            append("AST:\n")
            append("$astOutput\n")
        }
    }
}
