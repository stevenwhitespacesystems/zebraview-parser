package com.whitespacesystems.parser.utils

import com.whitespacesystems.parser.ast.EndFormatCommand
import com.whitespacesystems.parser.ast.StartFormatCommand
import com.whitespacesystems.parser.ast.ZplProgram
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class AstPrinterTest : StringSpec({
    val printer = AstPrinter()

    "should print StartFormatCommand correctly" {
        val command = StartFormatCommand()
        val program = ZplProgram(listOf(command))
        val result = printer.print(program)
        result shouldContain "StartFormatCommand()"
    }

    "should print EndFormatCommand correctly" {
        val command = EndFormatCommand()
        val program = ZplProgram(listOf(command))
        val result = printer.print(program)
        result shouldContain "EndFormatCommand()"
    }

    "should print complete ZplProgram with XA/XZ" {
        val program =
            ZplProgram(
                listOf(
                    StartFormatCommand(),
                    EndFormatCommand(),
                ),
            )
        val result = printer.print(program)

        result shouldContain "ZplProgram("
        result shouldContain "StartFormatCommand()"
        result shouldContain "EndFormatCommand()"
    }
})
