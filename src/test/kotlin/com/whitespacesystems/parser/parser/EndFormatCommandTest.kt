package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class EndFormatCommandTest : StringSpec({

    "should parse XZ end format command" {
        val lexer = Lexer("^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse XZ command with whitespace" {
        val lexer = Lexer("^XZ ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse XZ command with trailing whitespace" {
        val lexer = Lexer(" ^XZ ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse XZ in sequence with other commands" {
        val lexer = Lexer("^FO100,50^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        val secondCommand = program.commands[1]
        secondCommand.shouldBeInstanceOf<EndFormatCommand>()
    }

    "should parse multiple XZ commands" {
        val lexer = Lexer("^XZ^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 2
        program.commands[0].shouldBeInstanceOf<EndFormatCommand>()
        program.commands[1].shouldBeInstanceOf<EndFormatCommand>()
    }
})
