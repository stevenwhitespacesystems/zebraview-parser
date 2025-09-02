# ZPL Command: ^LH (Label Home)

## Description
The ^LH command sets the label home position. The default home position of a label is the upper-left corner (position 0,0 along the x and y axis). This is the axis reference point for labels. Any area below and to the right of this point is available for printing. The ^LH command changes this reference point. For instance, when working with preprinted labels, use this command to move the reference point below the preprinted area.

This command affects only fields that come after it. It is recommended to use ^LH as one of the first commands in the label format.

## Format
```
^LHx,y
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x** | x-axis position (in dots) | 0 to 32000 | 0 or last permanently saved value |
| **y** | y-axis position (in dots) | 0 to 32000 | 0 or last permanently saved value |

## Important Notes
- To be compatible with existing printers, this command must come before the first ^FS (Field Separator) command
- Once you have issued an ^LH command, the setting is retained until you turn off the printer or send a new ^LH command to the printer
- Only fields that come after this command are affected

### Printhead Resolution Reference
Depending on the printhead used in your printer, use one of these when figuring the values for x and y:
- 6 dots = 1 mm, 152 dots = 1 inch
- 8 dots = 1 mm, 203 dots = 1 inch  
- 11.8 dots = 1 mm, 300 dots = 1 inch
- 24 dots = 1 mm, 608 dots = 1 inch

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*