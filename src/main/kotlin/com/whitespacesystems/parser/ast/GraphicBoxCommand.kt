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
private object GraphicBoxLimits {
    const val MIN_THICKNESS = 1
    const val MAX_SIZE = 32000
    const val MIN_ROUNDING = 0
    const val MAX_ROUNDING = 8
}

data class GraphicBoxCommand(
    val width: Int = 1,
    val height: Int = 1,
    val thickness: Int = 1,
    val color: Char = 'B',
    val rounding: Int = 0,
) : ZplNode() {
    init {
        require(thickness in GraphicBoxLimits.MIN_THICKNESS..GraphicBoxLimits.MAX_SIZE) {
            "Thickness must be between ${GraphicBoxLimits.MIN_THICKNESS} and " +
                "${GraphicBoxLimits.MAX_SIZE}, got: $thickness"
        }
        require(width >= thickness && width <= GraphicBoxLimits.MAX_SIZE) {
            "Width must be between $thickness and ${GraphicBoxLimits.MAX_SIZE}, got: $width"
        }
        require(height >= thickness && height <= GraphicBoxLimits.MAX_SIZE) {
            "Height must be between $thickness and ${GraphicBoxLimits.MAX_SIZE}, got: $height"
        }
        require(color == 'B' || color == 'W') {
            "Color must be 'B' (black) or 'W' (white), got: $color"
        }
        require(rounding in GraphicBoxLimits.MIN_ROUNDING..GraphicBoxLimits.MAX_ROUNDING) {
            "Rounding must be between ${GraphicBoxLimits.MIN_ROUNDING} and " +
                "${GraphicBoxLimits.MAX_ROUNDING}, got: $rounding"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitGraphicBoxCommand(this)
}
