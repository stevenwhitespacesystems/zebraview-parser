package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.Code128Command
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.ZplNode
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/**
 * Registry for command-specific parsers, used to reduce ZplParser complexity by
 * extracting command parsing logic into dedicated functions.
 */
class CommandParserRegistry {
    private val commandParsers: Map<String, (List<Token>, Int) -> Pair<ZplNode, Int>>

    init {
        commandParsers =
            mapOf(
                "FO" to ::parseFieldOriginCommand,
                "FD" to ::parseFieldDataCommand,
                "A" to { _, position -> Pair(FontCommand('A', 'N', null, null), position) },
                "A0" to { _, position -> Pair(FontCommand('0', 'N', null, null), position) },
                "AB" to { _, position -> Pair(FontCommand('B', 'N', null, null), position) },
                "BC" to { _, position -> Pair(Code128Command('N', null, true, false, false, 'N'), position) },
            )
    }

    /**
     * Parses a command using registered parser, returning the parsed command and new position.
     */
    fun parseCommand(
        commandName: String,
        tokens: List<Token>,
        startPosition: Int,
    ): ZplNode {
        val normalizedCommand = normalizeCommandName(commandName)
        val parser =
            commandParsers[normalizedCommand]
                ?: throw ParseException("Unknown command: $commandName")

        val (command, _) = parser(tokens, startPosition)
        return command
    }

    /**
     * Parses a command using registered parser, returning the parsed command and updated position.
     */
    fun parseCommandWithPosition(
        commandName: String,
        tokens: List<Token>,
        startPosition: Int,
    ): Pair<ZplNode, Int> {
        val normalizedCommand = normalizeCommandName(commandName)
        val parser =
            commandParsers[normalizedCommand]
                ?: throw ParseException("Unknown command: $commandName")

        return parser(tokens, startPosition)
    }

    /**
     * Checks if a command is supported by this registry.
     */
    fun isCommandSupported(commandName: String): Boolean {
        val normalizedCommand = normalizeCommandName(commandName)
        return commandParsers.containsKey(normalizedCommand)
    }

    private fun normalizeCommandName(commandName: String): String {
        // Handle exact matches first
        if (commandParsers.containsKey(commandName)) {
            return commandName
        }

        // Handle font variants with embedded orientation (ABN, A0N, etc.)
        return if (commandName.startsWith("A") && commandName.length > 1) {
            val baseCommand = commandName.substring(0, 2)
            if (commandParsers.containsKey(baseCommand)) baseCommand else "A"
        } else {
            commandName
        }
    }

    private fun parseFieldOriginCommand(
        tokens: List<Token>,
        position: Int,
    ): Pair<FieldOriginCommand, Int> {
        var pos = position

        // Validate sufficient tokens and correct format for FO command
        val hasEnoughTokens = pos + 2 < tokens.size
        val hasCorrectFormat =
            hasEnoughTokens &&
                tokens[pos].type == TokenType.NUMBER &&
                tokens[pos + 1].type == TokenType.COMMA &&
                tokens[pos + 2].type == TokenType.NUMBER

        if (!hasCorrectFormat) {
            throw ParseException("^FO command requires format: NUMBER,NUMBER at position $pos")
        }

        // Parse coordinates
        val x = tokens[pos++].value.toInt()
        pos++ // Skip comma
        val y = tokens[pos++].value.toInt()

        return Pair(FieldOriginCommand(x, y), pos)
    }

    private fun parseFieldDataCommand(
        tokens: List<Token>,
        position: Int,
    ): Pair<FieldDataCommand, Int> {
        var pos = position

        // Parse field data
        val data =
            if (pos < tokens.size && tokens[pos].type == TokenType.STRING) {
                tokens[pos++].value
            } else {
                "" // Empty field data is valid
            }

        return Pair(FieldDataCommand(data), pos)
    }
}
