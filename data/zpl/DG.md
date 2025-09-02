# ZPL Command: ~DG (Download Graphics)

## Description
The ~DG command downloads an ASCII Hex representation of a graphic image. If .GRF is not the specified file extension, .GRF is automatically appended.

For more saving and loading options when downloading files, see ~DY on page 130.

## Format
```
~DGd:o.x,t,w,data
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Device to store image | R:, E:, B:, and A: | R: |
| **o** | Image name | 1 to 8 alphanumeric characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .GRF (fixed value) | .GRF |
| **t** | Total number of bytes in graphic | See calculation formula in examples below | - |
| **w** | Number of bytes per row | See calculation formula in examples below | - |
| **data** | ASCII hexadecimal string defining image | The data string defines the image and is an ASCII hexadecimal representation of the image. Each character represents a horizontal nibble of four dots. | - |

## Calculation Formulas
Where:
- **x** = width of the graphic in millimeters
- **y** = height of the graphic in millimeters  
- **z** = dots/mm = print density of the printer being programmed
- **8** = bits/byte

### To determine the t parameter (total bytes):
```
(x × z / 8) × (y × z) = total bytes
```

**Example:** For a graphic 8 mm wide, 16 mm high, and a print density of 8 dots/mm:
```
t = (8 × 8 / 8) × (16 × 8) = 8 × 128 = 1024
```
*Note: Raise any portion of a byte to the next whole byte.*

### To determine the w parameter (bytes per row):
```
w = (x × z) / 8
```

**Example:** For a graphic 8 mm wide and a print density of 8 dots/mm:
```
w = (8 × 8) / 8 = 8
```
*Note: Parameter w is the first value in the t calculation. Raise any portion of a byte to the next whole byte.*

## Data Format
The data parameter is a string of hexadecimal numbers sent as a representation of the graphic image. Each hexadecimal character represents a horizontal nibble of four dots. For example, if the first four dots of the graphic image are white and the next four black, the dot-by-dot binary code is 00001111. The hexadecimal representation of this binary value is 0F. The entire graphic image is coded in this way, and the complete graphic image is sent as one continuous string of hexadecimal values.

## Examples
*(Note: Specific examples were not provided in the original source text)*

## Important Notes
- Do not use spaces or periods when naming your graphics
- Always use different names for different graphics
- If two graphics with the same name are sent to the printer, the first graphic is erased and replaced by the second graphic
- The .GRF extension is automatically appended if not specified
- Each hexadecimal character represents exactly four dots in the graphic

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*