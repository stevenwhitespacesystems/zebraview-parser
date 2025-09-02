package com.whitespacesystems.parser.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class DemoRunnerTest : StringSpec({

    "should run simple label demo" {
        val result = DemoRunner.runSimpleLabelDemo()
        result.shouldContain("FieldOriginCommand")
        result.shouldContain("FontCommand")
        result.shouldContain("FieldDataCommand")
    }

    "should run complete label demo" {
        val result = DemoRunner.runCompleteLabelDemo()
        result.shouldContain("ZplProgram")
        result.shouldContain("Hello World")
    }

    "should run performance command demo" {
        val result = DemoRunner.runPerformanceCommandDemo()
        result.shouldContain("Simple Command")
        result.shouldContain("Complex Command")
    }

    "should format demo output correctly" {
        val programOutput = "ZplProgram(\n  FieldOriginCommand(x=100, y=50)\n)"
        val result = DemoRunner.formatDemoOutput("Test Demo", "^FO100,50", programOutput)
        result.shouldContain("Test Demo")
        result.shouldContain("ZPL: ^FO100,50")
        result.shouldContain("AST:")
    }
})
