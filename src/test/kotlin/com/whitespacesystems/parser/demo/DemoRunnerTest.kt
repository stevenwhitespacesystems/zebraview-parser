package com.whitespacesystems.parser.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class DemoRunnerTest : StringSpec({

    "should run simple label demo" {
        val result = DemoRunner.runSimpleLabelDemo()
        result.shouldContain("StartFormatCommand")
        result.shouldContain("EndFormatCommand")
        result.shouldContain("ZplProgram")
    }

    "should run complete label demo" {
        val result = DemoRunner.runCompleteLabelDemo()
        result.shouldContain("ZplProgram")
        result.shouldContain("StartFormatCommand")
        result.shouldContain("EndFormatCommand")
    }

    "should run performance command demo" {
        val result = DemoRunner.runPerformanceCommandDemo()
        result.shouldContain("Simple Command")
        result.shouldContain("Complex Command")
    }

    "should format demo output correctly" {
        val programOutput = "ZplProgram(\n  StartFormatCommand()\n  EndFormatCommand()\n)"
        val result = DemoRunner.formatDemoOutput("Test Demo", "^XA^XZ", programOutput)
        result.shouldContain("Test Demo")
        result.shouldContain("ZPL: ^XA^XZ")
        result.shouldContain("AST:")
    }
})
