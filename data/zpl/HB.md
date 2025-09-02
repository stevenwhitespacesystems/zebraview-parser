# ZPL Command: ~HB (Battery Status)

## Description
When the ~HB command is sent to the printer, a data string is sent back to the host. The string starts with an <STX> control code sequence and terminates by an <ETX><CR><LF> control code sequence.

## Format
```
~HB
```

## Parameters
This command has no input parameters. When the printer receives the command, it returns:

```
<STX>bb.bb,hh.hh,bt<ETX><CR><LF>
```

## Return Format
| Component | Description |
|-----------|-------------|
| **<STX>** | ASCII start-of-text character |
| **bb.bb** | Current battery voltage reading to the nearest 1/4 volt |
| **hh.hh** | Current head voltage reading to the nearest 1/4 volt |
| **bt** | Battery temperature in Celsius |
| **<ETX>** | ASCII end-of-text character |
| **<CR>** | ASCII carriage return |
| **<LF>** | ASCII line feed character |

## Examples
```
~HB
```

## Important Notes
This command is used for the power-supply battery of the printer and should not be confused with the battery backed-up RAM.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*