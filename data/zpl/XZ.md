# ZPL Command: ^XZ (End Format)

## Description
The ^XZ command is the ending (closing) bracket. It indicates the end of a label format. When this command is received, a label prints. This command can also be issued as a single ASCII control character ETX (Control-C, hexadecimal 03).

## Format
```
^XZ
```

## Parameters
This command takes no parameters.

## Important Notes
- Label formats must start with the ^XA command and end with the ^XZ command to be in valid ZPL II format.
- The ^XZ command triggers the printing of the label.
- Can alternatively be sent as ASCII ETX character (Control-C, hex 03).

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*