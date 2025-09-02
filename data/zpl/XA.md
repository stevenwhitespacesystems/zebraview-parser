# ZPL Command: ^XA (Start Format)

## Description
The ^XA command is used at the beginning of ZPL II code. It is the opening bracket and indicates the start of a new label format. This command is substituted with a single ASCII control character STX (control-B, hexadecimal 02).

## Format
```
^XA
```

## Parameters
This command takes no parameters.

## Important Notes
- Valid ZPL II format requires that label formats should start with the ^XA command and end with the ^XZ command
- This is the opening bracket for all ZPL II label formats
- Can be substituted with ASCII control character STX (control-B, hex 02)
- Essential command for proper ZPL II format structure
- Must be paired with ^XZ to complete the label format

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*