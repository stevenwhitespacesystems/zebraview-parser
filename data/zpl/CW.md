# ZPL Command: ^CW (Font Identifier)

## Description
All built-in fonts are referenced using a one-character identifier. The ^CW command assigns a single alphanumeric character to a font stored in DRAM, memory card, EPROM, or Flash.

If the assigned character is the same as that of a built-in font, the downloaded font is used in place of the built-in font. The new font is printed on the label wherever the format calls for the built-in font. If used in place of a built-in font, the change is in effect only until power is turned off.

If the assigned character is different, the downloaded font is used as an additional font. The assignment remains in effect until a new command is issued or the printer is turned off.

## Format
```
^CWa,d:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Letter of existing font to be substituted, or new font to be added | A through Z and 0 to 9 | One-character entry is required |
| **d** | Device to store font in (optional) | R:, E:, B:, and A: | R: |
| **o** | Name of the downloaded font to be substituted for the built-in, or as an additional font | Any name up to 8 characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .FNT (fixed value) | .FNT |

## Examples
These examples show how to use:

MYFONT.FNT stored in DRAM whenever a format calls for Font A:
```
^XA
^CWA,R:MYFONT.FNT
^XZ
```

MYFONT.FNT stored in DRAM as additional Font Q:
```
^XA
^CWQ,R:MYFONT.FNT
^XZ
```

NEWFONT.FNT stored in DRAM whenever a format calls for font F:
```
^XA
^CWF,R:NEWFONT.FNT
^XZ
```

## Important Notes
- The command allows assignment of custom fonts to single-character identifiers
- Downloaded fonts can either replace built-in fonts or be added as additional fonts
- Font replacements are temporary and only last until power is turned off
- All built-in fonts use single-character identifiers (A through Z and 0 to 9)
- Font files must have the .FNT extension
- The assignment affects all subsequent formats until changed or power is cycled

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*