# ZPL Command: ^XG (Recall Graphic)

## Description
The ^XG command is used to recall one or more graphic images for printing. This command is used in a label format to merge graphics, such as company logos and piece parts, with text data to form a complete label.

An image can be recalled and resized as many times as needed in each format. Other images and data might be added to the format.

## Format
```
^XGd:o.x,mx,my
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Source device of stored image | R:, E:, B:, and A: | Search priority (R:, E:, B:, and A:) |
| **o** | Name of stored image | 1 to 8 alphanumeric characters | UNKNOWN (if not specified) |
| **x** | Extension | .GRF (fixed value) | .GRF |
| **mx** | Magnification factor on the x-axis | 1 to 10 | 1 |
| **my** | Magnification factor on the y-axis | 1 to 10 | 1 |

## Examples
This example uses the ^XG command to recall the image SAMPLE.GRF from DRAM and print it in five different sizes in five different locations on the same label:

```zpl
^XA
^FO100,100^XGR:SAMPLE.GRF,1,1^FS
^FO100,200^XGR:SAMPLE.GRF,2,2^FS
^FO100,300^XGR:SAMPLE.GRF,3,3^FS
^FO100,400^XGR:SAMPLE.GRF,4,4^FS
^FO100,500^XGR:SAMPLE.GRF,5,5^FS
^XZ
```

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*