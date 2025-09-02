# ZPL Command: ^A@ (Use Font Name to Call Font)

## Description
The ^A@ command uses the complete name of a font, rather than the character designation used in ^A. Once a value for ^A@ is defined, it represents that font until a new font name is specified by ^A@.

## Format
```
^A@o,h,w,d:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Font orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | N or the last ^FW value |
| **h** | Character height (in dots) | **Scalable:** Height in dots of entire character block<br>**Bitmapped:** Rounded to nearest integer multiple of font's base height | Magnification specified by w or last ^CF value |
| **w** | Width (in dots) | **Scalable:** Width in dots of entire character block<br>**Bitmapped:** Rounded to nearest integer multiple of font's base width | Magnification specified by h or last ^CF value |
| **d** | Drive location of font | R:, E:, B:, A: | R: |
| **o** | Font name | Any valid font | Default set by ^CF, or font A if none specified |
| **x** | Extension | Fixed value: .FNT | .FNT |

## Examples
```zpl
^XA
^A@N,50,50,B:CYRI_UB.FNT
^FO100,100
^FDZebra Printer Fonts^FS
^A@N,40,40
^FO100,150
^FDThis uses the B:CYRI_UB.FNT^FS
^XZ
```

**Example breakdown:**
1. Starts the label format
2. Searches non-volatile printer memory (B:) for CYRI_UB.FNT and sets orientation to normal with 50x50 dot character size
3. Sets field origin at 100,100
4. Prints "Zebra Printer Fonts" on the label
5. Calls the font again with decreased character size (40x40 dots)
6. Sets new field origin at 100,150
7. Prints "This uses the B:CYRI_UB.FNT" on the label
8. Ends the label format

## Important Notes
- The font name carries over on all subsequent ^A@ commands without a font name
- For scalable fonts, magnification factors are unnecessary because characters are scaled
- For bitmapped fonts, values are rounded to the nearest integer multiple of the font's base dimensions
- For more information on scalable and bitmap fonts, see ZPL II Programming Guide Volume Two

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*