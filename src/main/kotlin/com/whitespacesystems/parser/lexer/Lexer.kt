package com.whitespacesystems.parser.lexer

/**
 * Lexical analyzer for ZPL language.
 * Converts a ZPL string into a sequence of tokens for parsing.
 *
 * Currently supports default ZPL characters:
 * - Format commands: ^ (can be changed via ^CC)
 * - Control commands: ~ (can be changed via ^CT)
 * - Parameter delimiter: , (can be changed via ^CD)
 *
 * The lexer handles:
 * - Format commands starting with ^ followed by letters/numbers (e.g., ^FO, ^FD, ^A)
 * - Control commands starting with ~ followed by letters/numbers (e.g., ~HI, ~HS, ~JD)
 * - Numeric parameters
 * - String data (for FD command)
 * - Commas separating parameters
 *
 * Performance optimized for high-throughput parsing.
 * Supports dynamic character changes via CC, CT, CD commands.
 */
class Lexer(private val input: String) {
    private var position = 0
    private var line = 1
    private var column = 1
    private var lastCommand: String? = null
    private var expectingFieldData = false

    // Dynamic ZPL characters (can be changed via ^CC, ^CD, ^CT commands)
    private var caretChar: Char = '^' // Format command prefix (changed by ^CC)
    private var tildeChar: Char = '~' // Control command prefix (changed by ^CT)
    private var delimiterChar: Char = ',' // Parameter delimiter (changed by ^CD)
    private val current: Char
        get() = if (position >= input.length) '\u0000' else input[position]

    /**
     * Tokenize the entire input string.
     * Returns a list of tokens ready for parsing.
     */
    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (position < input.length) {
            skipWhitespace()

            if (position >= input.length) break

            val token = nextToken()
            tokens.add(token)
        }

        tokens.add(Token(TokenType.EOF, "", position, line, column))
        return tokens
    }

    /**
     * Get the next token from the input.
     */
    private fun nextToken(): Token {
        val start = position
        val startLine = line
        val startColumn = column

        return when {
            current == caretChar || current == tildeChar -> {
                val prefix = current.toString()
                advance()
                Token(TokenType.CARET, prefix, start, startLine, startColumn)
            }
            current == delimiterChar -> {
                advance()
                Token(TokenType.COMMA, delimiterChar.toString(), start, startLine, startColumn)
            }
            expectingFieldData -> {
                // If we're expecting field data, always read as string regardless of character type
                readString(start, startLine, startColumn)
            }
            current in '0'..'9' -> readNumber(start, startLine, startColumn)
            current in 'A'..'Z' || current in 'a'..'z' -> {
                readCommand(start, startLine, startColumn)
            }
            else -> {
                throw LexerException("Unexpected character: '$current' at position $position")
            }
        }
    }

    /**
     * Read a numeric token (for coordinates, dimensions, etc.)
     * Supports both integers and decimal numbers (e.g., 2.5)
     */
    private fun readNumber(
        start: Int,
        startLine: Int,
        startColumn: Int,
    ): Token {
        val value = StringBuilder()

        while (current.isDigit()) {
            value.append(current)
            advance()
        }

        // Handle decimal point
        if (current == '.') {
            value.append(current)
            advance()

            // Read fractional part
            while (current.isDigit()) {
                value.append(current)
                advance()
            }
        }

        return Token(TokenType.NUMBER, value.toString(), start, startLine, startColumn)
    }

    /**
     * Read a command token (letters following ^ or ~)
     * Commands are typically 1-2 letters, possibly followed by single digits/letters for variants
     */
    private fun readCommand(
        start: Int,
        startLine: Int,
        startColumn: Int,
    ): Token {
        val value = StringBuilder()

        // Read base command (usually 2 letters)
        CommandRecognitionUtils.readBaseCommand(value, { current }, { advance() }, LexerUtils::isCompleteCommand)
        val baseCommandName = value.toString()

        // Read variant characters if supported by this command
        CommandRecognitionUtils.readCommandVariants(baseCommandName, value, { current }, { advance() })

        val finalCommandName = value.toString()
        lastCommand = finalCommandName

        // Update field data expectation based on command
        expectingFieldData = CommandRecognitionUtils.updateFieldDataExpectation(finalCommandName, expectingFieldData)

        return Token(TokenType.COMMAND, finalCommandName, start, startLine, startColumn)
    }

    /**
     * Read string data for FD command until next command or end of input
     */
    private fun readString(
        start: Int,
        startLine: Int,
        startColumn: Int,
    ): Token {
        val value = StringBuilder()

        // Read until we hit a command prefix (caret or tilde) or end of input
        while (current != caretChar && current != tildeChar && current != '\u0000') {
            if (current == '\n') {
                line++
                column = 0
            }
            value.append(current)
            advance()
        }

        // Reset field data expectation after reading the string
        expectingFieldData = false

        return Token(TokenType.STRING, value.toString().trim(), start, startLine, startColumn)
    }

    /**
     * Update the caret character (called when ^CC command is encountered)
     */
    fun updateCaretChar(newCaretChar: Char) {
        caretChar = newCaretChar
    }

    /**
     * Update the tilde character (called when ^CT command is encountered)
     */
    fun updateTildeChar(newTildeChar: Char) {
        tildeChar = newTildeChar
    }

    /**
     * Update the delimiter character (called when ^CD command is encountered)
     */
    fun updateDelimiterChar(newDelimiterChar: Char) {
        delimiterChar = newDelimiterChar
    }

    /**
     * Skip whitespace characters (space, tab, newline, etc.)
     */
    private fun skipWhitespace() {
        while (current.isWhitespace()) {
            if (current == '\n') {
                line++
                column = 0
            }
            advance()
        }
    }

    /**
     * Advance to the next character in the input
     */
    private fun advance() {
        if (position < input.length) {
            position++
            column++
        }
    }
}

/**
 * Exception thrown by the lexer when it encounters invalid input.
 */
class LexerException(message: String) : Exception(message)
