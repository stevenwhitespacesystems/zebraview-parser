package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.BECommand
import com.whitespacesystems.parser.ast.BarCodeDefaultCommand
import com.whitespacesystems.parser.ast.ChangeFontCommand
import com.whitespacesystems.parser.ast.Code128Command
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.GraphicBoxCommand
import com.whitespacesystems.parser.ast.ZplNode
import com.whitespacesystems.parser.ast.ZplProgram
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType

/*
 * ZPL parser that converts tokens into an Abstract Syntax Tree (AST).
 *
 * This is a recursive descent parser optimized for high-performance parsing
 * of ZPL commands. It follows the structure:
 *
 * Program -> Command*
 * Command -> FieldOriginCommand | FieldDataCommand | FontCommand
 * FieldOriginCommand -> '^' 'FO' NUMBER ',' NUMBER
 * FieldDataCommand -> '^' 'FD' STRING
 * FontCommand -> '^' 'A' [font] [',' NUMBER] [',' NUMBER]
 */
class ZplParser(private val tokens: List<Token>) {
    // ZPL specification default values
    private object ZplDefaults {
        const val COMMAND_TOKEN_MIN_LENGTH = 3
        const val CF_DEFAULT_HEIGHT = 9
        const val CF_DEFAULT_WIDTH = 5
        const val BY_DEFAULT_MODULE_WIDTH = 2
        const val BY_DEFAULT_WIDTH_RATIO = 3.0
        const val BY_DEFAULT_HEIGHT = 10
    }

    private var position = 0

    private val current: Token
        get() = if (position >= tokens.size) tokens.last() else tokens[position]

    /**
     * Parse tokens into a ZPL program AST.
     */
    fun parse(): ZplProgram {
        val commands = mutableListOf<ZplNode>()

        while (current.type != TokenType.EOF) {
            val command = parseCommand()
            commands.add(command)
        }

        return ZplProgram(commands)
    }

    /**
     * Parse a single ZPL command.
     */
    private fun parseCommand(): ZplNode {
        expect(TokenType.CARET)

        val commandToken = expect(TokenType.COMMAND)

        return when (commandToken.value) {
            "XA" -> BasicCommandParsingUtils.parseStartFormatCommand()
            "XZ" -> BasicCommandParsingUtils.parseEndFormatCommand()
            "FO" -> BasicCommandParsingUtils.parseFieldOriginCommand({ current }, ::advance, ::expect)
            "FD" -> BasicCommandParsingUtils.parseFieldDataCommand({ current }, ::advance, ::expect)
            "FX" -> BasicCommandParsingUtils.parseCommentCommand({ current }, ::advance, ::expect)
            "CF" -> parseChangeFontCommand()
            "GB" -> parseGraphicBoxCommand()
            "FR" -> BasicCommandParsingUtils.parseFieldReverseCommand()
            "FS" -> BasicCommandParsingUtils.parseFieldSeparatorCommand()
            "BY" -> parseBarCodeDefaultCommand()
            "BC" -> parseCode128Command()
            "BE" -> parseBECommand()
            "A" -> parseFontCommand(commandToken.value)
            else -> parseCommandVariant(commandToken)
        }
    }

    /**
     * Parse command variants (A0N, CFB, BCR, etc.)
     */
    private fun parseCommandVariant(commandToken: Token): ZplNode {
        if (!CommandParsingUtils.isCommandVariant(commandToken.value)) {
            throw ParseException(
                "Unknown command: ${commandToken.value} at position ${commandToken.position}",
            )
        }

        return when (CommandParsingUtils.getVariantType(commandToken.value)) {
            "A" -> parseFontCommand(commandToken.value)
            "CF" -> parseChangeFontCommand()
            "BC" -> parseCode128Command()
            "BE" -> parseBECommand()
            else -> throw ParseException(
                "Unsupported command variant: ${commandToken.value}",
            )
        }
    }

    /*
     * Parse Font command: ^A[font][orientation],height,width
     * Examples: ^A0N,30,30 or ^ABN,20 or ^A0
     */
    private fun parseFontCommand(commandValue: String): FontCommand {
        val font: Char
        val orientation: Char?

        // Parse font and orientation from command value
        when {
            commandValue.length >= 3 -> {
                font = commandValue[1]
                orientation = commandValue[2]
            }

            commandValue.length == 2 -> {
                font = commandValue[1]
                orientation = null
            }

            else -> {
                font = '0'
                orientation = null
            }
        }

        // Parse height parameter
        val height = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect)

