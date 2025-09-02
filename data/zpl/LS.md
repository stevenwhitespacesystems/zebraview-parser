# ZPL Command: ^LS (Label Shift)

## Description
The ^LS command allows for compatibility with Z-130 printer formats that are set for less than full label width. It is used to shift all field positions to the left so the same commands used on a Z-130 or Z-220 Printer can be used on other Zebra printers.

## Format
```
^LSa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Shift left value (in dots) | -9999 to 9999 | 0 |

## Important Notes
- The ability to save the ^LS command depends on the version of firmware
- When entering positive values, it is not necessary to use the + sign. The value is assumed to be positive unless preceded by a negative sign (-)
- To be compatible with existing Zebra printers, this command must come before the first ^FS (Field Separator) command
- Once you have issued an ^LS command, the setting is retained until you turn off the printer or send a new ^LS command to the printer

### Formula for Determining ^LS Value
To determine the value for the ^LS command, use this formula:
- Z-130 and Z-220 values for ^LHx + ^FOx (distance from edge of label) = printer value for ^LSa
- If the print position is less than 0, set ^LS to 0

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*