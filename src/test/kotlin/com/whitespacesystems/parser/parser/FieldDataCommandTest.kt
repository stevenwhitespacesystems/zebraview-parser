package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class FieldDataCommandTest : StringSpec({

    "should parse simple FD command with text data" {
        val lexer = Lexer("^FDHello World")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe "Hello World"
    }

    "should parse FD command with comma in text data" {
        val lexer = Lexer("^FDHello, World")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe "Hello, World"
    }

    "should parse FD command with special characters" {
        val lexer = Lexer("^FDPrice: \$19.99 (50% off!)")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe "Price: \$19.99 (50% off!)"
    }

    "should parse FD command with empty data" {
        val lexer = Lexer("^FD")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe ""
    }

    "should parse FD command with numbers in text" {
        val lexer = Lexer("^FDPART-12345")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe "PART-12345"
    }
})
