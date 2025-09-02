# ZPL Command: ^BF (Micro-PDF417 Bar Code)

## Description
The ^BF command creates a two-dimensional, multi-row, continuous, stacked symbology identical to PDF417, except it replaces the 17-module-wide start and stop patterns and left/right row indicators with a unique set of 10-module-wide row address patterns. These reduce overall symbol width and allow linear scanning at row heights as low as 2X.

Micro-PDF417 is designed for applications with a need for improved area efficiency but without the requirement for PDF417's maximum data capacity. It can be printed only in specific combinations of rows and columns up to a maximum of four data columns by 44 rows.

### Data Limitations
Field data (^FD) and field hexadecimal (^FH) are limited to:
- 250 7-bit characters
- 150 8-bit characters  
- 366 4-bit numeric characters

## Format
```
^BFo,h,m
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 9999 | Value set by ^BY or 10 (if no ^BY value exists) |
| **m** | Mode | 0 to 33 (see Micro-PDF417 Mode Table below) | 0 |

## Micro-PDF417 Mode Selection Process

To encode data into a Micro-PDF417 Bar Code:

1. **Determine the type of data** to be encoded (ASCII characters, numbers, 8-bit data, or combination)
2. **Determine the maximum amount of data** to be encoded within the bar code 
3. **Determine the percentage of check digits** that are used within the bar code (higher percentage = more damage resistance but larger size)
4. **Use the mode table below** with the gathered information to select the appropriate mode

## Micro-PDF417 Mode Table

| Mode | Data Columns | Data Rows | % of Max Cws for EC | Max Alpha Characters | Max Digits |
|------|-------------|-----------|-------------------|-------------------|-----------|
| 0 | 1 | 11 | 64 | 6 | 8 |
| 1 | 1 | 14 | 50 | 12 | 17 |
| 2 | 1 | 17 | 41 | 18 | 26 |
| 3 | 1 | 20 | 40 | 22 | 32 |
| 4 | 1 | 24 | 33 | 30 | 44 |
| 5 | 1 | 28 | 29 | 38 | 55 |
| 6 | 2 | 8 | 50 | 14 | 20 |
| 7 | 2 | 11 | 41 | 24 | 35 |
| 8 | 2 | 14 | 32 | 36 | 52 |
| 9 | 2 | 17 | 29 | 46 | 67 |
| 10 | 2 | 20 | 28 | 56 | 82 |
| 11 | 2 | 23 | 28 | 64 | 93 |
| 12 | 2 | 26 | 29 | 72 | 105 |
| 13 | 3 | 6 | 67 | 10 | 14 |
| 14 | 3 | 8 | 58 | 18 | 26 |
| 15 | 3 | 10 | 53 | 26 | 38 |
| 16 | 3 | 12 | 50 | 34 | 49 |
| 17 | 3 | 15 | 47 | 46 | 67 |
| 18 | 3 | 20 | 43 | 66 | 96 |
| 19 | 3 | 26 | 41 | 90 | 132 |
| 20 | 3 | 32 | 40 | 114 | 167 |
| 21 | 3 | 38 | 39 | 138 | 202 |
| 22 | 3 | 44 | 38 | 162 | 237 |
| 23 | 4 | 6 | 50 | 22 | 32 |
| 24 | 4 | 8 | 44 | 34 | 49 |
| 25 | 4 | 10 | 40 | 46 | 67 |
| 26 | 4 | 12 | 38 | 58 | 85 |
| 27 | 4 | 15 | 35 | 76 | 111 |
| 28 | 4 | 20 | 33 | 106 | 155 |
| 29 | 4 | 26 | 31 | 142 | 208 |
| 30 | 4 | 32 | 30 | 178 | 261 |
| 31 | 4 | 38 | 29 | 214 | 313 |
| 32 | 4 | 44 | 28 | 250 | 366 |
| 33 | 4 | 4 | 50 | 14 | 20 |

## Important Notes
- Micro-PDF417 is ideal for applications requiring improved area efficiency without PDF417's maximum data capacity
- Symbol width is reduced compared to PDF417 due to narrower address patterns
- Linear scanning is possible at row heights as low as 2X
- Mode selection is critical for optimal data encoding and error correction balance
- Maximum of four data columns by 44 rows
- Error correction level affects both damage resistance and symbol size

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*