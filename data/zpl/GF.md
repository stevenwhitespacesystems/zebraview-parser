# ZPL Command: ^GF (Graphic Field)

## Description
The ^GF command allows you to download graphic field data directly into the printer's bitmap storage area. This command follows the conventions for any other field, meaning a field orientation is included. The graphic field data can be placed at any location within the bitmap space.

## Format
```
^GFa,b,c,d,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Compression type | A = ASCII hexadecimal<br>B = binary<br>C = compressed binary | A |
| **b** | Binary byte count | 1 to 99999 (total bytes to be transmitted) | command ignored if not specified |
| **c** | Graphic field count | 1 to 99999 (total bytes comprising graphic format) | command ignored if not specified |
| **d** | Bytes per row | 1 to 99999 (bytes per row of image) | command ignored if not specified |
| **data** | Data | ASCII hexadecimal: 00 to FF<br>Binary data: Strictly binary | - |

## Examples
ASCII hexadecimal graphic data:
```zpl
^XA
^FO100,100^GFA,8000,8000,80,ASCII_hex_data_here^FS
^XZ
```

Binary graphic data:
```zpl
^XA
^FO100,100^GFB,8000,8000,80,Binary_data_here^FS
^XZ
```

## Important Notes
- For ASCII download, parameter b should match parameter c
- Count divided by bytes per row gives the number of lines in the image
- CR and LF can be inserted in ASCII data for readability
- A comma in ASCII data pads the current line with 00 (white space)
- Binary data ignores all control prefixes until the required byte count is reached
- Compressed binary format uses Zebra's compression algorithm

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*