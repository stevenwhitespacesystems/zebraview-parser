package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class FieldOriginCommandTest : StringSpec({

    "should parse simple FO command with coordinates" {
        // TDD RED: Write failing test first
        val lexer = Lexer("^FO100,50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 100
        command.y shouldBe 50
    }

    "should parse FO command with zero coordinates" {
        val lexer = Lexer("^FO0,0")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 0
        command.y shouldBe 0
    }

    "should parse FO command with maximum coordinates" {
        val lexer = Lexer("^FO32000,32000")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 32000
        command.y shouldBe 32000
    }

    "should parse FO command with whitespace" {
        val lexer = Lexer("^FO 100, 50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 100
        command.y shouldBe 50
    }
})
