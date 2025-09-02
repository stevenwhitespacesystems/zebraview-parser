# ZPL Command: ^JM (Set Dots per Millimeter)

## Description
The ^JM command lowers the density of the print. It reduces the resolution by half: 24 dots/mm becomes 12, 12 dots/mm becomes 6, 8 dots/mm becomes 4, and 6 dots/mm becomes 3. This command also affects the field origin (^FO) placement on the label and doubles the format size of the label.

## Format
```
^JMn
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **n** | Set dots per millimeter | A = normal resolution (24, 12, 8, or 6 dots/mm)<br/>B = half resolution (12, 6, 4, or 3 dots/mm) | A |

## Examples
```zpl
^JMA
^JMB
```

## Important Notes
- Must be entered before the first ^FS command in a format
- The effects of ^JM are persistent
- When ^JMB is used, the UPS Maxicode bar code becomes out of specification
- Normal dot-per-millimeter capabilities are 12 dots/mm (304 DPI), 8 dots/mm (203 DPI), or 6 dots/mm (153 DPI)
- Doubles the format size of the label when applied

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*