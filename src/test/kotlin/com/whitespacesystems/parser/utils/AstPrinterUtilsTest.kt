package com.whitespacesystems.parser.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AstPrinterUtilsTest : StringSpec({

    "should format optional parameters with all values present" {
        val params = listOf("value1", "value2", "value3")
        val result = AstPrinterUtils.formatOptionalParameters("Command", params)
        result shouldBe "Command(value1, value2, value3)"
    }

    "should format optional parameters with some null values" {
        val params = listOf("value1", null, "value3")
        val result = AstPrinterUtils.formatOptionalParameters("Command", params)
        result shouldBe "Command(value1, value3)"
    }

    "should format optional parameters with no values" {
        val params = listOf<String?>(null, null)
        val result = AstPrinterUtils.formatOptionalParameters("Command", params)
        result shouldBe "Command()"
    }

    "should format optional parameters with empty list" {
        val params = emptyList<String?>()
        val result = AstPrinterUtils.formatOptionalParameters("Command", params)
        result shouldBe "Command()"
    }

    "should escape string data correctly" {
        val input = "Hello \"World\" and \\backslash"
        val result = AstPrinterUtils.escapeStringData(input)
        result shouldBe "Hello \\\"World\\\" and \\\\backslash"
    }

    "should format coordinate pair" {
        val result = AstPrinterUtils.formatCoordinatePair(100, 200)
        result shouldBe "x=100, y=200"
    }

    "should format parameter pair" {
        val result = AstPrinterUtils.formatParameterPair("width", 30, "height", 40)
        result shouldBe "width=30, height=40"
    }
})
