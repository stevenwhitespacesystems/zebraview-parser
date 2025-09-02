# ZPL Command: ^JZ (Reprint After Error)

## Description
The ^JZ command reprints a partially printed label caused by a Ribbon Out, Media Out, or Head Open error condition. The label is reprinted as soon as the error condition is corrected.

## Format
```
^JZa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Reprint after error | Y (yes)<br/>N (no) | Y |

## Examples
```zpl
^JZY
^JZN
```

## Important Notes
- Remains active until another ^JZ command is sent or printer is turned off
- Sets the error mode for the printer
- Only affects labels printed after the command is sent
- If parameter is missing or incorrect, the command is ignored
- Applies to Ribbon Out, Media Out, and Head Open error conditions

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
