# ZPL Command: ^BU (UPC-A Bar Code)

## Description
The ^BU command produces a fixed length, numeric symbology. It is primarily used in the retail industry for labeling packages. The UPC-A bar code has 11 data characters.

The 6 dot/mm, 12 dot/mm, and 24 dot/mm printheads produce the UPC-A bar code (UPC/EAN symbologies) at 100 percent size. However, an 8 dot/mm printhead produces the UPC/EAN symbologies at a magnification factor of 77 percent.

## Format
```
^BUo,h,f,g,e
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 9999 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | N |
| **e** | Print check digit | Y (yes) or N (no) | Y |

## Interpretation Line Font Style
The font style of the interpretation line depends on the modulus (width of narrow bar) selected in ^BY:

- **6 dot/mm printer**: modulus of 2 dots or greater prints with OCR-B interpretation line; modulus of 1 dot prints font A
- **8 dot/mm printer**: modulus of 3 dots or greater prints with OCR-B interpretation line; modulus of 1 or 2 dots prints font A  
- **12 dot/mm printer**: modulus of 5 dots or greater prints with OCR-B interpretation line; modulus of 1, 2, or 3 dots prints font A
- **24 dot/mm printer**: modulus of 9 dots or greater prints with OCR-B interpretation line; modulus of 1 to 8 dots prints font A

## Important Notes
- ^BU supports a fixed print ratio
- Field data (^FD) is limited to exactly 11 characters
- ZPL II automatically truncates or pads on the left with zeros to achieve required number of characters
- The UPC-A bar code uses the Mod 10 check digit scheme for error checking
- For further information on Mod 10, see ZPL II Programming Guide Volume Two
- For additional information about the UPC-A bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*