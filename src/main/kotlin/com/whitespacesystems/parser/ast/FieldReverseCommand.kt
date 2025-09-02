package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FR (Field Reverse Print) command.
 * Allows a field to appear as white over black or black over white.
 *
 * Format: ^FR
 *
 * This command reverses the color of the output relative to its background.
 * When printing a field and the ^FR command has been used, the color of the
 * output is the reverse of its background. The ^FR command applies to only
 * one field and has to be specified each time.
 */
class FieldReverseCommand : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFieldReverseCommand(this)
}
