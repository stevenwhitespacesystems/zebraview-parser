package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^XA (Start Format) command.
 * Marks the beginning of a ZPL label format.
 *
 * Format: ^XA
 *
 * This command is used at the beginning of ZPL II code and indicates the start
 * of a new label format. It is the opening bracket for all ZPL II label formats
 * and must be paired with ^XZ to complete the label format.
 */
class StartFormatCommand : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitStartFormatCommand(this)
}
