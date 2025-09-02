# ZPL Command: ^FP (Field Parameter)

## Description
The ^FP command allows vertical formatting of the font field, commonly used for printing Asian fonts.

## Format
```
^FPd,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Direction | H = horizontal printing (left to right)<br>V = vertical printing (top to bottom)<br>R = reverse printing (right to left) | H |
| **g** | Additional inter-character gap (in dots) | 0 to 9999 | 0 if no value is entered |

## Examples
Using reverse and vertical printing:
```zpl
^XA
^FO100,50
^A0N,50,50
^FPR,5
^FDReverse Text^FS
^FO100,150
^A0N,50,50
^FPV,3
^FDVertical Text^FS
^XZ
```

## Important Notes
- When using reverse printing, the origin specified in ^FT is the lower-left corner of the right-most text character
- Commonly used for printing Asian fonts
- The additional gap parameter adds space between characters

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*