# ZPL Command: ^MD (Media Darkness)

## Description
The ^MD command adjusts the darkness relative to the current darkness setting.

## Format
```
^MDa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Media darkness level | -30 to 30, depending on current value | 0 |

## Examples
These examples show setting the printer to different darkness levels:

- If the current value (value on configuration label) is 16, entering the command `^MD-9` decreases the value to 7
- If the current value (value on configuration label) is 1, entering the command `^MD15` increases the value to 16
- If the current value (value on configuration label) is 25, entering the command `^MD10` increases only the value to 30, which is the maximum value allowed

### XiIIIPlus Darkness Setting Examples
The darkness setting range for the XiIIIPlus is 0 to 30 in increments of 0.1:
```
^MD8.3
~SD8.3
```

### Multiple Command Example
If two ^MD commands were received:
- Assume the current value is 15
- An `^MD-6` command is received that changes the current value to 9
- Another command, `^MD2`, is received. The current value changes to 17
- The two ^MD commands are treated individually in relation to the current value of 15

## Important Notes
- If no value is entered, this command is ignored
- Each ^MD command is treated separately in relation to the current value as printed on the configuration label
- The ~SD command value, if applicable, is added to the ^MD command

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*