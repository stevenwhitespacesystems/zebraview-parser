package com.whitespacesystems.parser

import com.whitespacesystems.parser.lexer.Lexer
import com.whitespacesystems.parser.lexer.LexerException
import com.whitespacesystems.parser.parser.ParseException
import com.whitespacesystems.parser.parser.ZplParser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ErrorHandlingTest : StringSpec({

    "should throw LexerException for unexpected character" {
        val lexer = Lexer("@")

        val exception =
            shouldThrow<LexerException> {
                lexer.tokenize()
            }

        exception.message shouldBe "Unexpected character: '@' at position 0"
    }

    "should throw ParseException for invalid command" {
        val lexer = Lexer("^XY123")
        val parser = ZplParser(lexer.tokenize())

        val exception =
            shouldThrow<ParseException> {
                parser.parse()
            }

        exception.message shouldBe "Unknown command: XY at position 1"
    }

    "should throw ParseException for unsupported command" {
        val lexer = Lexer("^FO100,50")
        val parser = ZplParser(lexer.tokenize())

        val exception =
            shouldThrow<ParseException> {
                parser.parse()
            }

        exception.message shouldBe "Unknown command: FO at position 1"
    }
})
