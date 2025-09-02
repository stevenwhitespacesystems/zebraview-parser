# ZPL Command: ^PM (Printing Mirror Image of Label)

## Description
The ^PM command prints the entire printable area of the label as a mirror image. This command flips the image from left to right.

## Format
```
^PMa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Print mirror image of entire label | Y (yes) or N (no) | N |

## Examples
```
^XA^PMY
^FO100,100
^CFG
^FDMIRROR^FS
^FO100,160
^FDIMAGE^FS
^XZ
```

## Important Notes
- If the parameter is missing or invalid, the command is ignored.
- Once entered, the ^PM command remains active until ^PMN is received or the printer is turned off.
- This command flips the entire label content from left to right.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*