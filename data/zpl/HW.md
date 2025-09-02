# ZPL Command: ^HW (Host Directory List)

## Description
^HW is used to transmit a directory listing of objects in a specific memory area (storage device) back to the host device. This command returns a formatted ASCII string of object names to the host.

Each object is listed on a line and has a fixed length. The total length of a line is also fixed. Each line listing an object begins with the asterisk (*) followed by a blank space.

## Format
```
^HWd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location to retrieve object listing | R:, E:, B:, and A: | R: |
| **o** | Object name | 1 to 8 alphanumeric characters | * (asterisk). A question mark (?) can also be used |
| **x** | Extension | Any extension conforming to Zebra conventions | * (asterisk). A question mark (?) can also be used |

## Response Format
The directory listing follows this format:
```
<STX><CR><LF>
DIR R: <CR><LF>
*Name.ext  (2sp)(6 obj. sz.)(2sp)(3 option flags)
*Name.ext  (2sp)(6 obj. sz.)(2sp)(3 option flags)
<CR><LF>
-xxxxxxx bytes free
<CR><LF>
<ETX>
```

Where:
- `<STX>` = start of text
- `<CR><LF>` = carriage return/line feed  
- `<ETX>` = end of text
- There are eight spaces for the object name, followed by a period and three spaces for the extension
- The extension is followed by two blank spaces, six spaces for the object size, two blank spaces, and three spaces for option flags (reserved for future use)

## Examples
```
^XA
^HWR:*.*
^XZ
```

Example response:
```
DIR R:*.*
*R:ARIALN1.FNT  49140
*R:ARIALN2.FNT  49140
*R:ARIALN3.FNT  49140
*R:ARIALN4.FNT  49140
*R:ARIALN.FNT   49140
*R:ZEBRA.GRF    8420
-794292 bytes free R:RAM
```

## Important Notes
- The command might be used in a stand-alone file to be issued to the printer at any time
- The printer returns the directory listing as soon as possible, based on other tasks it might be performing when the command is received
- This command, like all ^ (caret) commands, is processed in the order that it is received by the printer

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*