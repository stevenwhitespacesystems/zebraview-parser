# ZPL Command: ~JD (Enable Communications Diagnostics)

## Description
The ~JD command initiates Diagnostic Mode, which produces an ASCII printout (using current label length and full width of printer) of all characters received by the printer. This printout includes the ASCII characters, the hexadecimal value, and any communication errors.

## Format
```
~JD
```

## Parameters
This command does not accept parameters.

## Examples
```zpl
~JD
```

## Important Notes
- Uses current label length and full width of printer for the diagnostic printout
- Shows ASCII characters, hexadecimal values, and communication errors
- Initiates Diagnostic Mode for troubleshooting communication issues

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*