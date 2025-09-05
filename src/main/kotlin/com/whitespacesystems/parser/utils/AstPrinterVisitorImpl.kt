package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FieldSeparatorCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.ast.ZplNodeVisitor
import com.whitespacesystems.parser.ast.ZplProgram

/**
 * Implementation of ZplNodeVisitor for AST printing.
 * Separated from AstPrinter to reduce function count and improve maintainability.
 */
internal class AstPrinterVisitorImpl : ZplNodeVisitor<String> {
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

    override fun visitCommentCommand(command: CommentCommand): String {
        return "^FX${command.text}"
    }

    override fun visitFieldSeparatorCommand(command: FieldSeparatorCommand): String {
        return "^FS"
    }

    override fun visitFieldOriginCommand(command: FieldOriginCommand): String {
        return "^FO${command.x},${command.y},${command.z}"
    }
}
