# ZPL Command: ^IM (Image Move)

## Description
The ^IM command performs a direct move of an image from storage area into the bitmap. The command is identical to the ^XG command (Recall Graphic), except there are no sizing parameters.

## Format
```
^IMd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Location of stored object | R:, E:, B:, and A: | Search priority |
| **o** | Object name | 1 to 8 alphanumeric characters | UNKNOWN |
| **x** | Extension | .GRF (fixed value) | .GRF |

## Examples
This example moves the image SAMPLE.GRF from DRAM and prints it in several locations in its original size.

```
^XA
^FO100,100^IMR:SAMPLE.GRF^FS
^FO100,200^IMR:SAMPLE.GRF^FS
^FO100,300^IMR:SAMPLE.GRF^FS
^FO100,400^IMR:SAMPLE.GRF^FS
^FO100,500^IMR:SAMPLE.GRF^FS
^XZ
```

## Important Notes
- By using the ^FO command, the graphic image can be positioned anywhere on the label
- The difference between ^IM and ^XG: ^IM does not have magnification, and therefore might require less formatting time
- To take advantage of reduced formatting time, the image must be at a 8-, 16-, or 32-bit boundary
- This command moves images in their original size without any scaling capabilities

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*