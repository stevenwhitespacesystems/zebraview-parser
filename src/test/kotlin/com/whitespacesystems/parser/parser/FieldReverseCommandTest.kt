package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FieldReverseCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class FieldReverseCommandTest : StringSpec({

    "should parse simple FR command" {
        val lexer = Lexer("^FR")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldReverseCommand>()
    }

    "should parse FR command with whitespace" {
        val lexer = Lexer("^FR ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldReverseCommand>()
    }

    "should parse FR command in complete ZPL program" {
        val lexer = Lexer("^XA^FO100,100^FR^FDTest^FS^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 6
        val frCommand = program.commands[2]
        frCommand.shouldBeInstanceOf<FieldReverseCommand>()
    }
})
