# ZPL Command: ~SD (Set Darkness)

## Description
The ~SD command allows you to set the darkness of printing. ~SD is the equivalent of the darkness setting parameter on the front panel display.

## Format
```
~SD##
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **##** | Desired darkness setting (two-digit number) | 00 to 30 | Last permanently saved value |

## Examples
```
~SD15
```
```
~SD08
```

## Important Notes
- The ^MD command value, if applicable, is added to the ~SD command.
- This command is equivalent to the darkness setting parameter on the front panel display.
- The setting uses two digits, so single digit values should be preceded by a zero (e.g., 08 for darkness level 8).

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*