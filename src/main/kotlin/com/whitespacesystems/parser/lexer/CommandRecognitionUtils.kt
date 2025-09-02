package com.whitespacesystems.parser.lexer

/**
 * Utility functions for ZPL command recognition, extracted to reduce Lexer.readCommand complexity.
 */
object CommandRecognitionUtils {
    private object CommandLimits {
        const val MAX_BASE_COMMAND_LENGTH = 2
        const val MAX_FONT_VARIANT_CHARS = 2
        const val SINGLE_FONT_VARIANT_CHAR = 1
    }

    /**
     * Data class representing command parsing state.
     */
    data class CommandParsingState(
        val commandSoFar: String,
        val currentChar: Char,
        val isWhitespace: Boolean,
        val isComma: Boolean,
    )

    /**
     * Determines if additional characters should be read for command variants.
     */
    fun shouldReadVariantCharacters(
        commandName: String,
        current: Char,
    ): CommandVariantInfo? {
        return when (commandName) {
            "A" ->
                if (current.isDigit() || current.isLetter()) {
                    CommandVariantInfo(CommandLimits.MAX_FONT_VARIANT_CHARS, true)
                } else {
                    null
                }
            "CF" ->
                if (current.isDigit() || current.isLetter()) {
                    CommandVariantInfo(CommandLimits.SINGLE_FONT_VARIANT_CHAR, false)
                } else {
                    null
                }
            "BC" ->
                if (current.isLetter()) {
                    CommandVariantInfo(CommandLimits.SINGLE_FONT_VARIANT_CHAR, false)
                } else {
                    null
                }
            else -> null
        }
    }

    /**
     * Determines if parsing should stop based on current character and command type.
     */
    fun shouldStopParsing(
        state: CommandParsingState,
        respectsWhitespace: Boolean,
    ): Boolean {
        return if (respectsWhitespace) {
            state.isWhitespace || state.isComma
        } else {
            false
        }
    }

    /**
     * Updates field data expectation flags based on the parsed command.
     */
    fun updateFieldDataExpectation(
        commandName: String,
        currentExpectingFieldData: Boolean,
    ): Boolean {
        return when (commandName) {
            "FD", "FX" -> true
            "FS" -> false
            else -> currentExpectingFieldData
        }
    }

    /**
     * Information about command variant parsing.
     */
    data class CommandVariantInfo(
        val maxExtraChars: Int,
        val respectsWhitespaceAndComma: Boolean,
    )

    /**
     * Reads command variant characters based on the variant info.
     */
    fun readVariantCharacters(
        variantInfo: CommandVariantInfo,
        current: () -> Char,
        advance: () -> Unit,
        value: StringBuilder,
    ): Int {
        var extraChars = 0
        while ((current().isDigit() || current().isLetter()) && extraChars < variantInfo.maxExtraChars) {
            value.append(current())
            advance()
            extraChars++

            if (variantInfo.respectsWhitespaceAndComma) {
                val state =
                    CommandParsingState(
                        value.toString(),
                        current(),
                        current().isWhitespace(),
                        current() == ',',
                    )
                if (shouldStopParsing(state, true)) {
                    break
                }
            }
        }
        return extraChars
    }

    /**
     * Determines if a base command reading should stop.
     */
    fun shouldStopBaseCommandReading(
        commandSoFar: String,
        isCompleteCommand: (String) -> Boolean,
    ): Boolean {
        return isCompleteCommand(commandSoFar)
    }

    /**
     * Reads the base command (usually 1-2 letters).
     */
    fun readBaseCommand(
        value: StringBuilder,
        current: () -> Char,
        advance: () -> Unit,
        isCompleteCommand: (String) -> Boolean,
    ) {
        var lettersRead = 0
        while (current().isLetter() && lettersRead < 2) {
            value.append(current())
            advance()
            lettersRead++

            val commandSoFar = value.toString()
            if (shouldStopBaseCommandReading(commandSoFar, isCompleteCommand)) {
                break
            }
        }
    }

    /**
     * Reads command variant characters (like A0N, CFB, BCR).
     */
    fun readCommandVariants(
        baseCommandName: String,
        value: StringBuilder,
        current: () -> Char,
        advance: () -> Unit,
    ) {
        val variantInfo = shouldReadVariantCharacters(baseCommandName, current())
        if (variantInfo != null) {
            readVariantCharacters(
                variantInfo,
                current,
                advance,
                value,
            )
        }
    }
}
