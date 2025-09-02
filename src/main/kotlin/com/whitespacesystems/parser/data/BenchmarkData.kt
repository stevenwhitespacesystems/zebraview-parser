package com.whitespacesystems.parser.data

/**
 * Test data generators for ZPL parser benchmarks.
 * Provides consistent, realistic ZPL inputs for performance measurement.
 */
object BenchmarkData {
    // Label generation constants
    private object LabelConstants {
        const val INVENTORY_ITEM_COUNT = 15
        const val INVENTORY_START_Y = 60
        const val INVENTORY_LINE_HEIGHT = 25
        const val INVENTORY_BASE_SKU = 1000
        const val INVENTORY_QTY_MULTIPLIER = 10
        const val INVENTORY_PRICE_MULTIPLIER = 5

        const val REPEATED_FIELD_COUNT = 20
        const val REPEATED_FIELD_GRID_COLUMNS = 4
        const val REPEATED_FIELD_COLUMN_WIDTH = 100
        const val REPEATED_FIELD_ROW_HEIGHT = 30
        const val REPEATED_FIELD_BASE_X = 50
        const val REPEATED_FIELD_BASE_Y = 50
    }

    /**
     * Simple individual ZPL commands for basic performance measurement.
     * Each command represents a single parsing operation.
     */
    val SIMPLE_COMMANDS =
        listOf(
            // Format control commands (simple, should be <0.1ms)
            // Start Format
            "^XA",
            // End Format
            "^XZ",
            // Field positioning commands (complex, should be <1ms)
            // Field Origin with coordinates
            "^FO100,50",
            // Field Origin at origin
            "^FO0,0",
            // Field Origin with large coordinates
            "^FO999,999",
            // Field data commands (complex, should be <1ms)
            // Field Data with simple text
            "^FDHello World",
            // Field Data empty
            "^FD",
            // Field Data with special characters
            "^FDPrice: $29.99",
            // Font commands (complex, should be <1ms)
            // Font with all parameters
            "^A0N,30,30",
            // Font with minimal parameters
            "^A0N",
            // Font with different type
            "^ABN,25,25",
        )

    /**
     * Simple complete ZPL labels for end-to-end parsing benchmarks.
     * These represent minimal but complete ZPL programs.
     */
    val SIMPLE_LABELS =
        listOf(
            // Basic label with single field
            "^XA^FO100,50^FDHello^XZ",
            // Label with font specification
            "^XA^FO100,50^A0N,30,30^FDWorld^XZ",
            // Label with positioning and text
            "^XA^FO10,10^FDTest Label^XZ",
            // Empty label (format markers only)
            "^XA^XZ",
        )

    /**
     * Complex ZPL labels for comprehensive performance testing.
     * These represent real-world ZPL usage scenarios.
     */
    val COMPLEX_LABELS =
        listOf(
            // Multi-field label with different fonts and positioning
            """
            ^XA
            ^FO50,50^A0N,28^FDProduct Label
            ^FO50,100^ABN,20^FDPrice: ${"$"}29.99
            ^FO50,130^ABN,16^FD(50% OFF!)
            ^FO50,180^A0N,14^FDExp: 12/31/2024
            ^XZ
            """.trimIndent().replace("\n", ""),
            // Label with multiple positioning commands
            """
            ^XA
            ^FO10,10^A0N,25^FDItem: ABC123
            ^FO10,40^A0N,20^FDQty: 5
            ^FO200,10^A0N,25^FDPrice: ${"$"}99.99
            ^FO200,40^A0N,20^FDTotal: ${"$"}499.95
            ^XZ
            """.trimIndent().replace("\n", ""),
            // Shipping label simulation
            """
            ^XA
            ^FO30,30^A0N,30^FDSHIP TO:
            ^FO30,70^A0N,20^FDJohn Smith
            ^FO30,100^A0N,20^FD123 Main St
            ^FO30,130^A0N,20^FDCityville, ST 12345
            ^FO30,180^A0B,25^FDTRACKING: 1Z999E999
            ^FO400,30^A0N,40^FDPRIORITY
            ^XZ
            """.trimIndent().replace("\n", ""),
            // Retail price label
            """
            ^XA
            ^FO50,20^A0N,35^FDWireless Mouse
            ^FO50,70^A0N,20^FDModel: WM-2024
            ^FO50,100^A0N,20^FDSKU: 123456789
            ^FO50,140^A0N,50^FD${"$"}24.99
            ^FO200,140^A0N,20^FDwas ${"$"}39.99
            ^FO50,200^A0N,16^FDSave 37%!
            ^XZ
            """.trimIndent().replace("\n", ""),
        )

