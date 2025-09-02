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
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.utils.AstPrinter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ZplParserE2ETest : StringSpec({

    "should parse complete simple ZPL label" {
        // Simple label: position at 100,50, set font, print "Hello World"
        val zplCode = "^FO100,50^A0N,30,30^FDHello World"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3

        // Check field origin command
        val foCommand = program.commands[0]
        foCommand.shouldBeInstanceOf<FieldOriginCommand>()
        foCommand.x shouldBe 100
        foCommand.y shouldBe 50

        // Check font command
        val fontCommand = program.commands[1]
        fontCommand.shouldBeInstanceOf<FontCommand>()
        fontCommand.font shouldBe '0'
        fontCommand.orientation shouldBe 'N'
        fontCommand.height shouldBe 30
        fontCommand.width shouldBe 30

        // Check field data command
        val fdCommand = program.commands[2]
        fdCommand.shouldBeInstanceOf<FieldDataCommand>()
        fdCommand.data shouldBe "Hello World"
    }

    "should parse ZPL with multiple text fields" {
        val zplCode = "^FO50,100^ABN,25^FDFirst Line^FO50,130^ABN,25^FDSecond Line"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 6

        // First field
        program.commands[0].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[1].shouldBeInstanceOf<FontCommand>()
        program.commands[2].shouldBeInstanceOf<FieldDataCommand>()
        (program.commands[2] as FieldDataCommand).data shouldBe "First Line"

        // Second field
        program.commands[3].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[4].shouldBeInstanceOf<FontCommand>()
        program.commands[5].shouldBeInstanceOf<FieldDataCommand>()
        (program.commands[5] as FieldDataCommand).data shouldBe "Second Line"
    }

    "should parse ZPL with text containing special characters" {
        val zplCode = "^FO10,10^A0^FDPrice: \$29.99 (50% OFF!)"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3
        val fdCommand = program.commands[2] as FieldDataCommand
        fdCommand.data shouldBe "Price: \$29.99 (50% OFF!)"
    }

    "should parse ZPL with minimal font specification" {
        val zplCode = "^FO0,0^A^FDMinimal"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3
        val fontCommand = program.commands[1] as FontCommand
        fontCommand.font shouldBe 'A'
        fontCommand.orientation shouldBe null
        fontCommand.height shouldBe null
        fontCommand.width shouldBe null
    }

    "should parse ZPL with empty field data" {
        val zplCode = "^FO200,200^A0N,20^FD"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 3
        val fdCommand = program.commands[2] as FieldDataCommand
        fdCommand.data shouldBe ""
    }

    "should generate readable AST output" {
        val zplCode = "^FO100,50^A0N,30,30^FDHello World"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        val printer = AstPrinter()
        val ast = printer.print(program)

        // Verify AST contains expected structure
        ast.contains("ZplProgram") shouldBe true
        ast.contains("FieldOriginCommand(x=100, y=50)") shouldBe true
        ast.contains("FontCommand(font='0', orientation='N', height=30, width=30)") shouldBe true
        ast.contains("FieldDataCommand(data=\"Hello World\")") shouldBe true
    }

    "should parse complete ZPL label with XA/XZ boundaries" {
        val zplCode = "^XA^FO100,50^A0N,30,30^FDHello World^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 5

        // Check start format command
        val startCommand = program.commands[0]
        startCommand.shouldBeInstanceOf<StartFormatCommand>()

        // Check field origin command
        val foCommand = program.commands[1]
        foCommand.shouldBeInstanceOf<FieldOriginCommand>()
        foCommand.x shouldBe 100
        foCommand.y shouldBe 50

        // Check font command
        val fontCommand = program.commands[2]
        fontCommand.shouldBeInstanceOf<FontCommand>()
        fontCommand.font shouldBe '0'
        fontCommand.orientation shouldBe 'N'
        fontCommand.height shouldBe 30
        fontCommand.width shouldBe 30

        // Check field data command
        val fdCommand = program.commands[3]
        fdCommand.shouldBeInstanceOf<FieldDataCommand>()
        fdCommand.data shouldBe "Hello World"

        // Check end format command
        val endCommand = program.commands[4]
        endCommand.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse real-world product label with XA/XZ boundaries" {
        val zplCode =
            """
            ^XA
            ^FO300,30^A0N,30^FDProduct Label
            ^FO20,100^A0N,25^FDSKU: 112345678000001107
            ^XZ
            """.trimIndent()

        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        // First and last commands should be XA/XZ
        program.commands.first().shouldBeInstanceOf<StartFormatCommand>()
        program.commands.last().shouldBeInstanceOf<EndFormatCommand>()

        // Should contain multiple commands between start and end
        program.commands.size shouldBe 8 // XA, FO, A, FD, FO, A, FD, XZ
    }

    "should parse multiple complete ZPL labels" {
        val zplCode = "^XA^FO10,10^FDFirst^XZ^XA^FO20,20^FDSecond^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 8

        // First label
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[2].shouldBeInstanceOf<FieldDataCommand>()
        (program.commands[2] as FieldDataCommand).data shouldBe "First"
        program.commands[3].shouldBeInstanceOf<EndFormatCommand>()

        // Second label
        program.commands[4].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[5].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[6].shouldBeInstanceOf<FieldDataCommand>()
        (program.commands[6] as FieldDataCommand).data shouldBe "Second"
        program.commands[7].shouldBeInstanceOf<EndFormatCommand>()
    }

    "should generate readable AST output with XA/XZ commands" {
        val zplCode = "^XA^FO100,50^FDTest^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        val printer = AstPrinter()
        val ast = printer.print(program)

        // Verify AST contains expected structure
        ast.contains("ZplProgram") shouldBe true
        ast.contains("StartFormatCommand()") shouldBe true
        ast.contains("EndFormatCommand()") shouldBe true
        ast.contains("FieldOriginCommand(x=100, y=50)") shouldBe true
        ast.contains("FieldDataCommand(data=\"Test\")") shouldBe true
    }

    "should parse XA/XZ with empty content" {
        val zplCode = "^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse ZPL with comment and field separator" {
        val zplCode = "^XA^FXSHIPPING LABEL^FO100,100^FDTest^FS^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 6

        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()

        val commentCommand = program.commands[1]
        commentCommand.shouldBeInstanceOf<CommentCommand>()
        (commentCommand as CommentCommand).text shouldBe "SHIPPING LABEL"

        program.commands[2].shouldBeInstanceOf<FieldOriginCommand>()
        program.commands[3].shouldBeInstanceOf<FieldDataCommand>()

        val fsCommand = program.commands[4]
        fsCommand.shouldBeInstanceOf<FieldSeparatorCommand>()

        program.commands[5].shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse ZPL with field reverse and graphic box" {
        val zplCode = "^XA^FO100,100^GB150,100,5,W,2^FR^FDReversed Text^FS^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 7

        val gbCommand = program.commands[2]
        gbCommand.shouldBeInstanceOf<GraphicBoxCommand>()
        val gb = gbCommand as GraphicBoxCommand
        gb.width shouldBe 150
        gb.height shouldBe 100
        gb.thickness shouldBe 5
        gb.color shouldBe 'W'
        gb.rounding shouldBe 2

        val frCommand = program.commands[3]
        frCommand.shouldBeInstanceOf<FieldReverseCommand>()

        program.commands[5].shouldBeInstanceOf<FieldSeparatorCommand>()
    }

    "should parse ZPL with font changes and barcode settings" {
        val zplCode = "^XA^CFA,30,20^BY3,2.5,100^FO100,50^BCN,150,Y,N,N,A^FDTest123^FS^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 8

        val cfCommand = program.commands[1]
        cfCommand.shouldBeInstanceOf<ChangeFontCommand>()
        val cf = cfCommand as ChangeFontCommand
        cf.font shouldBe 'A'
        cf.height shouldBe 30
        cf.width shouldBe 20

        val byCommand = program.commands[2]
        byCommand.shouldBeInstanceOf<BarCodeDefaultCommand>()
        val by = byCommand as BarCodeDefaultCommand
        by.moduleWidth shouldBe 3
        by.widthRatio shouldBe 2.5
        by.height shouldBe 100

        val bcCommand = program.commands[4]
        bcCommand.shouldBeInstanceOf<Code128Command>()
        val bc = bcCommand as Code128Command
        bc.orientation shouldBe 'N'
        bc.height shouldBe 150
        bc.printInterpretation shouldBe true
        bc.printInterpretationAbove shouldBe false
        bc.uccCheckDigit shouldBe false
        bc.mode shouldBe 'A'
    }

    "should parse complex shipping label with all new commands" {
        val zplCode =
            """
            ^XA
            ^FXSHIPPING LABEL - Order #12345
            ^CFA,25,15
            ^FO50,50^GB400,300,4,B,0^FS
            ^FO100,100^FR^FDShip To:^FS
            ^FO100,150^FDJohn Doe^FS
            ^FO100,200^FD123 Main St^FS
            ^BY2,2.5,50
            ^FO100,250^BCN,100,Y,N,N,A^FD1234567890^FS
            ^XZ
            """.trimIndent()

        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        // Should contain all command types
        val commandTypes = program.commands.map { it::class.simpleName }
        commandTypes.contains("StartFormatCommand") shouldBe true
        commandTypes.contains("CommentCommand") shouldBe true
        commandTypes.contains("ChangeFontCommand") shouldBe true
        commandTypes.contains("GraphicBoxCommand") shouldBe true
        commandTypes.contains("FieldReverseCommand") shouldBe true
        commandTypes.contains("FieldSeparatorCommand") shouldBe true
        commandTypes.contains("BarCodeDefaultCommand") shouldBe true
        commandTypes.contains("Code128Command") shouldBe true
        commandTypes.contains("EndFormatCommand") shouldBe true
    }

    "should generate readable AST output with new commands" {
        val zplCode = "^XA^FXTEST^CF0,20^GB100,50,2,B,1^FR^FS^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        val printer = AstPrinter()
        val ast = printer.print(program)

        ast.contains("CommentCommand(text=\"TEST\")") shouldBe true
        ast.contains("ChangeFontCommand(font='0', height=20, width=5)") shouldBe true
        ast.contains("GraphicBoxCommand(width=100, height=50, thickness=2, color='B', rounding=1)") shouldBe true
        ast.contains("FieldReverseCommand()") shouldBe true
        ast.contains("FieldSeparatorCommand()") shouldBe true
    }
})
