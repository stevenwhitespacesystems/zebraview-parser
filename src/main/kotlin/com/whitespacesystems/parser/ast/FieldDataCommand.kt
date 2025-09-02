package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^FD (Field Data) command.
 * Defines the data string for the field. The field data can be any printable character
 * except those used as command prefixes (^ and ~).
 *
 * Format: ^FDa
 *
 * @param data The data to be printed (up to 3072 characters)
 */
data class FieldDataCommand(
    val data: String,
) : ZplNode() {
    init {
        require(data.length <= 3072) { "Field data cannot exceed 3072 characters, got: ${data.length}" }
        // Empty field data is allowed - it simply prints nothing
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFieldDataCommand(this)
}
