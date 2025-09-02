package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.CommentCommand
import com.whitespacesystems.parser.lexer.Lexer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class CommentCommandTest : StringSpec({

    "should parse simple FX command without text" {
        val lexer = Lexer("^FX")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        command.text shouldBe ""
    }

    "should parse FX command with comment text" {
        val lexer = Lexer("^FXThis is a comment")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        command.text shouldBe "This is a comment"
    }

    "should parse FX command with special characters" {
        val lexer = Lexer("^FXSHIPPING LABEL - Order #12345")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<CommentCommand>()
        command.text shouldBe "SHIPPING LABEL - Order #12345"
    }

    "should parse FX command in complete ZPL program" {
        val lexer = Lexer("^XA^FXSHIPPING LABEL^FO100,100^FDTest^FS^XZ")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands.size shouldBe 6
        val fxCommand = program.commands[1]
        fxCommand.shouldBeInstanceOf<CommentCommand>()
        fxCommand.text shouldBe "SHIPPING LABEL"
    }
})
