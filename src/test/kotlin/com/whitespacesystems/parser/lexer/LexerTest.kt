package com.whitespacesystems.parser.lexer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class LexerTest : StringSpec({

    "should tokenize simple FO command" {
        val lexer = Lexer("^FO100,50")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 6 // CARET, COMMAND, NUMBER, COMMA, NUMBER, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FO", 1)
        tokens[2] shouldBe Token(TokenType.NUMBER, "100", 3)
        tokens[3] shouldBe Token(TokenType.COMMA, ",", 6)
        tokens[4] shouldBe Token(TokenType.NUMBER, "50", 7)
        tokens[5] shouldBe Token(TokenType.EOF, "", 9)
    }

    "should tokenize FD command with string data" {
        val lexer = Lexer("^FDHello World")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 4 // CARET, COMMAND, STRING, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FD", 1)
        tokens[2] shouldBe Token(TokenType.STRING, "Hello World", 3)
        tokens[3] shouldBe Token(TokenType.EOF, "", 14)
    }

    "should handle FD command with comma in string data" {
        val lexer = Lexer("^FDHello, World")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 4 // CARET, COMMAND, STRING, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FD", 1)
        tokens[2] shouldBe Token(TokenType.STRING, "Hello, World", 3) // Comma should be part of string
        tokens[3] shouldBe Token(TokenType.EOF, "", 15)
    }

    "should tokenize A command with multiple parameters" {
        val lexer = Lexer("^A0N,50,50")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 7 // CARET, COMMAND, COMMAND, COMMA, NUMBER, COMMA, NUMBER, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "A0N", 1) // Font command with font and orientation
        tokens[2] shouldBe Token(TokenType.COMMA, ",", 4)
        tokens[3] shouldBe Token(TokenType.NUMBER, "50", 5)
        tokens[4] shouldBe Token(TokenType.COMMA, ",", 7)
        tokens[5] shouldBe Token(TokenType.NUMBER, "50", 8)
        tokens[6] shouldBe Token(TokenType.EOF, "", 10)
    }

    "should handle control commands with tilde" {
        val lexer = Lexer("~HI")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 3 // CARET, COMMAND, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "~", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "HI", 1)
        tokens[2] shouldBe Token(TokenType.EOF, "", 3)
    }

    "should skip whitespace correctly" {
        val lexer = Lexer("^FO 100, 50")
        val tokens = lexer.tokenize()

        tokens shouldHaveSize 6 // CARET, COMMAND, NUMBER, COMMA, NUMBER, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FO", 1)
        tokens[2] shouldBe Token(TokenType.NUMBER, "100", 4)
        tokens[3] shouldBe Token(TokenType.COMMA, ",", 7)
        tokens[4] shouldBe Token(TokenType.NUMBER, "50", 9)
        tokens[5] shouldBe Token(TokenType.EOF, "", 11)
    }

    "should tokenize complete ZPL program" {
        val lexer = Lexer("^FO100,50^A0N,30,30^FDHello World")
        val tokens = lexer.tokenize()

        // Should handle multiple commands in sequence
        tokens shouldHaveSize 15 // Multiple commands tokens + EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FO", 1)
        tokens[2] shouldBe Token(TokenType.NUMBER, "100", 3)
        tokens[3] shouldBe Token(TokenType.COMMA, ",", 6)
        tokens[4] shouldBe Token(TokenType.NUMBER, "50", 7)
        tokens[5] shouldBe Token(TokenType.CARET, "^", 9)
        tokens[6] shouldBe Token(TokenType.COMMAND, "A0N", 10)
        tokens[7] shouldBe Token(TokenType.COMMA, ",", 13)
        tokens[8] shouldBe Token(TokenType.NUMBER, "30", 14)
        tokens[9] shouldBe Token(TokenType.COMMA, ",", 16)
        tokens[10] shouldBe Token(TokenType.NUMBER, "30", 17)
        tokens[11] shouldBe Token(TokenType.CARET, "^", 19)
        tokens[12] shouldBe Token(TokenType.COMMAND, "FD", 20)
        tokens[13] shouldBe Token(TokenType.STRING, "Hello World", 22)
        tokens[14] shouldBe Token(TokenType.EOF, "", 33)
    }

    "should handle FD followed by another command" {
        val lexer = Lexer("^FDHello, World^FO100,50")
        val tokens = lexer.tokenize()

        // FD string should stop at the next ^ command
        tokens shouldHaveSize 9 // CARET, COMMAND, STRING, CARET, COMMAND, NUMBER, COMMA, NUMBER, EOF
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "FD", 1)
        tokens[2] shouldBe Token(TokenType.STRING, "Hello, World", 3) // Should include comma
        tokens[3] shouldBe Token(TokenType.CARET, "^", 15)
        tokens[4] shouldBe Token(TokenType.COMMAND, "FO", 16)
        tokens[5] shouldBe Token(TokenType.NUMBER, "100", 18)
        tokens[6] shouldBe Token(TokenType.COMMA, ",", 21)
        tokens[7] shouldBe Token(TokenType.NUMBER, "50", 22)
        tokens[8] shouldBe Token(TokenType.EOF, "", 24)
    }
})
