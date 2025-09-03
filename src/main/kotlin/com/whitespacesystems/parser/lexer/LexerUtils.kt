package com.whitespacesystems.parser.lexer

/**
 * Utility functions for lexer operations.
 * Extracted to reduce function count in main Lexer class.
 */
internal object LexerUtils {
    /**
     * Skip whitespace characters in the input string.
     */
    fun skipWhitespace(
        input: String,
        position: Int,
    ): Int {
        var pos = position
        while (pos < input.length && input[pos].isWhitespace()) {
            pos++
        }
        return pos
    }

    /**
     * Check if character is valid for command names.
     */
    fun isCommandChar(char: Char): Boolean {
        return char.isLetter() || char.isDigit()
    }

    /**
     * Check if character is valid for numeric tokens.
     */
    fun isNumericChar(char: Char): Boolean {
        return char.isDigit() || char == '.' || char == '-'
    }

    /**
     * Check if a command string is complete and valid.
     * This implementation uses the same logic as the original Lexer.isCompleteCommand
     * but with a more flexible approach that handles command variants.
     */
    fun isCompleteCommand(command: String): Boolean {
        if (command.length < 1) {
            return false
        }

        // Command information for efficient lookup (matches original Lexer commandInfo)
        val commandInfo =
            mapOf(
                "XA" to 2, "XZ" to 2, "FO" to 2, "FD" to 2, "FX" to 2,
                "CF" to 2, "GB" to 2, "FR" to 2, "FS" to 2, "BY" to 2,
                "BC" to 2, "A" to 1,
            )

        // Check if it's an exact match for known commands
        val exactMatch = commandInfo[command]
        val isExactMatch = exactMatch?.let { command.length >= it } ?: false

        // Check for command variants if not an exact match
        val isVariant =
            when {
                isExactMatch -> false
                else -> checkCommandVariants(command, commandInfo)
            }

        // Return result based on exact match, variant match, or default length check
        return isExactMatch || isVariant || (command.length >= 2)
    }

    /**
     * Check for command variants (like ABR, CFB, BCN).
     * Try to find base command and see if current command is a valid variant.
     */
    private fun checkCommandVariants(
        command: String,
        commandInfo: Map<String, Int>,
    ): Boolean {
        return commandInfo.keys.any { baseCmd ->
            val startsWithBase = command.startsWith(baseCmd)
            val meetsMinLength = command.length >= commandInfo[baseCmd]!!
            val isValidVariant =
                when (baseCmd) {
                    "A" -> command.length > 1 // Allow variants like A0, AB, A0N, ABR, etc.
                    "CF" -> command.length > 2 // Allow variants like CFB, CF0
                    "BC" -> command.length > 2 // Allow variants like BCR, BCN
                    else -> false
                }
            startsWithBase && meetsMinLength && isValidVariant
        }
    }
}
