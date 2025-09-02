package com.whitespacesystems.parser.lexer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CommandRecognitionUtilsTest : StringSpec({

    "should return variant info for A command with digit" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("A", '0')
        variantInfo shouldNotBe null
        variantInfo!!.maxExtraChars shouldBe 2
        variantInfo.respectsWhitespaceAndComma shouldBe true
    }

    "should return variant info for A command with letter" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("A", 'B')
        variantInfo shouldNotBe null
        variantInfo!!.maxExtraChars shouldBe 2
        variantInfo.respectsWhitespaceAndComma shouldBe true
    }

    "should return variant info for CF command with digit" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("CF", '0')
        variantInfo shouldNotBe null
        variantInfo!!.maxExtraChars shouldBe 1
        variantInfo.respectsWhitespaceAndComma shouldBe false
    }

    "should return variant info for CF command with letter" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("CF", 'B')
        variantInfo shouldNotBe null
        variantInfo!!.maxExtraChars shouldBe 1
        variantInfo.respectsWhitespaceAndComma shouldBe false
    }

    "should return variant info for BC command with letter" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("BC", 'R')
        variantInfo shouldNotBe null
        variantInfo!!.maxExtraChars shouldBe 1
        variantInfo.respectsWhitespaceAndComma shouldBe false
    }

    "should return null for BC command with digit" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("BC", '1')
        variantInfo shouldBe null
    }

    "should return null for unknown command" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("XY", 'A')
        variantInfo shouldBe null
    }

    "should return null for A command with non-alphanumeric" {
        val variantInfo = CommandRecognitionUtils.shouldReadVariantCharacters("A", ',')
        variantInfo shouldBe null
    }

    "should stop parsing on whitespace when respecting whitespace" {
        val state = CommandRecognitionUtils.CommandParsingState("A0", ' ', true, false)
        val shouldStop = CommandRecognitionUtils.shouldStopParsing(state, true)
        shouldStop shouldBe true
    }

    "should stop parsing on comma when respecting whitespace" {
        val state = CommandRecognitionUtils.CommandParsingState("A0", ',', false, true)
        val shouldStop = CommandRecognitionUtils.shouldStopParsing(state, true)
        shouldStop shouldBe true
    }

    "should not stop parsing when not respecting whitespace" {
        val state = CommandRecognitionUtils.CommandParsingState("CF", ' ', true, false)
        val shouldStop = CommandRecognitionUtils.shouldStopParsing(state, false)
        shouldStop shouldBe false
    }

    "should set field data expectation for FD command" {
        val expectation = CommandRecognitionUtils.updateFieldDataExpectation("FD", false)
        expectation shouldBe true
    }

    "should set field data expectation for FX command" {
        val expectation = CommandRecognitionUtils.updateFieldDataExpectation("FX", false)
        expectation shouldBe true
    }

    "should clear field data expectation for FS command" {
        val expectation = CommandRecognitionUtils.updateFieldDataExpectation("FS", true)
        expectation shouldBe false
    }

    "should preserve field data expectation for other commands" {
        val expectation = CommandRecognitionUtils.updateFieldDataExpectation("XA", true)
        expectation shouldBe true
    }

    "should read variant characters correctly" {
        var position = 0
        val chars = "0N,50"

        val current = { chars.getOrElse(position) { '\u0000' } }
        val advance = {
            position++
            Unit
        }
        val value = StringBuilder("A")

        val variantInfo = CommandRecognitionUtils.CommandVariantInfo(2, true)
        val charsRead = CommandRecognitionUtils.readVariantCharacters(variantInfo, current, advance, value)

        charsRead shouldBe 2
        value.toString() shouldBe "A0N"
        position shouldBe 2 // Stopped at comma
    }

    "should read single variant character without respecting whitespace" {
        var position = 0
        val chars = "B 50"

        val current = { chars.getOrElse(position) { '\u0000' } }
        val advance = {
            position++
            Unit
        }
        val value = StringBuilder("CF")

        val variantInfo = CommandRecognitionUtils.CommandVariantInfo(1, false)
        val charsRead = CommandRecognitionUtils.readVariantCharacters(variantInfo, current, advance, value)

        charsRead shouldBe 1
        value.toString() shouldBe "CFB"
        position shouldBe 1 // Did not stop at whitespace
    }

    "should determine base command reading should stop for complete command" {
        val isComplete = { command: String -> command == "FO" }
        val shouldStop = CommandRecognitionUtils.shouldStopBaseCommandReading("FO", isComplete)
        shouldStop shouldBe true
    }

    "should determine base command reading should continue for incomplete command" {
        val isComplete = { command: String -> command == "FO" }
        val shouldStop = CommandRecognitionUtils.shouldStopBaseCommandReading("F", isComplete)
        shouldStop shouldBe false
    }
})
