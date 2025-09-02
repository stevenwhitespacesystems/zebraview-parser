package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.Code128Command
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class Code128CommandTest : StringSpec({

    "should parse BC command with default values" {
        val lexer = Lexer("^BC")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<Code128Command>()
        command.orientation shouldBe 'N'
        command.height shouldBe null
        command.printInterpretation shouldBe true
        command.printInterpretationAbove shouldBe false
        command.uccCheckDigit shouldBe false
        command.mode shouldBe 'N'
    }

    "should parse BC command with orientation only" {
        val lexer = Lexer("^BCR")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<Code128Command>()
        command.orientation shouldBe 'R'
        command.height shouldBe null
        command.printInterpretation shouldBe true
        command.printInterpretationAbove shouldBe false
        command.uccCheckDigit shouldBe false
        command.mode shouldBe 'N'
    }

    "should parse BC command with all parameters" {
        val lexer = Lexer("^BCI,256,N,Y,Y,U")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<Code128Command>()
        command.orientation shouldBe 'I'
        command.height shouldBe 256
        command.printInterpretation shouldBe false
        command.printInterpretationAbove shouldBe true
        command.uccCheckDigit shouldBe true
        command.mode shouldBe 'U'
    }

    "should parse BC command with height and interpretation flags" {
        val lexer = Lexer("^BCN,150,Y,N")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<Code128Command>()
        command.orientation shouldBe 'N'
        command.height shouldBe 150
        command.printInterpretation shouldBe true
        command.printInterpretationAbove shouldBe false
        command.uccCheckDigit shouldBe false
        command.mode shouldBe 'N'
    }

    "should parse BC command with automatic mode" {
        val lexer = Lexer("^BC,256,Y,N,,A")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<Code128Command>()
        command.orientation shouldBe 'N'
        command.height shouldBe 256
        command.printInterpretation shouldBe true
        command.printInterpretationAbove shouldBe false
        command.uccCheckDigit shouldBe false
        command.mode shouldBe 'A'
    }

    "should validate orientation parameter" {
        shouldThrow<IllegalArgumentException> {
            Code128Command(orientation = 'X')
        }.message shouldBe "Orientation must be N, R, I, or B, got: X"
    }

    "should validate height parameter range" {
        shouldThrow<IllegalArgumentException> {
            Code128Command(height = 0)
        }.message shouldBe "Height must be between 1 and 32000, got: 0"

        shouldThrow<IllegalArgumentException> {
            Code128Command(height = 32001)
        }.message shouldBe "Height must be between 1 and 32000, got: 32001"
    }

    "should validate mode parameter" {
        shouldThrow<IllegalArgumentException> {
            Code128Command(mode = 'X')
        }.message shouldBe "Mode must be N, U, A, or D, got: X"
    }

    "should handle boolean flag conversions" {
        val validOrientations = setOf('N', 'R', 'I', 'B')
        val validModes = setOf('N', 'U', 'A', 'D')

        for (orientation in validOrientations) {
            Code128Command(orientation = orientation).orientation shouldBe orientation
        }

        for (mode in validModes) {
            Code128Command(mode = mode).mode shouldBe mode
        }
    }
})
