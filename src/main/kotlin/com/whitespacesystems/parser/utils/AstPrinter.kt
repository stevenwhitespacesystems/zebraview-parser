package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.ast.ZplNodeVisitor
import com.whitespacesystems.parser.ast.ZplProgram

/**
 * AST printer that produces a human-readable representation of the ZPL AST.
 * Useful for debugging and understanding the parsed structure.
 */
class AstPrinter : ZplNodeVisitor<String> {
    fun print(program: ZplProgram): String = program.accept(this)

    override fun visitZplProgram(program: ZplProgram): String {
        val commands = program.commands.joinToString("\n") { "  ${it.accept(this)}" }
        return "ZplProgram(\n$commands\n)"
    }

    override fun visitStartFormatCommand(command: StartFormatCommand): String {
        return "StartFormatCommand()"
    }

    override fun visitEndFormatCommand(command: EndFormatCommand): String {
        return "EndFormatCommand()"
    }

    override fun visitFieldOriginCommand(command: FieldOriginCommand): String {
        return "FieldOriginCommand(x=${command.x}, y=${command.y})"
    }

    override fun visitFieldDataCommand(command: FieldDataCommand): String {
        val escapedData = command.data.replace("\"", "\\\"")
        return "FieldDataCommand(data=\"$escapedData\")"
    }

    override fun visitFontCommand(command: FontCommand): String {
        val params =
            mutableListOf<String>().apply {
                add("font='${command.font}'")
                command.orientation?.let { add("orientation='$it'") }
                command.height?.let { add("height=$it") }
                command.width?.let { add("width=$it") }
            }
        return "FontCommand(${params.joinToString(", ")})"
    }
}
