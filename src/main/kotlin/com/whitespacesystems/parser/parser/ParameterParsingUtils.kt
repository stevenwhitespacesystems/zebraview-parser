package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/**
 * Utility functions for parsing optional parameters in ZPL commands.
 */
object ParameterParsingUtils {

    /**
     * Parse optional number parameter after a comma.
     */
    fun parseOptionalNumberParameter(current: () -> Token, advance: () -> Unit, expect: (TokenType) -> Token): Int? {
        return if (current().type == TokenType.COMMA) {
            advance()
            if (current().type == TokenType.NUMBER) {
                expect(TokenType.NUMBER).value.toInt()
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * Parse optional boolean parameter after a comma.
     */
    fun parseOptionalBooleanParameter(
        defaultValue: Boolean,
        current: () -> Token,
        advance: () -> Unit,
    ): Boolean {
        return if (current().type == TokenType.COMMA) {
            advance()
            if (current().type == TokenType.STRING || current().type == TokenType.COMMAND) {
                val token = current()
                advance()
                Code128ParsingUtils.parseBooleanFlag(token.value)
            } else {
                defaultValue
            }
        } else {
            defaultValue
        }
    }

    /**
     * Parse optional width parameter after a comma.
     */
    fun parseOptionalWidthParameter(current: () -> Token, advance: () -> Unit, expect: (TokenType) -> Token): Int? {
        return if (current().type == TokenType.COMMA) {
            advance()
            if (current().type == TokenType.NUMBER) {
                expect(TokenType.NUMBER).value.toInt()
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * Parse additional height and width parameters that follow a comma.
     */
    fun parseAdditionalFontParameters(
        current: () -> Token,
        advance: () -> Unit,
        expect: (TokenType) -> Token,
    ): Pair<Int?, Int?>? {
        val hasComma = current().type == TokenType.COMMA
        return if (hasComma) {
            advance() // Skip comma
            val hasNumber = current().type == TokenType.NUMBER
            if (hasNumber) {
                val height = expect(TokenType.NUMBER).value.toInt()
                val width = parseOptionalWidthParameter(current, advance, expect)
                Pair(height, width)
            } else {
                null
            }
        } else {
            null
        }
    }
}
