# ZPL Command: ^BQ (QR Code Bar Code)

## Description
The ^BQ command produces a matrix symbology consisting of an array of nominally square modules arranged in an overall square pattern. A unique pattern at three of the symbol's four corners assists in determining bar code size, position, and inclination.

A wide range of symbol sizes is possible, along with four levels of error correction. User-specified module dimensions provide a wide variety of symbol production techniques.

QR Code Model 1 is the original specification, while QR Code Model 2 is an enhanced form of the symbology. Model 2 provides additional features and can be automatically differentiated from Model 1. Model 2 is the recommended model and should normally be used.

Encodable character sets include numeric data, alphanumeric data, 8-bit byte data, and Kanji characters.

## Format
```
^BQa,b,c,d,e
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Field position | Normal (^FW has no effect on rotation) | Normal |
| **b** | Model | 1 (original)<br>2 (enhanced – recommended) | 2 |
| **c** | Magnification factor | 1 to 10 | 1 on 150 dpi printers<br>2 on 200 dpi printers<br>3 on 300 dpi printers<br>6 on 600 dpi printers |
| **d** | Error correction level | H = ultra-high reliability level<br>Q = high reliability level<br>M = standard level<br>L = high density level | Q if empty<br>M for invalid values |
| **e** | Mask value | 1 to 7 | 7 |

## ^FD Field Data Options

### Automatic Data Input (Normal Mode)
```
^FD<error correction level>A,<data character string>^FS
```

### Manual Data Input (Normal Mode)
```
^FD<error correction level>M,<character mode><data character string>^FS
```

### Character Modes
- **N** = Numeric data
- **A** = Alphanumeric data
- **Bxxxx** = 8-bit byte mode (xxxx = number of data characters in BCD)
- **K** = Kanji characters (Shift JIS system based on JIS X 0208)

## Examples

### Automatic Data Input Example
```
^XA
^FO20,20^BQ,2,10^FDQA,0123456789ABCD 2D code^FS
^XZ
```

### Manual Data Input Example
```
^XA
^FO20,20^BQ,2,10^FDHM,N123456789012345^FS
^XZ
```

### Manual Alphanumeric Example
```
^XA
^FO20,20^BQ,2,10^FDMM,AAC-42^FS
^XZ
```

## Important Notes
- For additional information about the QR Code bar code, go to www.aimglobal.org
- Mixed mode is supported for complex data structures (up to 200 data strings)
- Two types of data input mode exist: Automatic (A) and Manual (M)
- If A is specified, the character mode does not need to be specified
- If M is specified, the character mode must be specified
- When using Kanji mode (K), all parameters after the character mode should be 16-bit characters

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*