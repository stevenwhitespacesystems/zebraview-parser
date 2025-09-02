# ZPL Command: ^GS (Graphic Symbol)

## Description
The ^GS command enables you to generate the registered trademark, copyright symbol, and other symbols.

## Format
```
^GSo,h,w
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **o** | Font orientation | N = normal<br>R = rotate 90 degrees clockwise<br>I = inverted 180 degrees<br>B = bottom-up, 270 degrees | N or last ^FW value |
| **h** | Character height proportional to width (in dots) | 0 to 32000 | last ^CF value |
| **w** | Character width proportional to height (in dots) | 0 to 32000 | last ^CF value |

## Examples
Using graphic symbols:
```zpl
^XA
^FO100,100
^GS,50,50
^FDA^FS
^FO100,200
^GS,50,50
^FDB^FS
^FO100,300
^GS,50,50
^FDC^FS
^XZ
```

## Important Notes
- Use ^GS command followed by ^FD and appropriate character (A through E) to generate desired symbol
- Characters A through E represent different symbols like trademark, copyright, etc.
- Size is controlled by height and width parameters
- Orientation follows standard ZPL rotation conventions
- Must be used with field data (^FD) to specify which symbol to print

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*