# ZPL Command: ^BT (TLC39 Bar Code)

## Description
The ^BT bar code is the standard for the TCIF can tag telecommunications equipment.

The TCIF CLEI code, which is the Micro-PDF417 bar code, is always four columns. The firmware must determine what mode to use based on the number of characters to be encoded.

## Format
```
^BTo,w1,r1,h1,w2,h2
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated<br>I = inverted<br>B = bottom up | N |
| **w1** | Width of the Code 39 bar code (in dots) | 1 to 10 | 600 dpi: 4<br>200/300 dpi: 2 |
| **r1** | Wide to narrow bar width ratio for Code 39 | 2.0 to 3.0 (increments of 0.1) | 2.0 |
| **h1** | Height of the Code 39 bar code (in dots) | 1 to 9999 | 600 dpi: 120<br>300 dpi: 60<br>200 dpi: 40 |
| **w2** | Narrow bar width of the Micro-PDF417 bar code (in dots) | 1 to 10 | 600 dpi: 4<br>200/300 dpi: 2 |
| **h2** | Row height of the MicroPDF417 bar code (in dots) | 1 to 255 | 600 dpi: 8<br>200/300 dpi: 4 |

## Data Format
The ^FD field data should contain:

1. **ECI Number** (6 digits): Must be exactly 6 digits. If the seventh character is not a comma, only Code 39 prints.
2. **Serial Number** (up to 25 characters): Variable length alphanumeric characters stored in the Micro-PDF symbol.
3. **Additional Data** (up to 150 bytes total): Optional additional fields separated by commas, each field up to 25 alphanumeric characters.

## Examples
```
^XA
^FO100,100^BT^FD123456,ABCd12345678901234,5551212,88899^FS
^XZ
```

## Important Notes
- Use the command defaults to get results that are in compliance with TCIF industry standards, regardless of printhead density
- The firmware generates an invalid character error if it sees anything but 6 digits for the ECI number
- The ECI number is not padded
- Serial number must be alphanumeric (letters and numbers, no punctuation)
- Additional data is stored in the Micro-PDF symbol and appended after the serial number
- A comma must exist between each maximum of 25 characters in the additional fields

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*