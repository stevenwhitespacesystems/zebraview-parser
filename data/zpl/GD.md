# ZPL Command: ^GD (Graphic Diagonal Line)

## Description
The ^GD command produces a straight diagonal line on a label. This can be used in conjunction with other graphic commands to create a more complex figure.

## Format
```
^GDw,h,t,c,o
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Box width (in dots) | 3 to 32000 | value of t (thickness) or 1 |
| **h** | Box height (in dots) | 3 to 32000 | value of t (thickness) or 1 |
| **t** | Border thickness (in dots) | 1 to 32000 | 1 |
| **c** | Line color | B (black) or W (white) | B |
| **o** | Orientation (direction of diagonal) | R (or /) = right-leaning diagonal<br>L (or \) = left-leaning diagonal | R |

## Examples
Creating diagonal lines:
```zpl
^XA
^FO100,100^GD150,100,3,B,R^FS
^FO100,250^GD150,100,3,B,L^FS
^FO300,100^GD100,150,5,W,R^FS
^XZ
```

## Important Notes
- Can be used in conjunction with other graphic commands to create complex figures
- The diagonal connects corners within the specified width and height box
- Right-leaning diagonal (R or /) goes from bottom-left to top-right
- Left-leaning diagonal (L or \) goes from top-left to bottom-right

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*