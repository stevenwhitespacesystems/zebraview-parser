package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^GB (Graphic Box) command.
 * Used to draw boxes and lines as part of a label format.
 *
 * Format: ^GBw,h,t,c,r
 *
 * @param width Box width in dots (value of thickness to 32000)
 * @param height Box height in dots (value of thickness to 32000)
 * @param thickness Border thickness in dots (1 to 32000)
 * @param color Line color ('B' for black, 'W' for white)
 * @param rounding Degree of corner rounding (0 for no rounding to 8 for heaviest rounding)
 */
data class GraphicBoxCommand(
    val width: Int = 1,
    val height: Int = 1,
    val thickness: Int = 1,
    val color: Char = 'B',
    val rounding: Int = 0,
) : ZplNode() {
    init {
        require(thickness in 1..32000) {
            "Thickness must be between 1 and 32000, got: $thickness"
        }
        require(width >= thickness && width <= 32000) {
            "Width must be between $thickness and 32000, got: $width"
        }
        require(height >= thickness && height <= 32000) {
            "Height must be between $thickness and 32000, got: $height"
        }
        require(color == 'B' || color == 'W') {
            "Color must be 'B' (black) or 'W' (white), got: $color"
        }
        require(rounding in 0..8) {
            "Rounding must be between 0 and 8, got: $rounding"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitGraphicBoxCommand(this)
}
