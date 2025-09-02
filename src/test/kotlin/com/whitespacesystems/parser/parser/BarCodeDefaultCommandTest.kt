package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.BarCodeDefaultCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class BarCodeDefaultCommandTest : StringSpec({

    "should parse BY command with default values" {
        val lexer = Lexer("^BY")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<BarCodeDefaultCommand>()
        command.moduleWidth shouldBe 2
        command.widthRatio shouldBe 3.0
        command.height shouldBe 10
    }

    "should parse BY command with module width only" {
        val lexer = Lexer("^BY5")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<BarCodeDefaultCommand>()
        command.moduleWidth shouldBe 5
        command.widthRatio shouldBe 3.0
        command.height shouldBe 10
    }

    "should parse BY command with all parameters" {
        val lexer = Lexer("^BY3,2.5,20")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<BarCodeDefaultCommand>()
        command.moduleWidth shouldBe 3
        command.widthRatio shouldBe 2.5
        command.height shouldBe 20
    }

    "should parse BY command with width and ratio" {
        val lexer = Lexer("^BY7,2.2")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<BarCodeDefaultCommand>()
        command.moduleWidth shouldBe 7
        command.widthRatio shouldBe 2.2
        command.height shouldBe 10
    }

    "should validate module width range" {
        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(moduleWidth = 0)
        }.message shouldBe "Module width must be between 1 and 10, got: 0"

        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(moduleWidth = 11)
        }.message shouldBe "Module width must be between 1 and 10, got: 11"
    }

    "should validate width ratio range" {
        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(widthRatio = 1.9)
        }.message shouldBe "Width ratio must be between 2.0 and 3.0, got: 1.9"

        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(widthRatio = 3.1)
        }.message shouldBe "Width ratio must be between 2.0 and 3.0, got: 3.1"
    }

    "should validate width ratio precision to 0.1 increments" {
        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(widthRatio = 2.15)
        }.message shouldBe "Width ratio must be in 0.1 increments (2.0, 2.1, 2.2, ..., 3.0), got: 2.15"
    }

    "should validate height is non-negative" {
        shouldThrow<IllegalArgumentException> {
            BarCodeDefaultCommand(height = -1)
        }.message shouldBe "Height must be non-negative, got: -1"
    }

    "should accept valid ratio increments" {
        // Test boundary values
        BarCodeDefaultCommand(widthRatio = 2.0).widthRatio shouldBe 2.0
        BarCodeDefaultCommand(widthRatio = 2.5).widthRatio shouldBe 2.5
        BarCodeDefaultCommand(widthRatio = 3.0).widthRatio shouldBe 3.0
    }
})
