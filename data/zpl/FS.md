# ZPL Command: ^FS (Field Separator)

## Description
The ^FS command denotes the end of the field definition. Alternatively, ^FS command can also be issued as a single ASCII control code SI (Control-O, hexadecimal 0F).

## Format
```
^FS
```

## Parameters
This command has no parameters.

## Examples
Basic usage terminating field definitions:
```zpl
^XA
^FO50,50
^A0N,50,50
^FDHello World^FS
^FO50,100
^B3N,N,100,Y,N
^FD123456^FS
^XZ
```

## Important Notes
- Required to terminate every field definition
- Can be replaced with ASCII control code SI (Control-O, hexadecimal 0F)
- Must be used after every field command sequence
- Essential for proper ZPL syntax

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*