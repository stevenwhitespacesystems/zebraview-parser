package com.whitespacesystems.parser.ast

/**
 * Base class for all ZPL AST nodes.
 * Uses sealed class to ensure exhaustive when expressions.
 */
sealed class ZplNode {
    /**
     * Accept a visitor for traversing the AST.
     * This enables the visitor pattern for operations on the AST.
     */
    abstract fun <T> accept(visitor: ZplNodeVisitor<T>): T
}

/**
 * Visitor interface for traversing ZPL AST nodes.
 * Implementations can perform operations like printing, validation, or code generation.
 */
interface ZplNodeVisitor<T> {
    fun visitStartFormatCommand(command: StartFormatCommand): T

    fun visitEndFormatCommand(command: EndFormatCommand): T

    fun visitFieldOriginCommand(command: FieldOriginCommand): T

    fun visitFieldDataCommand(command: FieldDataCommand): T

    fun visitFontCommand(command: FontCommand): T

    fun visitCommentCommand(command: CommentCommand): T

    fun visitFieldReverseCommand(command: FieldReverseCommand): T

    fun visitFieldSeparatorCommand(command: FieldSeparatorCommand): T

    fun visitChangeFontCommand(command: ChangeFontCommand): T

    fun visitGraphicBoxCommand(command: GraphicBoxCommand): T

    fun visitBarCodeDefaultCommand(command: BarCodeDefaultCommand): T

    fun visitCode128Command(command: Code128Command): T

    fun visitZplProgram(program: ZplProgram): T
}
