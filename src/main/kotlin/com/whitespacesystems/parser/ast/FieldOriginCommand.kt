package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FO (Field Origin) command.
 * Sets the field origin (starting position) for subsequent field commands.
 *
 * Format: ^FOx,y,z
 * Where x, y are coordinates and z is justification (optional).
 * 
 * This is a minimal implementation for GREEN phase to make tests pass.
 */
data class FieldOriginCommand(
    val x: Int,
    val y: Int,
    val z: Int = 0
) : ZplNode() {
    override fun <T> accept(visitor: ZplNodeVisitor<T>): T {
        return visitor.visitFieldOriginCommand(this)
    }
}