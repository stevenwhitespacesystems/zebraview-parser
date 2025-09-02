# ZPL Command: ^FO (Field Origin)

## Description
The ^FO command sets a field origin, relative to the label home (^LH) position. ^FO sets the upper-left corner of the field area by defining points along the x-axis and y-axis independent of the rotation.

## Format
```
^FOx,y
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x** | X-axis location (in dots) | 0 to 32000 | 0 |
| **y** | Y-axis location (in dots) | 0 to 32000 | 0 |

## Examples
Setting field origin for text placement:
```zpl
^XA
^FO100,50
^A0N,50,50
^FDHello World^FS
^XZ
```

## Important Notes
- If the value entered for the x or y parameter is too high, it could position the field origin completely off the label
- Sets the upper-left corner of the field area
- Position is relative to the label home (^LH) position
- Works independent of rotation

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*