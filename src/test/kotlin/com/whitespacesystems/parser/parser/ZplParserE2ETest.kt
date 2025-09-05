package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.utils.AstPrinter
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ZplParserE2ETest : StringSpec({

    "should parse complete ZPL label with XA/XZ boundaries" {
        val zplCode = "^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2

        // Check start format command
        val startCommand = program.commands[0]
        startCommand.shouldBeInstanceOf<StartFormatCommand>()

        // Check end format command
        val endCommand = program.commands[1]
        endCommand.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse multiple complete ZPL labels" {
        val zplCode = "^XA^XZ^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 4

        // First label
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<EndFormatCommand>()

        // Second label
        program.commands[2].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[3].shouldBeInstanceOf<EndFormatCommand>()
    }

    "should generate readable AST output with XA/XZ commands" {
        val zplCode = "^XA^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        val printer = AstPrinter()
        val ast = printer.print(program)

        // Verify AST contains expected structure
        ast.contains("ZplProgram") shouldBe true
        ast.contains("StartFormatCommand()") shouldBe true
        ast.contains("EndFormatCommand()") shouldBe true
    }

    "should parse XA/XZ with whitespace" {
        val zplCode = " ^XA ^XZ "
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse XA only" {
        val zplCode = "^XA"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse XZ only" {
        val zplCode = "^XZ"
        val lexer = Lexer(zplCode)
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        program.commands[0].shouldBeInstanceOf<EndFormatCommand>()
    }
})
