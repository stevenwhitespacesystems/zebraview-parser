# ZPL Command: ^BK (ANSI Codabar Bar Code)

## Description
The ANSI Codabar bar code is used in a variety of information processing applications such as libraries, the medical industry, and overnight package delivery companies. This bar code is also known as USD-4 code, NW-7, and 2 of 7 code. It was originally developed for retail price labeling.

Each character in this code is composed of seven elements: four bars and three spaces. Codabar bar codes use two character sets, numeric and control (start and stop) characters.

## Format
```
^BKo,e,h,f,g,k,l
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **e** | Check digit | N (fixed value) | N |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |
| **k** | Designates start character | A, B, C, D | A |
| **l** | Designates stop character | A, B, C, D | A |

## Important Notes
- ^BK supports a print ratio of 2.0:1 to 3.0:1
- Field data (^FD) is limited to the width (or length, if rotated) of the label
- For additional information about the ANSI Codabar bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*