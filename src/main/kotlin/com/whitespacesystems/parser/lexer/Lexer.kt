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
 * TODO: Add support for dynamic character changes via CC, CT, CD commands
 * Performance optimized for high-throughput parsing.
 */
class Lexer(private val input: String) {
    private var position = 0
    private var line = 1
    private var column = 1
    private var lastCommand: String? = null
    private var expectingFieldData = false

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

        return when (val char = current) {
            '^', '~' -> {
                val prefix = char.toString()
                advance()
                Token(TokenType.CARET, prefix, start, startLine, startColumn)
            }
            ',' -> {
                advance()
                Token(TokenType.COMMA, ",", start, startLine, startColumn)
            }
            in '0'..'9' -> readNumber(start, startLine, startColumn)
            in 'A'..'Z', in 'a'..'z' -> {
                // Check if we're expecting field data after FD command
                if (expectingFieldData) {
                    readString(start, startLine, startColumn)
                } else {
                    readCommand(start, startLine, startColumn)
                }
            }
            else -> {
                // Handle any other characters as string data if we're in field data context
                if (expectingFieldData) {
                    readString(start, startLine, startColumn)
                } else {
                    throw LexerException("Unexpected character: '$char' at position $position")
                }
            }
        }
    }

    /**
     * Read a numeric token (for coordinates, dimensions, etc.)
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

        // Read exactly 2 letters for most commands (FO, FD, XA, etc.)
        // Or 1 letter for single-char commands (A)
        var lettersRead = 0
        while (current.isLetter() && lettersRead < 2) {
            value.append(current)
            advance()
            lettersRead++

            // Check if we have a complete command that we recognize
            val commandSoFar = value.toString()
            if (isCompleteCommand(commandSoFar)) {
                break
            }
        }

        val commandName = value.toString()

        // Some commands have single character/digit variants (like A0N for font)
        // But only for specific commands, not for FD
        if (commandName == "A" && (current.isDigit() || current.isLetter())) {
            // For A command, read up to 2 more characters for font identifier and orientation
            var extraChars = 0
            while ((current.isDigit() || current.isLetter()) && extraChars < 2) {
                value.append(current)
                advance()
                extraChars++
                // Stop if we hit whitespace or comma (parameter separator)
                if (current.isWhitespace() || current == ',') {
                    break
                }
            }
        }

        val finalCommandName = value.toString()
        lastCommand = finalCommandName

        // Set flag if we just read FD command - next non-whitespace token should be field data
        expectingFieldData = (finalCommandName == "FD")

        return Token(TokenType.COMMAND, finalCommandName, start, startLine, startColumn)
    }

    /**
     * Check if the given string is a complete ZPL command
     */
    private fun isCompleteCommand(command: String): Boolean {
        return when (command) {
            "FO", "FD", "FS", "XA", "XZ" -> true
            "A" -> true // Font command - can be followed by parameters
            else -> command.length >= 2 // Most commands are 2 characters
        }
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

        // Read until we hit a command prefix (^ or ~) or end of input
        while (current != '^' && current != '~' && current != '\u0000') {
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
