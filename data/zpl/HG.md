# ZPL Command: ^HG (Host Graphic)

## Description
The ^HG command is used to upload graphics to the host. The graphic image can be stored for future use, or it can be downloaded to any Zebra printer.

## Format
```
^HGd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Device location of object | R:, E:, B:, and A: | Search priority |
| **o** | Object name | 1 to 8 alphanumeric characters | UNKNOWN |
| **x** | Extension | .GRF (fixed value) | .GRF |

## Examples
```
^HGR:LOGO.GRF
^HGE:SYMBOL.GRF
```

## Important Notes
For more information on uploading graphics, see ^HY command.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*