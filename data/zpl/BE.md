# ZPL Command: ^BE (EAN-13 Bar Code)

## Description
The ^BE command is similar to the UPC-A bar code. It is widely used throughout Europe and Japan in the retail marketplace.

The EAN-13 bar code has 12 data characters, one more data character than the UPC-A code. An EAN-13 symbol contains the same number of bars as the UPC-A, but encodes a 13th digit into a parity pattern of the left-hand six digits. This 13th digit, in combination with the 12th digit, represents a country code.

### Key Features
- Supports fixed print ratios
- Field data (^FD) is limited to exactly 12 characters
- ZPL II automatically truncates or pads on the left with zeros to achieve required number of characters
- When using JAN-13 (Japanese Article Numbering), the first two non-zero digits sent to the printer must be 49

## Format
```
^BEo,h,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y = yes<br>N = no | Y |
| **g** | Print interpretation line above code | Y = yes<br>N = no | N |

## Important Notes
- Field data must be exactly 12 characters
- Automatic padding/truncation with zeros on the left to meet character requirements
- For JAN-13 applications, ensure first two non-zero digits are 49
- Uses Mod 10 check-digit scheme for error checking
- The 13th digit is encoded into the parity pattern of the left-hand six digits
- The 13th digit combined with the 12th digit represents a country code
- For additional information about EAN-13 bar codes, visit www.aimglobal.org
- For more information on Mod 10, see ZPL II Programming Guide Volume Two

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*