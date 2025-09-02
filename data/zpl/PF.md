# ZPL Command: ^PF (Slew Given Number of Dot Rows)

## Description
The ^PF command causes the printer to slew labels (move labels at a high speed without printing) a specified number of dot rows from the bottom of the label. This allows faster printing when the bottom portion of a label is blank.

## Format
```
^PF#
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **#** | Number of dots rows to slew | 0 to 32000 | A value must be entered or the command is ignored |

## Examples
```
^PF100
```

## Important Notes
- This command speeds up printing by allowing the printer to quickly move past blank areas at the bottom of labels.
- A value must be specified or the command will be ignored.

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*