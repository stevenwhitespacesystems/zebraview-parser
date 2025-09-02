package com.whitespacesystems.parser.parser

import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.lexer.Token
import com.whitespacesystems.parser.lexer.TokenType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class CommandParserRegistryTest : StringSpec({

    "should register and retrieve command parser for FO" {
        val registry = CommandParserRegistry()
        val foTokens =
            listOf(
                Token(TokenType.NUMBER, "100", 3),
                Token(TokenType.COMMA, ",", 6),
                Token(TokenType.NUMBER, "50", 7),
            )

        val command = registry.parseCommand("FO", foTokens, 0)
        command.shouldBeInstanceOf<FieldOriginCommand>()
        command.x shouldBe 100
        command.y shouldBe 50
    }

    "should register and retrieve command parser for FD" {
        val registry = CommandParserRegistry()
        val fdTokens =
            listOf(
                Token(TokenType.STRING, "Hello World", 3),
            )

        val command = registry.parseCommand("FD", fdTokens, 0)
        command.shouldBeInstanceOf<FieldDataCommand>()
        command.data shouldBe "Hello World"
    }

    "should register and retrieve command parser for A" {
        val registry = CommandParserRegistry()
        val aTokens = listOf<Token>() // Basic A command with no parameters

        val command = registry.parseCommand("A", aTokens, 0)
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe 'A' // Default font
        command.orientation shouldBe 'N' // Default orientation
    }

    "should handle unknown command gracefully" {
        val registry = CommandParserRegistry()
        val unknownTokens = listOf<Token>()

        val result =
            try {
                registry.parseCommand("UNKNOWN", unknownTokens, 0)
                null
            } catch (e: Exception) {
                e
            }

        result.shouldBeInstanceOf<ParseException>()
    }

    "should support command variants like A0, AB, ABN" {
        val registry = CommandParserRegistry()
        val a0Tokens = listOf<Token>()

        val command = registry.parseCommand("A0", a0Tokens, 0)
        command.shouldBeInstanceOf<FontCommand>()
        command.font shouldBe '0' // Scalable font
    }
})
