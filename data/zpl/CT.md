# ZPL Command: ^CT/~CT (Change Tilde)

## Description
The ^CT and ~CT commands are used to change the control command prefix. The default prefix is the tilde (~).

## Format
```
^CTa
```
or
```
~CTa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Change control command character | Any ASCII character | Parameter is required. If a parameter is not entered, the next character received is the new control command character. |

## Examples
This example shows how to change the control command prefix from a ^, to a +:

```
^XA
^CT+
^XZ
+DGR:GRAPHIC.GRF,04412,010
```

## Important Notes
- The command allows you to change the control character prefix used for ZPL commands
- The default control command prefix is the tilde (~)
- If no parameter is provided, the next character received becomes the new control command character
- This change affects all subsequent command processing until changed again or the printer is powered off

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*