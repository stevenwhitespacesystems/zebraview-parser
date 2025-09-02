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
private object FieldDataLimits {
    const val MAX_FIELD_DATA_LENGTH = 3072
}

data class FieldDataCommand(
    val data: String,
) : ZplNode() {
    init {
        require(
            data.length <= FieldDataLimits.MAX_FIELD_DATA_LENGTH,
        ) { "Field data cannot exceed ${FieldDataLimits.MAX_FIELD_DATA_LENGTH} characters, got: ${data.length}" }
        // Empty field data is allowed - it simply prints nothing
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFieldDataCommand(this)
}
