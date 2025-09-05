# Product Requirement Prompt: BT Command Implementation

## 1. Feature Summary

Command: ^BT (TLC39 Bar Code)
Purpose: Creates TLC39 Bar Code combining Code 39 and Micro-PDF417 for TCIF CLEI codes
Complexity: Complex (Hybrid barcode with dual encoding validation requiring field data format validation)
Affects: Field data format validation (6-digit ECI + comma-separated fields)

## 2. Command Specification

### Syntax
```
^BTo,w1,r1,h1,w2,h2
```

### Parameters
| Name | Type | Default | Validation |
|------|------|---------|------------|
| o | Char | 'N' | N=normal, R=rotated, I=inverted, B=bottom up |
| w1 | Int | 600 DPI: 4, 200/300 DPI: 2 | 1 to 10 dots |
| r1 | Double | 2.0 | 2.0 to 3.0 in 0.1 increments |
| h1 | Int | 600 DPI: 120, 300 DPI: 60, 200 DPI: 40 | 1 to 9999 dots |
| w2 | Int | 600 DPI: 4, 200/300 DPI: 2 | 1 to 10 dots |
| h2 | Int | 600 DPI: 8, 200/300 DPI: 4 | 1 to 255 dots |

### Field Data Requirements
- Exactly 6-digit ECI number followed by comma and serial number
- Serial number must be alphanumeric only (no punctuation)
- Additional data fields are comma-separated, each max 25 characters
- Format: `^FD[6-digit-ECI],[serial-number][,additional-data]...^FS`

### Modes
- **N** (Normal): Standard orientation
- **R** (Rotated): 90-degree clockwise rotation
- **I** (Inverted): 180-degree rotation
- **B** (Bottom up): 270-degree rotation

### Validation Rules
- Orientation must be N, R, I, or B
- w1 and w2 must be between 1 and 10 dots
- r1 (wide to narrow ratio) must be between 2.0 and 3.0 in 0.1 increments
- h1 must be between 1 and 9999 dots
- h2 must be between 1 and 255 dots
- Parameter defaults vary by printer DPI (200/300/600)
- ECI number must be exactly 6 digits
- Serial number must be alphanumeric only
- Additional data fields limited to 25 characters each

## 3. Dependencies & Prerequisites

### Required Prior Implementations
- [x] Field data format validation (6-digit ECI + comma-separated fields)
- [ ] Check: Parameter parsing utilities for float validation with precision
- [ ] Check: DPI-aware default parameter handling

### Shared Utilities
- [ ] TLC39ParsingUtils for ECI validation, field data parsing, parameter defaults by DPI
- [ ] Check CommandParsingUtils for existing float parsing helpers (validateFloatRange with precision)
- [ ] Check parameter validation utilities for range checking
- [ ] DPI context provider for parameter defaults (investigate printer context availability)

## 4. Implementation Design

### AST Node Structure
```kotlin
// Based on "AST: `TLC39Command` [Hybrid barcode with dual encoding validation]"
// Following pattern of existing barcode commands like BCCommand.kt
data class TLC39Command(
    val orientation: Char = 'N',
    val narrowBarWidth: Int = 2,  // w1 - default varies by DPI
    val wideToNarrowRatio: Double = 2.0,  // r1
    val barcodeHeight: Int = 40,  // h1 - default varies by DPI
    val moduleWidth: Int = 2,     // w2 - default varies by DPI
    val moduleHeight: Int = 4     // h2 - default varies by DPI
) : ZplNode {
    override fun <T> accept(visitor: ZplVisitor<T>): T = visitor.visit(this)
}
```

### Parser Integration
```kotlin
// Based on "Parser: `parseTLC39Command()` [Complex field data validation for ECI/serial format]"
// Following patterns in ZplParser for barcode commands with field data validation

private fun parseTLC39Command(): TLC39Command {
    // Parse parameters with DPI-aware defaults
    // Validate field data format (6-digit ECI + comma-separated fields)
    // Handle float parsing for r1 parameter with 0.1 increment precision
}

// Add to CommandInfo structure in Lexer.kt:
"BT" to CommandInfo(2, hasFieldData = true, hasVariants = false)
```

### Integration Points
| File | Modification | Purpose |
|------|-------------|---------|
| ast/TLC39Command.kt | Create new file | AST node definition |
| parser/TLC39ParsingUtils.kt | Create new file | Utility functions for validation |
| parser/CommandParserRegistry.kt | Update | Register BT command parser |
| ast/ZplNode.kt | Update | Add TLC39Command to visitor interface |
| lexer/Lexer.kt | Update | Add BT command info |

