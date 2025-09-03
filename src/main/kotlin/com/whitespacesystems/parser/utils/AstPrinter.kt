package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.ZplProgram

/**
 * AST printer that produces a human-readable representation of the ZPL AST.
 * Useful for debugging and understanding the parsed structure.
 *
 * Uses composition pattern with AstPrinterVisitorImpl to separate concerns
 * and reduce function count per class.
 */
class AstPrinter {
    private val visitor = AstPrinterVisitorImpl()

    /**
     * Print a ZPL program as a formatted string.
     */
    fun print(program: ZplProgram): String = program.accept(visitor)

    /**
     * Print any ZPL program with custom formatting options.
     */
    fun printWithIndentation(
        program: ZplProgram,
        indent: String = "  ",
    ): String {
        val result = program.accept(visitor)
        return if (indent != "  ") {
            result.replace("  ", indent)
        } else {
            result
        }
    }

    /**
     * Print a compact representation without extra whitespace.
     */
    fun printCompact(program: ZplProgram): String {
        return program.accept(visitor)
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
