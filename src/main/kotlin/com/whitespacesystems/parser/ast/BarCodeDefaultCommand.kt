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
private object BarCodeLimits {
    const val MIN_MODULE_WIDTH = 1
    const val MAX_MODULE_WIDTH = 10
    const val MIN_WIDTH_RATIO = 2.0
    const val MAX_WIDTH_RATIO = 3.0
    const val RATIO_PRECISION_FACTOR = 10
    const val RATIO_MODULO_CHECK = 1
}

data class BarCodeDefaultCommand(
    val moduleWidth: Int = 2,
    val widthRatio: Double = 3.0,
    val height: Int = 10,
) : ZplNode() {
    init {
        require(moduleWidth in BarCodeLimits.MIN_MODULE_WIDTH..BarCodeLimits.MAX_MODULE_WIDTH) {
            "Module width must be between ${BarCodeLimits.MIN_MODULE_WIDTH} and " +
                "${BarCodeLimits.MAX_MODULE_WIDTH}, got: $moduleWidth"
        }
        require(widthRatio >= BarCodeLimits.MIN_WIDTH_RATIO && widthRatio <= BarCodeLimits.MAX_WIDTH_RATIO) {
            "Width ratio must be between ${BarCodeLimits.MIN_WIDTH_RATIO} and " +
                "${BarCodeLimits.MAX_WIDTH_RATIO}, got: $widthRatio"
        }
        // Validate ratio precision to 0.1 increments
        val ratioTimes10 = (widthRatio * BarCodeLimits.RATIO_PRECISION_FACTOR).toInt()
        require(
            (ratioTimes10 / BarCodeLimits.RATIO_PRECISION_FACTOR.toDouble()) == widthRatio &&
                ratioTimes10 % BarCodeLimits.RATIO_MODULO_CHECK == 0,
        ) {
            "Width ratio must be in 0.1 increments " +
                "(${BarCodeLimits.MIN_WIDTH_RATIO}, 2.1, 2.2, ..., ${BarCodeLimits.MAX_WIDTH_RATIO}), got: $widthRatio"
        }
        require(height >= 0) {
            "Height must be non-negative, got: $height"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitBarCodeDefaultCommand(this)
}
