package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.StartFormatCommand

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
}
