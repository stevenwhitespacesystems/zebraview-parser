# ZPL Command: ^LL (Label Length)

## Description
The ^LL command defines the length of the label. This command is necessary when using continuous media (media not divided into separate labels by gaps, spaces, notches, slots, or holes).

To affect the current label and be compatible with existing printers, ^LL must come before the first ^FS (Field Separator) command. Once you have issued ^LL, the setting is retained until you turn off the printer or send a new ^LL command.

## Format
```
^LLy
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **y** | y-axis position (in dots) | 1 to 32000, not to exceed the maximum label size. While the printer accepts any value for this parameter, the amount of memory installed determines the maximum length of the label. | Typically set through the LCD (if applicable), or to the maximum label length capability of the printer. |

## Important Notes
- If multiple ^LL commands are issued in the same label format, the last ^LL command affects the next label unless it is prior to the first ^FS
- Values for y depend on the memory size. If the entered value for y exceeds the acceptable limits, the bottom of the label is cut off. The label also shifts down from top to bottom

### Formula for Calculating y Value
These formulas can be used to determine the value of y:

- **For 6 dot/mm printheads:** Label length in inches x 152.4 (dots/inch) = y
- **For 8 dot/mm printheads:** Label length in inches x 203.2 (dots/inch) = y  
- **For 12 dot/mm printheads:** Label length in inches x 304.8 (dots/inch) = y
- **For 24 dot/mm printheads:** Label length in inches x 609.6 (dots/inch) = y

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*