        // Parse width parameter
        val width = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect)

        return FontCommand(
            font,
            orientation,
            height,
            width,
        )
    }

    /**
     * Parse Change Font command: ^CFf,h,w
     */
    private fun parseChangeFontCommand(): ChangeFontCommand {
        val commandToken = tokens[position - 1] // Get the CF command token we just parsed
        var font = ChangeFontParsingUtils.extractFontFromCommand(commandToken)
        var height = ZplDefaults.CF_DEFAULT_HEIGHT
        var width = ZplDefaults.CF_DEFAULT_WIDTH

        // Parse font from parameter if not embedded in command
        if (commandToken.value.length == 2 && current.type == TokenType.STRING) {
            val fontParam = expect(TokenType.STRING)
            font = ChangeFontParsingUtils.parseFontParameter(fontParam)
        }

        // Parse height
        height = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect) ?: height

        // Parse width
        width = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect) ?: width

        return ChangeFontCommand(font, height, width)
    }

    /**
     * Advance to the next token.
     */
    private fun advance(): Token {
        val current = this.current
        if (position < tokens.size - 1) {
            position++
        }
        return current
    }

    /**
     * Expect a specific token type and advance.
     */
    private fun expect(expectedType: TokenType): Token {
        val currentToken = current
        if (currentToken.type == expectedType) {
            advance()
            return currentToken
        } else {
            throw ParseException(
                "Expected $expectedType but got ${currentToken.type} with value '${currentToken.value}' at position ${currentToken.position}",
            )
        }
    }

    /**
     * Parse Graphic Box command: ^GBw,h,t,c,r
     */
    private fun parseGraphicBoxCommand(): GraphicBoxCommand {
        val width = expect(TokenType.NUMBER).value.toInt()
        expect(TokenType.COMMA)
        val height = expect(TokenType.NUMBER).value.toInt()

        var thickness = 1
        var lineColor = 'B'
        var cornerRadius = 0

        // Parse optional thickness parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.NUMBER) {
                thickness = expect(TokenType.NUMBER).value.toInt()
            }
        }

        // Parse optional line color parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.STRING) {
                lineColor = expect(TokenType.STRING).value[0]
            }
        }

        // Parse optional corner radius parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.NUMBER) {
                cornerRadius = expect(TokenType.NUMBER).value.toInt()
            }
        }

        return GraphicBoxCommand(width, height, thickness, lineColor, cornerRadius)
    }

    /**
     * Parse Bar Code Default command: ^BYw,r,h
     */
    private fun parseBarCodeDefaultCommand(): BarCodeDefaultCommand {
        var moduleWidth = ZplDefaults.BY_DEFAULT_MODULE_WIDTH
        var widthRatio = ZplDefaults.BY_DEFAULT_WIDTH_RATIO
        var height = ZplDefaults.BY_DEFAULT_HEIGHT

        // Parse optional module width parameter
        moduleWidth = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect) ?: moduleWidth

        // Parse width ratio and height using barcode utils
        val (parsedRatio, parsedHeight) = BarCodeParsingUtils.parseBarCodeRatioAndHeight({ current }, ::advance, ::expect)
        widthRatio = parsedRatio
        height = parsedHeight

        return BarCodeDefaultCommand(moduleWidth, widthRatio, height)
    }

    /**
     * Parse Code 128 command: ^BCo,h,f,g,e,m
     */
    private fun parseCode128Command(): Code128Command {
        var height: Int? = null
        var printInterpretation = true
        var printInterpretationAbove = false
        var uccCheckDigit = false
        var mode = 'N'

        // Extract orientation from command token first
        val commandToken = tokens[position - 1]
        var orientation = Code128ParsingUtils.extractOrientationFromCommand(commandToken)

        // Parse orientation from string parameter if not in command token
        if (commandToken.value.length == 2 && current.type == TokenType.STRING && current.value.isNotEmpty()) {
            val stringToken = expect(TokenType.STRING)
            orientation = Code128ParsingUtils.parseCharacterValue(stringToken, 'N')
        }

        // Parse height parameter
        height = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect)

        // Parse print interpretation flag
        printInterpretation =
            ParameterParsingUtils.parseOptionalBooleanParameter(
                printInterpretation,
                { current },
                ::advance,
            )

        // Parse print interpretation above flag
        printInterpretationAbove =
            ParameterParsingUtils.parseOptionalBooleanParameter(printInterpretationAbove, {
                current
            }, ::advance)

        // Parse UCC check digit flag
        uccCheckDigit = ParameterParsingUtils.parseOptionalBooleanParameter(uccCheckDigit, { current }, ::advance)

        // Parse mode parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.STRING || current.type == TokenType.COMMAND) {
                val token = current
                advance()
                mode = Code128ParsingUtils.parseCharacterValue(token, 'N')
            }
        }

        return Code128Command(orientation, height, printInterpretation, printInterpretationAbove, uccCheckDigit, mode)
    }

    /**
     * Parse BE command: ^BEo,h,f,g
     */
    private fun parseBECommand(): BECommand {
        var height: Int? = null
        var printInterpretation = true
        var printInterpretationAbove = false

        // Extract orientation from command token first
        val commandToken = tokens[position - 1]
        var orientation = BEParsingUtils.extractOrientationFromCommand(commandToken)

        // Parse orientation from string parameter if not in command token
        if (commandToken.value.length == 2 && current.type == TokenType.STRING && current.value.isNotEmpty()) {
            val stringToken = expect(TokenType.STRING)
            orientation = BEParsingUtils.parseCharacterValue(stringToken, 'N')
        }

        // Parse height parameter
        height = ParameterParsingUtils.parseOptionalNumberParameter({ current }, ::advance, ::expect)

        // Parse print interpretation flag
        printInterpretation =
            ParameterParsingUtils.parseOptionalBooleanParameter(
                printInterpretation,
                { current },
                ::advance,
            )

        // Parse print interpretation above flag
        printInterpretationAbove =
            ParameterParsingUtils.parseOptionalBooleanParameter(printInterpretationAbove, {
                current
            }, ::advance)

        return BECommand(orientation, height, printInterpretation, printInterpretationAbove)
    }
}