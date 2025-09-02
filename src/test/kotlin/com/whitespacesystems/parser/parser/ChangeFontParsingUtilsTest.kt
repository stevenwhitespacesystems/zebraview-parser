package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ChangeFontParsingUtilsTest : StringSpec({

    "should extract font from command token CFB" {
        val token = Token(TokenType.COMMAND, "CFB", 0)
        val font = ChangeFontParsingUtils.extractFontFromCommand(token)
        font shouldBe 'B'
    }

    "should extract font from command token CF0" {
        val token = Token(TokenType.COMMAND, "CF0", 0)
        val font = ChangeFontParsingUtils.extractFontFromCommand(token)
        font shouldBe '0'
    }

    "should return default font for plain CF command" {
        val token = Token(TokenType.COMMAND, "CF", 0)
        val font = ChangeFontParsingUtils.extractFontFromCommand(token)
        font shouldBe 'A'
    }

    "should parse comma-separated parameters with all values" {
        val command = ChangeFontParsingUtils.parseCommaSeparatedParameters("B,20,15")
        command.font shouldBe 'B'
        command.height shouldBe 20
        command.width shouldBe 15
    }

    "should parse comma-separated parameters with font and height only" {
        val command = ChangeFontParsingUtils.parseCommaSeparatedParameters("0,25")
        command.font shouldBe '0'
        command.height shouldBe 25
        command.width shouldBe 5 // Default width
    }

    "should parse comma-separated parameters with font only" {
        val command = ChangeFontParsingUtils.parseCommaSeparatedParameters("D")
        command.font shouldBe 'D'
        command.height shouldBe 9 // Default height
        command.width shouldBe 5 // Default width
    }

    "should handle empty comma-separated parameters" {
        val command = ChangeFontParsingUtils.parseCommaSeparatedParameters("")
        command.font shouldBe 'A' // Default font
        command.height shouldBe 9 // Default height
        command.width shouldBe 5 // Default width
    }

    "should treat single character as font for plain CF command" {
        val commandToken = Token(TokenType.COMMAND, "CF", 0)
        val shouldTreat = ChangeFontParsingUtils.shouldTreatAsSingleFont("B", commandToken)
        shouldTreat shouldBe true
    }

    "should not treat single character as font for extended CF command" {
        val commandToken = Token(TokenType.COMMAND, "CFB", 0)
        val shouldTreat = ChangeFontParsingUtils.shouldTreatAsSingleFont("0", commandToken)
        shouldTreat shouldBe false
    }

    "should not treat empty string as font" {
        val commandToken = Token(TokenType.COMMAND, "CF", 0)
        val shouldTreat = ChangeFontParsingUtils.shouldTreatAsSingleFont("", commandToken)
        shouldTreat shouldBe false
    }

    "should parse single digit as font for plain CF command" {
        val commandToken = Token(TokenType.COMMAND, "CF", 0)
        val (font, height) = ChangeFontParsingUtils.parseNumberParameter("0", commandToken)
        font shouldBe '0'
        height shouldBe null
    }

    "should parse number as height for extended CF command" {
        val commandToken = Token(TokenType.COMMAND, "CFB", 0)
        val (font, height) = ChangeFontParsingUtils.parseNumberParameter("20", commandToken)
        font shouldBe null
        height shouldBe 20
    }

    "should parse multi-digit number as height even for plain CF command" {
        val commandToken = Token(TokenType.COMMAND, "CF", 0)
        val (font, height) = ChangeFontParsingUtils.parseNumberParameter("25", commandToken)
        font shouldBe null
        height shouldBe 25
    }

    "should detect parameters for STRING token" {
        val hasParams = ChangeFontParsingUtils.hasParameters(TokenType.STRING)
        hasParams shouldBe true
    }

    "should detect parameters for COMMA token" {
        val hasParams = ChangeFontParsingUtils.hasParameters(TokenType.COMMA)
        hasParams shouldBe true
    }

    "should not detect parameters for EOF token" {
        val hasParams = ChangeFontParsingUtils.hasParameters(TokenType.EOF)
        hasParams shouldBe false
    }

    "should not detect parameters for CARET token" {
        val hasParams = ChangeFontParsingUtils.hasParameters(TokenType.CARET)
        hasParams shouldBe false
    }
})
