package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class FontCommandTest : StringSpec({

    "should parse simple A command with default font" {
        val lexer = Lexer("^A")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe 'A'
        command.orientation shouldBe null
        command.height shouldBe null
        command.width shouldBe null
    }

    "should parse A command with font and orientation" {
        val lexer = Lexer("^A0N")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe '0'
        command.orientation shouldBe 'N'
        command.height shouldBe null
        command.width shouldBe null
    }

    "should parse A command with font, orientation and dimensions" {
        val lexer = Lexer("^A0N,30,25")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe '0'
        command.orientation shouldBe 'N'
        command.height shouldBe 30
        command.width shouldBe 25
    }

    "should parse A command with only height parameter" {
        val lexer = Lexer("^ABR,40")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe 'B'
        command.orientation shouldBe 'R'
        command.height shouldBe 40
        command.width shouldBe null
    }

    "should parse A command with letter font only" {
        val lexer = Lexer("^AB")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe 'B'
        command.orientation shouldBe null
        command.height shouldBe null
        command.width shouldBe null
    }

    "should parse A command with numeric font" {
        val lexer = Lexer("^A9I,50,50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe '9'
        command.orientation shouldBe 'I'
        command.height shouldBe 50
        command.width shouldBe 50
    }
})
