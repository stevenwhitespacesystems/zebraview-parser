package com.whitespacesystems.parser.utils

/**
 * Utility functions for AST node formatting and string generation.
 * Extracted from AstPrinter to reduce class complexity.
 */
object AstPrinterUtils {
    /**
     * Formats a command with optional parameters, omitting null values.
     */
    fun formatOptionalParameters(
        commandName: String,
        parameters: List<String?>,
    ): String {
        val nonNullParams = parameters.filterNotNull()
        val paramString = nonNullParams.joinToString(", ")
        return "$commandName($paramString)"
    }

    /**
     * Escapes special characters in string data for display.
     */
    fun escapeStringData(input: String): String {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
    }

    /**
     * Formats coordinate pair for display.
     */
    fun formatCoordinatePair(
        x: Int,
        y: Int,
    ): String {
        return "x=$x, y=$y"
    }

    /**
     * Formats parameter pair for display.
     */
    fun formatParameterPair(
        name1: String,
        value1: Any?,
        name2: String,
        value2: Any?,
    ): String {
        return "$name1=$value1, $name2=$value2"
    }
}
