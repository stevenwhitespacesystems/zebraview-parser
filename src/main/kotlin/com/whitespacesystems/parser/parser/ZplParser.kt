package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.BarCodeDefaultCommand
import com.whitespacesystems.parser.ast.ChangeFontCommand
import com.whitespacesystems.parser.ast.Code128Command
import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FieldReverseCommand
import com.whitespacesystems.parser.ast.FieldSeparatorCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.GraphicBoxCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
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
            "XA" -> parseStartFormatCommand()
            "XZ" -> parseEndFormatCommand()
            "FO" -> parseFieldOriginCommand()
            "FD" -> parseFieldDataCommand()
            "FX" -> parseCommentCommand()
            "CF" -> parseChangeFontCommand()
            "GB" -> parseGraphicBoxCommand()
            "FR" -> parseFieldReverseCommand()
            "FS" -> parseFieldSeparatorCommand()
            "BY" -> parseBarCodeDefaultCommand()
            "BC" -> parseCode128Command()
            "A" -> parseFontCommand(commandToken.value)
            else -> {
                // Handle command variants
                when {
                    commandToken.value.startsWith("A") -> parseFontCommand(commandToken.value)
                    commandToken.value.startsWith("CF") -> parseChangeFontCommand()
                    commandToken.value.startsWith("BC") -> parseCode128Command()
                    else -> throw ParseException(
                        "Unknown command: ${commandToken.value} at position ${commandToken.position}"
                    )
                }
            }
        }
    }

    /**
     * Parse Start Format command: ^XA
     */
    private fun parseStartFormatCommand(): StartFormatCommand {
        // No parameters to parse - XA command is complete after COMMAND token
        return StartFormatCommand()
    }

    /**
     * Parse End Format command: ^XZ
     */
    private fun parseEndFormatCommand(): EndFormatCommand {
        // No parameters to parse - XZ command is complete after COMMAND token
        return EndFormatCommand()
    }

    /**
     * Parse Field Origin command: ^FOx,y
     */
    private fun parseFieldOriginCommand(): FieldOriginCommand {
        val x = expect(TokenType.NUMBER).value.toInt()
        expect(TokenType.COMMA)
        val y = expect(TokenType.NUMBER).value.toInt()

        return FieldOriginCommand(x, y)
    }

    /**
     * Parse Field Data command: ^FDdata
     * Field data is optional - if no STRING token follows, data is empty
     */
    private fun parseFieldDataCommand(): FieldDataCommand {
        val data =
            if (current.type == TokenType.STRING) {
                expect(TokenType.STRING).value
            } else {
                // Empty field data
                ""
            }
        return FieldDataCommand(data)
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
            commandValue.length == 3 -> {
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
     * Parse Comment command: ^FXtext
     */
    private fun parseCommentCommand(): CommentCommand {
        val text =
            if (current.type == TokenType.STRING) {
                expect(TokenType.STRING).value
            } else {
                ""
            }
        return CommentCommand(text)
    }

    /**
     * Parse Field Reverse command: ^FR
     */
    private fun parseFieldReverseCommand(): FieldReverseCommand {
        return FieldReverseCommand()
    }

    /**
     * Parse Field Separator command: ^FS
     */
    private fun parseFieldSeparatorCommand(): FieldSeparatorCommand {
        return FieldSeparatorCommand()
    }

    /**
     * Parse Change Font command: ^CFf,h,w
     */
    private fun parseChangeFontCommand(): ChangeFontCommand {
        var font = 'A'
        var height = 9
        var width = 5

        // First check if command token itself contains the font (CFB, CF0, etc.)
        val commandToken = tokens[position - 1] // Get the CF command token we just parsed
        if (commandToken.value.length > 2) {
            font = commandToken.value[2] // Extract font from CFB -> B, CF0 -> 0
        }

        // Check if we have parameters coming (STRING, COMMAND, NUMBER, or COMMA for additional parameters)
        if (current.type == TokenType.STRING || current.type == TokenType.COMMAND || current.type == TokenType.NUMBER || current.type == TokenType.COMMA) {
            if (current.type == TokenType.STRING) {
                val text = expect(TokenType.STRING).value

                // Check if it contains comma-separated values
                if (text.contains(",")) {
                    val parts = text.split(",")
                    if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
                        font = parts[0][0]
                    }
                    if (parts.size > 1 && parts[1].isNotEmpty()) {
                        height = parts[1].toInt()
                    }
                    if (parts.size > 2 && parts[2].isNotEmpty()) {
                        width = parts[2].toInt()
                    }
                    return ChangeFontCommand(font, height, width)
                } else if (text.isNotEmpty() && commandToken.value.length == 2) {
                    // Single font character only if command was plain CF
                    font = text[0]
                }
            } else if (current.type == TokenType.COMMAND) {
                val text = expect(TokenType.COMMAND).value
                if (text.isNotEmpty() && commandToken.value.length == 2) {
                    // Single font character only if command was plain CF
                    font = text[0]
                }
            } else if (current.type == TokenType.NUMBER) {
                // Starts with a number, treat first as font or height
                val firstParam = expect(TokenType.NUMBER).value
                if (firstParam.length == 1 && firstParam[0].isDigit() && commandToken.value.length == 2) {
                    // Single digit font only if command was plain CF
                    font = firstParam[0]
                } else {
                    // It's probably height, font remains as set above
                    height = firstParam.toInt()
                }
            }

            // Parse height and width parameters (starting with comma or continuing from number)
            if (current.type == TokenType.COMMA) {
                advance()
                if (current.type == TokenType.NUMBER) {
                    height = expect(TokenType.NUMBER).value.toInt()

                    if (current.type == TokenType.COMMA) {
                        advance()
                        if (current.type == TokenType.NUMBER) {
                            width = expect(TokenType.NUMBER).value.toInt()
                        }
                    }
                }
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
        var moduleWidth = 2
        var widthRatio = 3.0
        var height = 10

        // Parse optional module width parameter
        if (current.type == TokenType.NUMBER) {
            moduleWidth = expect(TokenType.NUMBER).value.toInt()

            // Parse optional width ratio parameter
            if (current.type == TokenType.COMMA) {
                advance()
                if (current.type == TokenType.NUMBER) {
                    val ratioStr = expect(TokenType.NUMBER).value
                    widthRatio = ratioStr.toDouble()
                } else if (current.type == TokenType.STRING) {
                    // Handle case where decimal might be parsed as string
                    val ratioStr = expect(TokenType.STRING).value
                    widthRatio = ratioStr.toDouble()
                }

                // Parse optional height parameter
                if (current.type == TokenType.COMMA) {
                    advance()
                    if (current.type == TokenType.NUMBER) {
                        height = expect(TokenType.NUMBER).value.toInt()
                    }
                }
            }
        }

        return BarCodeDefaultCommand(moduleWidth, widthRatio, height)
    }

    /**
     * Parse Code 128 command: ^BCo,h,f,g,e,m
     */
    private fun parseCode128Command(): Code128Command {
        var orientation = 'N'
        var height: Int? = null
        var printInterpretation = true
        var printInterpretationAbove = false
        var uccCheckDigit = false
        var mode = 'N'

        // First check if command token itself contains the orientation (BCR, BCN, etc.)
        val commandToken = tokens[position - 1] // Get the BC command token we just parsed
        if (commandToken.value.length > 2) {
            orientation = commandToken.value[2] // Extract orientation from BCR -> R, BCN -> N
        }

        // Parse optional orientation parameter - may be first char of string (if not in command token)
        if (current.type == TokenType.STRING && current.value.isNotEmpty() && commandToken.value.length == 2) {
            val text = expect(TokenType.STRING).value
            if (text.isNotEmpty()) {
                orientation = text[0]
            }
        }

        // Parse optional height parameter
        if (current.type == TokenType.COMMA) {
            advance()
            if (current.type == TokenType.NUMBER) {
                height = expect(TokenType.NUMBER).value.toInt()
            }

            // Parse optional print interpretation parameter
            if (current.type == TokenType.COMMA) {
                advance()
                if (current.type == TokenType.STRING) {
                    val flagValue = expect(TokenType.STRING).value
                    if (flagValue.isNotEmpty()) {
                        printInterpretation = (flagValue[0] == 'Y')
                    }
                } else if (current.type == TokenType.COMMAND) {
                    val flagValue = expect(TokenType.COMMAND).value
                    if (flagValue.isNotEmpty()) {
                        printInterpretation = (flagValue[0] == 'Y')
                    }
                }

                // Parse optional print interpretation above parameter
                if (current.type == TokenType.COMMA) {
                    advance()
                    if (current.type == TokenType.STRING) {
                        val flagValue = expect(TokenType.STRING).value
                        if (flagValue.isNotEmpty()) {
                            printInterpretationAbove = (flagValue[0] == 'Y')
                        }
                    } else if (current.type == TokenType.COMMAND) {
                        val flagValue = expect(TokenType.COMMAND).value
                        if (flagValue.isNotEmpty()) {
                            printInterpretationAbove = (flagValue[0] == 'Y')
                        }
                    }

                    // Parse optional UCC check digit parameter
                    if (current.type == TokenType.COMMA) {
                        advance()
                        if (current.type == TokenType.STRING) {
                            val flagValue = expect(TokenType.STRING).value
                            if (flagValue.isNotEmpty()) {
                                uccCheckDigit = (flagValue[0] == 'Y')
                            }
                        } else if (current.type == TokenType.COMMAND) {
                            val flagValue = expect(TokenType.COMMAND).value
                            if (flagValue.isNotEmpty()) {
                                uccCheckDigit = (flagValue[0] == 'Y')
                            }
                        }

                        // Parse optional mode parameter
                        if (current.type == TokenType.COMMA) {
                            advance()
                            if (current.type == TokenType.STRING) {
                                val modeValue = expect(TokenType.STRING).value
                                if (modeValue.isNotEmpty()) {
                                    mode = modeValue[0]
                                }
                            } else if (current.type == TokenType.COMMAND) {
                                val modeValue = expect(TokenType.COMMAND).value
                                if (modeValue.isNotEmpty()) {
                                    mode = modeValue[0]
                                }
                            }
                        }
                    }
                }
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
