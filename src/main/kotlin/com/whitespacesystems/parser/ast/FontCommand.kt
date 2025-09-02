package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^A (Font) command.
 * Designates a scalable/bitmapped font using built-in or TrueType fonts.
 *
 * Format: ^Afo,h,w
 *
 * @param font Font name (A-Z, 1-9) - required parameter
 * @param orientation Font orientation (N=normal, R=90°, I=180°, B=270°) - optional
 * @param height Character height in dots - optional
 * @param width Width in dots - optional
 */
data class FontCommand(
    val font: Char = 'A',
    val orientation: Char? = null,
    val height: Int? = null,
    val width: Int? = null,
) : ZplNode() {
    init {
        require(font.isValidFont()) { "Font must be A-Z or 1-9, got: $font" }
        orientation?.let {
            require(it in setOf('N', 'R', 'I', 'B')) {
                "Orientation must be N, R, I, or B, got: $it"
            }
        }
        height?.let {
            require(it in 10..32000) { "Height must be between 10 and 32000, got: $it" }
        }
        width?.let {
            require(it in 10..32000) { "Width must be between 10 and 32000, got: $it" }
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitFontCommand(this)

    private fun Char.isValidFont(): Boolean = this in 'A'..'Z' || this in '0'..'9'
}
