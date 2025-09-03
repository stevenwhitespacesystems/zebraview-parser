package com.whitespacesystems.parser.parser

/**
 * Utility functions for ZPL command parsing to reduce parseCommand complexity.
 */
object CommandParsingUtils {
    /**
     * Determines if a command token represents a variant of a base command.
     */
    fun isCommandVariant(commandValue: String): Boolean {
        return when {
            commandValue.startsWith("A") && commandValue != "A" -> true
            commandValue.startsWith("CF") && commandValue != "CF" -> true
            commandValue.startsWith("BC") && commandValue != "BC" -> true
            commandValue.startsWith("BE") && commandValue != "BE" -> true
            else -> false
        }
    }

    /**
     * Gets the command type for parsing variant commands.
     */
    fun getVariantType(commandValue: String): String {
        return when {
            commandValue.startsWith("A") -> "A"
            commandValue.startsWith("CF") -> "CF"
            commandValue.startsWith("BC") -> "BC"
            commandValue.startsWith("BE") -> "BE"
            else -> throw ParseException(
                "Unknown command variant: $commandValue",
            )
        }
    }
}