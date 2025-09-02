# ZPL Command: ^PO (Print Orientation)

## Description
The ^PO command inverts the label format 180 degrees. The label appears to be printed upside down. If the original label contains commands such as ^LL, ^LS, ^LT and ^PF, the inverted label output is affected differently.

## Format
```
^POa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Invert label 180 degrees | N (normal) or I (invert) | N |

## Examples
```
^POI
```

## Important Notes
- The ^POI command inverts the x, y coordinates. All image placement is relative to these inverted coordinates.
- A different ^LH (Label Home) can be used to move the print back onto the label.
- If multiple ^PO commands are issued in the same label format, only the last command sent to the printer is used.
- Once the ^PO command is sent, the setting is retained until another ^PO command is received or the printer is turned off.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*