package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FX (Comment) command.
 * Non-printing comment command for documentation within label formats.
 *
 * Format: ^FXc
 * Where c is comment text until the next ^ or ~ delimiter.
 *
 * This command allows for documentation and comments within ZPL code
 * that will not appear on the printed label.
 */
data class CommentCommand(
    val text: String
) : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T {
        return visitor.visitCommentCommand(this)
    }
}