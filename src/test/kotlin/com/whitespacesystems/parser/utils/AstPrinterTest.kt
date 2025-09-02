package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.FieldDataCommand
import com.whitespacesystems.parser.ast.FieldOriginCommand
import com.whitespacesystems.parser.ast.FontCommand
import com.whitespacesystems.parser.ast.ZplProgram
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class AstPrinterTest : StringSpec({
    val printer = AstPrinter()

    "should print FieldOriginCommand correctly" {
        val command = FieldOriginCommand(100, 50)
        val result = printer.visitFieldOriginCommand(command)
        result shouldBe "FieldOriginCommand(x=100, y=50)"
    }

    "should print FieldDataCommand correctly" {
        val command = FieldDataCommand("Hello World")
        val result = printer.visitFieldDataCommand(command)
        result shouldBe "FieldDataCommand(data=\"Hello World\")"
    }

    "should escape quotes in FieldDataCommand" {
        val command = FieldDataCommand("Say \"Hello\"")
        val result = printer.visitFieldDataCommand(command)
        result shouldBe "FieldDataCommand(data=\"Say \\\"Hello\\\"\")"
    }

    "should print FontCommand with all parameters" {
        val command = FontCommand('B', 'R', 30, 25)
        val result = printer.visitFontCommand(command)
        result shouldBe "FontCommand(font='B', orientation='R', height=30, width=25)"
    }

    "should print FontCommand with minimal parameters" {
        val command = FontCommand('A')
        val result = printer.visitFontCommand(command)
        result shouldBe "FontCommand(font='A')"
    }

    "should print complete ZplProgram" {
        val program =
            ZplProgram(
                listOf(
                    FieldOriginCommand(100, 50),
                    FontCommand('A', 'N', 30, 30),
                    FieldDataCommand("Hello World"),
                ),
            )
        val result = printer.print(program)

        result shouldContain "ZplProgram("
        result shouldContain "FieldOriginCommand(x=100, y=50)"
        result shouldContain "FontCommand(font='A', orientation='N', height=30, width=30)"
        result shouldContain "FieldDataCommand(data=\"Hello World\")"
    }
})
