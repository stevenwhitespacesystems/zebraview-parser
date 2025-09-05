## Command: BT (Complexity: Complex)
Purpose: Creates TLC39 Bar Code combining Code 39 and Micro-PDF417 for TCIF CLEI codes
Format: ^BTo,w1,r1,h1,w2,h2

## Parameters
| Name | Type | Default | Validation |
|------|------|---------|------------|
| o | Char | 'N' | N=normal, R=rotated, I=inverted, B=bottom up |
| w1 | Int | 600 DPI: 4, 200/300 DPI: 2 | 1 to 10 dots |
| r1 | Double | 2.0 | 2.0 to 3.0 in 0.1 increments |
| h1 | Int | 600 DPI: 120, 300 DPI: 60, 200 DPI: 40 | 1 to 9999 dots |
| w2 | Int | 600 DPI: 4, 200/300 DPI: 2 | 1 to 10 dots |
| h2 | Int | 600 DPI: 8, 200/300 DPI: 4 | 1 to 255 dots |

## Implementation
- **AST**: `TLC39Command` [Hybrid barcode with dual encoding validation]
- **Parser**: `parseTLC39Command()` [Complex field data validation for ECI/serial format]
- **Utils**: `TLC39ParsingUtils` for ECI validation, field data parsing, parameter defaults by DPI
- **Files**: ast/TLC39Command.kt, parser/TLC39ParsingUtils.kt, parser/CommandParserRegistry.kt, ast/ZplNode.kt
- **Target**: <1ms (Complex)
- **Deps**: Field data format validation (6-digit ECI + comma-separated fields)

## Test Cases (8 prioritized)
1. Basic: ^BT^FD123456,ABC123^FS → TLC39Command('N', 4, 2.0, 120, 4, 8)
2. Defaults: ^BTN^FD123456,ABC123^FS → TLC39Command('N', 4, 2.0, 120, 4, 8)
3. Max params: ^BTR,10,3.0,9999,10,255^FD123456,ABC123456789012345678901^FS → TLC39Command('R', 10, 3.0, 9999, 10, 255)
4. Boundary: ^BT,1,2.0,1,1,1^FD123456,A^FS → TLC39Command('N', 1, 2.0, 1, 1, 1)
5. Invalid ECI: ^BT^FD12345,ABC123^FS → ParseError("ECI number must be exactly 6 digits, got: 12345")
6. Invalid orientation: ^BTX^FD123456,ABC123^FS → ParseError("Orientation must be N, R, I, or B, got: X")
7. Invalid ratio: ^BT,2,1.9,120^FD123456,ABC123^FS → ParseError("Wide to narrow ratio must be between 2.0 and 3.0, got: 1.9")
8. Complex data: ^BT^FD123456,ABCD1234567890123456789,5551212,88899^FS → validates multiple comma-separated fields

## Notes
- Requires field data validation: exactly 6-digit ECI number followed by comma and serial number
- Serial number must be alphanumeric only (no punctuation)
- Additional data fields are comma-separated, each max 25 characters
- Parameter defaults vary by DPI (200/300/600) requiring printer context
- Combines Code 39 (for ECI) and Micro-PDF417 (for serial/data) in single barcode
- Must validate ratio precision to 0.1 increments similar to BarCodeDefaultCommand