    /**
     * Large-scale ZPL labels for stress testing parser performance.
     * These test scalability with many fields and commands.
     */
    val LARGE_LABELS =
        listOf(
            // Label with many fields (10+ commands)
            generateLargeInventoryLabel(),
            // Label with repetitive structure
            generateRepeatedFieldsLabel(),
        )

    /**
     * Memory allocation test data - various sizes to test memory efficiency.
     */
    val MEMORY_TEST_DATA =
        listOf(
            // Small allocation
            "^XA^FO10,10^FDX^XZ",
            // Medium allocation
            SIMPLE_LABELS.random(),
            // Large allocation
            COMPLEX_LABELS.random(),
            // Very large allocation
            LARGE_LABELS.random(),
        )

    /**
     * Generates a large inventory label with many fields for stress testing.
     */
    private fun generateLargeInventoryLabel(): String {
        val sb = StringBuilder("^XA")

        // Add title
        sb.append("^FO50,20^A0N,30^FDInventory Report")

        // Add inventory items
        repeat(LabelConstants.INVENTORY_ITEM_COUNT) { i ->
            val y = LabelConstants.INVENTORY_START_Y + (i * LabelConstants.INVENTORY_LINE_HEIGHT)
            sb.append("^FO50,$y^A0N,16^FDItem ${i + 1}: SKU-${LabelConstants.INVENTORY_BASE_SKU + i}")
            sb.append("^FO300,$y^A0N,16^FDQty: ${(i + 1) * LabelConstants.INVENTORY_QTY_MULTIPLIER}")
            sb.append("^FO400,$y^A0N,16^FD$${(i + 1) * LabelConstants.INVENTORY_PRICE_MULTIPLIER}.99")
        }

        // Add footer
        sb.append("^FO50,450^A0N,20^FDTotal Items: ${LabelConstants.INVENTORY_ITEM_COUNT}")
        sb.append("^XZ")

        return sb.toString()
    }

    /**
     * Generates a label with repeated field patterns for consistent benchmarking.
     */
    private fun generateRepeatedFieldsLabel(): String {
        val sb = StringBuilder("^XA")

        repeat(LabelConstants.REPEATED_FIELD_COUNT) { i ->
            val xOffset = (i % LabelConstants.REPEATED_FIELD_GRID_COLUMNS) * LabelConstants.REPEATED_FIELD_COLUMN_WIDTH
            val x = LabelConstants.REPEATED_FIELD_BASE_X + xOffset
            val yOffset = (i / LabelConstants.REPEATED_FIELD_GRID_COLUMNS) * LabelConstants.REPEATED_FIELD_ROW_HEIGHT
            val y = LabelConstants.REPEATED_FIELD_BASE_Y + yOffset
            sb.append("^FO$x,$y^A0N,18^FDField$i")
        }

        sb.append("^XZ")
        return sb.toString()
    }

    /**
     * Returns a random simple command for variable testing.
     */
    fun randomSimpleCommand(): String = SIMPLE_COMMANDS.random()

    /**
     * Returns a random complex label for variable testing.
     */
    fun randomComplexLabel(): String = COMPLEX_LABELS.random()

    /**
     * Returns a random large label for stress testing.
     */
    fun randomLargeLabel(): String = LARGE_LABELS.random()
}
