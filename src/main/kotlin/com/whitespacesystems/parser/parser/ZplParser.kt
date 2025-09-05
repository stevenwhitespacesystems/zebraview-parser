package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FieldSeparatorCommand
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
 * Command -> StartFormatCommand | EndFormatCommand
 * StartFormatCommand -> '^' 'XA'
 * EndFormatCommand -> '^' 'XZ'
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
            command?.let { commands.add(it) } // Only add non-null commands
        }

        return ZplProgram(commands)
    }

    /**
     * Parse a single ZPL command.
     * Returns null for commands that should be ignored (like FS field terminators).
     */
    private fun parseCommand(): ZplNode? {
        expect(TokenType.CARET)

        val commandToken = expect(TokenType.COMMAND)

        return when (commandToken.value) {
            "XA" -> BasicCommandParsingUtils.parseStartFormatCommand()
            "XZ" -> BasicCommandParsingUtils.parseEndFormatCommand()
            "FX" -> parseComment()
            "FO" -> parseFieldOrigin()
            "FS" -> null // FS is a field terminator, not a command - ignore it
            else -> throw ParseException("Unknown command: ${commandToken.value} at position ${commandToken.position}")
        }
    }

    /**
     * Advance to the next token.
     */
    private fun advance(): Token {
        val current = this.current
        if (position < tokens.size - 1) {
            position++
        }
        return current
    }

    /**
     * Expect a specific token type and advance.
     */
    private fun expect(expectedType: TokenType): Token {
        val currentToken = current
        if (currentToken.type == expectedType) {
            advance()
            return currentToken
        } else {
            throw ParseException(
                "Expected $expectedType but got ${currentToken.type} with value " +
                    "'${currentToken.value}' at position ${currentToken.position}",
            )
        }
    }

    /**
     * Parse FX comment command: ^FXc
     * Where c is comment text until the next ^ or ~ delimiter.
     * 
     * Note: If there's no text between ^FX and the next command delimiter,
     * the lexer won't produce a STRING token, just the next CARET token.
     */
    private fun parseComment(): CommentCommand {
        // Check if there's a STRING token available (comment text)
        val commentText = if (current.type == TokenType.STRING) {
            val stringToken = expect(TokenType.STRING)
            stringToken.value
        } else {
            // Empty comment case - no STRING token produced by lexer
            // when there's no text between ^FX and next delimiter
            ""
        }
        
        return CommentCommand(commentText)
    }

    /**
     * Parse FS field separator command: ^FS
     * Simple command with no parameters that marks the end of a field.
     */
    private fun parseFieldSeparator(): FieldSeparatorCommand {
        // FS command has no parameters, just return the singleton object
        return FieldSeparatorCommand
    }

    /**
     * Parse FO field origin command: ^FOx,y,z
     * Minimal implementation for GREEN phase.
     */
    private fun parseFieldOrigin(): FieldOriginCommand {
        // Expect x coordinate
        val xToken = expect(TokenType.NUMBER)
        val x = xToken.value.toInt()
        
        // Expect comma
        expect(TokenType.COMMA)
        
        // Expect y coordinate  
        val yToken = expect(TokenType.NUMBER)
        val y = yToken.value.toInt()
        
        // Optional z coordinate (justification)
        val z = if (current.type == TokenType.COMMA) {
            advance() // consume comma
            val zToken = expect(TokenType.NUMBER)
            zToken.value.toInt()
        } else {
            0 // default value
        }
        
        return FieldOriginCommand(x, y, z)
    }
}
