package com.whitespacesystems.parser.lexer

/**
 * Represents different types of tokens in ZPL language.
 * Each token type corresponds to a meaningful unit in ZPL syntax.
 */
enum class TokenType {
    CARET, // ^
    COMMAND, // Command letters like FO, FD, A
    NUMBER, // Numeric values like 100, 50
    STRING, // Text data for FD command
    COMMA, // ,
    EOF, // End of input
}

/**
 * Represents a token in the ZPL language with its type, value, and position.
 *
 * @param type The type of token (CARET, COMMAND, etc.)
 * @param value The actual text value of the token
 * @param position The character position in the input string
 * @param line The line number (for error reporting)
 * @param column The column number (for error reporting)
 */
data class Token(
    val type: TokenType,
    val value: String,
    val position: Int,
    val line: Int = 1,
    val column: Int = position + 1,
) {
    override fun toString(): String = "Token($type, '$value', pos=$position)"
}
