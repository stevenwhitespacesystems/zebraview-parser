package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FS (Field Separator) command.
 * Denotes the end of the field definition.
 *
 * Format: ^FS
 *
 * This command is required to terminate every field definition and is
 * essential for proper ZPL syntax. It can be used after every field
 * command sequence to mark the end of that field's definition.
 * Alternatively, it can be issued as a single ASCII control code SI
 * (Control-O, hexadecimal 0F).
 */
class FieldSeparatorCommand : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFieldSeparatorCommand(this)
}
