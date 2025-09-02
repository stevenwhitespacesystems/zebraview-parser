package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.GraphicBoxCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class GraphicBoxCommandTest : StringSpec({

    "should parse GB command with width and height only" {
        val lexer = Lexer("^GB100,50")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<GraphicBoxCommand>()
        command.width shouldBe 100
        command.height shouldBe 50
        command.thickness shouldBe 1
        command.color shouldBe 'B'
        command.rounding shouldBe 0
    }

    "should parse GB command with all parameters" {
        val lexer = Lexer("^GB150,100,5,W,3")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<GraphicBoxCommand>()
        command.width shouldBe 150
        command.height shouldBe 100
        command.thickness shouldBe 5
        command.color shouldBe 'W'
        command.rounding shouldBe 3
    }

    "should parse GB command with thickness only" {
        val lexer = Lexer("^GB200,150,10")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<GraphicBoxCommand>()
        command.width shouldBe 200
        command.height shouldBe 150
        command.thickness shouldBe 10
        command.color shouldBe 'B'
        command.rounding shouldBe 0
    }

    "should validate thickness range" {
        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 100, thickness = 0)
        }.message shouldBe "Thickness must be between 1 and 32000, got: 0"

        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 100, thickness = 32001)
        }.message shouldBe "Thickness must be between 1 and 32000, got: 32001"
    }

    "should validate width is greater than or equal to thickness" {
        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 5, height = 100, thickness = 10)
        }.message shouldBe "Width must be between 10 and 32000, got: 5"
    }

    "should validate height is greater than or equal to thickness" {
        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 5, thickness = 10)
        }.message shouldBe "Height must be between 10 and 32000, got: 5"
    }

    "should validate color parameter" {
        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 100, color = 'X')
        }.message shouldBe "Color must be 'B' (black) or 'W' (white), got: X"
    }

    "should validate rounding range" {
        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 100, rounding = -1)
        }.message shouldBe "Rounding must be between 0 and 8, got: -1"

        shouldThrow<IllegalArgumentException> {
            GraphicBoxCommand(width = 100, height = 100, rounding = 9)
        }.message shouldBe "Rounding must be between 0 and 8, got: 9"
    }
})
