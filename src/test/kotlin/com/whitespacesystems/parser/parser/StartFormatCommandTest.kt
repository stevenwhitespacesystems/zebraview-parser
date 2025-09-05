package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class StartFormatCommandTest : StringSpec({

    "should parse XA start format command" {
        val lexer = Lexer("^XA")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse XA command with whitespace" {
        val lexer = Lexer("^XA ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse XA command with trailing whitespace" {
        val lexer = Lexer(" ^XA ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse XA in sequence with other commands" {
        val lexer = Lexer("^XA^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val firstCommand = program.commands[0]
        firstCommand.shouldBeInstanceOf<StartFormatCommand>()
    }

    "should parse multiple XA commands" {
        val lexer = Lexer("^XA^XA")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        program.commands[0].shouldBeInstanceOf<StartFormatCommand>()
        program.commands[1].shouldBeInstanceOf<StartFormatCommand>()
    }
})
