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

    fun visitZplProgram(program: ZplProgram): T
}
