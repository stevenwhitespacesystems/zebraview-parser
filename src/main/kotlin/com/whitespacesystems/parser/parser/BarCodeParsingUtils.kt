package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/**
 * Utility functions for parsing barcode-specific ZPL commands.
 */
object BarCodeParsingUtils {

    private object Defaults {
        const val BY_DEFAULT_MODULE_WIDTH = 2
        const val BY_DEFAULT_WIDTH_RATIO = 3.0
        const val BY_DEFAULT_HEIGHT = 10
    }

    /**
     * Parse width ratio parameter which can be NUMBER or STRING token.
     */
    fun parseBarCodeRatio(current: Token, expect: (TokenType) -> Token): Double {
        return when (current.type) {
            TokenType.NUMBER -> expect(TokenType.NUMBER).value.toDouble()
            TokenType.STRING -> expect(TokenType.STRING).value.toDouble()
            else -> Defaults.BY_DEFAULT_WIDTH_RATIO
        }
    }

    /**
     * Parse width ratio and height parameters for barcode commands.
     * Returns pair of (widthRatio, height) with defaults if not specified.
     */
    fun parseBarCodeRatioAndHeight(
        current: () -> Token,
        advance: () -> Unit,
        expect: (TokenType) -> Token,
    ): Pair<Double, Int> {
        var widthRatio = Defaults.BY_DEFAULT_WIDTH_RATIO
        var height = Defaults.BY_DEFAULT_HEIGHT

        if (current().type == TokenType.COMMA) {
            advance()
            widthRatio = parseBarCodeRatio(current(), expect)

            if (current().type == TokenType.COMMA) {
                advance()
                if (current().type == TokenType.NUMBER) {
                    height = expect(TokenType.NUMBER).value.toInt()
                }
            }
        }

        return Pair(widthRatio, height)
    }
}
