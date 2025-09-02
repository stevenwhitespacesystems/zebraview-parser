package com.whitespacesystems.parser.parser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CommandParsingUtilsTest : StringSpec({

    "should identify A command variants" {
        CommandParsingUtils.isCommandVariant("A0") shouldBe true
        CommandParsingUtils.isCommandVariant("A0N") shouldBe true
        CommandParsingUtils.isCommandVariant("ABR") shouldBe true
        CommandParsingUtils.isCommandVariant("A") shouldBe false
    }

    "should identify CF command variants" {
        CommandParsingUtils.isCommandVariant("CFB") shouldBe true
        CommandParsingUtils.isCommandVariant("CF0") shouldBe true
        CommandParsingUtils.isCommandVariant("CF") shouldBe false
    }

    "should identify BC command variants" {
        CommandParsingUtils.isCommandVariant("BCN") shouldBe true
        CommandParsingUtils.isCommandVariant("BCR") shouldBe true
        CommandParsingUtils.isCommandVariant("BC") shouldBe false
    }

    "should not identify non-variant commands" {
        CommandParsingUtils.isCommandVariant("FO") shouldBe false
        CommandParsingUtils.isCommandVariant("FD") shouldBe false
        CommandParsingUtils.isCommandVariant("XA") shouldBe false
    }

    "should get correct variant type for A commands" {
        CommandParsingUtils.getVariantType("A0") shouldBe "A"
        CommandParsingUtils.getVariantType("A0N") shouldBe "A"
        CommandParsingUtils.getVariantType("ABR") shouldBe "A"
    }

    "should get correct variant type for CF commands" {
        CommandParsingUtils.getVariantType("CFB") shouldBe "CF"
        CommandParsingUtils.getVariantType("CF0") shouldBe "CF"
    }

    "should get correct variant type for BC commands" {
        CommandParsingUtils.getVariantType("BCN") shouldBe "BC"
        CommandParsingUtils.getVariantType("BCR") shouldBe "BC"
    }

    "should throw exception for unknown variant" {
        shouldThrow<ParseException> {
            CommandParsingUtils.getVariantType("XY")
        }
    }
})
