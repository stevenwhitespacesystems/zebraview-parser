# ZPL Command: ~DY (Download Graphics)

## Description
The ~DY command downloads to the printer graphic objects in any supported format. This command can be used in place of ~DG for more saving and loading options.

## Format
```
~DYd:f,b,x,t,w,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | File location | R:, E:, B:, and A: | R: |
| **f** | File name | 1 to 8 alphanumeric characters | If a name is not specified, UNKNOWN is used |
| **b** | Format downloaded in data field (f) | A = uncompressed bitmap (.GRF, ASCII)<br>B = uncompressed bitmap (.GRF, binary)<br>C = AR-compressed bitmap (.GRF, compressed binary—used only by Zebra's BAR-ONE® v5)<br>P = PNG image (.PNG) | A value must be specified |
| **x** | Extension of stored file | G = raw bitmap (.GRF)<br>P = store as compressed (.PNG) | .GRF, unless parameter b is set to P (.PNG) |
| **t** | Total number of bytes in file | .GRF images: the size after decompression into memory<br>.PNG images: the size of the .PNG file | - |
| **w** | Total number of bytes per row | .GRF images: number of bytes per row<br>.PNG images: value ignored—data is encoded directly into .PNG data | - |
| **data** | Data | ASCII hexadecimal encoding, ZB64, or binary data, depending on b.<br>a, p = ASCII hexadecimal or ZB64<br>b, c = binary<br>When binary data is sent, all control prefixes and flow control characters are ignored until the total number of bytes needed for the graphic format is received. | - |

## Examples
*(Note: Specific examples were not provided in the original source text)*

## Important Notes
- This command can be used in place of ~DG for more saving and loading options
- Supports multiple formats including uncompressed bitmaps, compressed bitmaps, and PNG images
- When binary data is sent, all control prefixes and flow control characters are ignored until the total number of bytes needed for the graphic format is received
- For .GRF images, the t parameter represents the size after decompression into memory
- For .PNG images, the t parameter represents the size of the .PNG file
- For .PNG images, the w parameter (bytes per row) is ignored as data is encoded directly into PNG data
- For more information on ZB64 encoding and compression, see ZPL II Programming Guide Volume Two

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*