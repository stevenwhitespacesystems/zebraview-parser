# ZPL Command: ^BS (UPC/EAN Extensions)

## Description
The ^BS command creates the two-digit and five-digit add-on used primarily by publishers to create bar codes for ISBNs (International Standard Book Numbers). These extensions are handled as separate bar codes.

The ^BS command is designed to be used with the UPC-A bar code (^BU) and the UPC-E bar code (^B9).

## Format
```
^BSo,h,f,g
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Orientation | N = normal<br>R = rotated 90 degrees (clockwise)<br>I = inverted 180 degrees<br>B = read from bottom up, 270 degrees | Current ^FW value |
| **h** | Bar code height (in dots) | 1 to 32000 | Value set by ^BY |
| **f** | Print interpretation line | Y (yes) or N (no) | Y |
| **g** | Print interpretation line above code | Y (yes) or N (no) | Y |

## Field Origin Offsets

### UPC-A Extension Positioning
For UPC codes, with a module width of 2 (default):

| Orientation | X-Offset | Y-Offset |
|-------------|----------|----------|
| Normal | 209 Dots | 21 Dots |
| Rotated | 0 | 209 Dots |

### UPC-E Extension Positioning
For UPC-E codes:

| Orientation | X-Offset | Y-Offset |
|-------------|----------|----------|
| Normal | 122 Dots | 21 Dots |
| Rotated | 0 | 122 Dots |

## Important Notes
- ^BS supports a fixed print ratio
- Field data (^FD) is limited to exactly two or five characters
- ZPL II automatically truncates or pads on the left with zeros to achieve the required number of characters
- Care should be taken in positioning the UPC/EAN extension with respect to the UPC-A or UPC-E code to ensure the resulting composite code is within the UPC specification
- The bar code height for the extension should be 27 dots (0.135 inches) shorter than that of the primary code
- A primary UPC code height of 183 dots (0.900 inches) requires an extension height of 155 dots (0.765 inches)
- For additional information about the UPC/EAN bar code, go to www.aimglobal.org

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*