### Implementation Notes
- **Field Data Validation Pipeline**: Validate field data during command parsing, not AST creation
- **DPI Context Strategy**: Use printer context if available, otherwise default to 200 DPI for parameter defaults
- **Float Precision Implementation**: Use `BigDecimal` or custom validation for 0.1 increment checking (r1 parameter)
- **Error Message Format**: Follow existing parser error patterns: "Parameter [name] must be [requirement], got: [value]"
- **Field Data Format**: `^FD[6-digit-ECI],[alphanumeric-serial][,additional-data-max-25-chars]...^FS`
- **Validation Order**: Parameters first, then field data format, then field data content
- **Performance Optimization**: Cache DPI defaults, pre-compile regex for field data validation
- **Integration Pattern**: Follow existing barcode commands (BCCommand) for parser structure

### Utility Functions
Based on "Utils: TLC39ParsingUtils for ECI validation, field data parsing, parameter defaults by DPI":
```kotlin
object TLC39ParsingUtils {
    // Regex patterns (pre-compiled for performance)
    private val ECI_PATTERN = Regex("^\\d{6}$")
    private val SERIAL_PATTERN = Regex("^[A-Za-z0-9]+$")
    
    // Core validation functions
    fun validateEciNumber(eci: String): Boolean = ECI_PATTERN.matches(eci)
    fun validateSerialNumber(serial: String): Boolean = SERIAL_PATTERN.matches(serial) && serial.isNotEmpty()
    fun parseFieldData(data: String): TLC39FieldData // Returns structured field data
    fun validateRatioPrecision(ratio: Double): Boolean // Check 0.1 increments using BigDecimal
    
    // DPI-aware defaults (cached for performance)
    fun getDefaultsByDpi(dpi: Int): TLC39Defaults = when(dpi) {
        600 -> TLC39Defaults(w1=4, h1=120, w2=4, h2=8)
        300 -> TLC39Defaults(w1=2, h1=60, w2=2, h2=4)
        else -> TLC39Defaults(w1=2, h1=40, w2=2, h2=4) // 200 DPI default
    }
}

data class TLC39FieldData(val eci: String, val serial: String, val additionalData: List<String>)
data class TLC39Defaults(val w1: Int, val h1: Int, val w2: Int, val h2: Int)
```

## 5. Test Scenarios

### Test Implementation
```kotlin
// Test file: src/test/kotlin/com/whitespacesystems/parser/parser/TLC39CommandTest.kt
// Following Kotest StringSpec format as seen in existing barcode command tests

class TLC39CommandTest : StringSpec({
    // Tests are written FIRST - they will initially FAIL (RED phase)
    
    "should parse minimal BT command with field data" { }
    "should parse BT command with all parameters" { }
    "should handle complex field data validation" { }
    "should reject invalid ECI format" { }
    "should reject invalid orientation" { }
    "should reject invalid ratio precision" { }
})
```

### Basic Functionality
```kotlin
// From test cases 1-3 in research results:
"should parse basic BT command" {
    // ^BT^FD123456,ABC123^FS → TLC39Command('N', 4, 2.0, 120, 4, 8)
    val input = "^BT^FD123456,ABC123^FS"
    val result = parser.parse(input)
    // Validate command structure and field data
}

"should use defaults when parameters omitted" {
    // ^BTN^FD123456,ABC123^FS → TLC39Command('N', 4, 2.0, 120, 4, 8)
    val input = "^BTN^FD123456,ABC123^FS"
    val result = parser.parse(input)
    // Verify default parameter application
}

"should parse command with all parameters" {
    // ^BTR,10,3.0,9999,10,255^FD123456,ABC123456789012345678901^FS
    val input = "^BTR,10,3.0,9999,10,255^FD123456,ABC123456789012345678901^FS"
    val result = parser.parse(input)
    // Verify max parameter handling
}
```

### Edge Cases
```kotlin
"should handle minimum valid parameters" {
    // ^BT,1,2.0,1,1,1^FD123456,A^FS → TLC39Command('N', 1, 2.0, 1, 1, 1)
    val input = "^BT,1,2.0,1,1,1^FD123456,A^FS"
    val result = parser.parse(input)
    // Test boundary values
}

"should handle complex data with multiple fields" {
    // ^BT^FD123456,ABCD1234567890123456789,5551212,88899^FS
    val input = "^BT^FD123456,ABCD1234567890123456789,5551212,88899^FS"
    val result = parser.parse(input)
    // Validate multiple comma-separated fields
}
```

