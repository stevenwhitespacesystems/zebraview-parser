package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
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
        val zplCode = """
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
})
