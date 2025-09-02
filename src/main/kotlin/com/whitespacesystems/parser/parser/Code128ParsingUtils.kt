package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.lexer.Token

/**
 * Utility functions for parsing Code128 barcode commands, extracted to reduce parseCode128Command complexity.
 */
object Code128ParsingUtils {
    /**
     * Extracts orientation character from command token (BCR -> R, BCN -> N, BC -> N).
     */
    fun extractOrientationFromCommand(commandToken: Token): Char {
        return if (commandToken.value.length > 2) {
            commandToken.value[2] // Extract 'R' from "BCR"
        } else {
            'N' // Default orientation
        }
    }

    /**
     * Parses boolean flag from string value ('Y' -> true, anything else -> false).
     */
    fun parseBooleanFlag(value: String): Boolean {
        return value.isNotEmpty() && value[0] == 'Y'
    }

    /**
     * Parses single character from token value, with default fallback.
     */
    fun parseCharacterValue(
        token: Token,
        default: Char = '\u0000',
    ): Char {
        return if (token.value.isNotEmpty()) {
            token.value[0]
        } else {
            default
        }
    }

    /**
     * Determines orientation by checking command token first, then string token if needed.
     * Command token takes precedence (BCR -> R), otherwise uses string token value.
     */
    fun extractOrientationFromStringOrCommand(
        commandToken: Token,
        stringToken: Token?,
    ): Char {
        // If command token has orientation embedded (BCR, BCN), use it
        if (commandToken.value.length > 2) {
            return commandToken.value[2]
        }

        // Otherwise, extract from string token if available
        return stringToken?.let { parseCharacterValue(it, 'N') } ?: 'N'
    }
}
