# ZPL Command: ^FN (Field Number)

## Description
The ^FN command numbers the data fields. This command is used in both ^DF (Store Format) and ^XF (Recall Format) commands.

In a stored format, use the ^FN command where you would normally use the ^FD (Field Data) command. In recalling the stored format, use ^FN in conjunction with the ^FD command.

## Format
```
^FN#
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **#** | Number to be assigned to the field | 0 to 9999 | 0 |

## Examples
Using field numbers with stored formats:
```zpl
^XA
^DF formatname^FS
^FO50,50^A0N,50,50^FN1^FS
^FO50,100^A0N,50,50^FN2^FS
^XZ

^XA
^XF formatname^FS
^FN1^FDFirst Field^FS
^FN2^FDSecond Field^FS
^XZ
```

## Important Notes
- The same ^FN value can be stored with several different fields
- If a label format contains a field with ^FN and ^FD, the data in that field prints for any other field containing the same ^FN value
- Used primarily with ^DF (Store Format) and ^XF (Recall Format) commands

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*