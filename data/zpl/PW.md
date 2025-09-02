# ZPL Command: ^PW (Print Width)

## Description
The ^PW command allows you set the print width.

## Format
```
^PWa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Label width (in dots) | 2, to the width of the label. If the value exceeds the width of the label, the width is set to the label's maximum size. | Last permanently saved value |

## Examples
```
^PW800
```

## Important Notes
- Not all Zebra printers support the ^PW command.
- If the value exceeds the width of the label, the width is set to the label's maximum size.
- The default value is the last permanently saved value.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*