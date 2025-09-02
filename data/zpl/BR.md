# ZPL Command: ^BR (RSS - Reduced Space Symbology Bar Code)

## Description
The ^BR command creates bar code types for space-constrained identification from EAN International and the Uniform Code Council, Inc. (now known as GS1 DataBar).

## Format
```
^BRa,b,c,d,e,f
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Orientation | N = Normal<br>R = Rotated<br>I = Inverted<br>B = Bottom-up | R |
| **b** | Symbology type in the RSS-14 family | 1 = RSS14<br>2 = RSS14 Truncated<br>3 = RSS14 Stacked<br>4 = RSS14 Stacked Omnidirectional<br>5 = RSS Limited<br>6 = RSS Expanded<br>7 = UPC-A<br>8 = UPC-E<br>9 = EAN-13<br>10 = EAN-8<br>11 = UCC/EAN-128 & CC-A/B<br>12 = UCC/EAN-128 & CC-C | 1 |
| **c** | Magnification factor | 1 to 10 | 24 dot = 6, 12 dot is 3, 8 dot and lower is 2<br>12 dot = 6, > 8 dot is 3, 8 dot and less is 2 |
| **d** | Separator height | 1 or 2 | 1 |
| **e** | Bar code height | 1 to 32000 dots (only affects linear portion, UCC/EAN & CC-A/B/C only) | 25 |
| **f** | Segment width (RSS expanded only) | 2 to 22 (even numbers only), in segments per line | 22 |

## Examples

### UPC-A Example (Symbology Type 7)
```
^XA
^FO10,10^BRN,7,5,2,100^FD12345678901|this is composite info^FS
^XZ
```

### RSS14 Example (Symbology Type 1)
```
^XA
^FO10,10^BRN,1,5,2,100^FD12345678901|this is composite info^FS
^XZ
```

## Important Notes
- The bar code height only affects the linear portion of the bar code
- For composite codes, use the pipe symbol (|) to separate primary data from composite information

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*