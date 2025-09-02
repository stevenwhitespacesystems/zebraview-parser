package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.ast.ZplNode
import com.whitespacesystems.parser.ast.ZplProgram
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/*
 * ZPL parser that converts tokens into an Abstract Syntax Tree (AST).
 *
 * This is a recursive descent parser optimized for high-performance parsing
 * of ZPL commands. It follows the structure:
 *
 * Program -> Command*
 * Command -> FieldOriginCommand | FieldDataCommand | FontCommand
 * FieldOriginCommand -> '^' 'FO' NUMBER ',' NUMBER
 * FieldDataCommand -> '^' 'FD' STRING
 * FontCommand -> '^' 'A' [font] [',' NUMBER] [',' NUMBER]
 */
class ZplParser(private val tokens: List<Token>) {
    private var position = 0

    private val current: Token
        get() = if (position >= tokens.size) tokens.last() else tokens[position]

    /**
     * Parse tokens into a ZPL program AST.
     */
    fun parse(): ZplProgram {
        val commands = mutableListOf<ZplNode>()

        while (current.type != TokenType.EOF) {
            val command = parseCommand()
            commands.add(command)
        }

        return ZplProgram(commands)
    }

    /**
     * Parse a single ZPL command.
     */
    private fun parseCommand(): ZplNode {
        expect(TokenType.CARET)

        val commandToken = expect(TokenType.COMMAND)

        return when (commandToken.value) {
            "XA" -> parseStartFormatCommand()
            "XZ" -> parseEndFormatCommand()
            "FO" -> parseFieldOriginCommand()
            "FD" -> parseFieldDataCommand()
            "A" -> parseFontCommand(commandToken.value)
            else -> {
                // Handle font commands like A0N, ABN, etc.
                if (commandToken.value.startsWith("A")) {
                    parseFontCommand(commandToken.value)
                } else {
                    throw ParseException("Unknown command: ${commandToken.value} at position ${commandToken.position}")
                }
            }
        }
    }

    /**
     * Parse Start Format command: ^XA
     */
    private fun parseStartFormatCommand(): StartFormatCommand {
        // No parameters to parse - XA command is complete after COMMAND token
        return StartFormatCommand()
    }

    /**
     * Parse End Format command: ^XZ
     */
    private fun parseEndFormatCommand(): EndFormatCommand {
        // No parameters to parse - XZ command is complete after COMMAND token
        return EndFormatCommand()
    }

    /**
     * Parse Field Origin command: ^FOx,y
     */
    private fun parseFieldOriginCommand(): FieldOriginCommand {
        val x = expect(TokenType.NUMBER).value.toInt()
        expect(TokenType.COMMA)
        val y = expect(TokenType.NUMBER).value.toInt()

        return FieldOriginCommand(x, y)
    }

    /**
     * Parse Field Data command: ^FDdata
     * Field data is optional - if no STRING token follows, data is empty
     */
    private fun parseFieldDataCommand(): FieldDataCommand {
        val data =
            if (current.type == TokenType.STRING) {
                expect(TokenType.STRING).value
            } else {
                // Empty field data
                ""
            }
        return FieldDataCommand(data)
    }

    /*
     * Parse Font command: ^A[font][orientation],height,width
     * Examples: ^A0N,30,30 or ^ABN,20 or ^A0
     */
    private fun parseFontCommand(commandValue: String): FontCommand {
        val font: Char
        val orientation: Char?

        // Parse font and orientation from command value
        when {
            commandValue == "A" -> {
                // Just ^A, font will be default 'A'
                font = 'A'
                orientation = null
            }
            commandValue.length == 2 -> {
                // ^A0 or ^AB
                font = commandValue[1]
                orientation = null
            }
            commandValue.length == 3 -> {
                // ^A0N or ^ABR
                font = commandValue[1]
                orientation = commandValue[2]
            }
            else -> {
                throw ParseException("Invalid font command format: $commandValue")
            }
        }

        // Parse optional height and width parameters
        var height: Int? = null
        var width: Int? = null

        if (current.type == TokenType.COMMA) {
            advance() // consume comma
            height = expect(TokenType.NUMBER).value.toInt()

            if (current.type == TokenType.COMMA) {
                advance() // consume comma
                width = expect(TokenType.NUMBER).value.toInt()
            }
        }

        return FontCommand(font, orientation, height, width)
    }

    /**
     * Expect a specific token type and advance.
     */
    private fun expect(expectedType: TokenType): Token {
        if (current.type != expectedType) {
            throw ParseException("Expected $expectedType but found ${current.type} at position ${current.position}")
        }
        val token = current
        advance()
        return token
    }

    /**
     * Advance to the next token.
     */
    private fun advance() {
        if (position < tokens.size - 1) {
            position++
        }
    }
}

/**
 * Exception thrown by the parser when it encounters invalid syntax.
 */
class ParseException(message: String) : Exception(message)
