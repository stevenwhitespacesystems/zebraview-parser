# ZPL Command: ^BX (Data Matrix Bar Code)

## Description
The ^BX command creates a two-dimensional matrix symbology made up of square modules arranged within a perimeter finder pattern.

The ability to create a rectangular Data Matrix bar code is not available as a ZPL coding option.

## Format
```
^BXo,h,s,c,r,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Dimensional height of individual symbol elements | 1 to the width of the label (square elements specify both module and row height) | If 0 or not given, uses ^BY height parameter |
| **s** | Quality level (ECC value) | 0, 50, 80, 100, 140, 200 | 0 |
| **c** | Columns to encode | Quality 0-140: 9 to 49 (odd values only)<br>Quality 200: 10 to 144 (even values only) | Automatically determined |
| **r** | Rows to encode | Quality 0-140: 9 to 49 (odd values only)<br>Quality 200: 10 to 144 (even values only) | Automatically determined |
| **f** | Format ID (0 to 6) - not used with quality 200 | 1 = numeric + space<br>2 = uppercase alphanumeric + space<br>3 = uppercase alphanumeric + space, period, comma, dash, slash<br>4 = uppercase alphanumeric + space<br>5 = full 128 ASCII 7-bit set<br>6 = full 256 ISO 8-bit set | 6 |
| **g** | Escape sequence control character (quality 200 only) | Any character | ~ (tilde) |

## Quality Levels (ECC)
- **ECC 0, 50, 80, 100, 140**: Use convolution encoding (for closed applications only)
- **ECC 200**: Uses Reed-Solomon encoding (recommended for new applications)

Quality refers to the amount of data added to the symbol for error correction. For new applications, ECC 200 is recommended. ECC 000-140 should be used only in closed applications where a single party controls both production and reading.

## Maximum Field Sizes by Quality Level

| ECC Level | ID=1 | ID=2 | ID=3 | ID=4 | ID=5 | ID=6 |
|-----------|------|------|------|------|------|------|
| 0 | 596 | 452 | 394 | 413 | 310 | 271 |
| 50 | 457 | 333 | 291 | 305 | 228 | 200 |
| 80 | 402 | 293 | 256 | 268 | 201 | 176 |
| 100 | 300 | 218 | 190 | 200 | 150 | 131 |
| 140 | 144 | 105 | 91 | 96 | 72 | 63 |

## Field Data (^FD) Considerations

### Quality 0-140
- Field data limited to 596 characters maximum
- The \& and || can be used to insert carriage returns, line feeds, and backslash
- Other control characters can be inserted using ^FH
- Field data must correspond to user-specified format ID

### Quality 200
- Maximum 3072 characters (truncated if exceeded)
- Maximum numeric capacity: 3116 characters
- Maximum alphanumeric capacity: 2335 characters  
- Maximum 8-bit byte capacity: 1556 characters
- Uses escape sequences with underscore (_) or parameter g character

## Escape Sequences (Quality 200)
- **_X**: Shift character for control characters (e.g., _@=NUL, _G=BEL)
- **_1 to _3**: FNC characters 1 to 3
- **_2**: Structured Append (must be followed by nine digits)
- **_5NNN**: Code page NNN (three-digit code page value)
- **_dNNN**: ASCII decimal value NNN (must be three digits)
- **__**: Underscore in data (two underscores)

## Effects of ^BY on ^BX
- **w** (module width): No effect
- **r** (ratio): No effect  
- **h** (height): Used as approximate symbol height if not specified in ^BX

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*