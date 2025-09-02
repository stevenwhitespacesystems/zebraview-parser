package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FieldSeparatorCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class FieldSeparatorCommandTest : StringSpec({

    "should parse simple FS command" {
        val lexer = Lexer("^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldSeparatorCommand>()
    }

    "should parse FS command with whitespace" {
        val lexer = Lexer("^FS ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldSeparatorCommand>()
    }

    "should parse FS command terminating field data" {
        val lexer = Lexer("^FDHello^FS")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val fsCommand = program.commands[1]
        fsCommand.shouldBeInstanceOf<FieldSeparatorCommand>()
    }

    "should parse FS command in complete ZPL program" {
        val lexer = Lexer("^XA^FO100,100^FDTest^FS^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 5
        val fsCommand = program.commands[3]
        fsCommand.shouldBeInstanceOf<FieldSeparatorCommand>()
    }
})
