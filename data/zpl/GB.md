# ZPL Command: ^GB (Graphic Box)

## Description
The ^GB command is used to draw boxes and lines as part of a label format. Boxes and lines are used to highlight important information, divide labels into distinct areas, or to improve the appearance of a label. The same format command is used for drawing either boxes or lines.

## Format
```
^GBw,h,t,c,r
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **w** | Box width (in dots) | value of t to 32000 | value used for thickness (t) or 1 |
| **h** | Box height (in dots) | value of t to 32000 | value used for thickness (t) or 1 |
| **t** | Border thickness (in dots) | 1 to 32000 | 1 |
| **c** | Line color | B (black) or W (white) | B |
| **r** | Degree of corner rounding | 0 (no rounding) to 8 (heaviest rounding) | 0 |

## Examples
Various graphic box configurations:
```zpl
^XA
^FO50,50^GB150,100,10,B,0^FS
^FO250,50^GB0,100,20,B,0^FS
^FO400,50^GB100,0,30,B,0^FS
^FO50,200^GB150,100,10,B,5^FS
^XZ
```

## Important Notes
- For w and h parameters, calculate dimensions in millimeters and multiply by printer resolution (6, 8, 12, or 24 dots/mm)
- If width and height are not specified, you get a solid box with width and height as specified by value t
- Rounding formula: rounding-radius = (rounding-index / 8) × (shorter side / 2)
- Thickness extends inward from the outline
- Can be used to create lines by setting either width or height to 0

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*