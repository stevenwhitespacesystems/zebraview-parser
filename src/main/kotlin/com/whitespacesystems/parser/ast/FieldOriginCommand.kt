package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FO (Field Origin) command.
 * Sets the upper-left corner of a field area by defining points along the x-axis and y-axis.
 *
 * Format: ^FOx,y
 *
 * @param x X-axis location in dots (0 to 32000)
 * @param y Y-axis location in dots (0 to 32000)
 */
data class FieldOriginCommand(
    val x: Int = 0,
    val y: Int = 0,
) : ZplNode() {
    init {
        require(x in 0..32000) { "X coordinate must be between 0 and 32000, got: $x" }
        require(y in 0..32000) { "Y coordinate must be between 0 and 32000, got: $y" }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFieldOriginCommand(this)
}
