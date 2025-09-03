package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.BECommand
import com.whitespacesystems.parser.ast.BarCodeDefaultCommand
import com.whitespacesystems.parser.ast.ChangeFontCommand
import com.whitespacesystems.parser.ast.Code128Command
import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FieldReverseCommand
import com.whitespacesystems.parser.ast.FieldSeparatorCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.GraphicBoxCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.ast.ZplNodeVisitor
import com.whitespacesystems.parser.ast.ZplProgram

/**
 * Implementation of ZplNodeVisitor for AST printing.
 * Separated from AstPrinter to reduce function count and improve maintainability.
 *
 * Note: TooManyFunctions is suppressed because visitor pattern requires
 * one method per AST node type (14 types = 14 methods).
 */
@Suppress("TooManyFunctions")
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

    override fun visitFieldOriginCommand(command: FieldOriginCommand): String {
        return "FieldOriginCommand(${AstPrinterUtils.formatCoordinatePair(command.x, command.y)})"
    }

    override fun visitFieldDataCommand(command: FieldDataCommand): String {
        val escapedData = AstPrinterUtils.escapeStringData(command.data)
        return "FieldDataCommand(data=\"$escapedData\")"
    }

    override fun visitFontCommand(command: FontCommand): String {
        val params =
            listOf(
                "font='${command.font}'",
                command.orientation?.let { "orientation='$it'" },
                command.height?.let { "height=$it" },
                command.width?.let { "width=$it" },
            )
        return AstPrinterUtils.formatOptionalParameters("FontCommand", params)
    }

    override fun visitCommentCommand(command: CommentCommand): String {
        val escapedText = AstPrinterUtils.escapeStringData(command.text)
        return "CommentCommand(text=\"$escapedText\")"
    }

    override fun visitFieldReverseCommand(command: FieldReverseCommand): String {
        return "FieldReverseCommand()"
    }

    override fun visitFieldSeparatorCommand(command: FieldSeparatorCommand): String {
        return "FieldSeparatorCommand()"
    }

    override fun visitChangeFontCommand(command: ChangeFontCommand): String {
        return "ChangeFontCommand(font='${command.font}', height=${command.height}, width=${command.width})"
    }

    override fun visitGraphicBoxCommand(command: GraphicBoxCommand): String {
        return "GraphicBoxCommand(width=${command.width}, height=${command.height}, " +
            "thickness=${command.thickness}, color='${command.color}', rounding=${command.rounding})"
    }

    override fun visitBarCodeDefaultCommand(command: BarCodeDefaultCommand): String {
        return "BarCodeDefaultCommand(moduleWidth=${command.moduleWidth}, " +
            "widthRatio=${command.widthRatio}, height=${command.height})"
    }

    override fun visitCode128Command(command: Code128Command): String {
        val params =
            mutableListOf<String>().apply {
                add("orientation='${command.orientation}'")
                command.height?.let { add("height=$it") } ?: add("height=null")
                add("printInterpretation=${command.printInterpretation}")
                add("printInterpretationAbove=${command.printInterpretationAbove}")
                add("uccCheckDigit=${command.uccCheckDigit}")
                add("mode='${command.mode}'")
            }
        return "Code128Command(${params.joinToString(", ")})"
    }

    override fun visitBECommand(command: BECommand): String {
        val params =
            mutableListOf<String>().apply {
                add("orientation='${command.orientation}'")
                command.height?.let { add("height=$it") } ?: add("height=null")
                add("printInterpretation=${command.printInterpretation}")
                add("printInterpretationAbove=${command.printInterpretationAbove}")
            }
        return "BECommand(${params.joinToString(", ")})"
    }
}