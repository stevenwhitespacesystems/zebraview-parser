# ZPL Command: ^GC (Graphic Circle)

## Description
The ^GC command produces a circle on the printed label. The command parameters specify the diameter (width) of the circle, outline thickness, and color. Thickness extends inward from the outline.

## Format
```
^GCd,t,c
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Circle diameter (in dots) | 3 to 4095 (larger values are replaced with 4095) | 3 |
| **t** | Border thickness (in dots) | 2 to 4095 | 1 |
| **c** | Line color | B (black) or W (white) | B |

## Examples
Creating circles on a label:
```zpl
^XA
^FO100,100^GC100,5,B^FS
^FO250,100^GC50,10,W^FS
^FO400,100^GC75,2,B^FS
^XZ
```

## Important Notes
- Thickness extends inward from the outline
- Larger diameter values above 4095 are automatically replaced with 4095
- Minimum diameter is 3 dots
- Position is set by the ^FO (Field Origin) command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*