### Error Scenarios  
```kotlin
"should reject invalid ECI format" {
    // ^BT^FD12345,ABC123^FS → ParseError("ECI number must be exactly 6 digits, got: 12345")
    val input = "^BT^FD12345,ABC123^FS"
    shouldThrow<ParseException> {
        parser.parse(input)
    }.message shouldContain "ECI number must be exactly 6 digits"
}

"should reject invalid orientation" {
    // ^BTX^FD123456,ABC123^FS → ParseError("Orientation must be N, R, I, or B, got: X")
    val input = "^BTX^FD123456,ABC123^FS"
    shouldThrow<ParseException> {
        parser.parse(input)
    }.message shouldContain "Orientation must be N, R, I, or B"
}

"should reject invalid ratio precision" {
    // ^BT,2,1.9,120^FD123456,ABC123^FS → ParseError("Wide to narrow ratio must be between 2.0 and 3.0, got: 1.9")
    val input = "^BT,2,1.9,120^FD123456,ABC123^FS"
    shouldThrow<ParseException> {
        parser.parse(input)
    }.message shouldContain "Wide to narrow ratio must be between 2.0 and 3.0"
}
```

### Integration Tests
```kotlin
"should validate field data format requirements" {
    // Test ECI + serial number validation
    // Test alphanumeric-only serial number requirement
    // Test maximum 25 character limit per additional field
}

"should integrate with existing parser infrastructure" {
    // Test command recognition by lexer
    // Test visitor pattern implementation
    // Test AST node creation
}
```

### Property-Based Tests
```kotlin
"should handle all valid parameter combinations" {
    checkAll(
        Arb.choice('N', 'R', 'I', 'B'), // orientation
        Arb.int(1..10),                 // w1
        Arb.double(2.0, 3.0),          // r1
        Arb.int(1..9999),              // h1
        Arb.int(1..10),                // w2
        Arb.int(1..255)                // h2
    ) { o, w1, r1, h1, w2, h2 ->
        val command = "^BT$o,$w1,$r1,$h1,$w2,$h2^FD123456,VALID^FS"
        val result = parser.parse(command)
        result shouldBe instanceOf<TLC39Command>()
    }
}
```

### Performance Benchmarks
```kotlin
// Benchmark file: src/benchmark/kotlin/.../TLC39CommandBenchmarks.kt  
// Target: <1ms (Complex)
@State(Scope.Benchmark)
class TLC39CommandBenchmarks {
    @Benchmark
    fun parseTLC39Command(): TLC39Command {
        return parser.parse("^BTR,5,2.5,100,3,10^FD123456,BENCHMARK^FS")
    }
}
```

### Coverage Requirements
- Minimum: 80% code coverage
- All error paths must be tested
- All parameter combinations should be covered
- Field data validation paths must be tested

## 6. Definition of Done

### Feature Complete When:
- [x] Command is recognized by the lexer (BT command info added)
- [x] AST node structure correctly represents the command (TLC39Command)
- [x] Parser correctly handles all parameter combinations
- [x] Field data validation works for ECI/serial format
- [x] All test scenarios pass (100%)
- [x] Code coverage ≥80% for new implementation
- [x] Zero linting violations
- [x] Zero static analysis issues
- [x] Performance meets target: <1ms (Complex)
- [x] No performance regressions >10%
- [x] Demo application can parse the command
- [x] Real-world ZPL samples parse correctly (if provided)
- [x] Error messages are clear and actionable
- [x] Follows all existing patterns in the codebase

### Acceptance Criteria
- **Command Syntax**: Matches ZPL II TLC39 specification exactly (`^BTo,w1,r1,h1,w2,h2`)
- **Parameter Validation**: DPI-aware defaults work for 200/300/600 DPI contexts
- **Orientation Validation**: Accepts only N/R/I/B, rejects others with clear error messages
- **Float Precision**: r1 parameter validates 2.0-3.0 range with 0.1 increment precision using BigDecimal
- **Field Data Pipeline**: ECI format (6 digits), serial (alphanumeric), additional data (≤25 chars each)
- **Error Messages**: Follow format "Parameter [name] must be [requirement], got: [value]"
- **Integration**: Uses existing barcode command patterns (BCCommand structure)
- **Visitor Pattern**: Complete implementation with proper AST traversal
- **Performance**: Consistently parses complex commands in <1ms with cached utilities

### Quality Standards
- All build commands pass: `./gradlew check`
- No @Suppress annotations without approval
- Code follows existing barcode command patterns (BCCommand, etc.)
- Documentation is clear and complete for complex field data requirements
- Error messages provide actionable guidance for format violations