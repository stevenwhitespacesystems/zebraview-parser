package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.ChangeFontCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ChangeFontCommandTest : StringSpec({

    "should parse CF command with default values" {
        val lexer = Lexer("^CF")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<ChangeFontCommand>()
        command.font shouldBe 'A'
        command.height shouldBe 9
        command.width shouldBe 5
    }

    "should parse CF command with font only" {
        val lexer = Lexer("^CFB")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<ChangeFontCommand>()
        command.font shouldBe 'B'
        command.height shouldBe 9
        command.width shouldBe 5
    }

    "should parse CF command with all parameters" {
        val lexer = Lexer("^CF0,20,15")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<ChangeFontCommand>()
        command.font shouldBe '0'
        command.height shouldBe 20
        command.width shouldBe 15
    }

    "should parse CF command with height only" {
        val lexer = Lexer("^CFA,30")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<ChangeFontCommand>()
        command.font shouldBe 'A'
        command.height shouldBe 30
        command.width shouldBe 5
    }

    "should validate font parameter range" {
        shouldThrow<IllegalArgumentException> {
            ChangeFontCommand(font = '@')
        }.message shouldBe "Font must be A-Z or 0-9, got: @"
    }

    "should validate height parameter range" {
        shouldThrow<IllegalArgumentException> {
            ChangeFontCommand(height = -1)
        }.message shouldBe "Height must be between 0 and 32000, got: -1"

        shouldThrow<IllegalArgumentException> {
            ChangeFontCommand(height = 32001)
        }.message shouldBe "Height must be between 0 and 32000, got: 32001"
    }

    "should validate width parameter range" {
        shouldThrow<IllegalArgumentException> {
            ChangeFontCommand(width = -1)
        }.message shouldBe "Width must be between 0 and 32000, got: -1"

        shouldThrow<IllegalArgumentException> {
            ChangeFontCommand(width = 32001)
        }.message shouldBe "Width must be between 0 and 32000, got: 32001"
    }
})
