## FEATURE:

**Add Support for 7 Additional ZPL Commands**

Extend the ZPL parser to support 7 new commands: FX, CF, GB, FR, FS, BY, and BC. This enhancement will significantly expand the parser's capabilities to handle more complex ZPL labels including comments, configuration, graphics, and barcodes.

The implementation should:
- Add AST node classes for each new command following existing patterns
- Extend the lexer to recognize the new command tokens
- Update the parser logic to handle command-specific syntax and parameters
- Maintain full backward compatibility with existing commands (^XA, ^XZ, ^FO, ^FD, ^A)
- Include comprehensive test coverage for each new command using Kotest StringSpec
- Add performance benchmarks for all new commands with regression detection
- Validate that existing command performance remains unchanged
- Update documentation and examples to demonstrate new capabilities

Commands to implement:
- **FX** - Comment command for documentation within ZPL
- **CF** - Change Default Font command 
- **GB** - Graphic Box command for drawing rectangles
- **FR** - Field Reverse command for inverted printing
- **FS** - Field Separator command
- **BY** - Bar Code Field Default command for barcode parameters
- **BC** - Code 128 Bar Code command

## EXAMPLES:

Reference existing codebase patterns for consistent implementation:

**AST Node Patterns:**
- `src/main/kotlin/com/whitespacesystems/parser/ast/FieldOriginCommand.kt` - Example of command with numeric parameters (x,y coordinates)
- `src/main/kotlin/com/whitespacesystems/parser/ast/FontCommand.kt` - Example of command with multiple parameter variants and optional parameters
- `src/main/kotlin/com/whitespacesystems/parser/ast/FieldDataCommand.kt` - Example of command handling string data
- `src/main/kotlin/com/whitespacesystems/parser/ast/StartFormatCommand.kt` - Example of simple parameterless command
- `src/main/kotlin/com/whitespacesystems/parser/ast/ZplNode.kt` - Base sealed class and visitor pattern implementation

**Parser Logic Patterns:**
- `src/main/kotlin/com/whitespacesystems/parser/parser/ZplParser.kt` - Main parser with recursive descent patterns for existing commands
- `src/main/kotlin/com/whitespacesystems/parser/lexer/Lexer.kt` - Tokenization patterns and command recognition logic
- `src/main/kotlin/com/whitespacesystems/parser/lexer/Token.kt` - Token type definitions and position tracking

**Testing Patterns:**
- `src/test/kotlin/com/whitespacesystems/parser/parser/FieldOriginCommandTest.kt` - Test structure for commands with parameters
- `src/test/kotlin/com/whitespacesystems/parser/parser/FontCommandTest.kt` - Test patterns for commands with variants
- `src/test/kotlin/com/whitespacesystems/parser/parser/ZplParserE2ETest.kt` - End-to-end testing patterns for complete ZPL programs
- `src/test/kotlin/com/whitespacesystems/parser/lexer/LexerTest.kt` - Tokenization testing patterns
- Kotest StringSpec structure used throughout test files

**Benchmarking Infrastructure:**
- `src/benchmark/kotlin/com/whitespacesystems/parser/CommandBenchmarks.kt` - Individual command performance benchmarks
- `src/benchmark/kotlin/com/whitespacesystems/parser/E2EBenchmarks.kt` - End-to-end parsing performance tests
- `src/benchmark/kotlin/com/whitespacesystems/parser/MemoryBenchmarks.kt` - Memory allocation benchmarks
- `baseline.json` - Performance baseline storage and regression detection
- `build.gradle.kts` - Benchmark task configuration and integration

**Utility and Integration:**
- `src/main/kotlin/com/whitespacesystems/parser/utils/AstPrinter.kt` - Visitor pattern implementation for AST visualization
- `src/main/kotlin/com/whitespacesystems/parser/Main.kt` - Demo application patterns and performance testing examples

## DOCUMENTATION:

Reference ZPL command specifications and implementation documentation:

**ZPL Command Specifications (data/zpl/ directory):**
- `data/zpl/FX.md` - Comment Field command syntax and parameters
- `data/zpl/CF.md` - Change Default Font command format and options
- `data/zpl/GB.md` - Graphic Box command parameters (width, height, thickness, color)
- `data/zpl/FR.md` - Field Reverse command specification
- `data/zpl/FS.md` - Field Separator command format
- `data/zpl/BY.md` - Bar Code Field Default command parameters
- `data/zpl/BC.md` - Code 128 Bar Code command syntax and options

**Additional ZPL References:**
- Existing command specifications in `data/zpl/` for pattern consistency (A.md, FO.md, FD.md, XA.md, XZ.md)
- ZPL command parameter formats, syntax rules, and validation requirements
- Command interaction patterns and sequencing requirements

**Implementation Documentation:**
- Kotlin sealed class patterns and visitor pattern documentation
- Kotest testing framework documentation for StringSpec patterns
- Performance benchmarking best practices for JVM/Kotlin applications
- Gradle build system documentation for benchmark task integration

## OTHER CONSIDERATIONS:

**Performance Targets:**
- **Simple Commands** (FX, FR, FS): <0.1ms (100,000ns) average execution time
  - FX (Comment): Simple text parsing until next command
  - FR (Field Reverse): Simple toggle command
  - FS (Field Separator): Simple formatting command
- **Complex Commands** (CF, GB, BY, BC): <1ms (1,000,000ns) average execution time
  - CF (Change Font): Parameter parsing for font configuration
  - GB (Graphic Box): Multiple numeric parameters (width, height, thickness, color)
  - BY (Bar Code Default): Barcode parameter configuration
  - BC (Code 128): Complex barcode data and parameter parsing

**Performance Regression Detection:**
- Maintain existing command performance within 10% of current baselines
- Establish new baselines for the 7 new commands
- Update `baseline.json` with performance measurements for new commands
- Ensure total parsing time for complex ZPL programs doesn't degrade significantly

**Testing Requirements:**
- Achieve minimum 80% test coverage for all new command implementations
- Include edge cases for each command's parameter combinations
- Test integration with existing commands in complete ZPL programs
- Validate error handling for malformed command syntax

**Backward Compatibility:**
- All existing ZPL programs must continue to parse identically
- No changes to existing AST node structures or visitor pattern methods
- Maintain existing lexer behavior for previously supported commands
- Preserve existing error messages and exception types

**Implementation Quality:**
- Follow established patterns from existing command implementations
- Update visitor pattern in `ZplNode.kt` and `AstPrinter.kt` for new commands
- Maintain code quality standards with Detekt and Ktlint compliance
- Add demo examples in `Main.kt` showcasing new command capabilities
