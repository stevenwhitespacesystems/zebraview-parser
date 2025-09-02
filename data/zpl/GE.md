# ZPL Command: ^GE (Graphic Ellipse)

## Description
The ^GE command produces an ellipse in the label format.

## Format
```
^GEw,h,t,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Ellipse width (in dots) | 3 to 4095 (larger values are replaced with 4095) | value used for thickness (t) or 1 |
| **h** | Ellipse height (in dots) | 3 to 4095 | value used for thickness (t) or 1 |
| **t** | Border thickness (in dots) | 2 to 4095 | 1 |
| **c** | Line color | B (black) or W (white) | B |

## Examples
Creating ellipses on a label:
```zpl
^XA
^FO100,100^GE150,75,3,B^FS
^FO300,100^GE100,150,5,W^FS
^FO500,100^GE75,75,2,B^FS
^XZ
```

## Important Notes
- Larger width or height values above 4095 are automatically replaced with 4095
- Minimum width and height is 3 dots
- When width equals height, creates a circle (similar to ^GC command)
- Position is set by the ^FO (Field Origin) command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*