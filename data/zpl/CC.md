# ZPL Command: ^CC/~CC (Change Carets)

## Description
The ^CC command is used to change the format command prefix. The default prefix is the caret (^).

## Format
```
^CCx
~CCx
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **x** | Caret character change | Any ASCII character | A parameter is required. If no parameter is entered, the next character received is the new prefix character |

## Examples

### Change ^CC format prefix from ^ to /
```
^XA
^CC/
/XZ
```
The forward slash (/) is set as the new prefix. Note the /XZ ending tag uses the new designated prefix character (/).

### Change ~CC command prefix from ~ to /
```
~CC/
/XA/JUS/XZ
```

## Important Notes
- Once the prefix is changed, all subsequent commands must use the new prefix character
- The command affects both format commands (^) and control commands (~)
- If no parameter is provided, the next character received becomes the new prefix

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
