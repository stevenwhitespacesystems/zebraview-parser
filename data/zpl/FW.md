# ZPL Command: ^FW (Field Orientation)

## Description
The ^FW command sets the default orientation for all command fields that have an orientation (rotation) parameter. Fields can be rotated 0, 90, 180, or 270 degrees clockwise by using this command.

The ^FW command affects only fields that follow it. Once you have issued a ^FW command, the setting is retained until you turn off the printer or send a new ^FW command to the printer.

## Format
```
^FWr
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **r** | Rotate field | N = normal<br>R = rotated 90 degrees<br>I = inverted 180 degrees<br>B = bottom-up 270 degrees | N (Initial value at power-up) |

## Examples
Setting default field orientation:
```zpl
^XA
^FWR
^FO50,50
^A0N,50,50
^FDRotated Text^FS
^FO50,150
^A0N,50,50
^FDAlso Rotated^FS
^XZ
```

## Important Notes
- If the ^FW command is entered with the r parameter missing, the command is ignored
- ^FW affects only the orientation in commands where the rotation parameter has not been specifically set
- If a command has a specific rotation parameter, that value is used instead
- Setting is retained until printer is turned off or a new ^FW command is sent

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*