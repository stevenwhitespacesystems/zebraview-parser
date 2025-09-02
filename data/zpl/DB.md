# ZPL Command: ~DB (Download Bitmap Font)

## Description
The ~DB command sets the printer to receive a downloaded bitmap font and defines native cell size, baseline, space size, and copyright.

This command consists of two portions: a ZPL II command defining the font and a structured data segment that defines each character of the font.

## Format
```
~DBd:o.x,a,h,w,base,space,#char,©,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Drive to store font | R:, E:, B:, and A: | R: |
| **o** | Name of font | 1 to 8 alphanumeric characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .FNT (fixed value) | .FNT |
| **a** | Orientation of native font | Normal (fixed value) | Normal |
| **h** | Maximum height of cell (in dots) | 0 to 32000 | A value must be specified |
| **w** | Maximum width of cell (in dots) | 0 to 32000 | A value must be specified |
| **base** | Dots from top of cell to character baseline | 0 to 32000 | A value must be specified |
| **space** | Width of space or non-existent characters | 0 to 32000 | A value must be specified |
| **#char** | Number of characters in font | 1 to 256 (must match the characters being downloaded) | A value must be specified |
| **©** | Copyright holder | 1 to 63 alphanumeric characters | A value must be specified |
| **data** | Structured ASCII data that defines each character in the font | See data structure format below | - |

## Data Structure Format
The data parameter uses structured ASCII data to define each character:

```
#xxxx.h.w.x.y.i.data
```

Where:
- **#xxxx** = Character code (1 to 4 characters to allow for large international character sets)
- **h** = Bitmap height (in dot rows)
- **w** = Bitmap width (in dot rows)  
- **x** = X-offset (in dots)
- **y** = Y-offset (in dots)
- **i** = Typesetting motion displacement (width, including inter-character gap of a particular character in the font)
- **data** = Hexadecimal bitmap description

## Examples
This example shows the first two characters of a font being downloaded to DRAM:

```
~DBR:TIMES.FNT,N,5,24,3,10,2,ZEBRA 1992,
#0025.5.16.2.5.18.
OOFF
OOFF
FFOO
FFOO
FFFF
#0037.4.24.3.6.26.
OOFFOO
OFOOFO
OFOOFO
OOFFOO
```

## Important Notes
- The command allows downloading of custom bitmap fonts to the printer
- Font data consists of both header parameters and character definition data
- Each character is defined with its own bitmap data and positioning parameters
- Character codes can be 1-4 characters long to support international character sets
- The font file is saved with a .FNT extension
- All parameter values for dimensions, baseline, and space width must be specified

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*