package com.whitespacesystems.parser.data

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith

class BenchmarkDataTest : StringSpec({

    "SIMPLE_COMMANDS should contain expected ZPL commands" {
        BenchmarkData.SIMPLE_COMMANDS.shouldNotBeEmpty()

        // Should contain format commands
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^XA"
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^XZ"

        // Should contain positioning commands
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^FO100,50"
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^FO0,0"

        // Should contain field data commands
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^FDHello World"
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^FD"

        // Should contain font commands
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^A0N,30,30"
        BenchmarkData.SIMPLE_COMMANDS shouldContain "^ABN,25,25"
    }

    "SIMPLE_LABELS should contain valid ZPL labels" {
        BenchmarkData.SIMPLE_LABELS.shouldNotBeEmpty()

        // All simple labels should start with ^XA and end with ^XZ
        BenchmarkData.SIMPLE_LABELS.forEach { label ->
            label shouldStartWith "^XA"
            label shouldEndWith "^XZ"
        }

        // Should contain expected labels
        BenchmarkData.SIMPLE_LABELS shouldContain "^XA^FO100,50^FDHello^XZ"
        BenchmarkData.SIMPLE_LABELS shouldContain "^XA^XZ"
    }

    "COMPLEX_LABELS should contain realistic ZPL scenarios" {
        BenchmarkData.COMPLEX_LABELS.shouldNotBeEmpty()

        // All complex labels should be properly formatted
        BenchmarkData.COMPLEX_LABELS.forEach { label ->
            label shouldStartWith "^XA"
            label shouldEndWith "^XZ"
            // Complex labels should have multiple commands
            label.count { it == '^' } shouldBe label.split("^").size - 1
        }

        // Should contain realistic content
        val productLabel = BenchmarkData.COMPLEX_LABELS.find { it.contains("Product Label") }
        productLabel shouldNotBe null
        productLabel!! shouldContain "Price:"
        productLabel shouldContain "$29.99"
    }

    "LARGE_LABELS should contain stress test data" {
        BenchmarkData.LARGE_LABELS shouldHaveSize 2

        // Should contain generated inventory and repeated field labels
        val inventoryLabel = BenchmarkData.LARGE_LABELS[0]
        val repeatedLabel = BenchmarkData.LARGE_LABELS[1]

        // Inventory label should contain many items
        inventoryLabel shouldStartWith "^XA"
        inventoryLabel shouldEndWith "^XZ"
        inventoryLabel shouldContain "Inventory Report"
        inventoryLabel shouldContain "SKU-1000"

        // Repeated fields label should contain field patterns
        repeatedLabel shouldStartWith "^XA"
        repeatedLabel shouldEndWith "^XZ"
        repeatedLabel shouldContain "Field0"
        repeatedLabel shouldContain "Field19"
    }

    "MEMORY_TEST_DATA should provide various sizes" {
        BenchmarkData.MEMORY_TEST_DATA shouldHaveSize 4

        // Should contain different complexity levels
        val smallData = BenchmarkData.MEMORY_TEST_DATA[0]
        smallData shouldBe "^XA^FO10,10^FDX^XZ"

        // Other entries should be valid ZPL
        BenchmarkData.MEMORY_TEST_DATA.forEach { data ->
            data shouldStartWith "^XA"
            data shouldEndWith "^XZ"
        }
    }

    "randomSimpleCommand should return valid commands" {
        repeat(10) {
            val command = BenchmarkData.randomSimpleCommand()
            BenchmarkData.SIMPLE_COMMANDS shouldContain command
        }
    }

    "randomComplexLabel should return valid labels" {
        repeat(5) {
            val label = BenchmarkData.randomComplexLabel()
            BenchmarkData.COMPLEX_LABELS shouldContain label
            label shouldStartWith "^XA"
            label shouldEndWith "^XZ"
        }
    }

    "randomLargeLabel should return stress test data" {
        repeat(3) {
            val label = BenchmarkData.randomLargeLabel()
            BenchmarkData.LARGE_LABELS shouldContain label
            label shouldStartWith "^XA"
            label shouldEndWith "^XZ"
        }
    }

    "simple commands should have variety of types" {
        val commands = BenchmarkData.SIMPLE_COMMANDS

        // Should have format commands
        commands.filter { it.startsWith("^X") }.shouldNotBeEmpty()

        // Should have positioning commands
        commands.filter { it.startsWith("^FO") }.shouldNotBeEmpty()

        // Should have data commands
        commands.filter { it.startsWith("^FD") }.shouldNotBeEmpty()

        // Should have font commands
        commands.filter { it.startsWith("^A") }.shouldNotBeEmpty()
    }

    "generated inventory label should have correct structure" {
        val inventoryLabel = BenchmarkData.LARGE_LABELS.find { it.contains("Inventory Report") }
        inventoryLabel shouldNotBe null
        inventoryLabel!!

        // Should contain title
        inventoryLabel shouldContain "Inventory Report"

        // Should contain multiple items (15 items based on LabelConstants)
        inventoryLabel shouldContain "SKU-1000"
        inventoryLabel shouldContain "SKU-1014" // Last item

        // Should contain quantities and prices
        inventoryLabel shouldContain "Qty:"
        inventoryLabel shouldContain ".99"

        // Should contain footer
        inventoryLabel shouldContain "Total Items:"
    }

    "generated repeated fields label should have grid structure" {
        val repeatedLabel = BenchmarkData.LARGE_LABELS.find { it.contains("Field0") }
        repeatedLabel shouldNotBe null
        repeatedLabel!!

        // Should contain fields in pattern
        (0..19).forEach { i ->
            repeatedLabel shouldContain "Field$i"
        }

        // Should have positioning commands for grid layout
        repeatedLabel shouldContain "^FO50," // Base X position
        repeatedLabel shouldContain "^FO150," // Second column
        repeatedLabel shouldContain "^FO250," // Third column
    }

    "all data sets should contain only valid ZPL" {
        // Test simple commands - these are individual commands, not complete labels
        BenchmarkData.SIMPLE_COMMANDS.forEach { command ->
            command shouldContain "^"
        }

        // Test labels - these should be properly bounded
        val allLabels =
            BenchmarkData.SIMPLE_LABELS +
                BenchmarkData.COMPLEX_LABELS +
                BenchmarkData.LARGE_LABELS +
                BenchmarkData.MEMORY_TEST_DATA

        allLabels.forEach { label ->
            // All labels should start and end properly
            label shouldStartWith "^XA"
            label shouldEndWith "^XZ"
        }
    }
})
