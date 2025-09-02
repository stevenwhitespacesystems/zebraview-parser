package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^CF (Change Alphanumeric Default Font) command.
 * Sets the default font used in your printer for alphanumeric fields.
 *
 * Format: ^CFf,h,w
 *
 * @param font Specified default font (A through Z and 0 to 9)
 * @param height Individual character height in dots (0 to 32000)
 * @param width Individual character width in dots (0 to 32000)
 */
data class ChangeFontCommand(
    val font: Char = 'A',
    val height: Int = 9,
    val width: Int = 5,
) : ZplNode() {
    init {
        require(font in 'A'..'Z' || font in '0'..'9') {
            "Font must be A-Z or 0-9, got: $font"
        }
        require(height in 0..32000) {
            "Height must be between 0 and 32000, got: $height"
        }
        require(width in 0..32000) {
            "Width must be between 0 and 32000, got: $width"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitChangeFontCommand(this)
}
