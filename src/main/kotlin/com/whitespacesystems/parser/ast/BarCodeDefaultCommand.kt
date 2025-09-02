package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^BY (Bar Code Field Default) command.
 * Changes the default values for module width, wide bar to narrow bar width ratio, and bar code height.
 *
 * Format: ^BYw,r,h
 *
 * @param moduleWidth Module width in dots (1 to 10)
 * @param widthRatio Wide bar to narrow bar width ratio (2.0 to 3.0 in 0.1 increments)
 * @param height Bar code height in dots (no specified range)
 */
data class BarCodeDefaultCommand(
    val moduleWidth: Int = 2,
    val widthRatio: Double = 3.0,
    val height: Int = 10,
) : ZplNode() {
    init {
        require(moduleWidth in 1..10) {
            "Module width must be between 1 and 10, got: $moduleWidth"
        }
        require(widthRatio >= 2.0 && widthRatio <= 3.0) {
            "Width ratio must be between 2.0 and 3.0, got: $widthRatio"
        }
        // Validate ratio precision to 0.1 increments
        val ratioTimes10 = (widthRatio * 10).toInt()
        require((ratioTimes10 / 10.0) == widthRatio && ratioTimes10 % 1 == 0) {
            "Width ratio must be in 0.1 increments (2.0, 2.1, 2.2, ..., 3.0), got: $widthRatio"
        }
        require(height >= 0) {
            "Height must be non-negative, got: $height"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitBarCodeDefaultCommand(this)
}
