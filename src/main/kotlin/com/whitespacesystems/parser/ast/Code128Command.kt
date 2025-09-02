package com.whitespacesystems.parser.ast

/**
 * AST node representing the ^BC (Code 128 Bar Code) command.
 * Creates the Code 128 bar code, a high-density, variable length, continuous, alphanumeric symbology.
 *
 * Format: ^BCo,h,f,g,e,m
 *
 * @param orientation Barcode orientation ('N' = normal, 'R' = rotated 90°, 'I' = inverted 180°, 'B' = bottom up 270°)
 * @param height Bar code height in dots (1 to 32000, null uses ^BY value)
 * @param printInterpretation Print interpretation line below code (true = yes, false = no)
 * @param printInterpretationAbove Print interpretation line above code (true = yes, false = no)
 * @param uccCheckDigit UCC check digit (true = turns on, false = turns off)
 * @param mode Processing mode ('N' = no mode, 'U' = UCC Case, 'A' = Automatic, 'D' = New Mode)
 */
data class Code128Command(
    val orientation: Char = 'N',
    val height: Int? = null,
    val printInterpretation: Boolean = true,
    val printInterpretationAbove: Boolean = false,
    val uccCheckDigit: Boolean = false,
    val mode: Char = 'N',
) : ZplNode() {
    init {
        require(orientation in setOf('N', 'R', 'I', 'B')) {
            "Orientation must be N, R, I, or B, got: $orientation"
        }
        height?.let { h ->
            require(h in 1..32000) {
                "Height must be between 1 and 32000, got: $h"
            }
        }
        require(mode in setOf('N', 'U', 'A', 'D')) {
            "Mode must be N, U, A, or D, got: $mode"
        }
    }

    override fun <T> accept(visitor: ZplNodeVisitor<T>): T = visitor.visitCode128Command(this)
}
