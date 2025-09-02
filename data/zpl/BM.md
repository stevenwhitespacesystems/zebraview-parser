# ZPL Command: ^BM (MSI Bar Code)

## Description
The ^BM command creates a pulse-width modulated, continuous, non-self-checking symbology. It is a variant of the Plessey bar code (^BP).

Each character in the MSI bar code is composed of eight elements: four bars and four adjacent spaces.

## Format
```
^BMo,e,h,f,g,e2
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **e** | Check digit selection | A = no check digits<br>B = 1 Mod 10<br>C = 2 Mod 10<br>D = 1 Mod 11 and 1 Mod 10 | B |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |
| **e2** | Inserts check digit into the interpretation line | Y (yes) or N (no) | N |

## Important Notes
- ^BM supports a print ratio of 2.0:1 to 3.0:1
- For the bar code to be valid, field data (^FD) is limited to:
  - 1 to 14 digits when parameter e is B, C, or D
  - 1 to 13 digits when parameter e is A, plus a quiet zone
- For additional information about the MSI bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*