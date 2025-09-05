## Command: FX (Complexity: Simple)
Purpose: Non-printing comment command for documentation within label formats
Format: ^FXc

## Parameters
| Name | Type | Default | Validation |
|------|------|---------|------------|
| c | String | none | Any text string until next ^ or ~ command |

## Implementation
- **AST**: `FxCommand` [Simple text storage]
- **Parser**: `parseFxCommand()` [Read until next command delimiter]
- **Utils**: none
- **Files**: FxCommand.kt, ZplParser.kt, AstPrinter.kt, CommandVisitor.kt
- **Target**: <0.1ms (Simple)
- **Deps**: none

## Test Cases (7 prioritized)
1. Basic: ^FXThis is a comment^FS → FxCommand("This is a comment")
2. Defaults: ^FX^FS → FxCommand("")
3. Max params: ^FXLong comment with special chars !@#$%^&*()^FS → FxCommand("Long comment with special chars !@#$%^&*()")
4. Boundary: ^FX<1000 character string>^FS → FxCommand("<1000 character string>")
5. Invalid: ^FX → ParseError("Unexpected end of input, expected text or command delimiter")
6. Edge: ^FXComment with spaces   and   tabs	^FS → FxCommand("Comment with spaces   and   tabs	")
7. Delimiter: ^FXComment ending with tilde~GA → FxCommand("Comment ending with tilde")

## Notes
- Comment content continues until next ^ or ~ command delimiter
- Preserves exact text including whitespace and special characters
- Non-printing command that doesn't affect label output
- Commonly used for documentation and organization within ZPL code