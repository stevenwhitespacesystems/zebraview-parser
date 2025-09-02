# ZPL Command: ^BP (Plessey Bar Code)

## Description
The ^BP command creates a pulse-width modulated, continuous, non-self-checking symbology.

Each character in the Plessey bar code is composed of eight elements: four bars and four adjacent spaces.

## Format
```
^BPo,e,h,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **e** | Print check digit | Y (yes) or N (no) | N |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |

## Important Notes
- ^BP supports a print ratio of 2.0:1 to 3.0:1
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- For additional information about the Plessey bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*