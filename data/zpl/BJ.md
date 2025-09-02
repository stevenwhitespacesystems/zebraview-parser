# ZPL Command: ^BJ (Standard 2 of 5 Bar Code)

## Description
The ^BJ command creates a discrete, self-checking, continuous numeric symbology.

With Standard 2 of 5, all of the information is contained in the bars. Two bar widths are employed in this code, the wide bar measuring three times the width of the narrow bar.

## Format
```
^BJo,h,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |

## Important Notes
- ^BJ supports a print ratio of 2.0:1 to 3.0:1
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- For additional information about the Standard 2 of 5 bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*