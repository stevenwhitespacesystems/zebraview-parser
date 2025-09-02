package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FX (Comment) command.
 * Adds non-printing informational comments or statements within a label format.
 *
 * Format: ^FXc
 *
 * @param text Non-printing comment text. Any data after the ^FX command
 *             up to the next caret (^) or tilde (~) command does not have
 *             any effect on the label format.
 */
data class CommentCommand(
    val text: String = "",
) : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitCommentCommand(this)
}
