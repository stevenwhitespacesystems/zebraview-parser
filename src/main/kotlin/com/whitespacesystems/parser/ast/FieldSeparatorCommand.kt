package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FS (Field Separator) command.
 * Marks the end of a field definition or field data.
 *
 * Format: ^FS
 * This is a simple command with no parameters that indicates
 * the end of field-related commands.
 */
object FieldSeparatorCommand : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T {
        return visitor.visitFieldSeparatorCommand(this)
    }
}