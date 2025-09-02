# ZPL Command: ^FD (Field Data)

## Description
The ^FD command defines the data string for the field. The field data can be any printable character except those used as command prefixes (^ and ~).

## Format
```
^FDa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Data to be printed | Any ASCII string up to 3072 characters | none (a string of characters must be entered) |

## Examples
Basic field data usage:
```zpl
^XA
^FO50,50
^A0N,50,50
^FDHello World^FS
^XZ
```

## Important Notes
- The ^ and ~ characters can be printed by changing the prefix characters using ^CD ~CD and ^CT ~CT commands
- The new prefix characters cannot be printed
- Characters with codes above 127, or the ^ and ~ characters, can be printed using the ^FH and ^FD commands
- ^CI13 must be selected to print a backslash (\)

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*