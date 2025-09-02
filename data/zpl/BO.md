# ZPL Command: ^BO (Aztec Bar Code Parameters)

## Description
The ^BO command creates a two-dimensional matrix symbology made up of square modules arranged around a bulls-eye pattern at the center.

## Format
```
^BOa,b,c,d,e,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Orientation | N = normal<br>R = rotated<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **b** | Magnification factor | 1 to 10 | 1 on 150 dpi printers<br>2 on 200 dpi printers<br>3 on 300 dpi printers<br>6 on 600 dpi printers |
| **c** | Extended channel interpretation code indicator | Y = if data contains ECICs<br>N = if data does not contain ECICs | N |
| **d** | Error control and symbol size/type indicator | 0 = default error correction level<br>01 to 99 = error correction percentage (minimum)<br>101 to 104 = 1 to 4-layer compact symbol<br>201 to 232 = 1 to 32-layer full-range symbol<br>300 = a simple Aztec "Rune" | 0 |
| **e** | Menu symbol indicator | Y = if this symbol is to be a menu (bar code reader initialization) symbol<br>N = if it is not a menu symbol | N |
| **f** | Number of symbols for structured append | 1 through 26 | 1 |
| **g** | Optional ID field for structured append | Text string with 24-character maximum | No ID |

## Examples
```
^XA
^B0R,7,N,0,N,1,0
^FD 7. This is testing label 7^FS
^XZ
```

## Important Notes
- The Aztec bar code works with firmware v60.13.0.11A and higher

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*