# ZPL Command: ^CD/~CD (Change Delimiter)

## Description
The ^CD and ~CD commands are used to change the delimiter character. This character is used to separate parameter values associated with several ZPL II commands. The default delimiter is a comma (,).

## Format
```
^CDa
~CDa
```

## Parameters
| Parameter | Description | Accepted Values | Default Value |
|-----------|-------------|----------------|---------------|
| **a** | Delimiter character change | Any ASCII character | A parameter is required. If no parameter is entered, the next character received is the new delimiter character |

## Examples

### Change character delimiter to a semicolon (;)
```
^XA
^CD;
^XZ
```

### Save delimiter change using JUS command
```
~CD;
^XA^JUS^XZ
```

## Important Notes
- This command changes the delimiter for all subsequent ZPL II commands
- The delimiter separates parameter values in commands
- To save the delimiter change permanently, use the JUS command
- If no parameter is provided, the next character received becomes the new delimiter

---
*Source: ZPL Programming Guide Volume One (45541L-002 Rev. A)*
