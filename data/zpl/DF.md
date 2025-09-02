# ZPL Command: ^DF (Download Format)

## Description
The ^DF command saves ZPL II format commands as text strings to be later merged using ^XF with variable data. The format to be stored might contain field number (^FN) commands to be referenced when recalled.

While use of stored formats reduces transmission time, no formatting time is saved—this command saves ZPL II as text strings formatted at print time.

Enter the ^DF stored format command immediately after the ^XA command, then enter the format commands to be saved.

## Format
```
^DFd:o.x
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **d** | Device to store format | R:, E:, B:, and A: | R: |
| **o** | Format name | 1 to 8 alphanumeric characters | If a name is not specified, UNKNOWN is used |
| **x** | Extension | .ZPL (fixed value) | .ZPL |

## Examples
*(Note: Examples were not provided in the original source text)*

## Important Notes
- The ^DF command saves ZPL II format commands as text strings for later use
- Saved formats can be merged with variable data using the ^XF command
- The format may contain field number (^FN) commands to be referenced when recalled
- Using stored formats reduces transmission time but does not save formatting time
- The command should be entered immediately after the ^XA command
- Format commands to be saved should be entered after the ^DF command
- Files are saved with a .ZPL extension

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*