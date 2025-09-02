# ZPL Command: ^BL (LOGMARS Bar Code)

## Description
The ^BL command is a special application of Code 39 used by the Department of Defense. LOGMARS is an acronym for Logistics Applications of Automated Marking and Reading Symbols.

## Format
```
^BLo,h,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |

## Important Notes
- ^BL supports a print ratio of 2.0:1 to 3.0:1
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- Lowercase letters in the ^FD string are converted to the supported uppercase LOGMARS characters
- The LOGMARS bar code produces a mandatory check digit using Mod 43 calculations
- For further information on the Mod 43 check digit, see ZPL II Programming Guide Volume Two
- For additional information about the LOGMARS bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*