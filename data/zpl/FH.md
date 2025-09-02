# ZPL Command: ^FH (Field Hexadecimal Indicator)

## Description
The ^FH command allows you to enter the hexadecimal value for any character directly into the ^FD statement. The ^FH command must precede each ^FD command that uses hexadecimals in its field.

Within the ^FD statement, the hexadecimal indicator must precede each hexadecimal value. The default hexadecimal indicator is _ (underscore). There must be a minimum of two characters designated to follow the underscore. The a parameter can be added when a different hexadecimal indicator is needed.

This command can be used with any of the commands that have field data (that is ^FD, ^FV (Field Variable), and ^SN (Serialized Data)).

## Format
```
^FHa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Hexadecimal indicator | Any character except current format and control prefix (^ and ~ by default) | _ (underscore) |

## Examples
Using hexadecimal values in field data:
```zpl
^XA
^FH
^FO50,50
^A0N,50,50
^FDHello_48world^FS
^XZ
```

## Important Notes
- Valid hexadecimal characters are: 0 1 2 3 4 5 6 7 8 9 A B C D E F a b c d e f
- There must be a minimum of two characters designated to follow the hexadecimal indicator
- The ^FH command must precede each ^FD command that uses hexadecimals
- Can be used with ^FD, ^FV (Field Variable), and ^SN (Serialized Data) commands

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*