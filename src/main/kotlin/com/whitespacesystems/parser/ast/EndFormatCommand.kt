package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^XZ (End Format) command.
 * Marks the end of a ZPL label format and triggers printing.
 *
 * Format: ^XZ
 *
 * This command is the ending (closing) bracket for ZPL II label formats.
 * When this command is received, a label prints. It must be paired with ^XA
 * to create a complete and valid ZPL II label format.
 */
class EndFormatCommand : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitEndFormatCommand(this)
}
