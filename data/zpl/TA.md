# ZPL Command: ~TA (Tear-off Adjust Position)

## Description
The ~TA command lets you adjust the rest position of the media after a label is printed, which changes the position at which the label is torn or cut.

## Format
```
~TA###
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **###** | Change in media rest position (3-digit value in dot rows) | -120 to 120 | Last permanent value saved |

## Important Notes
- For 600 dpi printers, the step size doubles
- If the number of characters is less than 3, the command is ignored
- Command is ignored if parameter is missing or invalid
- Must use exactly 3 digits for the position value
- Negative values move the rest position backwards
- Positive values move the rest position forwards
- Affects the tear-off or cutting position for improved label separation

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*