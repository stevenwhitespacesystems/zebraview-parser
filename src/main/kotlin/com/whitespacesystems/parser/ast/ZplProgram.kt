package com.whitespacesystems.parser.ast

/**
 * Root AST node representing a complete ZPL program.
 * A ZPL program is a sequence of commands that form a complete label definition.
 */
data class ZplProgram(
    val commands: List<ZplNode>,
) : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitZplProgram(this)
}
