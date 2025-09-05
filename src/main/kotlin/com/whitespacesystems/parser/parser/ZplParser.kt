package com.whitespacesystems.parser.parser

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
            "XA" -> BasicCommandParsingUtils.parseStartFormatCommand()
            "XZ" -> BasicCommandParsingUtils.parseEndFormatCommand()
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
}
