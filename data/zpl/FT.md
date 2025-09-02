# ZPL Command: ^FT (Field Typeset)

## Description
The ^FT command also sets the field position, relative to the home position of the label designated by the ^LH command. The typesetting origin of the field is fixed with respect to the contents of the field and does not change with rotation.

## Format
```
^FTx,y
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x** | X-axis location (in dots) | 0 to 32000 | position after last formatted text field |
| **y** | Y-axis location (in dots) | 0 to 32000 | position after last formatted text field |

## Examples
Comparing ^FT and ^FO positioning with different orientations:
```zpl
^XA
^FO100,75^GB1,50,1,B,0^FS
^FO75,100^GB50,1,1,B,0^FS
^FO100,100^A0N,25,25^FDFO NORMAL^FS
^FO400,75^GB1,50,1,B,0^FS
^FO375,100^GB50,1,1,B,0^FS
^FT400,100^A0N,25,25^FDFT NORMAL^FS
^XZ
```

Field concatenation example:
```zpl
^XA
^FT100,100
^A0N,50,50
^FDFirst Field^FS
^FT
^FDSecond Field^FS
^XZ
```

## Important Notes
- **Text**: The origin is at the start of the character string, at the baseline of the font
- **Bar Codes**: The origin is at the base of the bar code, even when an interpretation is present
- **Graphic Boxes**: The origin is at the bottom-left corner of the box
- **Images**: The origin is at the bottom-left corner of the rectangular image area
- When coordinates are missing, the position following the last formatted field is assumed
- Not recommended without specifying x and y parameters when:
  - Positioning the first field in a label format
  - Using with ^FN (Field Number) command
  - Following an ^SN (Serialization Data) command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*