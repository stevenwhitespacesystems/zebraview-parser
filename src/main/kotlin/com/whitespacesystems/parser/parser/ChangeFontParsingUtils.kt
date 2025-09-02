package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.ChangeFontCommand
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/**
 * Utility functions for parsing Change Font (^CF) commands, extracted to reduce parseChangeFontCommand complexity.
 */
object ChangeFontParsingUtils {
    private object Defaults {
        const val CF_DEFAULT_HEIGHT = 9
        const val CF_DEFAULT_WIDTH = 5
        const val DEFAULT_FONT = 'A'
        const val PLAIN_CF_COMMAND_LENGTH = 2
        const val MIN_PARTS_SIZE_FOR_HEIGHT = 2
        const val MIN_PARTS_SIZE_FOR_WIDTH = 3
    }

    /**
     * Extracts font character from command token (CFB -> B, CF0 -> 0, CF -> A).
     */
    fun extractFontFromCommand(commandToken: Token): Char {
        return if (commandToken.value.length > Defaults.PLAIN_CF_COMMAND_LENGTH) {
            commandToken.value[2] // Extract font from CFB -> B, CF0 -> 0
        } else {
            Defaults.DEFAULT_FONT // Plain CF command uses default font
        }
    }

    /**
     * Parses comma-separated parameters from a string token (e.g., "B,20,15").
     */
    fun parseCommaSeparatedParameters(text: String): ChangeFontCommand {
        val parts = text.split(",")
        var font = Defaults.DEFAULT_FONT
        var height = Defaults.CF_DEFAULT_HEIGHT
        var width = Defaults.CF_DEFAULT_WIDTH

        if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
            font = parts[0][0]
        }
        if (parts.size >= Defaults.MIN_PARTS_SIZE_FOR_HEIGHT && parts[1].isNotEmpty()) {
            height = parts[1].toInt()
        }
        if (parts.size >= Defaults.MIN_PARTS_SIZE_FOR_WIDTH && parts[2].isNotEmpty()) {
            width = parts[2].toInt()
        }

        return ChangeFontCommand(font, height, width)
    }

    /**
     * Determines if a string token should be treated as a single font character.
     */
    fun shouldTreatAsSingleFont(
        text: String,
        commandToken: Token,
    ): Boolean {
        return text.isNotEmpty() && commandToken.value.length == Defaults.PLAIN_CF_COMMAND_LENGTH
    }

    /**
     * Determines if a number parameter should be treated as font or height.
     */
    fun parseNumberParameter(
        numberValue: String,
        commandToken: Token,
    ): Pair<Char?, Int?> {
        val shouldTreatAsFont =
            numberValue.length == 1 &&
                numberValue[0].isDigit() &&
                commandToken.value.length == Defaults.PLAIN_CF_COMMAND_LENGTH

        return if (shouldTreatAsFont) {
            Pair(numberValue[0], null) // Font character, no height
        } else {
            Pair(null, numberValue.toInt()) // Height value, no font change
        }
    }

    /**
     * Checks if current token type indicates parameter presence.
     */
    fun hasParameters(tokenType: TokenType): Boolean {
        return tokenType == TokenType.STRING ||
            tokenType == TokenType.COMMAND ||
            tokenType == TokenType.NUMBER ||
            tokenType == TokenType.COMMA
    }
}
