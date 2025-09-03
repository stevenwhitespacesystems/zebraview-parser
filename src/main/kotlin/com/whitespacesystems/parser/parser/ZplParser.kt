package com.whitespacesystems.parser.parser

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
            commandValue == "A" -> {
                // Just ^A, font will be default 'A'
                font = 'A'
                orientation = null
            }
            commandValue.length == 2 -> {
                // ^A0 or ^AB
                font = commandValue[1]
                orientation = null
            }
            commandValue.length == ZplDefaults.COMMAND_TOKEN_MIN_LENGTH -> {
                // ^A0N or ^ABR
                font = commandValue[1]
                orientation = commandValue[2]
            }
            else -> {
                throw ParseException("Invalid font command format: $commandValue")
            }
        }

        // Parse optional height and width parameters
        var height: Int? = null
        var width: Int? = null

        if (current.type == TokenType.COMMA) {
            advance() // consume comma
            height = expect(TokenType.NUMBER).value.toInt()

            if (current.type == TokenType.COMMA) {
                advance() // consume comma
                width = expect(TokenType.NUMBER).value.toInt()
            }
        }

        return FontCommand(font, orientation, height, width)
    }

    /**
     * Parse Change Font command: ^CFf,h,w
     */
    private fun parseChangeFontCommand(): ChangeFontCommand {
        val commandToken = tokens[position - 1] // Get the CF command token we just parsed
        var font = ChangeFontParsingUtils.extractFontFromCommand(commandToken)
        var height = ZplDefaults.CF_DEFAULT_HEIGHT
        var width = ZplDefaults.CF_DEFAULT_WIDTH

        // Check if we have parameters
        if (ChangeFontParsingUtils.hasParameters(current.type)) {
            when (current.type) {
                TokenType.STRING -> {
                    val text = expect(TokenType.STRING).value
                    if (text.contains(",")) {
                        // Comma-separated parameters - handle completely and return
                        return ChangeFontParsingUtils.parseCommaSeparatedParameters(text)
                    } else if (ChangeFontParsingUtils.shouldTreatAsSingleFont(text, commandToken)) {
                        font = text[0]
                    }
                }
                TokenType.COMMAND -> {
                    val text = expect(TokenType.COMMAND).value
                    if (ChangeFontParsingUtils.shouldTreatAsSingleFont(text, commandToken)) {
                        font = text[0]
                    }
                }
                TokenType.NUMBER -> {
                    val numberValue = expect(TokenType.NUMBER).value
                    val (parsedFont, parsedHeight) =
                        ChangeFontParsingUtils.parseNumberParameter(
                            numberValue,
                            commandToken,
                        )
                    parsedFont?.let { font = it }
                    parsedHeight?.let { height = it }
                }
                else -> { /* COMMA case handled below */ }
            }

            // Parse additional height and width parameters after comma
            ParameterParsingUtils.parseAdditionalFontParameters(
                { current },
                ::advance,
                ::expect,
            )?.let { (additionalHeight, additionalWidth) ->
                additionalHeight?.let { height = it }
                additionalWidth?.let { width = it }
            }
        }

        return ChangeFontCommand(font, height, width)
    }

    /**
     * Parse Graphic Box command: ^GBw,h,t,c,r
     */
    private fun parseGraphicBoxCommand(): GraphicBoxCommand {
        val width = expect(TokenType.NUMBER).value.toInt()
        expect(TokenType.COMMA)
        val height = expect(TokenType.NUMBER).value.toInt()

        var thickness = 1
        var color = 'B'
        var rounding = 0

        // Parse optional thickness parameter
        if (current.type == TokenType.COMMA) {
            advance()
            thickness = expect(TokenType.NUMBER).value.toInt()
        }

        // Parse optional color parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.STRING) {
                val colorValue = expect(TokenType.STRING).value
                if (colorValue.length == 1) {
                    color = colorValue[0]
                }
            } else if (current.type == TokenType.COMMAND) {
                // Handle case where single letter color is parsed as command (like 'W')
                val colorValue = expect(TokenType.COMMAND).value
                if (colorValue.length == 1) {
                    color = colorValue[0]
                }
            } else if (current.type == TokenType.NUMBER) {
                // Handle case where color might be parsed as number (skip for now)
                advance()
            }
        }

        // Parse optional rounding parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.NUMBER) {
                rounding = expect(TokenType.NUMBER).value.toInt()
            }
        }

        return GraphicBoxCommand(width, height, thickness, color, rounding)
    }

    /**
     * Parse Bar Code Default command: ^BYw,r,h
     */
    private fun parseBarCodeDefaultCommand(): BarCodeDefaultCommand {
        var moduleWidth = ZplDefaults.BY_DEFAULT_MODULE_WIDTH
        var widthRatio = ZplDefaults.BY_DEFAULT_WIDTH_RATIO
        var height = ZplDefaults.BY_DEFAULT_HEIGHT

        // Parse optional module width parameter
        if (current.type == TokenType.NUMBER) {
            moduleWidth = expect(TokenType.NUMBER).value.toInt()

            // Parse optional width ratio and height parameters
            val (parsedRatio, parsedHeight) =
                BarCodeParsingUtils.parseBarCodeRatioAndHeight(
                    { current },
                    ::advance,
                    ::expect,
                )
            widthRatio = parsedRatio
            height = parsedHeight
        }

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
     * Expect a specific token type and advance.
     */
    private fun expect(expectedType: TokenType): Token {
        if (current.type != expectedType) {
            throw ParseException("Expected $expectedType but found ${current.type} at position ${current.position}")
        }
        val token = current
        advance()
        return token
    }

    /**
     * Advance to the next token.
     */
    private fun advance() {
        if (position < tokens.size - 1) {
            position++
        }
    }
}

/**
 * Exception thrown by the parser when it encounters invalid syntax.
 */
class ParseException(message: String) : Exception(message)
