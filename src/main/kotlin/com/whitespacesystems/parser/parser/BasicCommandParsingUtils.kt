package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FieldReverseCommand
import com.whitespacesystems.parser.ast.FieldSeparatorCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/**
 * Utility functions for parsing basic ZPL commands with simple structures.
 */
object BasicCommandParsingUtils {
    /**
     * Parse Start Format command: ^XA
     */
    fun parseStartFormatCommand(): StartFormatCommand {
        return StartFormatCommand()
    }

    /**
     * Parse End Format command: ^XZ
     */
    fun parseEndFormatCommand(): EndFormatCommand {
        return EndFormatCommand()
    }

    /**
     * Parse Field Origin command: ^FOx,y
     */
    @Suppress("UnusedParameter")
    fun parseFieldOriginCommand(
        current: () -> Token,
        advance: () -> Unit,
        expect: (TokenType) -> Token,
    ): FieldOriginCommand {
        val x = expect(TokenType.NUMBER).value.toInt()
        expect(TokenType.COMMA)
        val y = expect(TokenType.NUMBER).value.toInt()
        return FieldOriginCommand(x, y)
    }

    /**
     * Parse Field Data command: ^FDdata
     */
    @Suppress("UnusedParameter")
    fun parseFieldDataCommand(
        current: () -> Token,
        advance: () -> Unit,
        expect: (TokenType) -> Token,
    ): FieldDataCommand {
        val data =
            if (current().type == TokenType.STRING) {
                expect(TokenType.STRING).value
            } else {
                ""
            }
        return FieldDataCommand(data)
    }

    /**
     * Parse Comment command: ^FXtext
     */
    @Suppress("UnusedParameter")
    fun parseCommentCommand(
        current: () -> Token,
        advance: () -> Unit,
        expect: (TokenType) -> Token,
    ): CommentCommand {
        val text =
            if (current().type == TokenType.STRING) {
                expect(TokenType.STRING).value
            } else {
                ""
            }
        return CommentCommand(text)
    }

    /**
     * Parse Field Reverse command: ^FR
     */
    fun parseFieldReverseCommand(): FieldReverseCommand {
        return FieldReverseCommand()
    }

    /**
     * Parse Field Separator command: ^FS
     */
    fun parseFieldSeparatorCommand(): FieldSeparatorCommand {
        return FieldSeparatorCommand()
    }
}
