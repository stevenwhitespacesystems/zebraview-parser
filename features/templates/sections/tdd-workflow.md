# TDD WORKFLOW & TESTING PATTERNS

## Testing Strategy: Kotest StringSpec with TDD Flow

**RED-GREEN-REFACTOR Testing Pattern:**
```kotlin
// STEP 1: Write test FIRST (before ANY implementation exists)
// This test WILL FAIL initially - that's the RED phase
class NewCommandTest : StringSpec({
    
    "should parse new command with parameters" {
        // This test WILL FAIL because NewCommand doesn't exist yet
        val lexer = Lexer("^NewCommand100,200")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        program.commands.size shouldBe 1
        val command = program.commands[0]
        command.shouldBeInstanceOf<NewCommand>()
        command.param1 shouldBe 100
        command.param2 shouldBe 200
    }
    
    "should handle new command with default values" {
        val lexer = Lexer("^NewCommand")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()
        
        val command = program.commands[0] as NewCommand
        command.param1 shouldBe 0  // default value
        command.param2 shouldBe 0  // default value
    }
    
    "should throw ParseException for invalid syntax" {
        val lexer = Lexer("^NewCommandInvalid")
        val parser = ZplParser(lexer.tokenize())
        
        shouldThrow<ParseException> {
            parser.parse()
        }.message shouldContain "Expected parameter"
    }
})

// STEP 2: Run test to confirm RED phase
// ./gradlew test --tests "NewCommandTest"
// Result: Tests FAIL because NewCommand class doesn't exist

// STEP 3: Create MINIMAL implementation for GREEN phase
// Create NewCommand.kt with bare minimum to pass tests

// STEP 4: Run test to confirm GREEN phase  
// ./gradlew test --tests "NewCommandTest"
// Result: Tests PASS

// STEP 5: REFACTOR - Now optimize and improve the implementation
// Add better error handling, performance improvements, etc.
// Tests must still pass after refactoring
```

## Testing Strategy (Kotest StringSpec Approach)

**Parser Components Testing:**
- **Lexer**: Tokenization, context-aware parsing, error handling
- **Parser**: Command parsing, AST generation, syntax validation  
- **AST**: Node creation, visitor pattern, immutability
- **E2E**: Complete ZPL parsing workflows with real examples

### Lexer Testing Pattern
```kotlin
class NewLexerTest : StringSpec({
    "should tokenize new ZPL command" {
        val lexer = Lexer("^NewCommand100,200")
        val tokens = lexer.tokenize()
        
        tokens shouldHaveSize expectedSize
        tokens[0] shouldBe Token(TokenType.CARET, "^", 0)
        tokens[1] shouldBe Token(TokenType.COMMAND, "NewCommand", 1)
        // Verify token types and positions
    }
    
    "should handle special characters" {
        val lexer = Lexer("^NewCommand$special,data")
        // Verify proper handling of special syntax
    }
})
```

### Parser Testing Pattern
```kotlin
class NewCommandTest : StringSpec({
    "should parse new command with parameters" {
        val lexer = Lexer("^NewCommand100,200,option")
        val parser = ZplParser(lexer.tokenize())
        val program = parser.parse()

        program.commands[0].shouldBeInstanceOf<NewCommand>()
        // Verify parameters
    }

    "should throw ParseException for invalid syntax" {
        shouldThrow<ParseException> {
            ZplParser(Lexer("^NewCommandInvalid").tokenize()).parse()
        }.message shouldContain "Expected parameter"
    }
})
```

### End-to-End Testing Pattern
```kotlin
class NewFeatureE2ETest : StringSpec({
    "should parse complete ZPL with new command" {
        val zplCode = "^FO100,100^NewCommand200,300^FDTest^FS"
        val program = ZplParser(Lexer(zplCode).tokenize()).parse()

        program.commands[1].shouldBeInstanceOf<NewCommand>()
        AstPrinter().print(program) shouldContain "NewCommand"
    }
})
```

### Performance Benchmark Testing Pattern
```kotlin
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class NewCommandBenchmarks {

    @Benchmark
    fun benchmarkNewCommand(): ZplProgram {
        val lexer = Lexer("^NewCommand100,200")
        return ZplParser(lexer.tokenize()).parse()
    }

    @Benchmark
    fun benchmarkNewCommandInCompleteLabel(): ZplProgram {
        val lexer = Lexer("^XA^FO100,50^NewCommand200,300^FDTest^XZ")
        return ZplParser(lexer.tokenize()).parse()
    }
}
```

### Benchmark Validation Testing Pattern
```kotlin
class BenchmarkSystemTest : StringSpec({
    "should execute benchmarks within thresholds" {
        val results = runBenchmark(NewCommandBenchmarks::class)
        results.forEach { (_, result) ->
            result.averageTimeNs shouldBeLessThan 1_000_000 // < 1ms
        }
    }
    
    "should detect performance regression" {
        val comparison = BaselineComparison.compare(runBenchmark(), loadBaseline())
        if (comparison.hasRegression) {
            println("PERFORMANCE WARNING: ${comparison.regressionDetails}")
        }
    }
})
```