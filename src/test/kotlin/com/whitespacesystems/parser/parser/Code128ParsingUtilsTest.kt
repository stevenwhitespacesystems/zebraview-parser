package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Code128ParsingUtilsTest : StringSpec({

    "should extract orientation from command token" {
        val token = Token(TokenType.COMMAND, "BCR", 0)
        val orientation = Code128ParsingUtils.extractOrientationFromCommand(token)
        orientation shouldBe 'R'
    }

    "should extract default orientation for basic BC command" {
        val token = Token(TokenType.COMMAND, "BC", 0)
        val orientation = Code128ParsingUtils.extractOrientationFromCommand(token)
        orientation shouldBe 'N'
    }

    "should extract BCN orientation" {
        val token = Token(TokenType.COMMAND, "BCN", 0)
        val orientation = Code128ParsingUtils.extractOrientationFromCommand(token)
        orientation shouldBe 'N'
    }

    "should parse Y flag as true" {
        val flagValue = Code128ParsingUtils.parseBooleanFlag("Y")
        flagValue shouldBe true
    }

    "should parse N flag as false" {
        val flagValue = Code128ParsingUtils.parseBooleanFlag("N")
        flagValue shouldBe false
    }

    "should parse empty string as false" {
        val flagValue = Code128ParsingUtils.parseBooleanFlag("")
        flagValue shouldBe false
    }

    "should parse single character from token value" {
        val token = Token(TokenType.STRING, "R", 0)
        val char = Code128ParsingUtils.parseCharacterValue(token)
        char shouldBe 'R'
    }

    "should parse default character for empty token" {
        val token = Token(TokenType.STRING, "", 0)
        val char = Code128ParsingUtils.parseCharacterValue(token, default = 'N')
        char shouldBe 'N'
    }

    "should parse orientation from string when command token is basic BC" {
        val commandToken = Token(TokenType.COMMAND, "BC", 0)
        val stringToken = Token(TokenType.STRING, "R", 0)
        val orientation = Code128ParsingUtils.extractOrientationFromStringOrCommand(commandToken, stringToken)
        orientation shouldBe 'R'
    }

    "should use command token orientation when available" {
        val commandToken = Token(TokenType.COMMAND, "BCR", 0)
        val stringToken = Token(TokenType.STRING, "N", 0) // Should be ignored
        val orientation = Code128ParsingUtils.extractOrientationFromStringOrCommand(commandToken, stringToken)
        orientation shouldBe 'R'
    }
})
